package com.example.model.mapper;

import com.example.model.Task;
import com.example.model.User;
import com.example.model.dto.TaskDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "user.name", target = "name")
    })
    @Named("taskToTaskDTO")
    TaskDTO taskToTaskDTO(Task task);

    @IterableMapping(qualifiedByName = "taskToTaskDTO")
    List<TaskDTO> taskToTaskDTO(List<Task> tasks);

    @Mapping(source = "taskDTO.id", target = "id")
    Task taskDTOToTask(TaskDTO taskDTO, User user);
}
