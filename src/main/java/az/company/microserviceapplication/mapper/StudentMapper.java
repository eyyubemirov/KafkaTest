package az.company.microserviceapplication.mapper;

import az.company.microserviceapplication.dto.StudentResponse;
import az.company.microserviceapplication.dto.request.CreateStudentRequest;
import az.company.microserviceapplication.dto.request.UpdateStudentRequest;
import az.company.microserviceapplication.model.Student;
import org.mapstruct.*;
//3 CU  mapping update edir ve mappere yazilan 3 cu config  onu edirki
// putmapping zamani nul geden deyerelere
// null  vermir evvvelki kimi saxlayir deyismir
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {

    @Mapping(target = "id",ignore = true)
    Student mapRequestToStudent(CreateStudentRequest request);


    StudentResponse  mapEntityToResponse(Student student);

    Student mapUpdateResponseToEntitiy(@MappingTarget Student student, UpdateStudentRequest request);
}
