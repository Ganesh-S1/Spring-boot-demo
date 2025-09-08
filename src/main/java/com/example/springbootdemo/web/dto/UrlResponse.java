package com.example.springbootdemo.web.dto;

public class UrlResponse {
  public String code;
  public String shortUrl;
  public String url;
  public long hits;

  public UrlResponse(String code, String shortUrl, String url, long hits) {
    this.code = code; this.shortUrl = shortUrl; this.url = url; this.hits = hits;
  }
}