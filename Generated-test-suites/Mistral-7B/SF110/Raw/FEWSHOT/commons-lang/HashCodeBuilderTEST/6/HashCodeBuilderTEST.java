package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        HashCodeBuilder builder1 = new HashCodeBuilder(13, 23);
        HashCodeBuilder builder2 = new HashCodeBuilder(13, 23);
        assertEquals(builder1.toHashCode(), builder2.toHashCode());

        builder = new HashCodeBuilder(13, 23).append(true).append(1).append(1.0);
        builder1 = new HashCodeBuilder(13, 23).append(true).append(1).append(1.0);
        assertEquals(builder.toHashCode(), builder1.toHashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Object obj = new TestClass();
        assertEquals(220548681, HashCodeBuilder.reflectionHashCode(obj, false));
        assertEquals(1331238588, HashCodeBuilder.reflectionHashCode(obj, true));
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object obj = new TestClass();
        String[] excludeFields = {"field1", "field2"};
        assertEquals(127323913, HashCodeBuilder.reflectionHashCode(obj, false, TestClass.class, excludeFields));
    }

    @Test
    public void testRegistration() {
        Object obj = new TestClass();
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertNull(registry);
        HashCodeBuilder.reflectionHashCode(obj, false);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
        IDKey idKey = registry.iterator().next();
        assertEquals(obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj)), idKey.toString());
    }

    @Test
    public void testReflectionHashCodeWithSuperClass() {
        Object superObj = new TestSuperClass();
        Object obj = new TestClass();
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(superObj.hashCode());
        builder.append(obj);
        int hashCode = builder.toHashCode();
        int superHashCode = superObj.hashCode();
        int objHashCode = obj.hashCode();
        int expectedHashCode = (hashCode(superHashCode) * 37 + hashCode(objHashCode));
        assertEquals(expectedHashCode, hashCode);
    }

    private static int hashCode(int hashCode) {
        return 37 * hashCode + 1;
    }

    @Test
    public void testReflectionAppend() throws Exception {
        Object obj = new TestClass();
        HashCodeBuilder builder = new HashCodeBuilder();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = FieldUtils.readField(obj, fieldName, true);
            builder.append(fieldValue);
        }
        int expectedHashCode = 1612221811;
        int actualHashCode = builder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testIsRegistered() {
        Object obj = new TestClass();
        boolean isRegistered = HashCodeBuilder.isRegistered(obj);
        assertFalse(isRegistered);
        HashCodeBuilder.reflectionHashCode(obj, false);
        isRegistered = HashCodeBuilder.isRegistered(obj);
        assertTrue(isRegistered);
    }

    @Test
    public void testReflectionAppendWithTransients() throws Exception {
        Object obj = new TestClassWithTransientField();
        HashCodeBuilder builder = new HashCodeBuilder();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = FieldUtils.readField(obj, fieldName, true);
            if (!Modifier.isTransient(field.getModifiers())) {
                builder.append(fieldValue);
            }
        }
        int expectedHashCode = 1612221811;
        int actualHashCode = builder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionAppendWithExcludeFields() throws Exception {
        Object obj = new TestClass();
        HashCodeBuilder builder = new HashCodeBuilder();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String[] excludeFields = {"field1", "field2"};
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!ArrayUtils.contains(excludeFields, fieldName)) {
                Object fieldValue = FieldUtils.readField(obj, fieldName, true);
                builder.append(fieldValue);
            }
        }
        int expectedHashCode = 1428676521;
        int actualHashCode = builder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    private static class TestClass {
        private int field1;
        private String field2;

        @Override
        public String toString() {
            return "TestClass [field1=" + field1 + ", field2=" + field2 + "]";
        }
    }

    private static class TestClassWithTransientField {
        @SuppressWarnings("unused")
        private transient int transientField;

        private int field1;
        private String field2;

        @Override
        public String toString() {
            return "TestClassWithTransientField [field1=" + field1 + ", field2=" + field2 + "]";
        }
    }

    private static class TestSuperClass {

    }
}