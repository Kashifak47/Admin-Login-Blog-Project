package com.website.adminloginproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.website.adminloginproject.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
