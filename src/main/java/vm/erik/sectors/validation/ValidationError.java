package vm.erik.sectors.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {
    @NonNull
    private String code;

    private String message;
    private List<String> arguments;

    public ValidationError(String message) {
        this.code = "error";
        this.message = message;
    }

    public ValidationError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
