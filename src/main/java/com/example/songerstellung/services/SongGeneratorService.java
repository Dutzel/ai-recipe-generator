package com.example.songerstellung.services;

import com.example.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SongGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(SongGeneratorService.class);

    private final ChatClient chatClient;
    private final Optional<ImageModel> imageModel;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/recipe-for-ingredients")
    private Resource recipeForIngredientsPromptResource;

    @Value("classpath:/prompts/recipe-for-available-ingredients")
    private Resource recipeForAvailableIngredientsPromptResource;

    @Value("classpath:/prompts/prefer-own-recipe")
    private Resource preferOwnRecipePromptResource;

    @Value("classpath:/prompts/image-for-recipe")
    private Resource imageForRecipePromptResource;

    public SongGeneratorService(ChatClient chatClient, Optional<ImageModel> imageModel, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.imageModel = imageModel;
        this.vectorStore = vectorStore;
    }

    public void addRecipeDocumentForRag(Resource pdfResource, int pageTopMargin, int pageBottomMargin) {
        log.info("Add recipe document {} for rag", pdfResource.getFilename());
        var documentReaderConfig = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(pageTopMargin)
                .withPageBottomMargin(pageBottomMargin)
                .build();
        var documentReader = new PagePdfDocumentReader(pdfResource, documentReaderConfig);
        var documents = new TokenTextSplitter().apply(documentReader.get());
        vectorStore.accept(documents);
    }

    public Song fetchRecipeFor(List<String> ingredients, boolean preferAvailableIngredients, boolean preferOwnRecipes) {
        Song song;
        if (!preferAvailableIngredients && !preferOwnRecipes) {
            song = fetchRecipeFor(ingredients);
        } else if (preferAvailableIngredients && !preferOwnRecipes) {
            song = fetchRecipeWithFunctionCallingFor(ingredients);
        } else if (!preferAvailableIngredients && preferOwnRecipes) {
            song = fetchRecipeWithRagFor(ingredients);
        } else {
            song = fetchRecipeWithRagAndFunctionCallingFor(ingredients);
        }

        if (imageModel.isPresent()) {
            var imagePromptTemplate = new PromptTemplate(imageForRecipePromptResource);
            var imagePromptInstructions = imagePromptTemplate.render(Map.of("song", song.name(), "words", String.join(",", song.words())));
            var imageGeneration = imageModel.get().call(new ImagePrompt(imagePromptInstructions)).getResult();
            return new Song(song, imageGeneration.getOutput().getUrl());
        }
        return song;
    }

    private Song fetchRecipeFor(List<String> words) {
        log.info("Fetch song without additional information");

        return chatClient.prompt()
                .user(us -> us
                        .text(recipeForIngredientsPromptResource)
                        .param("words", String.join(",", words)))
                .call()
                .entity(Song.class);
    }

    private Song fetchRecipeWithFunctionCallingFor(List<String> words) {
        log.info("Fetch song with additional information from function calling");

        return chatClient.prompt()
                .user(us -> us
                        .text(recipeForAvailableIngredientsPromptResource)
                        .param("words", String.join(",", words)))
                .functions("fetchIngredientsAvailableAtHome")
                .call()
                .entity(Song.class);
    }

    private Song fetchRecipeWithRagFor(List<String> words) {
        log.info("Fetch song with additional information from vector store");
        var promptTemplate = new PromptTemplate(recipeForIngredientsPromptResource,
                Map.of("words", String.join(",", words)));
        var advise = new PromptTemplate(preferOwnRecipePromptResource).getTemplate();
        var advisorSearchRequest = SearchRequest.defaults().withTopK(2).withSimilarityThreshold(0.7);

        return chatClient.prompt()
                .user(promptTemplate.render())
                .advisors(new QuestionAnswerAdvisor(vectorStore, advisorSearchRequest, advise))
                .call()
                .entity(Song.class);
    }

    private Song fetchRecipeWithRagAndFunctionCallingFor(List<String> words) {
        log.info("Fetch song with additional information from vector store and function calling");
        var promptTemplate = new PromptTemplate(recipeForAvailableIngredientsPromptResource,
                Map.of("words", String.join(",", words)));
        var advise = new PromptTemplate(preferOwnRecipePromptResource).getTemplate();
        var advisorSearchRequest = SearchRequest.defaults().withTopK(2).withSimilarityThreshold(0.7);

        Song availableAtHome = chatClient.prompt()
                .user(promptTemplate.render())
                .functions("fetchIngredientsAvailableAtHome")
                .advisors(new QuestionAnswerAdvisor(vectorStore, advisorSearchRequest, advise))
                .call()
                .entity(Song.class);
        return availableAtHome;
    }
}