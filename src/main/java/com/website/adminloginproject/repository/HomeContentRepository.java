package com.website.adminloginproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.website.adminloginproject.entity.HomeContent;

@Repository
public interface HomeContentRepository extends JpaRepository<HomeContent, Long> {

}
