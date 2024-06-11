import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;
        builder.append(value);
        int expectedHashCode = builder.toHashCode();
        boolean actualHashCodeIsCorrect = expectedHashCode % 2 == 1;
        assertTrue(actualHashCodeIsCorrect);
    }
}