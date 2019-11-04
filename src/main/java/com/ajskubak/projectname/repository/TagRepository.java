package com.ajskubak.projectname.repository;

import com.ajskubak.projectname.model.TagModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagModel, Long>{}