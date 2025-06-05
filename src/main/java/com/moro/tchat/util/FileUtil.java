package com.moro.tchat.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class FileUtil {

    public static File multipartToFile(MultipartFile multipart) {
        try {
            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipart.getOriginalFilename());
            multipart.transferTo(tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File imageUrlToFile(String url) {
        try {
            URL fileURL = URI.create(url).toURL();
            File tempFile = File.createTempFile(System.getProperty("java.io.tmpdir") + "/image", url.substring(url.lastIndexOf('.')));
            Files.copy(fileURL.openStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
