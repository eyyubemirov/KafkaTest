package az.company.microserviceapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {
    @NotBlank(message = "error.emptyName")
    private String name;

    @NotBlank(message = "error.emptySurname")
    private String surname;

    @NotBlank(message = "error.emptyGroupName")
    private String groupName;

}
