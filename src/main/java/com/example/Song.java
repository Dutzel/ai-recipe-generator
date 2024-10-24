package com.example;

import java.util.List;

public record Song(String name, String description, List<String> words, List<String> instructions, String imageUrl) {

    public Song(Song song, String imageUrl) {
        this(song.name, song.description, song.words, song.instructions, imageUrl);
    }
}