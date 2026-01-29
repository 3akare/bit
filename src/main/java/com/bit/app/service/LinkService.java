package com.bit.app.service;

public interface LinkService {
    String redirectURL(String shortCode);
    void shorten(String url);
}
