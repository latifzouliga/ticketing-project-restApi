package com.cydeo.controller;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllTasks() {
        List<TaskDTO> taskDTOList = taskService.listAllTasks();
        return ResponseEntity.ok(
                new ResponseWrapper("Tasks retrieved successfully",taskDTOList ,HttpStatus.OK)
        );
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseWrapper> getTask(@PathVariable Long taskId) {
        TaskDTO taskDTO = taskService.findById(taskId);
        return ResponseEntity.ok(new ResponseWrapper("Task retrieved successfully",taskDTO ,HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO) {
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper("A new task is successfully created", HttpStatus.CREATED));
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseWrapper("Task retrieved successfully", HttpStatus.NO_CONTENT));
    }


    @PutMapping
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO ) {

        taskService.update(taskDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseWrapper("Task updated successfully", HttpStatus.OK));

    }



    @GetMapping("/employee/pending-tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks() {
       List<TaskDTO> taskDTOList = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(
                new ResponseWrapper("Pending tasks retrieved successfully",taskDTOList ,HttpStatus.OK)
        );
    }

    @PutMapping("/employee/update")
    public ResponseEntity<ResponseWrapper> employeeUpdateTaskStatus(@RequestBody TaskDTO taskDTO) {
        taskService.updateStatus(taskDTO);
        return ResponseEntity.ok(
                new ResponseWrapper("Tasks status updated successfully" ,HttpStatus.OK)
        );
    }



    @GetMapping("/employee/archive")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks() {
        List<TaskDTO> taskDTOList = taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(
                new ResponseWrapper("Completed tasks retrieved successfully",taskDTOList ,HttpStatus.OK)
        );
    }


}
