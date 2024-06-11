package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.test.EqualsVerifier;
import org.apache.commons.lang3.test.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.annotation.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({HashCodeBuilder.class})
public class HashCodeBuilderTest {

    private static final int INITIAL_VALUE = 17;
    private static final int MULTIPLIER = 37;
    private static final boolean TEST_TRANSIENTS = false;
    private static final Class<?> TEST_CLASS = Object[].class;
    private static final String[] EXCLUDE_FIELDS = new String[]{"excludeField"};
    private static final Object TEST_OBJECT = new Object[]{1, 2, 3};

    private static final Set<IDKey> REGISTRY = new HashSet<>();

    @Before
    public void setUp() {
        HashCodeBuilder.getRegistry().clear();
        HashCodeBuilder.REGISTRY.set(REGISTRY);
    }

    @Test
    public void testReflectionHashCodeWithDefaults() {
        int result = reflectionHashCode(INITIAL_VALUE, MULTIPLIER, TEST_OBJECT, TEST_TRANSIENTS, TEST_CLASS, EXCLUDE_FIELDS);
        assertEquals(54662155, result);

        verifyHashCode(TEST_OBJECT, TEST_CLASS, EXCLUDE_FIELDS);
    }

    @Test
    public void testReflectionHashCodeWithCustomInitialValueAndMultiplier() {
        int result = reflectionHashCode(7, 41, TEST_OBJECT, TEST_TRANSIENTS, TEST_CLASS, EXCLUDE_FIELDS);
        assertEquals(322735315, result);

        verifyHashCode(TEST_OBJECT, TEST_CLASS, EXCLUDE_FIELDS);
    }

    @Test
    public void testReflectionHashCodeWithTestTransients() {
        int result = reflectionHashCode(INITIAL_VALUE, MULTIPLIER, TEST_OBJECT, true, TEST_CLASS, EXCLUDE_FIELDS);
        assertEquals(157289813, result);

        verifyHashCode(TEST_OBJECT, TEST_CLASS, EXCLUDE_FIELDS);
    }

    @Test
    public void testReflectionHashCodeWithCustomUpToClass() {
        int result = reflectionHashCode(INITIAL_VALUE, MULTIPLIER, TEST_OBJECT, TEST_TRANSIENTS, String[].class, EXCLUDE_FIELDS);
        assertEquals(1021267165, result);

        verifyHashCode(TEST_OBJECT, String[].class, EXCLUDE_FIELDS);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        int result = reflectionHashCode(INITIAL_VALUE, MULTIPLIER, TEST_OBJECT, TEST_TRANSIENTS, TEST_CLASS, "excludeField");
        assertEquals(-1226283135, result);

        verifyHashCode(TEST_OBJECT, TEST_CLASS, new String[]{"excludeField"});
    }

    @Test
    public void testReflectionHashCodeWithNull() {
        int result = reflectionHashCode(INITIAL_VALUE, MULTIPLIER, null, TEST_TRANSIENTS, TEST_CLASS, EXCLUDE_FIELDS);
        assertEquals(-1, result);

        verifyHashCode(null, TEST_CLASS, EXCLUDE_FIELDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithInvalidInitialValue() {
        reflectionHashCode(0, MULTIPLIER, TEST_OBJECT, TEST_TRANSIENTS, TEST_CLASS, EXCLUDE_FIELDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithInvalidMultiplier() {
        reflectionHashCode(INITIAL_VALUE, 0, TEST_OBJECT, TEST_TRANSIENTS, TEST_CLASS, EXCLUDE_FIELDS);
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(false);
        int result = builder.toHashCode();
        assertEquals(-1473541251, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(false);
        result = builder.toHashCode();
        assertEquals(-1627537491, result);
    }

    @Test
    public void testHashCodeBuilderAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = new boolean[]{true, false};
        builder.append(booleanArray);
        int result = builder.toHashCode();
        assertEquals(1666306329, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(booleanArray);
        result = builder.toHashCode();
        assertEquals(-1411448579, result);
    }

    @Test
    public void testHashCodeBuilderAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int result = builder.toHashCode();
        assertEquals(-1745163861, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append((byte) 1);
        result = builder.toHashCode();
        assertEquals(1312822313, result);
    }

    @Test
    public void testHashCodeBuilderAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = new byte[]{1, 2};
        builder.append(byteArray);
        int result = builder.toHashCode();
        assertEquals(-1917658607, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(byteArray);
        result = builder.toHashCode();
        assertEquals(-1500612037, result);
    }

    @Test
    public void testHashCodeBuilderAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int result = builder.toHashCode();
        assertEquals(-1606757357, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append('a');
        result = builder.toHashCode();
        assertEquals(1188281101, result);
    }

    @Test
    public void testHashCodeBuilderAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = new char[]{'a', 'b'};
        builder.append(charArray);
        int result = builder.toHashCode();
        assertEquals(-1061026915, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(charArray);
        result = builder.toHashCode();
        assertEquals(1461753859, result);
    }

    @Test
    public void testHashCodeBuilderAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.2);
        int result = builder.toHashCode();
        assertEquals(-155145956, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(1.2);
        result = builder.toHashCode();
        assertEquals(-1370589153, result);
    }

    @Test
    public void testHashCodeBuilderAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = new double[]{1.2, 2.3};
        builder.append(doubleArray);
        int result = builder.toHashCode();
        assertEquals(-2007311260, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(doubleArray);
        result = builder.toHashCode();
        assertEquals(-1585227142, result);
    }

    @Test
    public void testHashCodeBuilderAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.2f);
        int result = builder.toHashCode();
        assertEquals(-1535630257, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(1.2f);
        result = builder.toHashCode();
        assertEquals(-1165225749, result);
    }

    @Test
    public void testHashCodeBuilderAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = new float[]{1.2f, 2.3f};
        builder.append(floatArray);
        int result = builder.toHashCode();
        assertEquals(-1473479227, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(floatArray);
        result = builder.toHashCode();
        assertEquals(-1194915221, result);
    }

    @Test
    public void testHashCodeBuilderAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        int result = builder.toHashCode();
        assertEquals(-1402379529, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(1);
        result = builder.toHashCode();
        assertEquals(1598566753, result);
    }

    @Test
    public void testHashCodeBuilderAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = new int[]{1, 2};
        builder.append(intArray);
        int result = builder.toHashCode();
        assertEquals(-542064991, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(intArray);
        result = builder.toHashCode();
        assertEquals(1826164111, result);
    }

    @Test
    public void testHashCodeBuilderAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        int result = builder.toHashCode();
        assertEquals(-1251694265, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(1L);
        result = builder.toHashCode();
        assertEquals(1164879137, result);
    }

    @Test
    public void testHashCodeBuilderAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = new long[]{1L, 2L};
        builder.append(longArray);
        int result = builder.toHashCode();
        assertEquals(-1593184771, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(longArray);
        result = builder.toHashCode();
        assertEquals(-1295141341, result);
    }

    @Test
    public void testHashCodeBuilderAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new String("test"));
        int result = builder.toHashCode();
        assertEquals(149534113, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(new String("test"));
        result = builder.toHashCode();
        assertEquals(-1510182449, result);
    }

    @Test
    public void testHashCodeBuilderAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objectArray = new Object[]{new String("test"), new String("test2")};
        builder.append(objectArray);
        int result = builder.toHashCode();
        assertEquals(-83743077, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(objectArray);
        result = builder.toHashCode();
        assertEquals(-1746305165, result);
    }

    @Test
    public void testHashCodeBuilderAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        int result = builder.toHashCode();
        assertEquals(-1462594103, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append((short) 1);
        result = builder.toHashCode();
        assertEquals(-1194802325, result);
    }

    @Test
    public void testHashCodeBuilderAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = new short[]{1, 2};
        builder.append(shortArray);
        int result = builder.toHashCode();
        assertEquals(-1506714036, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.append(shortArray);
        result = builder.toHashCode();
        assertEquals(-1301857495, result);
    }

    @Test
    public void testHashCodeBuilderAppendSuperInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(1);
        int result = builder.toHashCode();
        assertEquals(1, result);

        builder = new HashCodeBuilder(MULTIPLIER);
        builder.appendSuper(1);
        result = builder.toHashCode();
        assertEquals(1, result);
    }

    private void verifyHashCode(Object object, Class<?> clazz, String... excludeFields) {
        int hashCode = reflectionHashCode(INITIAL_VALUE, MULTIPLIER, object, TEST_TRANSIENTS, clazz, excludeFields);

        try {
            ReflectionTestUtils.invokeMethod(HashCodeBuilder.class, "register", object);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Arrays.stream(excludeFields).noneMatch(field::getName)) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(object);
                    EqualsVerifier.defaultVerifyEquals(object, new ReflectionToStringBuilder(object).append(".", field.getName()).toString(), fieldValue);
                    HashCodeBuilder newBuilder = new HashCodeBuilder();
                    newBuilder.append(fieldValue);
                    EqualsVerifier.defaultVerifyEqualHashCodes(object, newBuilder.toHashCode(), fieldValue.hashCode());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                ReflectionTestUtils.invokeMethod(HashCodeBuilder.class, "unregister", object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}