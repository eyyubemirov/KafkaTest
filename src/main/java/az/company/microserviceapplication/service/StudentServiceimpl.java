package az.company.microserviceapplication.service;

import az.company.microserviceapplication.dto.StudentResponse;
import az.company.microserviceapplication.dto.request.CreateStudentRequest;
import az.company.microserviceapplication.dto.request.UpdateStudentRequest;
import az.company.microserviceapplication.exception.StudentNotFoundException;
import az.company.microserviceapplication.mapper.StudentMapper;
import az.company.microserviceapplication.repositroy.StudentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceimpl {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private  final KafkaTemplate<String,StudentResponse> kafkaTemplate;


        public void createStudent(CreateStudentRequest studentRequest){

        studentRepository.save(studentMapper.mapRequestToStudent(studentRequest));
    }
    @CircuitBreaker(name = "getAllStudent")
//     @Cacheable(cacheNames = "students",key = "'getAllStudent'")
   public    List<StudentResponse> getAllStudent(){
        var students= studentRepository.findAll()
                .stream()
                .map(studentMapper::mapEntityToResponse)
                .collect(Collectors.toList());
    log.info("Students: {}",students);
        students.forEach(student->kafkaTemplate.send("student-topic",student));
        return students;
    }

    @Cacheable(cacheNames = "students",key = "#id")
    public  StudentResponse getById(Long id){
        var student= studentRepository.findById(id)
                .orElseThrow(this::exStudentNotFound);
        return studentMapper.mapEntityToResponse(student);
    }

    @CacheEvict(cacheNames = "students",key = "#id")
    public StudentResponse updateStudent(Long id, UpdateStudentRequest request){
        var student=studentRepository.findById(id)
                .orElseThrow(this::exStudentNotFound);
        studentMapper.mapUpdateResponseToEntitiy(student,request);
        studentRepository.save(student);

        return studentMapper.mapEntityToResponse(student);


    }

    public  void deleteById(Long id){
        var student= studentRepository.findById(id)
                .orElseThrow(this::exStudentNotFound);
        studentRepository.deleteById(student.getId());

    }

    public  StudentNotFoundException  exStudentNotFound ( ){
        throw new StudentNotFoundException();
    }

}
