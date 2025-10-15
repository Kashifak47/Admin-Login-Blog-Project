package com.website.adminloginproject.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.website.adminloginproject.entity.Blog;
import com.website.adminloginproject.service.BlogService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BlogController {

	@Autowired
	private BlogService blogService;
	
	@GetMapping("/blog")
	public String showBlog(Model model) {
		List<Blog> blogs = blogService.getAllBlogs();
		model.addAttribute("blogs", blogs);
		return "blog";
	}
	
	 @GetMapping("/admin/blog/dashboard")
	    public String showAdminBlog(Model model, HttpSession session) {
	        if (session.getAttribute("loggedInAdmin") == null) {
	            return "redirect:/login";
	        }
	        List<Blog> blogs = blogService.getAllBlogs();
	        model.addAttribute("blogs", blogs);
	        return "dashboard";
	    }
	
	@PostMapping("/admin/blog/add")
	public String addBlog(@RequestParam String title, @RequestParam String description, @RequestParam String content,
			@RequestParam MultipartFile imageFile) {
		Blog blog = new Blog();
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = blogService.uploadImage(imageFile);
                blog.setImageUrl(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blogService.addBlog(blog);
        return "redirect:/dashboard";
	}
	
}
