package gmky.codebase.mapper;

import gmky.codebase.api.model.FunctionPrivilegeResponse;
import gmky.codebase.api.model.SummaryResponse;
import gmky.codebase.model.entity.FunctionPrivilege;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FunctionPrivilegeMapper extends EntityMapper<FunctionPrivilegeResponse, FunctionPrivilege> {
    default List<SummaryResponse> toSummary(Collection<FunctionPrivilege> entityList) {
        var data = entityList.stream().collect(Collectors.groupingBy(FunctionPrivilege::getBfCode));
        var result = new ArrayList<SummaryResponse>();
        data.forEach((key, value) -> {
            var tmp = new SummaryResponse();
            tmp.setName(key);
            tmp.setPrivileges(value.stream().map(FunctionPrivilege::getPrivilegeCode).toList());
            result.add(tmp);
        });
        return result;
    }
}
