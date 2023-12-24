package com.cydeo.controller;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.enums.Status;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/task")
@Tag(name = "Task",description = "Task API")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Get all tasks")
    public ResponseEntity<ResponseWrapper> getAllTasks() {
        List<TaskDTO> taskDTOList = taskService.listAllTasks();
        return ResponseEntity.ok(
                new ResponseWrapper("Tasks retrieved successfully",taskDTOList ,HttpStatus.OK)
        );
    }

    @GetMapping("/{taskId}")
    @RolesAllowed("Manager")
    @Operation(summary = "Get a task by id")
    public ResponseEntity<ResponseWrapper> getTask(@PathVariable Long taskId) {
        TaskDTO taskDTO = taskService.findById(taskId);
        return ResponseEntity.ok(new ResponseWrapper("Task retrieved successfully",taskDTO ,HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Create a task")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO) {
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper("A new task is successfully created", HttpStatus.CREATED));
    }


    @DeleteMapping("/{taskId}")
    @RolesAllowed("Manager")
    @Operation(summary = "Delete a task by id")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseWrapper("Task retrieved successfully", HttpStatus.NO_CONTENT));
    }


    @PutMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Update task")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO ) {

        taskService.update(taskDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseWrapper("Task updated successfully", HttpStatus.OK));

    }



    @GetMapping("/employee/pending-tasks")
    @RolesAllowed("Employee")
    @Operation(summary = "Get pending tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks() throws TicketingProjectException {
       List<TaskDTO> taskDTOList = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(
                new ResponseWrapper("Pending tasks retrieved successfully",taskDTOList ,HttpStatus.OK)
        );
    }

    @PutMapping("/employee/update")
    @RolesAllowed("Employee")
    @Operation(summary = "Update a task")
    public ResponseEntity<ResponseWrapper> employeeUpdateTaskStatus(@RequestBody TaskDTO taskDTO) {
        taskService.updateStatus(taskDTO);
        return ResponseEntity.ok(
                new ResponseWrapper("Tasks status updated successfully" ,HttpStatus.OK)
        );
    }



    @GetMapping("/employee/archive")
    @RolesAllowed("Employee")
    @Operation(summary = "Get all archived tasks")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks() throws TicketingProjectException {
        List<TaskDTO> taskDTOList = taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(
                new ResponseWrapper("Completed tasks retrieved successfully",taskDTOList ,HttpStatus.OK)
        );
    }


}
