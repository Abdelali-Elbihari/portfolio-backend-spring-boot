package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.Project;
import com.abdelalielbihari.portfolio.repository.ProjectRepository;
import com.abdelalielbihari.portfolio.util.UrlCache;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final ImageService imageService;
  private final UrlCache urlCache;

  @Override
  @Cacheable(value = "projects", key = "'allProjects'")
  public List<Project> getAllProjects() {
    return projectRepository.findAll();
  }

  @Override
  @Cacheable(value = "projects", key = "#id")
  public Optional<Project> getProjectById(String id) {
    return projectRepository.findById(id).map(project -> {
      project.setImgUrl(urlCache.getOrGeneratePresignedImgUrl(project.getImgUrl()));
      return project;
    });
  }

  @Override
  public Project addProject(Project project, MultipartFile image) throws IOException {
    return handleSaveWithImage(project, image);
  }

  @Override
  public Optional<Project> updateProject(String id, Project project, MultipartFile image) throws IOException {
    Optional<Project> currentProject = projectRepository.findById(id);

    if (currentProject.isPresent()) {
      evictProjectCache(currentProject.get());
      Project updatedProject = currentProject.get();
      updatedProject.setTitle(project.getTitle());
      updatedProject.setDescription(project.getDescription());
      updatedProject.setProjectLink(project.getProjectLink());
      updatedProject.setCodeLink(project.getCodeLink());
      updatedProject.setTags(project.getTags());
      return Optional.of(handleSaveWithImage(updatedProject, image));
    }

    return currentProject;
  }

  @Override
  @CacheEvict(value = "projects", key = "#id")
  public void deleteProject(String id) {
    projectRepository
        .findById(id)
        .ifPresent(p -> {
          projectRepository.deleteById(p.getId());
          urlCache.evictCacheForImage(p.getImgUrl());
          // todo delete image from s3
        });

  }

  private Project handleSaveWithImage(Project project, MultipartFile image) throws IOException {
    // todo replace image instead
    project.setImgUrl(imageService.uploadImage(image));

    //Save entity with normal url
    Project newProject = projectRepository.save(project);

    //get Or Generate Presigned Url
    newProject.setImgUrl(urlCache.getOrGeneratePresignedImgUrl(newProject.getImgUrl()));
    return newProject;
  }

  @CacheEvict(value = "projects", key = "#project.id")
  public void evictProjectCache(Project project) {
    urlCache.evictCacheForImage(project.getImgUrl());
  }
}
