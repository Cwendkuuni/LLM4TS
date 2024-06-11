package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int i = 1;
            int j = 2;
        };
        Object rhs = new Object() {
            int i = 2;
            int j = 1;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
        };
        rhs = new Object() {
            int i = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new Object() {
            int i = null;
        };
        rhs = new Object() {
            int i = 1;
        };
        try {
            comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        lhs = new Object() {
            int i = 1;
        };
        rhs = null;
        try {
            comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        lhs = new int[3];
        rhs = new int[3];
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3, 4};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludedFields() {
        Object lhs = new ExcludedFieldObject() {
            int i = 1;
            int j = 2;
            String excludedField = "excluded";
        };
        Object rhs = new ExcludedFieldObject() {
            int i = 2;
            int j = 1;
            String excludedField = "excluded";
        };
        Comparator<?> comparator = Comparators.naturalOrder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(-1, comparison);

        lhs = new ExcludedFieldObject() {
            int i = 1;
            int j = 2;
            String excludedField = null;
        };
        rhs = new ExcludedFieldObject() {
            int i = 2;
            int j = 1;
            String excludedField = null;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(0, comparison);

        lhs = new ExcludedFieldObject();
        rhs = new ExcludedFieldObject();
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(0, comparison);

        lhs = new ExcludedFieldObject() {
            int i = 1;
        };
        rhs = new ExcludedFieldObject() {
            int i = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(0, comparison);

        lhs = new ExcludedFieldObject() {
            int i = 1;
        };
        rhs = new IntArrayObject() {
            int[] array = {1};
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithTransients() {
        Object lhs = new TransientFieldObject() {
            int i = 1;
            transient int j = 2;
        };
        Object rhs = new TransientFieldObject() {
            int i = 2;
            transient int j = 1;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(-1, comparison);

        lhs = new TransientFieldObject() {
            int i = 1;
            transient int j = 1;
        };
        rhs = new TransientFieldObject() {
            int i = 1;
            transient int j = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);

        lhs = new TransientFieldObject() {
            int i = 1;
        };
        rhs = new TransientFieldObject() {
            int i = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);

        lhs = new TransientFieldObject() {
            int i = 1;
        };
        rhs = new IntArrayObject() {
            int[] array = {1};
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();

        Object lhs = new Object() {
            int i = 1;
        };
        Object rhs = new Object() {
            int i = 1;
        };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new Object() {
            int i = 1;
        };
        rhs = new Object() {
            int i = 2;
        };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new Object() {
            int i = 1;
        };
        rhs = null;
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = null;
        rhs = new Object() {
            int i = 1;
        };
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());

        lhs = new int[3];
        rhs = new int[3];
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3};
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3, 4};
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new String[] {"foo", "bar"};
        rhs = new String[] {"bar", "foo"};
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new String[] {"foo", "bar"};
        rhs = new String[] {"foo", "bar", "baz"};
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithComparator() {
        CompareToBuilder builder = new CompareToBuilder();

        Object lhs = new Object() {
            int i = 1;
        };
        Object rhs = new Object() {
            int i = 1;
        };
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(0, builder.toComparison());

        lhs = new Object() {
            int i = 1;
        };
        rhs = new Object() {
            int i = 2;
        };
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(-1, builder.toComparison());

        lhs = new Object() {
            int i = 1;
        };
        rhs = null;
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(-1, builder.toComparison());

        lhs = null;
        rhs = new Object() {
            int i = 1;
        };
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(1, builder.toComparison());

        lhs = new int[3];
        rhs = new int[3];
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(0, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3};
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(0, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(-1, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3, 4};
        builder.append(lhs, rhs, Comparators.naturalOrder());
        assertEquals(-1, builder.toComparison());

        lhs = new String[] {"foo", "bar"};
        rhs = new String[] {"bar", "foo"};
        builder.append(lhs, rhs, new StringComparator());
        assertEquals(1, builder.toComparison());

        lhs = new String[] {"foo", "bar"};
        rhs = new String[] {"foo", "bar", "baz"};
        builder.append(lhs, rhs, new StringComparator());
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();

        Object lhs = new SuperClassObject() {
            int i = 1;
        };
        Object rhs = new SuperClassObject() {
            int i = 2;
        };
        builder.appendSuper(1);
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());

        lhs = new SuperClassObject() {
            int i = 1;
        };
        rhs = new SubClassObject() {
            int i = 1;
            int j = 2;
        };
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new SubClassObject() {
            int i = 1;
            int j = 1;
        };
        rhs = new SubClassObject() {
            int i = 1;
            int j = 1;
        };
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new SuperClassObject() {
            int i = 1;
        };
        rhs = null;
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = null;
        rhs = new SuperClassObject() {
            int i = 1;
        };
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());

        lhs = new int[3];
        rhs = new int[3];
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3};
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3, 4};
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new String[] {"foo", "bar"};
        rhs = new String[] {"bar", "foo"};
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());

        lhs = new String[] {"foo", "bar"};
        rhs = new String[] {"foo", "bar", "baz"};
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    private static class ExcludedFieldObject {
        int i;
        String excludedField;

        @Override
        public String toString() {
            return "ExcludedFieldObject{" +
                    "i=" + i +
                    ", excludedField=" + excludedField +
                    '}';
        }
    }

    private static class IntArrayObject {
        int[] array;

        @Override
        public String toString() {
            return "IntArrayObject{" +
                    "array=" + Arrays.toString(array) +
                    '}';
        }
    }

    private static class SubClassObject extends SuperClassObject {
        int j;

        @Override
        public String toString() {
            return "SubClassObject{" +
                    "i=" + super.i +
                    ", j=" + j +
                    '}';
        }
    }

    private static class SuperClassObject {
        int i;

        @Override
        public String toString() {
            return "SuperClassObject{" +
                    "i=" + i +
                    '}';
        }
    }

    private static class StringComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof String && o2 instanceof String) {
                String s1 = (String) o1;
                String s2 = (String) o2;
                return s1.compareTo(s2);
            }
            throw new IllegalArgumentException();
        }
    }
}