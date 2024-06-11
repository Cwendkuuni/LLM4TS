import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int expectedHashCode = 37;
        int actualHashCode = hcb.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}