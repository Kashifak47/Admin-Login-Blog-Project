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

import com.website.adminloginproject.entity.GalleryItem;
import com.website.adminloginproject.repository.GalleryItemRepository;

@Service
public class GalleryItemService {

    private final String folderPath = "upload/";

	@Autowired
	private GalleryItemRepository galleryItemRepository;
	
	
	public GalleryItemService(GalleryItemRepository galleryItemRepository) {
		this.galleryItemRepository = galleryItemRepository;
	}
	public List<GalleryItem> getAllGalleryItems(){
		return galleryItemRepository.findAll();
	}
	public GalleryItem getGalleryItemById(Long id) {
        return galleryItemRepository.findById(id).orElse(null);
    }
	public void addGalleryItem(GalleryItem item) {
		galleryItemRepository.save(item);
	}
	public void deleteGalleryItem(Long id) {
        GalleryItem item = getGalleryItemById(id);
        if (item != null && item.getImageUrl() != null) {
            deleteOldImage(item.getImageUrl());
        }
        galleryItemRepository.deleteById(id);
    }
	
	public String uploadImage(MultipartFile file) throws IOException{
		String folderPath = "upload/";
		String fileName = System.currentTimeMillis()+ "_" + file.getOriginalFilename();
		Path filePath = Paths.get(folderPath+fileName);
		Files.createDirectories(filePath.getParent());
		Files.write(filePath, file.getBytes());
		return "/files/" + fileName;
	}
	
	private void deleteOldImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                String fileName = imageUrl.replace("/upload/", "");
                Path filePath = Paths.get(folderPath, fileName);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.out.println("Failed to delete old image: " + e.getMessage());
            }
        }
    }
	public void updateGalleryItem(GalleryItem item) {
		galleryItemRepository.save(item);
		
	}
	public void updateGalleryItem(Long id, String title, String description, MultipartFile imageFile) {

		GalleryItem item = getGalleryItemById(id);
		if(item == null) {
			throw new RuntimeException("Gallery item not found");
		}
		
		item.setTitle(title);
		item.setDescription(description);
		
		if(imageFile != null && !imageFile.isEmpty()) {
			deleteOldImage(item.getImageUrl());
			String newImageUrl;
			try {
				newImageUrl = uploadImage(imageFile);
				item.setImageUrl(newImageUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			galleryItemRepository.save(item);
			
		}
	}
	
}
