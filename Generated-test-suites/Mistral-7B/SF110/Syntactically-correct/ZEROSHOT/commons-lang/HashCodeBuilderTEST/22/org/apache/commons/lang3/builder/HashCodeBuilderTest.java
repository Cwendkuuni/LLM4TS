package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

public class HashCodeBuilderTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testReflectionHashCode() {
        Object object = new Object() {
            int i = 1;
            String s = "test";
            char c = 'a';
            short sh = 1;
            boolean bool = true;
            byte b = 1;
            long l = 1L;
            float f = 1.0f;
            double d = 1.0;
            int[] intArray = { 1, 2, 3 };
            char[] charArray = { 'a', 'b', 'c' };
            boolean[] boolArray = { true, false, true };
            short[] shortArray = { 1, 2, 3 };
            byte[] byteArray = { 1, 2, 3 };
            long[] longArray = { 1, 2, 3 };
            float[] floatArray = { 1.0f, 2.0f, 3.0f };
            double[] doubleArray = { 1.0, 2.0, 3.0 };
            Object[] objArray = { "test1", new Object(), 1 };
        };

        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertNull(registry);

        int hashCode = HashCodeBuilder.reflectionHashCode(object);
        assertEquals(139329873, hashCode);

        registry = HashCodeBuilder.getRegistry();
        assertNotNull(registry);
        assertEquals(15, registry.size());

        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(object);
        int hashCodeBuilderValue = hashCodeBuilder.toHashCode();
        assertEquals(139329873, hashCodeBuilderValue);
        assertEquals(hashCodeBuilderValue, hashCode);

        // Test reflectionHashCode with different arguments
        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object);
        assertEquals(139329873, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object, true);
        assertEquals(139329873, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object, true, Object.class, new String[]{"i"});
        assertEquals(139329873, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object, false, null, new String[0]);
        assertEquals(139329873, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        assertEquals(139329873, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(object, Arrays.asList("i", "s"));
        assertEquals(139329873, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(object, Arrays.asList("i", "s", "c"));
        assertEquals(1393813941, hashCode);

        // Test reflectionHashCode with arrays
        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, intArray);
        assertEquals(302299146, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, charArray);
        assertEquals(825268545, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, boolArray);
        assertEquals(112797211, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, shortArray);
        assertEquals(-1626985517, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, byteArray);
        assertEquals(1983106757, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, longArray);
        assertEquals(-392256856432931543L, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, floatArray);
        assertEquals(-1719096084016343356L, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(17, 37, doubleArray);
        assertEquals(12629146772838425930000L, hashCode);
    }

    @org.junit.Test
    public void testAppend() {
        Object object = new Object() {
            int i = 1;
            String s = "test";
            char c = 'a';
            short sh = 1;
            boolean bool = true;
            byte b = 1;
            long l = 1L;
            float f = 1.0f;
            double d = 1.0;
            int[] intArray = { 1, 2, 3 };
            char[] charArray = { 'a', 'b', 'c' };
            boolean[] boolArray = { true, false, true };
            short[] shortArray = { 1, 2, 3 };
            byte[] byteArray = { 1, 2, 3 };
            long[] longArray = { 1, 2, 3 };
            float[] floatArray = { 1.0f, 2.0f, 3.0f };
            double[] doubleArray = { 1.0, 2.0, 3.0 };
            Object[] objArray = { "test1", new Object(), 1 };
        };

        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

        hashCodeBuilder.append(object.i);
        hashCodeBuilder.append(object.s);
        hashCodeBuilder.append(object.c);
        hashCodeBuilder.append(object.sh);
        hashCodeBuilder.append(object.bool);
        hashCodeBuilder.append(object.b);
        hashCodeBuilder.append(object.l);
        hashCodeBuilder.append(object.f);
        hashCodeBuilder.append(object.d);

        for (int i = 0; i < intArray.length; i++) {
            hashCodeBuilder.append(intArray[i]);
        }

        for (char c : charArray) {
            hashCodeBuilder.append(c);
        }

        for (boolean b : boolArray) {
            hashCodeBuilder.append(b);
        }

        for (short sh : shortArray) {
            hashCodeBuilder.append(sh);
        }

        for (byte b : byteArray) {
            hashCodeBuilder.append(b);
        }

        for (long l : longArray) {
            hashCodeBuilder.append(l);
        }

        for (float f : floatArray) {
            hashCodeBuilder.append(f);
        }

        for (double d : doubleArray) {
            hashCodeBuilder.append(d);
        }

        for (Object o : objArray) {
            hashCodeBuilder.append(o);
        }

        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(139329873, hashCode);
    }

    @org.junit.Test
    public void testRegisterAndUnregister() {
        Object object = new Object() {
            int i = 1;
            String s = "test";
            char c = 'a';
            short sh = 1;
            boolean bool = true;
            byte b = 1;
            long l = 1L;
            float f = 1.0f;
            double d = 1.0;
            int[] intArray = { 1, 2, 3 };
            char[] charArray = { 'a', 'b', 'c' };
            boolean[] boolArray = { true, false, true };
            short[] shortArray = { 1, 2, 3 };
            byte[] byteArray = { 1, 2, 3 };
            long[] longArray = { 1, 2, 3 };
            float[] floatArray = { 1.0f, 2.0f, 3.0f };
            double[] doubleArray = { 1.0, 2.0, 3.0 };
            Object[] objArray = { "test1", new Object(), 1 };
        };

        Set<IDKey> registry = HashCodeBuilder.getRegistry();

        assertNull(registry);

        // Test registering an object
        HashCodeBuilder.append(object, HashCodeBuilder.class, hashCodeBuilder, true, null, new String[0]);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());

        // Test unregistering an object
        HashCodeBuilder.unregister(object);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(0, registry.size());

        // Test registering and unregistering multiple objects
        HashCodeBuilder.append(object, HashCodeBuilder.class, hashCodeBuilder, true, null, new String[0]);
        HashCodeBuilder.append(new Object() {}.getClass(), HashCodeBuilder.class, hashCodeBuilder, true, null, new String[0]);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(2, registry.size());

        HashCodeBuilder.unregister(object.getClass());
        registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());

        HashCodeBuilder.unregister(new Object() {}.getClass());
        registry = HashCodeBuilder.getRegistry();
        assertEquals(0, registry.size());
    }

    @org.junit.Test
    public void testAppendSuper() {
        Parent parent = new Parent();
        Child child = new Child(parent);

        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(parent.hashCode());
        hashCodeBuilder.append(child);

        int hashCode = hashCodeBuilder.toHashCode();
        assertEquals(139329873, hashCode);
    }

    private static class Parent {
        int i = 1;
        String s = "test";
    }

    private static class Child extends Parent {
        int j = 2;

        public Child(Parent parent) {
            super();
            this.i = parent.i;
            this.s = parent.s;
        }
    }
}

class IDKey {
    final Object value;

    IDKey(Object value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IDKey) {
            IDKey other = (IDKey) obj;
            return this.value == other.value || (this.value != null && this.value.equals(other.value));
        }
        return false;
    }
}