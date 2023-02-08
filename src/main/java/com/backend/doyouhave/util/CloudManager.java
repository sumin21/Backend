package com.backend.doyouhave.util;

import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.repository.post.PostRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
@RequiredArgsConstructor
@PropertySource("classpath:env.properties")
public class CloudManager {
    private Cloudinary cloudinary;
    private PostRepository postRepository;
    @Autowired
    public CloudManager(@Value("${cloud.key}") String key,
                        @Value("${cloud.secret}") String secret,
                        @Value("${cloud.name}") String cloud) {
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName = cloud;
        cloudinary.config.apiSecret = secret;
        cloudinary.config.apiKey = key;
    }

    public void uploadFile(Long postId, MultipartFile uploadFile, MultipartFile uploadFileSecond, Post savedPost) throws IOException {
        String folderName = String.valueOf(postId);
        try {
            if(uploadFile != null) {
                String newFileName = getFileUrlByUpload(folderName, uploadFile);
                savedPost.setImg("http://res.cloudinary.com/do-you-have/image/upload/Post_"+folderName+"/"+newFileName+".jpg");
            }

            if(uploadFileSecond != null) {
                String newFileNameSecond = getFileUrlByUpload(folderName, uploadFileSecond);
                savedPost.setImgSecond("http://res.cloudinary.com/do-you-have/image/upload/Post_"+folderName+"/"+newFileNameSecond+".jpg");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileUrlByUpload(String folderName, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + extension;
        Map params = ObjectUtils.asMap("public_id", "Post_"+folderName+"/"+newFileName);
        cloudinary.uploader().upload(file.getBytes(), params);

        return newFileName;
    }

    public void deleteFile(Post foundedPost, String uploadedFile, String uploadedFileSecond) throws IOException {
        String folderName = String.valueOf(foundedPost.getId());
        if(uploadedFile!=null) {
            String fileName = uploadedFile.substring(uploadedFile.lastIndexOf("/"), uploadedFile.length()-4);
            System.out.println(fileName);
            Map options = ObjectUtils.asMap("type", "upload");
            options.put("invalidate", true);
            cloudinary.uploader().destroy("Post_"+folderName+fileName, options);
        }

        if(uploadedFileSecond!=null) {
            String fileNameSecond = uploadedFileSecond.substring(uploadedFileSecond.lastIndexOf("/"), uploadedFileSecond.length()-4);
            System.out.println(fileNameSecond);
            Map optionsSecond = ObjectUtils.asMap("type", "upload");
            optionsSecond.put("invalidate", true);
            cloudinary.uploader().destroy("Post_"+folderName+fileNameSecond, optionsSecond);
        }
    }
}
