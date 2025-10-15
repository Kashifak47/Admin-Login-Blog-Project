package com.website.adminloginproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.website.adminloginproject.entity.GalleryItem;

public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {

}
