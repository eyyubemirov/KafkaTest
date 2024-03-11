package az.company.microserviceapplication.exception;

import az.company.microserviceapplication.service.TranslationRepoService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
private final TranslationRepoService translationRepoService;

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFoundException(GenericException ex,
                                                                        WebRequest request) {
        var path=((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return  createErrorResponse( ex, path);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                               WebRequest request) {
        var path=((ServletWebRequest) request).getRequest().getRequestURL().toString();
           List<String> errors=ex.getBindingResult().getFieldErrors()
                   .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        System.out.println(errors);
        var build=ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code(Objects.requireNonNull(ex.getFieldError()).getCode())
                .path(path)
                .timestamp(LocalDateTime.now())
                .detail(translationRepoService.findByKey(errors))
                .build();


        return ResponseEntity.badRequest().body(build);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ErrorResponse>handleCircuit(CallNotPermittedException exception,
                                                      WebRequest request){
        var path =((ServletWebRequest) request).getRequest().getRequestURL().toString();
       var build =ErrorResponse.builder()
               .status(HttpStatus.SERVICE_UNAVAILABLE.value())
               .code("SERVICE_UNAVAILABLE")
               .path(path)
               .timestamp(LocalDateTime.now())
               .detail(translationRepoService.findByKey(List.of(exception.getMessage())))
               .build();
       return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(build);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(GenericException exception,
                                                             String path){
        var build=ErrorResponse.builder()
                .status(exception.getStatus())
                .code(exception.getCode())
                .path(path)
                .timestamp(LocalDateTime.now())
                .detail(translationRepoService.findByKey(List.of(exception.getMessage())))
                .build();
        System.out.println(exception.getMessage());
        return ResponseEntity.status(exception.getStatus()).body(build);
    }
}
