package com.website.adminloginproject.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.website.adminloginproject.entity.Contact;
import com.website.adminloginproject.repository.ContactRepository;

@Controller
public class ContactController {

	@Autowired
	private ContactRepository contactRepository;
	
	
	public ContactController(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}
	

	@GetMapping("/contact-us")
	public String showContactForm(Model model) {
		model.addAttribute("contact", new Contact());
		return "contact";
	}
	
	@PostMapping("/contact-us")
	public String submitContactForm(@ModelAttribute Contact contact) {
		contactRepository.save(contact);
		return "redirect:/contact-us?success";
	}
	
	@GetMapping("/admin/edit-contact/{id}")
	public String editContact(@PathVariable Long id, Model model) {
		Optional<Contact> contactOptional = contactRepository.findById(id);
		if(contactOptional.isPresent()) {
			model.addAttribute("contact", contactOptional.get());
			return "edit-contact";
		}
		else {
			return "redirect:/dashboard";
		}
	}
	
	@PostMapping("/admin/update-contact")
	public String updateContact(@ModelAttribute Contact contact, RedirectAttributes redirectAttributes) {
		contactRepository.save(contact);
		redirectAttributes.addFlashAttribute("successMessage", "Contact Updated Successfully");
		return "redirect:/admin/edit-contact/"+contact.getId();
	}
	
	@PostMapping("/admin/delete-contact/{id}")
	public String deleteContact(@PathVariable Long id) {
		contactRepository.deleteById(id);
		return "redirect:/dashboard?deleteSuccess";
	}
}
