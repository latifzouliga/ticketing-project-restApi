package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.exception.TicketingProjectException;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id);
    List<TaskDTO> listAllTasks();
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);
    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);

    void deleteByProject(ProjectDTO project);

    void completeByProject(ProjectDTO project);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status) throws TicketingProjectException;

    void updateStatus(TaskDTO task);

    List<TaskDTO> listAllTasksByStatus(Status status) throws TicketingProjectException;

    List<TaskDTO> readAllByAssignedEmployee(User assignedEmployee);

}
