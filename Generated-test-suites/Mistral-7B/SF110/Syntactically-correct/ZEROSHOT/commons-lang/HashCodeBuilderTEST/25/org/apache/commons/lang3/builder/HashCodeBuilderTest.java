import org.apache.commons.lang3.Builder;
import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.Modifier;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderWithDefaultValues() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int hashCode = builder.toHashCode();
        assertEquals(17, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithCustomValues() {
        HashCodeBuilder builder = new HashCodeBuilder(13, 11);
        builder.append(3);
        builder.append("test");
        int hashCode = builder.toHashCode();
        assertEquals(166933023, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-1224569175, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {1, 2, 3};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-1550133737, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = {'a', 'b', 'c'};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-1743674671, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = {1.0, 2.0, 3.0};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-1826018937, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = {1.0f, 2.0f, 3.0f};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-1353214633, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-1361526475, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = {1L, 2L, 3L};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-7074888182945560659L, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {1, "test", new Integer(3)};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(1296311938, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithArrayOfShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = {1, 2, 3};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(-1276936266, hashCode);
    }

    @Test
    public void testHashCodeBuilderWithCustomObject() {
        MyCustomObject customObject = new MyCustomObject("test");
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(customObject);
        int hashCode = builder.toHashCode();
        assertEquals(412851158, hashCode);
    }

    @Test
    public void testReflectionHashCode() {
        MyCustomObject customObject = new MyCustomObject("test");
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(customObject);
        int hashCode = builder.toHashCode();
        assertEquals(412851158, hashCode);
        assertEquals(412851158, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        MyCustomObject customObject = new MyCustomObject("test");
        String[] excludeFields = {"excludedField"};
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(customObject, null, false, MyCustomObject.class, excludeFields);
        int hashCode = builder.toHashCode();
        assertEquals(412851158, hashCode);
        assertEquals(412851158, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeWithTestTransients() {
        MyCustomObject customObject = new MyCustomObject("test");
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(customObject, true, null, MyCustomObject.class, new String[0]);
        int hashCode = builder.toHashCode();
        assertEquals(412851158, hashCode);
        assertEquals(412851158, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeWithSpecifiedSuperHashCode() {
        MyCustomSuperObject customObject = new MyCustomSuperObject("test");
        int superHashCode = 42;
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(customObject, superHashCode, false, MyCustomSuperObject.class, new String[0]);
        int hashCode = builder.toHashCode();
        assertEquals(17 + 42, hashCode);
        assertEquals(reflectionHashCode, hashCode);
    }

    @Test
    public void testIsRegistered() {
        MyCustomObject customObject = new MyCustomObject("test");
        boolean registered = HashCodeBuilder.isRegistered(customObject);
        boolean registered2 = HashCodeBuilder.isRegistered(customObject);
        assertTrue(registered);
        assertTrue(registered2);
    }

    @Test
    public void testReflectionAppend() throws IllegalAccessException, NoSuchFieldException {
        MyCustomObject customObject = new MyCustomObject("test");
        Class<?> clazz = customObject.getClass();
        Field field = clazz.getDeclaredField("excludedField");
        FieldUtils.makeAccessible(field);
        String value = "testField";
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(customObject);
        FieldUtils.setFieldValue(field, customObject, value);
        HashCodeBuilder builder2 = new HashCodeBuilder();
        HashCodeBuilder.reflectionAppend(customObject, clazz, builder2, false, new String[0]);
        int hashCode = builder2.toHashCode();
        int hashCode2 = builder.toHashCode();
        assertEquals(hashCode2, hashCode);
    }

    @Test
    public void testGetRegistry() {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertTrue(registry != null);
        MyCustomObject customObject1 = new MyCustomObject("test1");
        MyCustomObject customObject2 = new MyCustomObject("test2");
        MyCustomObject customObject3 = new MyCustomObject("test3");
        registry.add(new IDKey(customObject1));
        registry.add(new IDKey(customObject2));
        registry.add(new IDKey(customObject3));
        Set<IDKey> registry2 = HashCodeBuilder.getRegistry();
        assertEquals(registry.size(), registry2.size());
        assertTrue(registry.containsAll(registry2));
        assertTrue(registry2.containsAll(registry));
    }

    private static class MyCustomObject {
        private String field;

        public MyCustomObject(String field) {
            this.field = field;
        }

        @Override
        public int hashCode() {
            return this.field.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MyCustomObject) {
                MyCustomObject other = (MyCustomObject) obj;
                return this.field.equals(other.field);
            }
            return false;
        }

        private String getExcludedField() {
            return "excludedField";
        }

        private static class IDKey {
            private Object key;

            public IDKey(Object key) {
                this.key = key;
            }

            @Override
            public int hashCode() {
                return key.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return key.equals(obj);
            }
        }
    }

    private static class MyCustomSuperObject extends MyCustomObject {
        private int superHashCode;

        public MyCustomSuperObject(String field, int superHashCode) {
            super(field);
            this.superHashCode = superHashCode;
        }
    }
}