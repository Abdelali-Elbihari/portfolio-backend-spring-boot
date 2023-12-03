package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.model.About;

import java.util.List;
import java.util.Optional;

public class AboutServiceImpl implements AboutService {
    @Override
    public Optional<About> getOneAbout(String id) {
        return Optional.empty();
    }

    @Override
    public List<About> getAllAbouts() {
        return null;
    }

    @Override
    public About addAbout(About about) {
        return null;
    }

    @Override
    public Optional<About> updateAbout(String id, About updatedAbout) {
        return Optional.empty();
    }

    @Override
    public void deleteAbout(String id) {

    }
}
