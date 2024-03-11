package az.company.microserviceapplication.repositroy;

import az.company.microserviceapplication.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
