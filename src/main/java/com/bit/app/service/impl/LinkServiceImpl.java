package com.bit.app.service.impl;

import com.bit.app.entity.Link;
import com.bit.app.repository.LinkRepository;
import com.bit.app.service.LinkService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinkRepository linkRepository;
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public String getOriginalUrl(String shortCode) {
        Link link = linkRepository.findUrlByShortCode(shortCode);
        if (link == null)
            return "";
        return link.getUrl();
    }

    @Override
    public String shorten(String url) {
        Link link = new Link();
        link.setUrl(url);
        link.setShortCode("");
        link = linkRepository.save(link);
        String code = encode(link.getId());
        link.setShortCode(code);
        return code;
    }

    private String encode(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.insert(0, BASE62.charAt((int) (id % 62)));
            id /= 62;
        }
        return sb.toString();
    }
}
