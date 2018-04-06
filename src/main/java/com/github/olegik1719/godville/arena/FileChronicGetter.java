package com.github.olegik1719.godville.arena;

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

    @Override
    public String getHtml(String chronicleID) {
        //String chronicleFile = pathToChronicles + chronicleID + extension;
        Path chroniclePath = FileSystems.getDefault().getPath(pathToChronicles
                , chronicleID
                , extension);
        if (Files.exists(chroniclePath)) {
            try {
                return Files.lines(chroniclePath).collect(Collectors.joining());
            } catch (IOException e) {
                return "";
            }
        }
        //if (chroniclePath)
        //File chronicleFile = new File(filePath);
        return "";
    }
}
