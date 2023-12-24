package com.cydeo.controller;


import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/project")
@Tag(name = "Project",description = "Project API")
public class ProjectController {

    private final ProjectService projectService;
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Get all projects")
    public ResponseEntity<ResponseWrapper> getProjects() {

        List<ProjectDTO> projects = projectService.listAllProjects();
        String message = projects.isEmpty() ? "There are no projects to show" : "projects retrieved Successfully";

        return ResponseEntity.ok(
                new ResponseWrapper(message, projects, HttpStatus.OK)
        );
    }

    @GetMapping("/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Get project by project code")
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
    @RolesAllowed("Manager")
    @Operation(summary = "Create a project")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) {
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper("A new project is created", HttpStatus.CREATED));

    }

    @PutMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Update a project")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO) {
        projectService.update(projectDTO);
        return ResponseEntity.ok(
                new ResponseWrapper("Project updated successfully", projectDTO, HttpStatus.ACCEPTED)
        );
    }

    @DeleteMapping("/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Delete a project by project code")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable String projectCode) {
        projectService.delete(projectCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseWrapper("Project deleted successfully", HttpStatus.NO_CONTENT));
    }

    @GetMapping("/manager/project-status")
    @RolesAllowed("Manager")
    @Operation(summary = "Get project status")
    public ResponseEntity<ResponseWrapper> getProjectsStatus() throws TicketingProjectException {
        List<ProjectDTO> projects = projectService.listAllProjectDetails();
        return ResponseEntity.ok(
                new ResponseWrapper("Successfully retrieved completed projects", projects, HttpStatus.OK)
        );
    }


    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed("Manager")
    @Operation(summary = "Complete project by project code")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable String projectCode) {
        projectService.complete(projectCode);
        return ResponseEntity.ok(
                new ResponseWrapper("Project marked for completion successfully", projectCode, HttpStatus.ACCEPTED)
        );
    }






}














