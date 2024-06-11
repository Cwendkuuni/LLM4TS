import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class HashCodeBuilderTest {

    private static final int INITIAL_VALUE = 1;
    private static final int MULTIPLIER = 31;
    private static final boolean TEST_TRANSIENTS = false;
    private static final Class<?> UPPER_CLASS = Object[].class;
    private static final String[] EXCLUDE_FIELDS = {"transientField"};

    private Object object;
    private int expectedHashCode;

    public HashCodeBuilderTest(Object object, int expectedHashCode) {
        this.object = object;
        this.expectedHashCode = expectedHashCode;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"foo", "bar"}, 42},
                {new int[]{1, 2, 3}, 321},
                {new boolean[]{true, false}, -16777216},
                {new long[]{1, 2, 3}, 113},
                {new double[]{1.1, 2.2, 3.3}, 19},
                {new float[]{1.1f, 2.2f, 3.3f}, 11},
                {new boolean[]{true}, 0},
                {new int[0], 0},
                {new String[0], 0},
                {null, 0}
        });
    }

    @Before
    public void setUp() {
        HashCodeBuilder.register(object);
    }

    @Test
    public void testReflectionHashCode() {
        int calculatedHashCode = HashCodeBuilder.reflectionHashCode(INITIAL_VALUE, MULTIPLIER, object, TEST_TRANSIENTS, UPPER_CLASS, EXCLUDE_FIELDS);
        assertEquals("Incorrect hash code calculated for the given object", expectedHashCode, calculatedHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(INITIAL_VALUE, MULTIPLIER, null, TEST_TRANSIENTS, UPPER_CLASS, EXCLUDE_FIELDS);
            fail("Expected IllegalArgumentException when called with a null object");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRegisterAndUnregister() {
        assertFalse("Object should not be registered initially", HashCodeBuilder.isRegistered(object));
        HashCodeBuilder.reflectionHashCode(INITIAL_VALUE, MULTIPLIER, object, TEST_TRANSIENTS, UPPER_CLASS, EXCLUDE_FIELDS);
        assertTrue("Object should be registered after the hash code calculation", HashCodeBuilder.isRegistered(object));
        HashCodeBuilder.unregister(object);
        assertFalse("Object should no longer be registered", HashCodeBuilder.isRegistered(object));
    }

    @Test(expected = InternalError.class)
    public void testIllegalAccessException() {
        try {
            Field field = FieldUtils.getField(object.getClass(), "transientField", true);
            HashCodeBuilder.reflectionHashCode(INITIAL_VALUE, MULTIPLIER, object, TEST_TRANSIENTS, object.getClass(), new String[]{field.getName()});
            fail("InternalError should be thrown when attempting to access a transient field");
        } catch (IllegalAccessException e) {
            // Expected
        }
    }
}