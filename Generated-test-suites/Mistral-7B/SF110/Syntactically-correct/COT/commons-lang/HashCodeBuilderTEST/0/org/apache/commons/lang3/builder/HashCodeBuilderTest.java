package org.apache.commons.lang3.builder; // make sure the test file is in the same package as the tested class

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for HashCodeBuilder class in org.apache.commons.lang3.builder package
 */
public class HashCodeBuilderTest {
    // Test instances for various methods
    private static Object testObject;
    private static Object[] testObjectArray;
    private static Class<?> testClass;

    @BeforeClass
    public static void setup() throws NoSuchFieldException, IllegalAccessException {
        testObject = new Object() {
            int field1 = 1;
            int field2 = 2;
            String field3 = "test";
        };

        testObjectArray = new Object[]{new Integer(1), new Double(1.0), new Byte((byte) 1), new Short((short) 1), new Character('1'), new Float(1.0f)};

        testClass = String[].class;
    }

    @Test
    public void testHashCodeBuilderConstruction() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        assertEquals(17, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 31);
        assertEquals(11, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        hashCodeBuilder.append(false);
        assertEquals(33, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(new boolean[]{true, false});
        assertEquals(35, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 1);
        hashCodeBuilder.append((byte) -1);
        assertEquals(33, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte[])new byte[]{1, -1});
        assertEquals(35, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('1');
        hashCodeBuilder.append('z');
        assertEquals(37, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((char[])new char[]{'1', 'z'});
        assertEquals(39, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.0);
        hashCodeBuilder.append(Math.PI);
        assertEquals(43, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((double[])new double[]{1.0, Math.PI});
        assertEquals(45, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.0f);
        hashCodeBuilder.append(Float.NaN);
        assertEquals(43, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((float[])new float[]{1.0f, Float.NaN});
        assertEquals(45, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1);
        hashCodeBuilder.append(2);
        assertEquals(33, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((int[])new int[]{1, 2});
        assertEquals(35, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1L);
        hashCodeBuilder.append(-1L);
        assertEquals(33, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((long[])new long[]{1L, -1L});
        assertEquals(35, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(testObject);
        hashCodeBuilder.append(new Object());
        assertEquals(53, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((Object[])new Object[]{new Integer(1), new Double(1.0), new Byte((byte) 1), new Short((short) 1), new Character('1'), new Float(1.0f)});
        assertEquals(63, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 1);
        hashCodeBuilder.append((short) -1);
        assertEquals(33, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short[])new short[]{1, -1});
        assertEquals(35, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendSuperHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(17);
        assertEquals(31, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeObject() {
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, false);
        assertEquals(53, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectBoolean() {
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, true);
        assertEquals(55, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectBooleanClassCollectionStringArray() {
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, true, testClass, null, new String[]{"field1", "field2", "field3"});
        assertEquals(57, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectBooleanClassCollectionStringArrayNullField() {
        try {
            HashCodeBuilder.reflectionHashCode(testObject, true, testClass, null, new String[]{"field4"});
            fail();
        } catch (IllegalAccessException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeNullObject() {
        HashCodeBuilder.reflectionHashCode(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeNullObjectBoolean() {
        HashCodeBuilder.reflectionHashCode(null, true);
    }

    @Test
    public void testIsRegistered() {
        HashCodeBuilder.register(testObject);
        assertTrue(HashCodeBuilder.isRegistered(testObject));
        HashCodeBuilder.unregister(testObject);
        assertFalse(HashCodeBuilder.isRegistered(testObject));
    }

    @Test
    public void testGetRegistry() {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertNotNull(registry);
        assertEquals(0, registry.size());
        HashCodeBuilder.register(testObject);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
    }
}