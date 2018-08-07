package com.github.olegik1719.godville.duels.chronicgetters;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class FileChronicGetter implements ChronicGetter{

    private final String pathToChronicles;
    private final String extension;

    public FileChronicGetter(String path, String extension){
        this.extension = extension;
        pathToChronicles = path;
    }

    public FileChronicGetter(String path){
        this(path, ".html");
    }

    public FileChronicGetter(){
        this("res/log/");
    }

    @Override
    public String getHtml(String chronicleID) {
        if (chronicleID.length() == 0) return "";
        Path chroniclePath = FileSystems.getDefault().getPath(
                pathToChronicles
                + chronicleID
                + extension);
        if (Files.exists(chroniclePath)) {
            try {
                return Files.lines(chroniclePath).collect(Collectors.joining());
            } catch (IOException e) {
                return "";
            }
        }
        return "";
    }
}
