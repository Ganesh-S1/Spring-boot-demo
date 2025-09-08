package com.example.springbootdemo.service;

import com.example.springbootdemo.model.UrlMapping;
import com.example.springbootdemo.repo.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UrlService {

  private final UrlMappingRepository repo;
  private final SecureRandom random = new SecureRandom();
  private static final char[] ALPHABET =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

  public UrlService(UrlMappingRepository repo) {
    this.repo = repo;
  }

  /** Create a new short code for a target URL. */
  @Transactional
  public UrlMapping create(String targetUrl) {
    String code = generateUniqueCode(6);
    UrlMapping m = new UrlMapping();
    m.setCode(code);
    m.setTargetUrl(targetUrl);
    return repo.save(m);
  }

  /** Increment hit counter if code exists and return mapping. */
  @Transactional
  public Optional<UrlMapping> hit(String code) {
    return repo.findByCode(code).map(m -> {
      m.setHitCount(m.getHitCount() + 1);
      return m; // dirty-tracked; will flush on tx commit
    });
  }

  public Optional<UrlMapping> find(String code) {
    return repo.findByCode(code);
  }

  @Transactional
  public void delete(String code) {
    repo.findByCode(code).ifPresent(repo::delete);
  }

  // --- helpers ---

  private String generateUniqueCode(int len) {
    while (true) {
      String c = randomCode(len);
      if (!repo.existsByCode(c)) return c;
    }
  }

  private String randomCode(int len) {
    char[] buf = new char[len];
    for (int i = 0; i < len; i++) buf[i] = ALPHABET[random.nextInt(ALPHABET.length)];
    return new String(buf);
  }
}