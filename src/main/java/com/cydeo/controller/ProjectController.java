package com.cydeo.controller;


import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.entity.User;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private UserService userService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects() {
        List<ProjectDTO> projects = projectService.listAllProjects();
        return ResponseEntity.ok(
                new ResponseWrapper("Successfully retrieved projects", projects, HttpStatus.OK)
        );
    }

    @GetMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> getProject(@PathVariable String projectCode) {
        ProjectDTO projectDTO = projectService.getByProjectCode(projectCode);
        return ResponseEntity.ok(
                new ResponseWrapper(
                        "Successfully retrieved project",
                        projectDTO,
                        HttpStatus.OK)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) {
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper("A new project is created", HttpStatus.CREATED));

    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO) {
        projectService.update(projectDTO);
        return ResponseEntity.ok(
                new ResponseWrapper("Project updated successfully", projectDTO, HttpStatus.ACCEPTED)
        );
    }

    @DeleteMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable String projectCode) {
        projectService.delete(projectCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseWrapper("Project deleted successfully", HttpStatus.NO_CONTENT));
    }

    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectsStatus() {
        List<ProjectDTO> projects = projectService.listAllProjectDetails();
        return ResponseEntity.ok(
                new ResponseWrapper("Successfully retrieved completed projects", projects, HttpStatus.OK)
        );
    }


    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable String projectCode) {
        projectService.complete(projectCode);
        return ResponseEntity.ok(
                new ResponseWrapper("Project marked for completion successfully", projectCode, HttpStatus.ACCEPTED)
        );
    }






}














