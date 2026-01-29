package com.bit.app.service;

public interface LinkService {
    String getOriginalUrl(String shortCode);
    String shorten(String url);
}
