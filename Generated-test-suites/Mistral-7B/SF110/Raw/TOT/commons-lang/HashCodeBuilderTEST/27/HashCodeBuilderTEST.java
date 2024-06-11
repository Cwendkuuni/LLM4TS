package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Builder;
import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

public class HashCodeBuilderTest {

    private static final int INITIAL_ODD_NUMBER = 17;
    private static final int MULTIPLIER_ODD_NUMBER = 37;
    private static final boolean TEST_TRANSIENTS = true;
    private static final Class<Object> REFLECT_UP_TO_CLASS = Object.class;
    private static final String[] EXCLUDE_FIELDS = new String[]{"serialVersionUID"};

    private HashCodeBuilder hashCodeBuilder;

    @Before
    public void setUp() {
        hashCodeBuilder = new HashCodeBuilder(INITIAL_ODD_NUMBER, MULTIPLIER_ODD_NUMBER);
    }

    @Test
    public void testHashCodeBuilderWithBasicTypes() {
        hashCodeBuilder.append(true);
        hashCodeBuilder.append((byte) 1);
        hashCodeBuilder.append((short) 1);
        hashCodeBuilder.append((char) 'a');
        hashCodeBuilder.append((int) 1);
        hashCodeBuilder.append((long) 1L);
        hashCodeBuilder.append((float) 1.0f);
        hashCodeBuilder.append((double) 1.0);

        Assert.assertEquals(258, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderWithArrays() {
        boolean[] booleanArray = new boolean[]{true, false};
        byte[] byteArray = new byte[]{1, 2};
        char[] charArray = new char[]{'a', 'b'};
        short[] shortArray = new short[]{1, 2};
        int[] intArray = new int[]{1, 2};
        long[] longArray = new long[]{1L, 2L};
        float[] floatArray = new float[]{1.0f, 2.0f};
        double[] doubleArray = new double[]{1.0, 2.0};

        hashCodeBuilder.append(booleanArray);
        hashCodeBuilder.append(byteArray);
        hashCodeBuilder.append(charArray);
        hashCodeBuilder.append(shortArray);
        hashCodeBuilder.append(intArray);
        hashCodeBuilder.append(longArray);
        hashCodeBuilder.append(floatArray);
        hashCodeBuilder.append(doubleArray);

        Assert.assertEquals(14411, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderWithObjects() {
        String string = "test";
        Object object = new Object() {
            private static final long serialVersionUID = 1L;
        };
        hashCodeBuilder.append(string);
        hashCodeBuilder.append(object);

        Assert.assertEquals(1108627335, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderWithTransients() {
        class TestClass {
            private volatile int transientField;

            public TestClass() {
                this.transientField = 1;
            }
        }

        TestClass testClass = new TestClass();
        hashCodeBuilder.append(testClass);

        Assert.assertEquals(1335122365, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderWithReflection() {
        Object object = new TestReflectionHashCode();
        hashCodeBuilder.reflectionHashCode(object, TEST_TRANSIENTS, REFLECT_UP_TO_CLASS, EXCLUDE_FIELDS);

        Assert.assertEquals(1178335219, hashCodeBuilder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithNullObject() {
        hashCodeBuilder.append(null);
    }

    @Test
    public void testHashCodeBuilderWithRegisteredObjects() {
        Object object = new TestRegisteredObject();
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        registry.add(new IDKey(object));

        hashCodeBuilder.reflectionHashCode(object, TEST_TRANSIENTS, REFLECT_UP_TO_CLASS, EXCLUDE_FIELDS);

        Assert.assertEquals(846288330, hashCodeBuilder.toHashCode());
    }
}

class TestReflectionHashCode {
    private int transientField;

    public int getTransientField() {
        return transientField;
    }

    public void setTransientField(int transientField) {
        this.transientField = transientField;
    }
}

class TestRegisteredObject {
    private int registeredField;

    public int getRegisteredField() {
        return registeredField;
    }

    public void setRegisteredField(int registeredField) {
        this.registeredField = registeredField;
    }
}