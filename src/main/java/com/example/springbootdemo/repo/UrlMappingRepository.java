package com.example.springbootdemo.repo;

import com.example.springbootdemo.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
  Optional<UrlMapping> findByCode(String code);
  boolean existsByCode(String code);
}