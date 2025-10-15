package com.website.adminloginproject.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.website.adminloginproject.entity.HomeContent;
import com.website.adminloginproject.service.HomeContentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeContentController {

	@Autowired
    private HomeContentService homeContentService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<HomeContent> homeContents = homeContentService.getAllHomeContents();
        model.addAttribute("homeContents", homeContents);
        return "home";
    }

    // 📌 Admin Home Content Management Dashboard
    @GetMapping("/admin/home/dashboard")
    public String showAdminHomeDashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }
        List<HomeContent> homeContents = homeContentService.getAllHomeContents();
        model.addAttribute("homeContents", homeContents);
        return "dashboard"; // Admin Home Content Management Page
    }

    // 📌 Add Home Content (Admin Only)
    @PostMapping("/admin/home/add")
    public String addHomeContent(@RequestParam String title, @RequestParam String description,
                                 @RequestParam MultipartFile imageFile, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }
        try {
            String imageUrl = homeContentService.uploadImage(imageFile);
            HomeContent newContent = new HomeContent();
            newContent.setTitle(title);
            newContent.setDescription(description);
            newContent.setImageUrl(imageUrl);
            homeContentService.addHomeContent(newContent);
            redirectAttributes.addFlashAttribute("success", "Home content added successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("failed", "Failed to upload image.");
        }
        return "redirect:/admin/home/dashboard";
    }

    // 📌 Delete Home Content (Admin Only)
    @PostMapping("/admin/home/delete/{id}")
    public String deleteHomeContent(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }
        homeContentService.deleteHomeContent(id);
        redirectAttributes.addFlashAttribute("success", "Home content deleted successfully!");
        return "redirect:/dashboard";
    }

    // 📌 Update Home Content (Admin Only)
    @PostMapping("/admin/home/update/{id}")
    public String updateHomeContent(@PathVariable Long id, @RequestParam String title, 
                                    @RequestParam String description, @RequestParam(required = false) MultipartFile imageFile,
                                    HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/login";
        }

        HomeContent existingContent = homeContentService.getHomeContentById(id);
        if (existingContent != null) {
            existingContent.setTitle(title);
            existingContent.setDescription(description);

            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String imageUrl = homeContentService.uploadImage(imageFile);
                    existingContent.setImageUrl(imageUrl);
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("error", "Image upload failed.");
                    return "redirect:/dashboard";
                }
            }

            homeContentService.updateHomeContent(existingContent);
            redirectAttributes.addFlashAttribute("success", "Home content updated successfully!");
        }

        return "redirect:/dashboard";
    }

}
