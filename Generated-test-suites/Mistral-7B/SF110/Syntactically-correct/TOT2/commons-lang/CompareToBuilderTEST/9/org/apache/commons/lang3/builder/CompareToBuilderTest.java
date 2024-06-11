package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testAppend_Objects_Null_Null() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        compareToBuilder.append(null, null);
        assertEquals(0, compareToBuilder.build());
    }

    @Test
    public void testAppend_Objects_Null_Nonnull() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        Object obj1 = new Object();
        compareToBuilder.append(null, obj1);
        assertEquals(-1, compareToBuilder.build());
    }

    @Test
    public void testAppend_Objects_Nonnull_Null() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        Object obj1 = new Object();
        compareToBuilder.append(obj1, null);
        assertEquals(1, compareToBuilder.build());
    }

    @Test
    public void testAppend_Objects() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        Object obj1 = new Object();
        Object obj2 = new Object();
        compareToBuilder.append(obj1, obj2);
        assertEquals(-1, compareToBuilder.build());
    }

    @Test
    public void testAppend_Primitive_Int() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        int int1 = 1;
        compareToBuilder.append(int1, int1);
        assertEquals(0, compareToBuilder.build());
    }

    @Test
    public void testAppend_Primitive_Int_Negative() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        int int1 = 1;
        compareToBuilder.append(int1, -int1);
        assertEquals(-2, compareToBuilder.build());
    }

    @Test
    public void testAppend_Primitive_Int_Positive() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        int int1 = 1;
        compareToBuilder.append(int1, int1 + 1);
        assertEquals(1, compareToBuilder.build());
    }

    @Test
    public void testAppend_Primitive_Long() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        long long1 = 1L;
        compareToBuilder.append(long1, long1);
        assertEquals(0, compareToBuilder.build());
    }

    @Test
    public void testAppend_Primitive_Long_Negative() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        long long1 = 1L;
        compareToBuilder.append(long1, -long1);
        assertEquals(-2, compareToBuilder.build());
    }

    @Test
    public void testAppend_Primitive_Long_Positive() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        long long1 = 1L;
        compareToBuilder.append(long1, long1 + 1);
        assertEquals(1, compareToBuilder.build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppend_Primitive_IllegalArgument() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        assertNotNull(compareToBuilder.build());
        assertEquals(0, compareToBuilder.build());

        compareToBuilder.append(Short.MIN_VALUE, Byte.MAX_VALUE);
    }

    @Test
    public void testReflectionCompare_Objects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompare_Objects_Null() {
        Object obj1 = new Object();
        Object obj2 = null;
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompare_Objects_SameInstance() {
        Object obj1 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj1);
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompare_Objects_Subclass() {
        Class<?> subclass = TestSubclass.class;
        Object obj1 = new TestSubclass();
        Object obj2 = new TestSubclass();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompare_Objects_DifferentClasses() {
        Object obj1 = new Object();
        Object obj2 = new String("Hello World");
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompare_Objects_Collections() {
        Object obj1 = Arrays.asList(1, 2, 3);
        Object obj2 = Arrays.asList(2, 3, 4);
        int result = CompareToBuilder.reflectionCompare(obj1, obj2, null);
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompare_Objects_Collections_Comparator() {
        Object obj1 = Arrays.asList(1, 2, 3);
        Object obj2 = Arrays.asList(3, 2, 1);
        int result = CompareToBuilder.reflectionCompare(obj1, obj2, Collections.reverseOrder());
        assertEquals(1, result);
    }

    @Test
    public void testReflectionCompare_Objects_Collections_CustomComparator() {
        Object obj1 = Arrays.asList(1, 2, 3);
        Object obj2 = Arrays.asList(3, 2, 1);
        CustomComparator customComparator = new CustomComparator();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2, customComparator);
        assertEquals(-1, result);
    }

    @Test
    public void testAppendSuper_Int() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(1);
        assertEquals(1, compareToBuilder.build());
    }

    @Test
    public void testAppendSuper_Int_Negative() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(-1);
        assertEquals(-1, compareToBuilder.build());
    }

    @Test
    public void testAppendSuper_Int_Positive() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(2);
        assertEquals(2, compareToBuilder.build());
    }

    @Test
    public void testReflectionAppend_Objects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Class<?> obj1Class = obj1.getClass();
        Field[] fields = obj1Class.getDeclaredFields();
        for (Field field : fields) {
            if (ArrayUtils.contains(new String[]{"excludeFields1", "excludeFields2"}, field.getName())) {
                continue;
            }
            try {
                compareToBuilder.append(field.get(obj1), field.get(obj2));
            } catch (IllegalAccessException e) {
                fail();
            }
        }
        int result = compareToBuilder.build();
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionAppend_Objects_Transients() {
        TestWithTransientField testObject = new TestWithTransientField();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Field[] fields = TestWithTransientField.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            try {
                compareToBuilder.append(field.get(testObject), field.get(testObject));
            } catch (IllegalAccessException e) {
                fail();
            }
        }
        int result = compareToBuilder.build();
        assertEquals(0, result);
    }

    @Test
    public void testReflectionAppend_Objects_Static() {
        TestWithStaticField testObject = new TestWithStaticField();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Field[] fields = TestWithStaticField.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                compareToBuilder.append(field.get(testObject), field.get(testObject));
            } catch (IllegalAccessException e) {
                fail();
            }
        }
        int result = compareToBuilder.build();
        assertEquals(0, result);
    }

    private static class TestSubclass extends Object {
    }

    private static class CustomComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            return o2.toString().compareTo(o1.toString());
        }
    }

    private static class TestWithTransientField {
        @SuppressWarnings("transient")
        private transient int transientField = 1;
        private int nonTransientField = 2;

        public TestWithTransientField() {
        }

        public int getTransientField() {
            return transientField;
        }

        public int getNonTransientField() {
            return nonTransientField;
        }
    }

    private static class TestWithStaticField {
        private static int staticField = 1;
        private int nonStaticField = 2;

        public TestWithStaticField() {
        }

        public static int getStaticField() {
            return staticField;
        }

        public int getNonStaticField() {
            return nonStaticField;
        }
    }
}