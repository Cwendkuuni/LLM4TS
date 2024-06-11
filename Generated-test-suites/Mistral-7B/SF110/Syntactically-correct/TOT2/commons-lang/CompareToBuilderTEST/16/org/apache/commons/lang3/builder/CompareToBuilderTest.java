import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testAppendObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();

        builder.append(lhs, rhs);

        int comparison = builder.toComparison();
        int expectedComparison = 0; // Assuming that equal objects result in 0 comparison

        // Check functionality
        assertEquals(expectedComparison, comparison);
    }
}