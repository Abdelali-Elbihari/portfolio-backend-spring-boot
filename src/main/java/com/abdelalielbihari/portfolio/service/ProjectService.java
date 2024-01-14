package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.Project;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProjectService {

  List<Project> getAllProjects();
  Optional<Project> getProjectById(String id);
  Project addProject(Project project, MultipartFile image) throws IOException;

  Optional<Project> updateProject(String id, Project project, MultipartFile image) throws IOException;

  void deleteProject(String id);
}
