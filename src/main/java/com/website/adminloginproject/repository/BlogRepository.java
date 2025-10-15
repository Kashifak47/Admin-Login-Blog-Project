package com.website.adminloginproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.website.adminloginproject.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {

}
