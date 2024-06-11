package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int initialHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        assertEquals(initialHashCode, hashCodeBuilder1.hashCode());

        hashCodeBuilder.append(true);
        int finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder();
        hashCodeBuilder2.append(true);
        assertEquals(finalHashCode, hashCodeBuilder2.hashCode());

        hashCodeBuilder.append(1);
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder();
        hashCodeBuilder3.append(1);
        assertEquals(finalHashCode, hashCodeBuilder3.hashCode());

        hashCodeBuilder.append("test");
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder4 = new HashCodeBuilder();
        hashCodeBuilder4.append("test");
        assertEquals(finalHashCode, hashCodeBuilder4.hashCode());

        hashCodeBuilder.append(1.0);
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder5 = new HashCodeBuilder();
        hashCodeBuilder5.append(1.0);
        assertEquals(finalHashCode, hashCodeBuilder5.hashCode());

        hashCodeBuilder.append(new byte[] { 1, 2, 3 });
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder6 = new HashCodeBuilder();
        hashCodeBuilder6.append(new byte[] { 1, 2, 3 });
        assertEquals(finalHashCode, hashCodeBuilder6.hashCode());

        hashCodeBuilder.append(new char[] { 'a', 'b', 'c' });
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder7 = new HashCodeBuilder();
        hashCodeBuilder7.append(new char[] { 'a', 'b', 'c' });
        assertEquals(finalHashCode, hashCodeBuilder7.hashCode());

        hashCodeBuilder.append(Arrays.asList(1, 2, 3));
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder8 = new HashCodeBuilder();
        hashCodeBuilder8.append(Arrays.asList(1, 2, 3));
        assertEquals(finalHashCode, hashCodeBuilder8.hashCode());

        hashCodeBuilder.append(new Object[] { 1, "test", 1.0 });
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder9 = new HashCodeBuilder();
        hashCodeBuilder9.append(new Object[] { 1, "test", 1.0 });
        assertEquals(finalHashCode, hashCodeBuilder9.hashCode());

        hashCodeBuilder.append(new short[] { 1, 2, 3 });
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder10 = new HashCodeBuilder();
        hashCodeBuilder10.append(new short[] { 1, 2, 3 });
        assertEquals(finalHashCode, hashCodeBuilder10.hashCode());

        hashCodeBuilder.appendSuper(123);
        finalHashCode = hashCodeBuilder.hashCode();
        HashCodeBuilder hashCodeBuilder11 = new HashCodeBuilder();
        hashCodeBuilder11.appendSuper(123);
        assertEquals(finalHashCode, hashCodeBuilder11.hashCode());

        hashCodeBuilder.toHashCode();
    }

    @Test
    public void testReflectionHashCode() {
        Object obj = new Object() {
            int a = 1;
            int b = 2;
            String c = "test";
            double d = 1.0;
            char e = 'e';
            short f = 3;
            int[] g = { 1, 2, 3 };
            String[] h = { "test1", "test2" };
        };

        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj, true);
        int reflectionHashCodeWithoutTransients = HashCodeBuilder.reflectionHashCode(obj);

        assertEquals(reflectionHashCode, reflectionHashCodeWithoutTransients);

        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertNotNull(registry);
        assertEquals(1, registry.size());

        IDKey idKey = registry.iterator().next();
        assertEquals(obj.getClass(), idKey.getValue());

        registry.clear();

        reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, true);
        reflectionHashCodeWithoutTransients = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj);

        assertEquals(reflectionHashCode, reflectionHashCodeWithoutTransients);

        try {
            FieldUtils.writeFieldValue(obj, "a", null, true);
            reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, true, null, new String[0]);
            reflectionHashCodeWithoutTransients = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, false, null, new String[0]);
            assertEquals(reflectionHashCode, reflectionHashCodeWithoutTransients);
        } finally {
            FieldUtils.writeFieldValue(obj, "a", 1, true);
        }

        try {
            FieldUtils.writeFieldValue(obj, "c", null, true);
            reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, true, new String[] { "c" }, new String[0]);
            reflectionHashCodeWithoutTransients = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, false, new String[] { "c" }, new String[0]);
            assertNotEquals(reflectionHashCode, reflectionHashCodeWithoutTransients);
        } finally {
            FieldUtils.writeFieldValue(obj, "c", "test", true);
        }

        try {
            reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, true, null, new String[] { "c" });
            reflectionHashCodeWithoutTransients = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, false, null, new String[] { "c" });
            assertNotEquals(reflectionHashCode, reflectionHashCodeWithoutTransients);
        } finally {
            reflectionHashCode = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, true, null, new String[0]);
            reflectionHashCodeWithoutTransients = HashCodeBuilder.reflectionHashCode(obj.getClass(), obj, false, null, new String[0]);
            assertEquals(reflectionHashCode, reflectionHashCodeWithoutTransients);
        }
    }

    @Test
    public void testRegisterAndUnregister() {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        IDKey idKey1 = new IDKey(new Object());
        IDKey idKey2 = new IDKey(new Object());

        registry.add(idKey1);
        registry.add(idKey2);

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder();

        hashCodeBuilder1.append(new Object());
        hashCodeBuilder2.append(new Object());

        registry.clear();

        assertEquals(hashCodeBuilder1.hashCode(), hashCodeBuilder2.hashCode());

        registry.add(idKey1);
        registry.add(idKey2);

        assertEquals(hashCodeBuilder1.hashCode(), hashCodeBuilder2.hashCode());

        registry.remove(idKey1);

        assertNotEquals(hashCodeBuilder1.hashCode(), hashCodeBuilder2.hashCode());
    }

    static class IDKey {
        private final Object value;

        IDKey(Object value) {
            this.value = value;
        }

        Object getValue() {
            return value;
        }
    }
}