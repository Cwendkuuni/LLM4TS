import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        int actual = hashCodeBuilder.toHashCode();
        int expected = 0; // The actual hash code value would depend on the initial hash value and multiplier
        assertEquals(expected, actual);
    }
}