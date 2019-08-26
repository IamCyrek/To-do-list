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

    @Mapping(source = "user.id", target = "userId")
    @Named("taskToTaskDTO")
    TaskDTO taskToTaskDTO(Task task);

    @IterableMapping(qualifiedByName = "taskToTaskDTO")
    List<TaskDTO> taskToTaskDTO(List<Task> tasks);

    @Mappings({
            @Mapping(source = "taskDTO.id", target = "id"),
            @Mapping(source = "taskDTO.content", target = "content"),
            @Mapping(source = "taskDTO.priority", target = "priority"),
            @Mapping(source = "taskDTO.creationTime", target = "creationTime"),
            @Mapping(source = "taskDTO.isRemoved", target = "isRemoved")
    })
    Task taskDTOToTask(TaskDTO taskDTO, User user);
}
