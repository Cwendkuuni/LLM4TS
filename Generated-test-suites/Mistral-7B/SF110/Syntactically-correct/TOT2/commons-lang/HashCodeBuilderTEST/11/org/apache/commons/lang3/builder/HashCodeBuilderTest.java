import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {
    @Test
    public void testAppendInteger() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1);
        int expectedHashCode = 38; // 1 * 37 + 1
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}