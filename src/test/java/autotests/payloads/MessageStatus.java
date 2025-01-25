package autotests.payloads;
public enum MessageStatus {
    DELETED ("Duck is deleted"), UPDATED("Duck with id = ${id} is updated"),
    NOT_UPDATED("Duck with id = ${id} is not updated"), FLYING("I'm flying"),
    CANNOT_FLY("I can't fly"), WITHOUT_WINGS("Wings are not detected"), SWIMMING("I'm swimming"),
    CANNOT_SWIM("Paws are not found");
    private String value;

    MessageStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}