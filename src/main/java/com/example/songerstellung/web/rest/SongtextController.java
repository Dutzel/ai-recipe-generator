package com.example.songerstellung.web.rest;
import com.example.Song;
import com.example.songerstellung.domain.SongGenerationData;
import com.example.songerstellung.services.SongGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songtext")
public class SongtextController {
    private final SongGeneratorService songGeneratorService;
    public SongtextController(SongGeneratorService songGeneratorService) {
        this.songGeneratorService = songGeneratorService;
    }

    @PostMapping
    public ResponseEntity<Song> fetchSongtext(@RequestBody SongtextRequest request) {
        String songtext = request.getWort();
        SongGenerationData songData = new SongGenerationData();
        songData.setIngredientsStr(songtext);

        Song song = this.songGeneratorService.fetchRecipeFor(songData.ingredients(), false, false);

        return ResponseEntity.ok(song);
    }

    // Hilfsklasse für den Request
    public static class SongtextRequest {
        private String wort;

        public String getWort() {
            return wort;
        }

        public void setWort(String wort) {
            this.wort = wort;
        }
    }
}
