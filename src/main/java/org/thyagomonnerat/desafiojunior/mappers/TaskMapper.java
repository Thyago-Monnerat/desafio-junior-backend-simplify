package org.thyagomonnerat.desafiojunior.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.thyagomonnerat.desafiojunior.dtos.TaskDto;
import org.thyagomonnerat.desafiojunior.models.TaskModel;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskModel fromDtoToModel(TaskDto dto);
    TaskDto fromModelToDto(TaskModel model);

    @Mapping(target = "id", ignore = true)
    void updateModelFromDto(TaskDto dto, @MappingTarget TaskModel model);
}
