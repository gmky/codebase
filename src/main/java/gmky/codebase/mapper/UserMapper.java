package gmky.codebase.mapper;

import gmky.codebase.api.model.CreateUserReq;
import gmky.codebase.api.model.UpdateUserReq;
import gmky.codebase.model.entity.User;
import org.mapstruct.Mapper;
import gmky.codebase.api.model.UserResponse;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserResponse, User> {
    User toEntity(CreateUserReq req);

    User partialUpdate(@MappingTarget User entity, UpdateUserReq req);
}
