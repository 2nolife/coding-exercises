package iconsltd;

/**
 * The outcome of user registration.
 */
public class Outcome {

    private final OutcomeType outcomeType;
    private final String outcomeMessage;

    public Outcome(OutcomeType outcomeType, String outcomeMessage) {
        this.outcomeType = outcomeType;
        this.outcomeMessage = outcomeMessage;
    }

    public OutcomeType getOutcomeType() {
        return outcomeType;
    }

    public String getOutcomeMessage() {
        return outcomeMessage;
    }

    public enum OutcomeType {
        SUCCESS, FAILURE
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Outcome other = (Outcome) obj;
        return outcomeType.equals(other.outcomeType) && outcomeMessage.equals(other.outcomeMessage);
    }

    @Override
    public String toString() {
        return String.format("Outcome %s message %s", outcomeType, outcomeMessage);
    }
}
