package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        lhs = null;
        rhs = null;
        try {
            comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object();
        rhs = new Object();
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new Object() {
            int value = 1;
        };
        rhs = lhs;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithCompareTransientsTrue() {
        class WithTransientField {
            transient int transientField;
            int nonTransientField = 1;
        }

        WithTransientField lhs = new WithTransientField();
        WithTransientField rhs = new WithTransientField();
        rhs.nonTransientField = 2;
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        class WithExcludedField {
            String excludedField;
            int fieldToCompare = 1;

            @Override
            public String toString() {
                return "Object{" +
                        "excludedField='" + excludedField + '\'' +
                        ", fieldToCompare=" + fieldToCompare + '}';
            }
        }

        WithExcludedField lhs = new WithExcludedField();
        WithExcludedField rhs = new WithExcludedField();
        rhs.fieldToCompare = 2;

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField1", "excludedField2"});
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null);
        assertEquals(Exception.class, comparisonInstanceOf(comparison, Exception.class));
    }

    @Test
    public void testReflectionCompareWithExcludeFieldsAndCompareTransientsTrue() {
        class WithTransientAndExcludedFields {
            transient int transientField;
            String excludedField;
            int fieldToCompare = 1;

            @Override
            public String toString() {
                return "Object{" +
                        "transientField=" + transientField +
                        ", excludedField='" + excludedField + '\'' +
                        ", fieldToCompare=" + fieldToCompare + '}';
            }
        }

        WithTransientAndExcludedFields lhs = new WithTransientAndExcludedFields();
        WithTransientAndExcludedFields rhs = new WithTransientAndExcludedFields();
        rhs.fieldToCompare = 2;

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"}, true);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder anotherBuilder = new CompareToBuilder();
        int superComparisonTo = 3;

        builder.appendSuper(superComparisonTo);
        int comparison = anotherBuilder.comparison;
        builder.append(lhs, rhs);
        int expectedComparison = superComparisonTo;
        assertEquals(expectedComparison, comparison);
    }

    @Test
    public void testAppend() {
        class WithComparableField implements Comparable<Object> {
            int comparableField = 1;

            @Override
            public int compareTo(Object o) {
                if (o instanceof WithComparableField) {
                    WithComparableField other = (WithComparableField) o;
                    return Integer.compare(comparableField, other.comparableField);
                }
                throw new UnsupportedOperationException();
            }
        }

        WithComparableField lhs = new WithComparableField();
        WithComparableField rhs = new WithComparableField();
        rhs.comparableField = 2;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.comparison;
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendWithComparator() {
        class WithComparableField implements Comparable<Object> {
            int comparableField = 1;

            @Override
            public int compareTo(Object o) {
                if (o instanceof WithComparableField) {
                    WithComparableField other = (WithComparableField) o;
                    return Integer.compare(comparableField, other.comparableField);
                }
                throw new UnsupportedOperationException();
            }
        }

        WithComparableField lhs = new WithComparableField();
        WithComparableField rhs = new WithComparableField();
        rhs.comparableField = 2;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof WithComparableField && o2 instanceof WithComparableField) {
                    WithComparableField lhs = (WithComparableField) o1;
                    WithComparableField rhs = (WithComparableField) o2;
                    return lhs.comparableField.compareTo(rhs.comparableField) * -1;
                }
                throw new UnsupportedOperationException();
            }
        });
        int comparison = builder.comparison;
        assertEquals(1, comparison);
    }

    @Test
    public void testAppendWithArray() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 4};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.comparison;
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendWithArrayAndComparator() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 4};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof int[] && o2 instanceof int[]) {
                    int[] lhs = (int[]) o1;
                    int[] rhs = (int[]) o2;
                    int length = Math.min(lhs.length, rhs.length);
                    for (int i = 0; i < length; ++i) {
                        if (lhs[i] < rhs[i]) {
                            return -1;
                        } else if (lhs[i] > rhs[i]) {
                            return 1;
                        }
                    }
                    return Integer.compare(lhs.length, rhs.length);
                }
                throw new UnsupportedOperationException();
            }
        });
        int comparison = builder.comparison;
        assertEquals(1, comparison);
    }

    @Test
    public void testAppendWithLongArray() {
        long[] lhs = {1L, 2L};
        long[] rhs = {1L, 3L};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.comparison;
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendWithLongArrayAndComparator() {
        long[] lhs = {1L, 2L};
        long[] rhs = {1L, 3L};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof long[] && o2 instanceof long[]) {
                    long[] lhs = (long[]) o1;
                    long[] rhs = (long[]) o2;
                    int length = Math.min(lhs.length, rhs.length);
                    for (int i = 0; i < length; ++i) {
                        if (lhs[i] < rhs[i]) {
                            return -1;
                        } else if (lhs[i] > rhs[i]) {
                            return 1;
                        }
                    }
                    return Long.compare(lhs.length, rhs.length);
                }
                throw new UnsupportedOperationException();
            }
        });
        int comparison = builder.comparison;
        assertEquals(1, comparison);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean comparisonInstanceOf(int comparison, Class<?> clazz) {
        return comparison instanceof Integer && ((Integer) comparison).intValue() instanceof clazz;
    }
}