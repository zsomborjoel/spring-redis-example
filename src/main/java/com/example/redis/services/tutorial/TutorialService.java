package com.example.redis.services.tutorial;

import com.example.redis.entities.Tutorial;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TutorialService {

    private final TutorialRepository tutorialRepository;

    @Cacheable("tutorials")
    public List<Tutorial> findAll() {
        doLongRunningTask();

        return tutorialRepository.findAll();
    }

    @Cacheable("tutorial")
    public List<Tutorial> findByTitleContaining(String title) {
        doLongRunningTask();

        return tutorialRepository.findByTitleContaining(title);
    }

    @Cacheable("tutorial")
    public Optional<Tutorial> findById(long id) {
        doLongRunningTask();

        return tutorialRepository.findById(id);
    }

    public Tutorial save(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }

    // in order to refresh data in cache
    @CacheEvict(value = "tutorial", key = "#tutorial.id")
    public Tutorial update(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }

    @CacheEvict(value = "tutorial", key = "#tutorial.id")
    public void deleteById(long id) {
        tutorialRepository.deleteById(id);
    }

    // Data under these values will be removed
    @CacheEvict(value = { "tutorial", "tutorials", "published_tutorials" }, allEntries = true)
    public void deleteAll() {
        tutorialRepository.deleteAll();
    }

    @Cacheable("published_tutorials")
    public List<Tutorial> findByPublished(boolean isPublished) {
        doLongRunningTask();

        return tutorialRepository.findByPublished(isPublished);
    }

    /**
     * In order to represent delay between cache and DB call
     */
    private void doLongRunningTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
