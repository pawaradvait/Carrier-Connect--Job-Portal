package com.CareerConnect.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownload {

    private Path foundPath;

    public Resource getFile(String uploadDir, String fileName) throws IOException {
        Path path = Paths.get(uploadDir);

        Files.list(path).forEach(file ->{
            if(file.getFileName().toString().equals(fileName)) {
                foundPath = file;
            }
        });

        if(foundPath != null) {
            return new UrlResource(foundPath.toUri());
        }

//        if (Files.exists(path)) {
//            foundPath = path;
//            return new UrlResource(path.toUri());
//        }
        return null;
    }

}
