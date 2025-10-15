package com.website.adminloginproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.website.adminloginproject.entity.Admin;
import com.website.adminloginproject.entity.Contact;
import com.website.adminloginproject.entity.GalleryItem;
import com.website.adminloginproject.repository.AdminRepository;
import com.website.adminloginproject.repository.ContactRepository;
import com.website.adminloginproject.service.GalleryItemService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private AdminRepository adminRepository;
    @Autowired
	private ContactRepository contactRepository;
    @Autowired
    private GalleryItemService galleryItemService;
	
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model,
			RedirectAttributes redirectAttributes) {
		Optional<Admin> adminOptional = adminRepository.findByUsername(username);
		
		if(adminOptional.isPresent() && adminOptional.get().getPassword().equals(password)) {
			session.setAttribute("loggedInAdmin", adminOptional.get());
			return "redirect:/dashboard";
		}
		else {
			redirectAttributes.addFlashAttribute("error", "Invalid Username or Password");
			return "redirect:/login";
		}
	}
	
	@GetMapping("dashboard")
	public String showDashboard(HttpSession session, Model model) {
		Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
		
		if(loggedInAdmin == null) {
			return "redirect:/login";
		}
		List<Contact> contacts = contactRepository.findAll();
        List<GalleryItem> galleryItems = galleryItemService.getAllGalleryItems(); 

		model.addAttribute("admin", loggedInAdmin);
		model.addAttribute("contacts", contacts);
		model.addAttribute("galleryItems",galleryItems);
		return "dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	@PostMapping("/admin/update-credentials")
	public String updateAdminCredentials(@RequestParam String newUsername, @RequestParam String newPassword, HttpSession session) {
		Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
		if(loggedInAdmin != null) {
			loggedInAdmin.setUsername(newUsername);
			loggedInAdmin.setPassword(newPassword);
			adminRepository.save(loggedInAdmin);
			session.setAttribute("loggedInAdmin", loggedInAdmin);
		}
		return "redirect:/dashboard";
	}
	
	
}
