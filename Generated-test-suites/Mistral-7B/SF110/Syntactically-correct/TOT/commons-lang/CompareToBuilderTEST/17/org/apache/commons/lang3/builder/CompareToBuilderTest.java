package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_objectsWithSameHashCode() {
        Object lhs = new Object();
        Object rhs = new Object();
        Assert.assertEquals(0, CompareToBuilder.reflectionCompare(lhs, rhs));
    }

    @Test
    public void testReflectionCompare_objectsWithDifferentClasses() {
        Object lhs = new Object();
        Object rhs = new String("test");
        Assert.assertFalse(lhs.getClass().isInstance(rhs));
        try {
            CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompare_objectsWithNullValues() {
        Object lhs = null;
        Object rhs = new Object();
        try {
            CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        Object lhs2 = new Object();
        Object rhs2 = null;
        try {
            CompareToBuilder.reflectionCompare(lhs2, rhs2);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompare_objectsWithSuperClassComparison() {
        Object parent = new ParentClass();
        Object child1 = new ChildClass1();
        Object child2 = new ChildClass2();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(CompareToBuilder.reflectionCompare((Object)parent, (Object)child1));
        compareToBuilder.appendSuper(CompareToBuilder.reflectionCompare((Object)child1, (Object)child2));
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(CompareToBuilder.reflectionCompare((Object)parent, (Object)child2), comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithNestedObjects() {
        Object lhs = new NestedObject1();
        Object rhs = new NestedObject2();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Field[] fieldsLhs = lhs.getClass().getDeclaredFields();
        Field[] fieldsRhs = rhs.getClass().getDeclaredFields();
        for (Field fieldLhs : fieldsLhs) {
            for (Field fieldRhs : fieldsRhs) {
                if (!ArrayUtils.contains(new String[]{fieldLhs.getName(), fieldRhs.getName()}, "transientField")) {
                    try {
                        compareToBuilder.append(fieldLhs.get(lhs), fieldRhs.get(rhs));
                    } catch (IllegalAccessException e) {
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(CompareToBuilder.reflectionCompare(lhs, rhs), comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithTransientFields() {
        Object lhs = new TransientFieldsObject();
        Object rhs = new TransientFieldsObject();
        CompareToBuilder compareToBuilder = new CompareToBuilder(false);
        Field[] fieldsLhs = lhs.getClass().getDeclaredFields();
        Field[] fieldsRhs = rhs.getClass().getDeclaredFields();
        for (Field fieldLhs : fieldsLhs) {
            for (Field fieldRhs : fieldsRhs) {
                if (!ArrayUtils.contains(new String[]{fieldLhs.getName(), fieldRhs.getName()}, "transientField")) {
                    try {
                        compareToBuilder.append(fieldLhs.get(lhs), fieldRhs.get(rhs));
                    } catch (IllegalAccessException e) {
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(CompareToBuilder.reflectionCompare(lhs, rhs), comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithExcludedFields() {
        Object lhs = new ObjectWithExcludedFields();
        Object rhs = new ObjectWithExcludedFields();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Comparator<?> comparator = (l, r) -> l.getClass().getName().compareTo(r.getClass().getName());
        int comparison = compareToBuilder.append(lhs, rhs, comparator).toComparison();
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithDifferentNumberOfFields() {
        Object lhs = new ObjectWithDifferentNumberOfFields();
        Object rhs = new ObjectWithDifferentNumberOfFields();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Field[] fieldsLhs = lhs.getClass().getDeclaredFields();
        Field[] fieldsRhs = rhs.getClass().getDeclaredFields();
        for (Field fieldLhs : fieldsLhs) {
            try {
                compareToBuilder.append(fieldLhs.get(lhs), null);
            } catch (IllegalAccessException e) {
                throw new InternalError("Unexpected IllegalAccessException");
            }
        }
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(-1, comparison);

        for (Field fieldRhs : fieldsRhs) {
            try {
                compareToBuilder.append(null, fieldRhs.get(rhs));
            } catch (IllegalAccessException e) {
                throw new InternalError("Unexpected IllegalAccessException");
            }
        }
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithPrimitiveArrays() {
        int[] intArrayLhs = {1, 2, 3};
        int[] intArrayRhs = {1, 2, 3};
        Comparator<int[]> comparator = (l, r) -> Arrays.compare(l, r);
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = compareToBuilder.append(intArrayLhs, intArrayRhs, comparator).toComparison();
        Assert.assertEquals(0, comparison);

        int[] intArrayLhsLonger = {1, 2, 3, 4};
        int[] intArrayRhs = {1, 2, 3};
        comparison = compareToBuilder.append(intArrayLhsLonger, intArrayRhs).toComparison();
        Assert.assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithNullPrimitiveArrays() {
        int[] intArrayLhs = null;
        int[] intArrayRhs = {1, 2, 3};
        Comparator<int[]> comparator = (l, r) -> Arrays.compare(l, r);
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = compareToBuilder.append(intArrayLhs, intArrayRhs, comparator).toComparison();
        Assert.assertEquals(-1, comparison);

        int[] intArrayLhsNull = {1, 2, 3};
        int[] intArrayRhsNull = null;
        comparison = compareToBuilder.append(intArrayLhsNull, intArrayRhsNull, comparator).toComparison();
        Assert.assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithComparableObjects() {
        Comparable<Object> comparableLhs = new ComparableObjectLhs();
        Comparable<Object> comparableRhs = new ComparableObjectRhs();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = compareToBuilder.append(comparableLhs, comparableRhs).toComparison();
        Assert.assertEquals(comparableLhs.compareTo(comparableRhs), comparison);
    }

    @Test
    public void testReflectionCompare_objectsWithComparator() {
        Object lhs = new ObjectWithComparator();
        Object rhs = new ObjectWithComparator();
        Comparator<Object> comparator = (l, r) -> ((ObjectWithComparator)l).comparator.compareTo(((ObjectWithComparator)r).comparator);
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = compareToBuilder.append(lhs, rhs, comparator).toComparison();
        Assert.assertEquals(0, comparison);
    }

    private static class ParentClass {
    }

    private static class ChildClass1 extends ParentClass {
    }

    private static class ChildClass2 extends ParentClass {
    }

    private static class NestedObject1 {
        private int field1;
        private String transientField;

        public NestedObject1(int field1) {
            this.field1 = field1;
        }

        public int getField1() {
            return field1;
        }

        private String getTransientField() {
            return transientField;
        }
    }

    private static class NestedObject2 {
        private int field1;
        private String transientField;

        public NestedObject2(int field1) {
            this.field1 = field1;
        }

        public int getField1() {
            return field1;
        }

        private String getTransientField() {
            return transientField;
        }
    }

    private static class TransientFieldsObject {
        @SuppressWarnings("transient")
        private transient int transientField;

        public int getNonTransientField() {
            return 0;
        }
    }

    private static class ObjectWithExcludedFields {
        private String excludedField1;
        private String excludedField2;

        public ObjectWithExcludedFields() {
        }

        public String getExcludedField1() {
            return excludedField1;
        }

        public String getExcludedField2() {
            return excludedField2;
        }

        public String getNonExcludedField() {
            return "non-excluded field";
        }
    }

    private static class ObjectWithDifferentNumberOfFields {
        private int field1;
        private int field2;

        public ObjectWithDifferentNumberOfFields() {
        }

        public int getField1() {
            return field1;
        }

        public int getField2() {
            return field2;
        }
    }

    private static class ComparableObjectLhs implements Comparable<Object> {
        private int value;

        public ComparableObjectLhs(int value) {
            this.value = value;
        }

        public int compareTo(Object o) {
            if (o instanceof ComparableObjectRhs) {
                ComparableObjectRhs rhs = (ComparableObjectRhs)o;
                return this.value - rhs.value;
            }
            throw new ClassCastException();
        }

        public int getValue() {
            return value;
        }
    }

    private static class ComparableObjectRhs implements Comparable<Object> {
        private int value;

        public ComparableObjectRhs(int value) {
            this.value = value;
        }

        public int compareTo(Object o) {
            if (o instanceof ComparableObjectLhs) {
                ComparableObjectLhs lhs = (ComparableObjectLhs)o;
                return lhs.value - this.value;
            }
            throw new ClassCastException();
        }

        public int getValue() {
            return value;
        }
    }

    private static class ObjectWithComparator {
        private Comparator<Object> comparator;

        public ObjectWithComparator(Comparator<Object> comparator) {
            this.comparator = comparator;
        }
    }
}