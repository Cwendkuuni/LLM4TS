package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object() {
            int value = 10;
            private int transientValue = 20;
        };
        Object obj2 = new Object() {
            int value = 10;
            private int transientValue = 20;
        };

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, comparison);

        obj1.getClass().getDeclaredField("transientValue").setAccessible(true);
        obj1.setTransientField("transientValue", 30);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, comparison);

        obj2.getClass().getDeclaredField("transientValue").setAccessible(true);
        obj2.setTransientField("transientValue", 35);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-1, comparison);

        obj1 = new Object() {
            int value = 10;
            private int transientValue = 20;
        };
        obj2 = null;

        try {
            comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        obj1 = null;
        obj2 = new Object() {
            int value = 10;
        };

        try {
            comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        obj1 = new Object() {
            int value = 10;
        };
        obj2 = new Object() {
            int value = 11;
        };

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludedFields() {
        Object obj1 = new ExcludedFieldObject() {
            int value = 10;
            private int excludedField = 20;
        };
        Object obj2 = new ExcludedFieldObject() {
            int value = 10;
            private int excludedField = 25;
        };

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("excludedField"));
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendSuper() {
        Object parent = new ParentObject();
        Object child1 = new ChildObject(5);
        Object child2 = new ChildObject(7);

        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(parent, child1));
        builder.append(child1, child2);

        int comparison = builder.toComparison();
        assertEquals(2, comparison);
    }

    @Test
    public void testAppendArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(arr1, arr2);

        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        arr1 = new int[] {1, 2, 3};
        arr2 = new int[] {1, 2, 4};

        comparison = builder.toComparison();
        assertEquals(-1, comparison);

        arr1 = null;
        arr2 = new int[] {1, 2, 3};

        try {
            builder.append(arr1, arr2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        arr1 = new int[] {1, 2, 3};
        arr2 = null;

        try {
            builder.append(arr1, arr2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        arr1 = new int[] {1, 2, 3};
        arr2 = new int[] {1, 2, 3};

        builder.append(arr1, arr2, new IntArrayComparator());

        comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendObjects() {
        Object obj1 = new Object() {
            int value = 10;
        };
        Object obj2 = new Object() {
            int value = 10;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);

        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        obj1 = new Object() {
            int value = 10;
        };
        obj2 = new Object() {
            int value = 11;
        };

        comparison = builder.toComparison();
        assertEquals(1, comparison);

        obj1 = new Object() {
            int value = 10;
        };
        obj2 = null;

        try {
            builder.append(obj1, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        obj1 = null;
        obj2 = new Object() {
            int value = 10;
        };

        try {
            builder.append(obj1, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        obj1 = new Object() {
            int value = 10;
        };
        obj2 = new Object() {
            int value = 10;
        };

        builder.append(obj1, obj2, new ObjectComparator());

        comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    private static class ParentObject {
    }

    private static class ChildObject extends ParentObject {
        private int value;

        public ChildObject(int value) {
            this.value = value;
        }
    }

    private static class ExcludedFieldObject {
        private int excludedField;
        private int value;

        public ExcludedFieldObject() {
        }

        public ExcludedFieldObject(int value) {
            this.value = value;
        }

        private void setExcludedField(int value) {
            this.excludedField = value;
        }

        private int getExcludedField() {
            return this.excludedField;
        }
    }

    private static class IntArrayComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof int[]) {
                int[] arr1 = (int[])o1;
                if (o2 instanceof int[]) {
                    int[] arr2 = (int[])o2;
                    return Arrays.compare(arr1, arr2);
                }
            }
            if (o2 instanceof int[]) {
                int[] arr2 = (int[])o2;
                throw new ClassCastException("Expected int[]");
            }
            throw new ClassCastException("Expected int[]");
        }
    }

    private static class ObjectComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Comparable) {
                Comparable<?> comparable1 = (Comparable<?>)o1;
                if (o2 instanceof Comparable) {
                    Comparable<?> comparable2 = (Comparable<?>)o2;
                    return comparable1.compareTo(comparable2);
                }
                throw new ClassCastException("Expected Comparable<?>");
            }
            if (o2 instanceof Comparable) {
                Comparable<?> comparable2 = (Comparable<?>)o2;
                throw new ClassCastException("Expected Comparable<?>");
            }
            throw new ClassCastException("Expected Comparable<?>");
        }
    }
}