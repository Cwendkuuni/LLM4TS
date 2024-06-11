package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        assertEquals(33, hashCodeBuilder.toHashCode());

        hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(false);
        assertEquals(31, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 5);
        assertEquals(59, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = new byte[]{1, 2, 3};
        hashCodeBuilder.append(byteArray);
        assertEquals(134332067, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('a');
        assertEquals(31839, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = new char[]{'a', 'b', 'c'};
        hashCodeBuilder.append(charArray);
        assertEquals(898203431, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.23);
        assertEquals(2133043095159151707L, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = new double[]{1.23, 2.46, 3.69};
        hashCodeBuilder.append(doubleArray);
        long expectedHashCode = 3286922035364957182L;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.23f);
        assertEquals(1612365535, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = new float[]{1.23f, 2.46f, 3.69f};
        hashCodeBuilder.append(floatArray);
        int expectedHashCode = 1435453913;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1);
        assertEquals(31, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = new int[]{1, 2, 3};
        hashCodeBuilder.append(intArray);
        assertEquals(-1193341586, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1L);
        assertEquals(33, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = new long[]{1, 2, 3};
        hashCodeBuilder.append(longArray);
        assertEquals(-1172593071111884053L, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object object = new Object();
        hashCodeBuilder.append(object);
        int expectedHashCode = 44113018;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objectArray = new Object[]{new Object(), new Object(), new Object()};
        hashCodeBuilder.append(objectArray);
        int expectedHashCode = -1637751306;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 5);
        assertEquals(53073, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = new short[]{1, 2, 3};
        hashCodeBuilder.append(shortArray);
        assertEquals(134332067, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int superHashCode = 5;
        hashCodeBuilder.appendSuper(superHashCode);
        assertEquals(317713339, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectiveHashCode() {
        Object object = new Object();
        int expectedHashCode = reflectionHashCode(17, 37, object, false, null, new String[0]);
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(object);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectiveHashCodeExcludeFields() {
        Object object = new Object() {
            private int field1 = 1;
            private int field2 = 2;

            @Override
            public String toString() {
                return "Object{" +
                        "field1=" + field1 +
                        ", field2=" + field2 +
                        '}';
            }
        };
        int expectedHashCode = reflectionHashCode(17, 37, object, false, Object.class, new String[]{"field1", "field2"});
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(object, new String[]{"field1", "field2"});
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectiveHashCodeTestTransients() {
        Object object = new Object() {
            private transient int field1 = 1;
            private int field2 = 2;

            @Override
            public String toString() {
                return "Object{" +
                        "field1=" + field1 +
                        ", field2=" + field2 +
                        '}';
            }
        };
        int expectedHashCode = reflectionHashCode(17, 37, object, true, null, new String[0]);
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(object, true);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectiveHashCodeUpToClass() {
        Object object = new Object() {
            private int field1 = 1;
            private int field2 = 2;

            @Override
            public String toString() {
                return "Object{" +
                        "field1=" + field1 +
                        ", field2=" + field2 +
                        '}';
            }

            static class InnerClass {}
        };
        int expectedHashCode = reflectionHashCode(17, 37, object, false, Object.class, new String[0]);
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(object);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);

        expectedHashCode = reflectionHashCode(17, 37, object, false, InnerClass.class, new String[0]);
        hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(object, InnerClass.class);
        actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}