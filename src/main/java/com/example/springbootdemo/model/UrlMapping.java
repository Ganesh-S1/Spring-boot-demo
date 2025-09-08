package com.example.springbootdemo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
  name = "url_mapping",
  indexes = @Index(name = "idx_url_code", columnList = "code", unique = true)
)
public class UrlMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 12)
  private String code;

  @Column(nullable = false, length = 2048)
  private String targetUrl;

  @Column(nullable = false)
  private long hitCount = 0;

  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  // Getters / Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }

  public String getTargetUrl() { return targetUrl; }
  public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }

  public long getHitCount() { return hitCount; }
  public void setHitCount(long hitCount) { this.hitCount = hitCount; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}