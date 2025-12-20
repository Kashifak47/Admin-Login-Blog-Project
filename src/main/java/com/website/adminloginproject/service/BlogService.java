package com.website.adminloginproject.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.website.adminloginproject.entity.Blog;
import com.website.adminloginproject.repository.BlogRepository;

@Service
public class BlogService {

    private final String folderPath = "upload/";

    @Autowired
    private BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public Blog addBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        if (blog.getImageUrl() != null && !blog.getImageUrl().isEmpty()) {
            deleteOldImage(blog.getImageUrl());
        }
        blogRepository.deleteById(id);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String folderPath = "upload/";
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(folderPath+fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return "/files/" + fileName;
    }

    private void deleteOldImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                String fileName = imageUrl.replace("/files/", "");
                Path filePath = Paths.get(folderPath, fileName);
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                System.out.println("Failed to delete old image: " + ex.getMessage());
            }
        }
    }

    public void updateBlog(Long id, String title, String description, String content, MultipartFile imageFile) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog Not Found"));

        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);

        if (imageFile != null && !imageFile.isEmpty()) {
            deleteOldImage(blog.getImageUrl());
            try {
                String newImageUrl = uploadImage(imageFile);
                blog.setImageUrl(newImageUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        blogRepository.save(blog);
    }
}
