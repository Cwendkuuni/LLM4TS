import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

public class HashCodeBuilderTest {
    @Test
    public void testHashCodeBuilderEmptyConstructor() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 17; // initial non-zero odd number
        assertEquals(expectedHashCode, actualHashCode);
    }
}