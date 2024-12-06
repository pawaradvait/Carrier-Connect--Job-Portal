package com.CareerConnect.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileHandler {

    public static void  saveFile(String uploadDir,String fileName, MultipartFile multipartFile) throws IOException {

        Path path = Paths.get(uploadDir);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }

        Files.copy(multipartFile.getInputStream(),path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

    }
}
