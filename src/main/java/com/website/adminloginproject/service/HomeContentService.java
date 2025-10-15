package com.website.adminloginproject.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.website.adminloginproject.entity.HomeContent;
import com.website.adminloginproject.repository.HomeContentRepository;

@Service
public class HomeContentService {

    private final String folderPath = "src/main/resources/static/uploads/";

    @Autowired
    private HomeContentRepository homeContentRepository;

    public HomeContentService(HomeContentRepository homeContentRepository) {
        this.homeContentRepository = homeContentRepository;
    }

    public List<HomeContent> getAllHomeContents() {
        return homeContentRepository.findAll();
    }

    public HomeContent getHomeContentById(Long id) {
        return homeContentRepository.findById(id).orElse(null);
    }

    public void addHomeContent(HomeContent content) {
        homeContentRepository.save(content);
    }

    public void deleteHomeContent(Long id) {
        HomeContent content = getHomeContentById(id);
        if (content != null && content.getImageUrl() != null) {
            deleteOldImage(content.getImageUrl());
        }
        homeContentRepository.deleteById(id);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(folderPath + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return "/uploads/" + fileName;
    }

    private void deleteOldImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                String fileName = imageUrl.replace("/uploads/", "");
                Path filePath = Paths.get(folderPath, fileName);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.out.println("Failed to delete old image: " + e.getMessage());
            }
        }
    }

    public void updateHomeContent(HomeContent content) {
        homeContentRepository.save(content);
    }

    public void updateHomeContent(Long id, String title, String description, MultipartFile imageFile) {
        HomeContent content = getHomeContentById(id);
        if (content == null) {
            throw new RuntimeException("Home content not found");
        }

        content.setTitle(title);
        content.setDescription(description);

        if (imageFile != null && !imageFile.isEmpty()) {
            deleteOldImage(content.getImageUrl());
            try {
                String newImageUrl = uploadImage(imageFile);
                content.setImageUrl(newImageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        homeContentRepository.save(content);
    }
}
