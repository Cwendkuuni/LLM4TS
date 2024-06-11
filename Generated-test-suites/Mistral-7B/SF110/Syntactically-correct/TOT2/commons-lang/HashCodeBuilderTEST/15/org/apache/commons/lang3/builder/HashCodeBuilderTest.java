import static org.junit.Assert.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int expectedHashCode = 38; // calculated hashcode based on the method signature and initial values
        int actualHashCode = hcb.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}