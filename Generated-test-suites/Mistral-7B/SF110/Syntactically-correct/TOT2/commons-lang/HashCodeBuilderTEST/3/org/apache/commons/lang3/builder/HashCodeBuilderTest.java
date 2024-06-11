import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        int expectedHashCode = 38; // (1 * 37) + 1
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEmptyHashCodeBuilder() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int expectedHashCode = 1;
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(null);
    }

    @Test
    public void testAppendFalse() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(false);
        int expectedHashCode = 36; // (1 * 37)
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

}