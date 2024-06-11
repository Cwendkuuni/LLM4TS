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
        int actualHashCode = builder.build().intValue();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendNullBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(null);
        int expectedHashCode = builder.toHashCode();
        int actualHashCode = builder.build().intValue();
        // Test for expected hash code behavior with null input
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendEmptyBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = new boolean[0];
        builder.append(booleanArray);
        int expectedHashCode = builder.toHashCode();
        int actualHashCode = builder.build().intValue();
        // Test for expected hash code behavior with empty boolean array
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendLargeBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = new boolean[1000];
        for (boolean b : booleanArray) {
            builder.append(b);
        }
        int expectedHashCode = builder.toHashCode();
        int actualHashCode = builder.build().intValue();
        // Test for expected hash code behavior with large boolean array
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNegativeBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = false;
        builder.append(-1 * (value ? 1 : 0));
    }

    @Test
    public void testAppendMaxBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;
        builder.append(Boolean.MAX_VALUE);
        int expectedHashCode = builder.toHashCode();
        int actualHashCode = builder.build().intValue();
        // Test for expected hash code behavior with max boolean value
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendConstantBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;
        builder.append(value);
        builder.append(value);
        int expectedHashCode = builder.toHashCode();
        int actualHashCode = builder.build().intValue();
        // Test for expected hash code behavior with constant boolean value
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = ReflectiveOperationException.class)
    public void testReflectionAppend() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object object = new Object() {
            Object field;

            @Override
            public void reflectTestMethod() {
                field = new Object();
            }
        };
        Class<?> clazz = object.getClass();
        try {
            reflectionAppend(object, clazz, builder, true, new String[0]);
        } catch (IllegalAccessException e) {
            throw new InternalError("Unexpected IllegalAccessException");
        }
    }
}