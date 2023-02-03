package com.backend.doyouhave.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CloudManager {

    private Cloudinary cloudinary;
    @Autowired
    public CloudManager(@Value("${cloud.key}") String key,
                        @Value("${cloud.secret}") String secret,
                        @Value("${cloud.name}") String cloud) {
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName = cloud;
        cloudinary.config.apiSecret = secret;
        cloudinary.config.apiKey = key;
    }

    public Map uploadFile(Long postId, MultipartFile uploadFile, Map options) throws IOException {
        String folderName = String.valueOf(postId);
        String fileName = uploadFile.getOriginalFilename();
        File physicalFile = new File(fileName);
        FileOutputStream fout = new FileOutputStream(folderName+"/"+physicalFile);
        fout.write(uploadFile.getBytes());
        fout.close();
        try {
            File toUpload = new File(folderName+"/"+fileName);
            //Map params = ObjectUtils.asMap("public_id", "AppImages/"+fileName);
            return cloudinary.uploader().upload(toUpload, options);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public String createUrl(String name, int width, int height, String action) {
        return cloudinary.url()
                .transformation(new Transformation()
                .width(width).height(height)
                .border("2px _solid _black").crop(action))
                .imageTag(name);
    }
}
