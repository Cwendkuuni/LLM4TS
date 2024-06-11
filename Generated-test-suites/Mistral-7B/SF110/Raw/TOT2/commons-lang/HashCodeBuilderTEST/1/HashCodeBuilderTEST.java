import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int expectedHashCode = 38; // Calculate this based on the class's constants
        int actualHashCode = builder.toHashCode();
        assertExpectedHashCodeEqualsActualHashCode(expectedHashCode, actualHashCode);
    }

    private void assertExpectedHashCodeEqualsActualHashCode(int expectedHashCode, int actualHashCode) {
        if (expectedHashCode != actualHashCode) {
            throw new AssertionError("Expected: " + expectedHashCode + ", but got: " + actualHashCode);
        }
    }
}