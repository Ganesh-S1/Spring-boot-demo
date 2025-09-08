package com.example.springbootdemo.web;

import com.example.springbootdemo.model.UrlMapping;
import com.example.springbootdemo.service.UrlService;
import com.example.springbootdemo.web.dto.CreateUrlRequest;
import com.example.springbootdemo.web.dto.UrlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RestController
public class UrlController {

  private final UrlService service;

  public UrlController(UrlService service) {
    this.service = service;
  }

  /** Create short URL: POST /api/urls  { "url": "https://example.com" } */
  @PostMapping("/api/urls")
  public ResponseEntity<UrlResponse> create(@RequestBody CreateUrlRequest req) {
    if (req == null || req.getUrl() == null || req.getUrl().isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    UrlMapping m = service.create(req.getUrl());
    String base = ServletUriComponentsBuilder.fromCurrentContextPath()
                   .build().toUriString();
    String shortUrl = base + "/" + m.getCode();
    UrlResponse resp = new UrlResponse(m.getCode(), shortUrl, m.getTargetUrl(), m.getHitCount());
    return ResponseEntity.created(URI.create(shortUrl)).body(resp);
  }

  /** Redirect: GET /{code} -> 302 to target URL (404 if not found) */
  @GetMapping("/{code}")
  public RedirectView redirect(@PathVariable String code) {
    return service.hit(code)
        .map(m -> {
          RedirectView rv = new RedirectView(m.getTargetUrl());
          rv.setExposeModelAttributes(false);
          return rv;
        })
        .orElseGet(() -> {
          RedirectView rv = new RedirectView("/404");
          rv.setStatusCode(org.springframework.http.HttpStatus.NOT_FOUND);
          rv.setExposeModelAttributes(false);
          return rv;
        });
  }

  /** Metadata: GET /api/urls/{code} */
  @GetMapping("/api/urls/{code}")
  public ResponseEntity<UrlResponse> info(@PathVariable String code) {
    return service.find(code)
        .map(m -> {
          String base = ServletUriComponentsBuilder.fromCurrentContextPath()
                         .build().toUriString();
          String shortUrl = base + "/" + m.getCode();
          return ResponseEntity.ok(new UrlResponse(m.getCode(), shortUrl, m.getTargetUrl(), m.getHitCount()));
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /** Delete: DELETE /api/urls/{code} */
  @DeleteMapping("/api/urls/{code}")
  public ResponseEntity<Void> delete(@PathVariable String code) {
    service.delete(code);
    return ResponseEntity.noContent().build();
  }
}