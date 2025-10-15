package com.website.adminloginproject.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.website.adminloginproject.entity.GalleryItem;
import com.website.adminloginproject.service.GalleryItemService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GalleryController {

	@Autowired
	private GalleryItemService galleryItemService;
	
	@GetMapping("/gallery")
	public String showGallery(Model model) {
		List<GalleryItem> galleryItems = galleryItemService.getAllGalleryItems();
		model.addAttribute("galleryItems", galleryItems);
		return "gallery";
	}
	
	@GetMapping("/admin/dashboard")
	public String showAdminGallery(Model model, HttpSession session) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/login";
		}
		List<GalleryItem> galleryItems = galleryItemService.getAllGalleryItems();
		model.addAttribute("galleryItems",galleryItems);
		return "dashboard";
	}
	
	@PostMapping("/admin/gallery/add")
	public String addGalleryItem(@RequestParam String title, @RequestParam String description,
			@RequestParam MultipartFile imageFile, HttpSession session, 
			RedirectAttributes redirectAttributes) {
		
			if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }
			try {
				String imageUrl = galleryItemService.uploadImage(imageFile);
				GalleryItem newItem = new GalleryItem();
				newItem.setTitle(title);
				newItem.setDescription(description);
				newItem.setImageUrl(imageUrl);
				
				galleryItemService.addGalleryItem(newItem);
				redirectAttributes.addFlashAttribute("success", "Gallery item added successfully!");
				
			}catch(IOException e) {
				redirectAttributes.addFlashAttribute("failed", "Failed to upload image.");
			}
			return "redirect:/dashboard";
	}
	
	@PostMapping("/admin/gallery/delete/{id}")
	public String deleteGalleryItem(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/login";
		}
		galleryItemService.deleteGalleryItem(id);
		redirectAttributes.addFlashAttribute("success", "Gallery item deleted");
		return "redirect:/dashboard";
	}
	
	@PostMapping("/admin/gallery/update/{id}")
	public String updateGalleryItem(
			@PathVariable Long id, @RequestParam String title, @RequestParam String description, 
			@RequestParam( required=false) MultipartFile imageFile,
			HttpSession session, RedirectAttributes redirectAttributes
			) {
		 if (session.getAttribute("loggedInAdmin") == null) {
		        return "redirect:/login";
		    }
		 	GalleryItem existingItem = galleryItemService.getGalleryItemById(id);
		    if (existingItem != null) {
		        existingItem.setTitle(title);
		        existingItem.setDescription(description);

		        if (imageFile != null && !imageFile.isEmpty()) {
		            try {
		                String imageUrl = galleryItemService.uploadImage(imageFile);
		                existingItem.setImageUrl(imageUrl);
		            } catch (IOException e) {
		                redirectAttributes.addFlashAttribute("error", "Image upload failed.");
		                return "redirect:/dashboard";
		            }
		        }

		        galleryItemService.updateGalleryItem(existingItem);
		        redirectAttributes.addFlashAttribute("success", "Gallery item updated successfully!");
		    }

		 return "redirect:/dashboard";
	}
}
