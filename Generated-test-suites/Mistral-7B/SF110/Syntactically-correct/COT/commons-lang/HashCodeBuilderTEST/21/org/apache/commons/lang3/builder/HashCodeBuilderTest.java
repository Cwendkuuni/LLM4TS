package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder.IDKey;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderConstructor() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        HashCodeBuilder hcbWithParams = new HashCodeBuilder(17, 37);
        assertEquals(17, hcbWithParams.getiTotal());
        assertEquals(37, hcbWithParams.getiConstant());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + 1 + 0;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] array = {true, false};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * (37 * 32 + 1) + 0 * (37 * 32 + 0);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        hcb.append((byte) -1);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 2 * 37 + 1 + (-1 + (1 << 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] array = {1, -1};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 2 * 37 + 1 + (-1 + (1 << 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('z');
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + 'a' + 'z';
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] array = {'a', 'z'};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + 'a' + 'z';
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.1);
        hcb.append(2.2);
        long doubleHashCode = Double.doubleToLongBits(1.1);
        long secondDoubleHashCode = Double.doubleToLongBits(2.2);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + (int)(doubleHashCode ^ (doubleHashCode >>> 32)) + (int)(secondDoubleHashCode ^ (secondDoubleHashCode >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] array = {1.1, 2.2};
        hcb.append(array);
        long firstDoubleHashCode = Double.doubleToLongBits(1.1);
        long secondDoubleHashCode = Double.doubleToLongBits(2.2);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + (int)(firstDoubleHashCode ^ (firstDoubleHashCode >>> 32)) + (int)(secondDoubleHashCode ^ (secondDoubleHashCode >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float) 1.1);
        hcb.append((float) 2.2);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + Float.floatToIntBits(1.1f) + Float.floatToIntBits(2.2f);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] array = {1.1f, 2.2f};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + Float.floatToIntBits(1.1f) + Float.floatToIntBits(2.2f);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.append(2);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + 1 + 2;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] array = {1, 2};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + 1 + 2;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        hcb.append(2L);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + (int)(1L ^ (1L >>> 32)) + (int)(2L ^ (2L >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] array = {1L, 2L};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + (int)(1L ^ (1L >>> 32)) + (int)(2L ^ (2L >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        String str = "test";
        hcb.append(str);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32 * 37 + str.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] array = {"test1", "test2"};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 0;
        for (Object o : array) {
            expectedHashCode += 32 * 37 + o.hashCode();
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 1);
        hcb.append((short) -1);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 2 * 37 + 1 + (-1 + (1 << 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] array = {1, -1};
        hcb.append(array);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 2 * 37 + 1 + (-1 + (1 << 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendSuperInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 10;
        hcb.appendSuper(superHashCode);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = superHashCode * 37 + hashCode;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testRegisterUnregister() {
        Object obj = new Object();
        HashCodeBuilder hcb = new HashCodeBuilder();
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        int hashCode = 0;
        
        // Test register
        hcb.append(obj);
        registry.add(new IDKey(obj));
        hashCode = hcb.toHashCode();
        IDKey idKey = HashCodeBuilder.getRegistry().iterator().next();
        assertEquals(idKey.getValue(), obj);

        // Test unregister
        hcb.unregister(obj);
        registry.remove(new IDKey(obj));
        hashCode = hcb.toHashCode();
        assertFalse(HashCodeBuilder.isRegistered(obj));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHashCodeBuilderInitialValue() {
        new HashCodeBuilder(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHashCodeBuilderMultiplier() {
        new HashCodeBuilder(1, 0);
    }

    @Test
    public void testReflectionHashCodeObject() {
        Object obj = new Object();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj);
        int expectedHashCode = obj.hashCode();
        assertEquals(expectedHashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectWithTestTransientsFalse() {
        Object obj = new Object();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj, false);
        int expectedHashCode = obj.hashCode();
        assertEquals(expectedHashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectWithTestTransientsTrue() {
        Object obj = new Object() {
            private transient int transientField;
        };
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj, true);
        int expectedHashCode = obj.hashCode();
        // Transient fields are not included in the hashCode
        // so the hashCode from reflectionHashCode should be different
        assertNotEquals(expectedHashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectWithExcludeFields() {
        Object obj = new Object() {
            private String name;
            private int age;
            private String secret;
        };
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj, Arrays.asList("secret"));
        int expectedHashCode = obj.hashCode();
        // The "secret" field is excluded from the hashCode calculation
        // so the hashCode from reflectionHashCode should be different
        assertNotEquals(expectedHashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectWithSuperClass() {
        Object obj = new SuperClass();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj, SuperClass.class, false);
        int expectedHashCode = obj.getClass().getSuperclass().hashCode();
        assertEquals(expectedHashCode, reflectionHashCode);
    }
}

class IDKey {
    private Object value;

    public IDKey(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}

class SuperClass {
    int superField;
}