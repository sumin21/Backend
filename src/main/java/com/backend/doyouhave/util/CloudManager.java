package com.backend.doyouhave.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:env.properties")
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

    public Map uploadFile(Long postId, MultipartFile uploadFile) throws IOException {
        String folderName = String.valueOf(postId);
        String fileName = uploadFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        try {
            UUID uuid = UUID.randomUUID();
            String newFileName = uuid.toString() + extension;
            Map params = ObjectUtils.asMap("public_id", "Post_"+folderName+"/"+newFileName);
            return cloudinary.uploader().upload(uploadFile.getBytes(), params);
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
