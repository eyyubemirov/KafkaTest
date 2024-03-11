package az.company.microserviceapplication.exception;


import lombok.Getter;


@Getter
public class GenericException extends RuntimeException {
    private final String code;
    private final String message;
    private final int status;

    public GenericException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public GenericException(String errorBody, HttpStatus httpStatus) {
        super(errorBody);
        this.status = httpStatus.value();
        this.code = httpStatus.name();
        this.message = errorBody;
    }

    @Override
    public String toString() {
        return String.format("s%{status=%d,code='%s',message='%s'}",
                this.getClass().getSimpleName(), status, code, message);
    }
}
