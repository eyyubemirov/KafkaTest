package az.company.microserviceapplication.controller;

import az.company.microserviceapplication.dto.StudentResponse;
import az.company.microserviceapplication.dto.request.CreateStudentRequest;
import az.company.microserviceapplication.dto.request.UpdateStudentRequest;
import az.company.microserviceapplication.service.StudentServiceimpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentServiceimpl studentServiceimpl;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity CreateStudent(@Valid @RequestBody CreateStudentRequest request) {
        studentServiceimpl.createStudent(request);
        return ResponseEntity.ok(request);
    }
    @CircuitBreaker(name = "getAllStudent")
    @GetMapping("/getAll")
    public List<StudentResponse> getAllStudent() {
        return studentServiceimpl.getAllStudent();
    }

    @GetMapping("/get")
    public StudentResponse getById(@RequestParam Long id) {
        return studentServiceimpl.getById(id);
    }

    @DeleteMapping("/delete")
    public void deleteById(@RequestParam Long id) {
        studentServiceimpl.deleteById(id);
    }

    @PutMapping("update")
    public StudentResponse updateStudent(@RequestParam Long id, @RequestBody UpdateStudentRequest request) {
        return studentServiceimpl.updateStudent(id, request);
    }
}
