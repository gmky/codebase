package gmky.codebase.mapper;

import gmky.codebase.model.entity.JobRole;
import gmky.codebase.api.model.JobRoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobRoleMapper extends EntityMapper<JobRoleResponse, JobRole> {
}
