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
    public void testConstructorWithInitialOddNumberAndMultiplierOddNumber() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(13, 17);
        assertEquals(13, hashCodeBuilder.iTotal);
        assertEquals(17, hashCodeBuilder.iConstant);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEvenInitialNumber() {
        new HashCodeBuilder(2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEvenMultiplierNumber() {
        new HashCodeBuilder(1, 2);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        assertEquals(32, hashCodeBuilder.iTotal);
        hashCodeBuilder.append(false);
        assertEquals(33, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hashCodeBuilder.append(booleanArray);
        assertEquals(35, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 5);
        assertEquals(63, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder.append(byteArray);
        assertEquals(199, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('a');
        assertEquals(123, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hashCodeBuilder.append(charArray);
        assertEquals(361, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.0);
        assertEquals(112037, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        hashCodeBuilder.append(doubleArray);
        assertEquals(502117, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.5f);
        assertEquals(268835, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        hashCodeBuilder.append(floatArray);
        assertEquals(674753, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1);
        assertEquals(33, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hashCodeBuilder.append(intArray);
        assertEquals(65537, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1L);
        assertEquals(65, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        hashCodeBuilder.append(longArray);
        assertEquals(1953125, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object object = new Object();
        hashCodeBuilder.append(object);
        assertEquals(hashCode(object), hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object(), new Object()};
        hashCodeBuilder.append(objectArray);
        for (Object obj : objectArray) {
            assertEquals(hashCode(obj), hashCodeBuilder.iTotal);
        }
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 5);
        assertEquals(73, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hashCodeBuilder.append(shortArray);
        assertEquals(34867, hashCodeBuilder.iTotal);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int superHashCode = 10;
        hashCodeBuilder.appendSuper(superHashCode);
        assertEquals(superHashCode, hashCodeBuilder.iTotal);
    }

    @Test
    public void testReflectionHashCodeWithObjectAndFalse() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        assertEquals(hashCode(object), hashCode);
    }

    @Test
    public void testReflectionHashCodeWithObjectAndIncludeTransients() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true);
        assertEquals(hashCode(object), hashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullExcludeFieldsArray() {
        HashCodeBuilder.reflectionHashCode(new Object(), (String[]) null, false);
    }

    @Test
    public void testReflectionHashCodeWithObjectAndExcludeFieldsArray() {
        Object object = new Object();
        String[] excludeFields = {"field1", "field2"};
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false, excludeFields);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
        IDKey idKey = registry.iterator().next();
        assertEquals(object, idKey.getValue());
    }

    @Test
    public void testReflectionHashCodeWithObjectAndTransientsAndExcludeFieldsArray() {
        Object object = new Object();
        String[] excludeFields = {"field1", "field2"};
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true, excludeFields);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
        IDKey idKey = registry.iterator().next();
        assertEquals(object, idKey.getValue());
    }

    @Test
    public void testRegisterAndUnregister() {
        Object object = new Object();
        HashCodeBuilder.register(object);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
        IDKey idKey = registry.iterator().next();
        assertEquals(object, idKey.getValue());
        HashCodeBuilder.unregister(object);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(0, registry.size());
    }

    private int hashCode(Object object) {
        return object.hashCode();
    }
}