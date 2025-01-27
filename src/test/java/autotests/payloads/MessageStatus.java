package autotests.payloads;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors (fluent = true)
public enum MessageStatus {
    DELETED ("Duck is deleted"), UPDATED("Duck with id = ${id} is updated"),
    NOT_UPDATED("Duck with id = ${id} is not updated"), FLYING("I'm flying"),
    CANNOT_FLY("I can't fly"), WITHOUT_WINGS("Wings are not detected"), SWIMMING("I'm swimming"),
    CANNOT_SWIM("Paws are not found");
    private final String value;
}