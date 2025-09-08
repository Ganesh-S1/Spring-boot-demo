package com.example.springbootdemo.web;

import com.example.springbootdemo.model.UrlMapping;
import com.example.springbootdemo.service.UrlService;
import com.example.springbootdemo.web.dto.CreateUrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlControllerTest {

    @Test
    void info_not_found_returns_404() {
        UrlService svc = mock(UrlService.class);
        when(svc.find("nope")).thenReturn(Optional.empty());
        UrlController ctrl = new UrlController(svc);

        ResponseEntity<?> res = ctrl.info("nope");

        assertEquals(404, res.getStatusCode().value());
    }

    @Test
    void create_valid_returns_201() {
        UrlService svc = mock(UrlService.class);

        UrlMapping m = new UrlMapping();
        m.setCode("abc123");
        m.setTargetUrl("https://example.com");
        when(svc.create("https://example.com")).thenReturn(m);

        UrlController ctrl = new UrlController(svc);

        CreateUrlRequest req = new CreateUrlRequest();
        req.setUrl("https://example.com");

        ResponseEntity<?> res = ctrl.create(req);

        assertEquals(201, res.getStatusCode().value());
    }
}