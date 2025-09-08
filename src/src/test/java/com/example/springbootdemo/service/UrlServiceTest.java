package com.example.springbootdemo.service;

import com.example.springbootdemo.model.UrlMapping;
import com.example.springbootdemo.repo.UrlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlServiceTest {

    private UrlMappingRepository repo;
    private UrlService service;

    @BeforeEach
    void setup() {
        repo = mock(UrlMappingRepository.class);
        service = new UrlService(repo);
    }

    @Test
    void create_generatesUniqueCodeAndSaves() {
        when(repo.existsByCode(anyString())).thenReturn(false);

        UrlMapping saved = new UrlMapping();
        saved.setId(1L);
        saved.setCode("abc123");
        saved.setTargetUrl("https://example.com");

        when(repo.save(any(UrlMapping.class))).thenReturn(saved);

        UrlMapping result = service.create("https://example.com");

        assertNotNull(result.getCode());
        assertEquals("https://example.com", result.getTargetUrl());
        verify(repo).save(any(UrlMapping.class));
    }

    @Test
    void find_returnsMappingIfExists() {
        UrlMapping m = new UrlMapping();
        m.setCode("abc123");
        m.setTargetUrl("https://example.com");

        when(repo.findByCode("abc123")).thenReturn(Optional.of(m));

        Optional<UrlMapping> result = service.find("abc123");

        assertTrue(result.isPresent());
        assertEquals("https://example.com", result.get().getTargetUrl());
    }

    @Test
    void hit_incrementsHitCount() {
        UrlMapping m = new UrlMapping();
        m.setCode("abc123");
        m.setTargetUrl("https://example.com");
        m.setHitCount(0);

        when(repo.findByCode("abc123")).thenReturn(Optional.of(m));

        Optional<UrlMapping> result = service.hit("abc123");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getHitCount());
    }
}