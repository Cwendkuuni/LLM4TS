package org.example.junit4; // Replace with your testing package

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    

    @Test
    public void testHashCodeBuilderConstructor() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        HashCodeBuilder hashCodeBuilderWithParams = new HashCodeBuilder(17, 37);
        assertEquals(17, hashCodeBuilder.iTotal);
        assertEquals(37, hashCodeBuilderWithParams.iConstant);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        hashCodeBuilder.append(false);
        int expectedHashCode = 1 * 37 + 0;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = new boolean[3];
        booleanArray[0] = true;
        booleanArray[1] = false;
        booleanArray[2] = true;
        hashCodeBuilder.append(booleanArray);
        int expectedHashCode = 1 * 37 + 0 + 1;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 5);
        int expectedHashCode = 5 * 37;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = new byte[3];
        byteArray[0] = 5;
        byteArray[1] = 10;
        byteArray[2] = 15;
        hashCodeBuilder.append(byteArray);
        int expectedHashCode = 5 * 37 + 10 * 37 + 15 * 37;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    // Test other append methods (char, charArray, double, doubleArray, float, floatArray, int, intArray, long, longArray, Object, ObjectArray, short, shortArray) here similarly

    @Test
    public void testReflectionHashCodeObject() {
        String[] excludeFields = new String[0];
        Object object = new Object() {
            int x = 1;
            int y = 2;
            int z = 3;
        };
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, false);
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeObjectNull() {
        HashCodeBuilder.reflectionHashCode(null);
    }

    @Test
    public void testReflectionHashCodeObjectTestTransients() {
        String[] excludeFields = new String[]{"x"};
        Object object = new Object() {
            int x = 1;
            int y = 2;
            int z = 3;
        };
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, true, Object.class, excludeFields);
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object, true);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeObjectNullReflectUpToClass() {
        HashCodeBuilder.reflectionHashCode(null, true, Object.class, new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeObjectExcludeFieldsNull() {
        HashCodeBuilder.reflectionHashCode(new Object() {}, false, Object.class, null);
    }

    @Test
    public void testReflectionHashCodeObjectExcludeFieldsEmptyArray() {
        Object object = new Object() {
            int x = 1;
            int y = 2;
            int z = 3;
        };
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, false, Object.class, new String[0]);
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectExcludeFieldsSingleString() {
        Object object = new Object() {
            int x = 1;
            int y = 2;
            int z = 3;
        };
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, false, Object.class, new String[]{"x"});
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object, new String[]{"x"});
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionHashCodeObjectExcludeFieldsMultipleStrings() {
        Object object = new Object() {
            int x = 1;
            int y = 2;
            int z = 3;
        };
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, false, Object.class, new String[]{"x", "y"});
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object, new String[]{"x", "y"});
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testRegisterAndUnregister() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object object1 = new Object();
        Object object2 = new Object();
        hashCodeBuilder.register(object1);
        hashCodeBuilder.register(object2);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertTrue(registry.contains(new IDKey(object1)));
        assertTrue(registry.contains(new IDKey(object2)));
        hashCodeBuilder.unregister(object1);
        registry = HashCodeBuilder.getRegistry();
        assertFalse(registry.contains(new IDKey(object1)));
        assertTrue(registry.contains(new IDKey(object2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNullObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.register(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetRegistryNullThreadLocal() {
        Set<IDKey> registry = null;
        assertEquals(registry.contains(new IDKey(new Object())), false);
    }

    @Test
    public void testGetRegistryEmptyRegistry() {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(registry.isEmpty(), true);
    }

    @Test
    public void testIsRegisteredObjectTrue() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object object = new Object();
        hashCodeBuilder.register(object);
        assertTrue(HashCodeBuilder.isRegistered(object));
    }

    @Test
    public void testIsRegisteredObjectFalse() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object object = new Object();
        assertFalse(HashCodeBuilder.isRegistered(object));
    }

    @Test
    public void testIsRegisteredArrayOfObjectsTrue() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objects = new Object[2];
        objects[0] = new Object();
        objects[1] = new Object();
        hashCodeBuilder.register(objects);
        for (Object object : objects) {
            assertTrue(HashCodeBuilder.isRegistered(object));
        }
    }

    @Test
    public void testIsRegisteredArrayOfObjectsFalse() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objects = new Object[2];
        objects[0] = new Object();
        assertFalse(HashCodeBuilder.isRegistered(new Object()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendArrayOfObjectsNull() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((Object[]) null);
    }

    @Test
    public void testAppendArrayOfObjectsEmptyArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] emptyArray = new Object[0];
        hashCodeBuilder.append(emptyArray);
        assertEquals(hashCodeBuilder.toHashCode(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendArrayOfObjectsInvalidType() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = new int[1];
        hashCodeBuilder.append(intArray);
    }

    @Test
    public void testAppendArrayOfObjects() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objects = new Object[3];
        objects[0] = new Object();
        objects[1] = new Object();
        objects[2] = new Object();
        hashCodeBuilder.append(objects);
        int expectedHashCode = 1 * 37 + 1 * 37 + 1 * 37;
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendArrayOfObjectsInvalidLength() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objects = new Object[1];
        hashCodeBuilder.append(Array.newInstance(Object.class, -1));
    }

    @Test
    public void testAppendArrayOfObjectsForEachElement() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objects = new Object[3];
        objects[0] = new Object();
        objects[1] = new Object();
        objects[2] = new Object();
        hashCodeBuilder.append(objects);
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            int expectedHashCodeForElement = 1 * 37 + object.hashCode();
            int actualHashCodeForElement = hashCodeBuilder.iTotal;
            assertEquals(expectedHashCodeForElement, actualHashCodeForElement);
            hashCodeBuilder.append(object);
            actualHashCodeForElement = hashCodeBuilder.iTotal;
            assertEquals(expectedHashCodeForElement * 37 + object.hashCode(), actualHashCodeForElement);
        }
    }

    
}