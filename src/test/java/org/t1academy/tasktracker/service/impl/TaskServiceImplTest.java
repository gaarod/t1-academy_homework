package org.t1academy.tasktracker.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.t1academy.tasktracker.dto.TaskDto;
import org.t1academy.tasktracker.entity.Task;
import org.t1academy.tasktracker.entity.TaskStatus;
import org.t1academy.tasktracker.exception.AppException;
import org.t1academy.tasktracker.mapper.TaskMapper;
import org.t1academy.tasktracker.mapper.TaskMapperImpl;
import org.t1academy.tasktracker.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.t1academy.tasktracker.utils.TaskBuilder.getTask;
import static org.t1academy.tasktracker.utils.TaskBuilder.getTaskDto;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Spy
    private final TaskMapper taskMapper = new TaskMapperImpl();

    @Mock
    private KafkaTaskStatusProducer kafkaTaskStatusProducer;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void getTaskById_ShouldReturnTaskDto() {
        Long taskId = 1L;
        Task task = getTask(taskId,1L,TaskStatus.TO_DO);
        TaskDto taskDto = getTaskDto(taskId, 1L, TaskStatus.TO_DO);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDto result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(taskDto, result);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskMapper, times(1)).toDto(task);
    }

    @Test
    public void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> taskService.getTaskById(taskId));
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void getAllTasks_ShouldReturnAllTasks() {
        Long taskId = 1L;
        Task task = getTask(taskId, 1L, TaskStatus.TO_DO);
        TaskDto taskDto = getTaskDto(taskId, 1L, TaskStatus.TO_DO);
        List<TaskDto> expected = List.of(taskDto);

        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskDto> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        assertEqualsTaskDtoList(expected, result);
        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, times(1)).toDto(task);
    }

    @Test
    public void createTask_ShouldReturnTaskDto() {
        Long taskId = 1L;
        TaskDto requestTaskDto = getTaskDto(null, 1L, TaskStatus.TO_DO);
        Task task = getTask(null, 1L, TaskStatus.TO_DO);
        Task savedTask = getTask(taskId, 1L, TaskStatus.TO_DO);
        TaskDto responseTaskDto = getTaskDto(taskId, 1L, TaskStatus.TO_DO);

        when(taskRepository.save(task)).thenReturn(savedTask);

        TaskDto result = taskService.createTask(requestTaskDto);

        assertNotNull(result);
        assertEquals(responseTaskDto, result);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toEntity(requestTaskDto);
        verify(taskMapper, times(1)).toDto(savedTask);

    }

    @Test
    public void updateTask_ShouldUpdateAndSendNotification_WhenTaskStatusChanges() {
        Long taskId = 1L;
        TaskDto updateTaskDto = getTaskDto(null, 1L, TaskStatus.IN_PROGRESS);
        Task existingTask = getTask(taskId, 1L, TaskStatus.TO_DO);
        Task updatedTask = getTask(taskId, 1L, TaskStatus.IN_PROGRESS);
        TaskDto responseTaskDto = getTaskDto(taskId, 1L, TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        TaskDto result = taskService.updateTask(taskId, updateTaskDto);

        assertNotNull(result);
        assertEquals(responseTaskDto, result);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(updatedTask);
        verify(taskMapper, times(1)).toDto(updatedTask);
        verify(kafkaTaskStatusProducer, times(1)).sendTaskStatusUpdate(any());
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        TaskDto updateTaskDto = getTaskDto(null, 1L, TaskStatus.TO_DO);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> taskService.updateTask(taskId, updateTaskDto));
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteTaskById(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);
        assertThrows(AppException.class, () -> taskService.deleteTaskById(taskId));
    }



    public static void assertEqualsTaskDtoList(List<TaskDto> expectedList, List<TaskDto> resultList) {

        for ( int i = 0; i < expectedList.size(); i++ ) {
            TaskDto expected = expectedList.get(i);
            TaskDto result = resultList.get(i);
            assertEquals(expected, result);
        }
    }

}