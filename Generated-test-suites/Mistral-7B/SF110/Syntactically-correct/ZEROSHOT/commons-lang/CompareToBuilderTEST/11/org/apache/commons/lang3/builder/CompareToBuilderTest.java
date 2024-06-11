package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class CompareToBuilderTest {

    @org.junit.Test
    public void testReflectionCompare_Objects() {
        Object lhs = new Object();
        Object rhs = new Object();
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);
    }

    @org.junit.Test
    public void testReflectionCompare_ObjectsDifferentTypes() {
        Object lhs = new Object();
        String rhs = new String("test");
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testReflectionCompare_ObjectsDifferentValues() {
        Object lhs = new Object() {
            private int value = 1;
        };
        Object rhs = new Object() {
            private int value = 2;
        };
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testReflectionCompare_ExcludeFields() {
        Object lhs = new ExcludedFieldObject() {
            private int excludedField = 1;
        };
        Object rhs = new ExcludedFieldObject() {
            private int excludedField = 2;
        };
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("excludedField"));
        assertEquals(0, result);
    }

    @org.junit.Test
    public void testReflectionCompare_CompareTransients() {
        Object lhs = new TransientFieldObject();
        Object rhs = new TransientFieldObject();
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, result);
    }

    @org.junit.Test
    public void testComparisonWithSuperClass() {
        Object lhs = new SuperClass();
        Object rhs = new Subclass();
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(lhs, rhs, SuperClass.class));
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testComparisonWithArray_Boolean() {
        boolean[] lhs = {true, false};
        boolean[] rhs = {false, true};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testComparisonWithArray_Int() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {3, 2, 1};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs, null);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testComparisonWithArray_String() {
        String[] lhs = {"a", "b", "c"};
        String[] rhs = {"b", "c", "a"};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs, new StringComparator());
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testComparisonWithArray_CustomComparator() {
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {3, 2, 1};
        Comparator<Integer> comparator = (o1, o2) -> o1.compareTo(o2) * -1;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs, comparator);
        int result = builder.toComparison();
        assertEquals(1, result);
    }

    private static class SuperClass {
        // Empty class
    }

    private static class Subclass extends SuperClass {
        // Empty class
    }

    private static class ExcludedFieldObject {
        private int excludedField;

        @Override
        public String toString() {
            return "ExcludedFieldObject{" +
                    "excludedField=" + excludedField +
                    '}';
        }
    }

    private static class TransientFieldObject {
        @SuppressWarnings("unused")
        private transient int transientField;

        @Override
        public String toString() {
            return "TransientFieldObject{" +
                    "transientField=" + transientField +
                    '}';
        }
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
}

class ReflectionTestUtils {
    static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, CompareToBuilder builder, boolean useTransients, String[] excludeFields) {
        try {
            AccessibleObject.setAccessible(FieldUtils.allFields(clazz), true);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!Arrays.asList(excludeFields).contains(field.getName()) &&
                        !field.getName().contains("$") &&
                        (useTransients || !Modifier.isTransient(field.getModifiers())) &&
                        !Modifier.isStatic(field.getModifiers())) {
                    Object lhsValue = FieldUtils.readField(lhs, field.getName(), true);
                    Object rhsValue = FieldUtils.readField(rhs, field.getName(), true);
                    builder.append(lhsValue, rhsValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}