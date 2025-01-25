package autotests.payloads;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
@Getter
@Setter
@Accessors (fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    @JsonProperty("message")
    private String message;
}