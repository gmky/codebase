package gmky.codebase.mapper;

import gmky.codebase.api.model.CreateUserReq;
import gmky.codebase.api.model.RegisterUserReq;
import gmky.codebase.api.model.UpdateUserReq;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.model.entity.User;
import gmky.codebase.util.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MapperUtil.class})
public interface UserMapper extends EntityMapper<UserResponse, User> {

    User toEntity(CreateUserReq req);

    User partialUpdate(@MappingTarget User entity, UpdateUserReq req);

    @Mapping(target = "status", expression = "java(gmky.codebase.enumeration.UserStatusEnum.PENDING)")
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    User toEntity(RegisterUserReq req);
}
