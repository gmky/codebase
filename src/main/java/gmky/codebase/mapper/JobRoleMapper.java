package gmky.codebase.mapper;

import gmky.codebase.api.model.JobRoleResponse;
import gmky.codebase.model.entity.JobRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobRoleMapper extends EntityMapper<JobRoleResponse, JobRole> {
}
