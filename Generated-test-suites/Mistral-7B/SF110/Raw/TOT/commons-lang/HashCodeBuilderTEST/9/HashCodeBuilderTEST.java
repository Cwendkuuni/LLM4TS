import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderBasicUsage() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append("Hello");
        hcb.append(123);
        hcb.append(true);
        int hashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append("Hello");
        hcb2.append(123);
        hcb2.append(true);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testHashCodeBuilderArrayUsage() {
        int[] arr = {1, 2, 3};
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(arr);
        int hashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(arr);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testHashCodeBuilderClassUsage() {
        String className = "java.lang.String";
        Object obj = "Test string";
        Class<? extends Object> clazz = (Class<? extends Object>)Class.forName(className);
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(obj.getClass().getName());
        hcb.append(obj);
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(clazz.getName());
        hcb2.append(obj);
        int hashCode = hcb.toHashCode();
        int hashCode2 = hcb2.toHashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderNullObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderNullArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] arr = null;
        hcb.append(arr);
    }

    @Test
    public void testHashCodeBuilderReflectionUsage() {
        Object obj = new Object() {
            int i = 1;
            int j = 2;
            int k = 3;
        };
        Class<?> clazz = obj.getClass();
        int initialNonZeroOddNumber = 17;
        int multiplierNonZeroOddNumber = 37;
        int hashCode = HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, obj, false, clazz, new String[0]);
        HashCodeBuilder hcb = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        hcb.append(obj);
        int hashCode2 = hcb.toHashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testHashCodeBuilderReflectionUsageWithExcludedFields() {
        Object obj = new Object() {
            int i = 1;
            int j = 2;
            int k = 3;
            private int m = 4;
            private int n = 5;
        };
        Class<?> clazz = obj.getClass();
        int initialNonZeroOddNumber = 17;
        int multiplierNonZeroOddNumber = 37;
        String[] excludeFields = {"m", "n"};
        int hashCode = HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, obj, false, clazz, excludeFields);
        HashCodeBuilder hcb = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        hcb.append(obj);
        int hashCode2 = hcb.toHashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testHashCodeBuilderReflectionUsageWithTestTransients() {
        Object obj = new Object() {
            int i = 1;
            int j = 2;
            int k = 3;
            private int m = 4;
            private transient int n = 5;
        };
        Class<?> clazz = obj.getClass();
        int initialNonZeroOddNumber = 17;
        int multiplierNonZeroOddNumber = 37;
        int hashCode = HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, obj, true, clazz, new String[0]);
        HashCodeBuilder hcb = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        hcb.append(obj);
        int hashCode2 = hcb.toHashCode();
        assertEquals(hashCode, hashCode2);
    }

    private static class IDKey {
        private final Object value;

        IDKey(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return other != null && other instanceof IDKey && value.equals(((IDKey)other).value);
        }
    }

    @Test
    public void testHashCodeBuilderRegistry() throws Exception {
        Object obj = new Object();
        int hashCodeBefore = HashCodeBuilder.reflectionHashCode(obj);
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(obj);
        int hashCodeAfter = hcb.toHashCode();
        assertTrue(hashCodeBefore != hashCodeAfter);
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(obj);
        int hashCodeAfter2 = hcb2.toHashCode();
        assertTrue(hashCodeAfter == hashCodeAfter2);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
        assertEquals(new IDKey(obj), registry.iterator().next().getValue());
    }
}