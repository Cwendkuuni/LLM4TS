package org.apache.commons.lang3.builder;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object() {
            int value = 5;
            long anotherValue = 10L;
            boolean booleanValue = true;
            char charValue = 'A';
            short shortValue = 10;
            byte byteValue = 5;
            double doubleValue = 3.14;
            float floatValue = 1.2F;
            boolean[] booleanArray = {true, false};
            int[] intArray = {1, 2, 3};
            short[] shortArray = {1, 2, 3};
            char[] charArray = {'A', 'B', 'C'};
            byte[] byteArray = {5, 10};
            double[] doubleArray = {1.2, 3.14, 5.6};
            float[] floatArray = {1.2F, 3.1F, 5.6F};
        };

        Object obj2 = new Object() {
            int value = 5;
            long anotherValue = 10L;
            boolean booleanValue = true;
            char charValue = 'A';
            short shortValue = 11;
            byte byteValue = 5;
            double doubleValue = 3.14;
            float floatValue = 1.2F;
            boolean[] booleanArray = {true, false};
            int[] intArray = {1, 2, 3};
            short[] shortArray = {1, 2, 3};
            char[] charArray = {'A', 'B', 'C'};
            byte[] byteArray = {5, 10};
            double[] doubleArray = {1.2, 3.14, 5.6};
            float[] floatArray = {1.2F, 3.1F, 5.6F};
        };

        Object[] objs = new Object[]{ obj1, obj2 };
        Comparator<?> comparator = null;

        for (int i = 0; i < objs.length - 1; i++) {
            for (int j = i + 1; j < objs.length; j++) {
                int comparison = CompareToBuilder.reflectionCompare(objs[i], objs[j]);
                Assert.assertNotEquals("Comparison between objects should not be 0", 0, comparison);
                if (comparison < 0) {
                    Assert.assertTrue("First object should come before the second", CompareToBuilder.reflectionCompare(objs[j], objs[i]) > 0);
                } else {
                    Assert.assertTrue("Second object should come before the first", CompareToBuilder.reflectionCompare(objs[i], objs[j]) < 0);
                }

                comparison = CompareToBuilder.reflectionCompare(objs[i], objs[i]);
                Assert.assertEquals("Comparison of the same object should be 0", 0, comparison);

                comparison = CompareToBuilder.reflectionCompare(null, objs[i]);
                Assert.assertTrue("Null object should come before any non-null object", comparison < 0);

                comparison = CompareToBuilder.reflectionCompare(objs[i], null);
                Assert.assertTrue("Non-null object should come before a null object", comparison > 0);

                comparison = CompareToBuilder.reflectionCompare(objs[i], objs[j], new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return ((Integer)o1).compareTo((Integer)o2);
                    }
                });
                Assert.assertEquals("Comparison using a custom comparator should match the result of reflectionCompare", comparison, CompareToBuilder.reflectionCompare(objs[i], objs[j]));
            }
        }
    }

    @Test
    public void testReflectionCompareExcludeFields() {
        Object obj1 = new Object() {
            int value = 5;
            long anotherValue = 10L;
            boolean booleanValue = true;
            char charValue = 'A';
            private short shortValue = 10;
            private byte byteValue = 5;

            @Override
            public String toString() {
                return "Object{" +
                        "value=" + value +
                        ", anotherValue=" + anotherValue +
                        ", booleanValue=" + booleanValue +
                        ", charValue=" + charValue +
                        '}';
            }
        };

        Object obj2 = new Object() {
            int value = 5;
            long anotherValue = 10L;
            boolean booleanValue = true;
            char charValue = 'A';
            private short shortValue = 11;
            private byte byteValue = 5;

            @Override
            public String toString() {
                return "Object{" +
                        "value=" + value +
                        ", anotherValue=" + anotherValue +
                        ", booleanValue=" + booleanValue +
                        ", charValue=" + charValue +
                        '}';
            }
        };

        String[] excludeFields = new String[]{"shortValue", "byteValue"};

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, excludeFields);
        Assert.assertEquals("Comparison excluding specific fields should match the result of reflectionCompare without excluding fields", comparison, CompareToBuilder.reflectionCompare(obj1, obj2));
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException1() {
        CompareToBuilder.reflectionCompare(null, null);
    }

    @Test(expected = ClassCastException.class)
    public void testClassCastException() {
        Object obj1 = new String("Hello");
        Object obj2 = new Integer(5);
        CompareToBuilder.reflectionCompare(obj1, obj2);
    }

    @Test(expected = ClassCastException.class)
    public void testClassCastException2() {
        Object obj1 = new Integer(5);
        Object obj2 = new String("Hello");
        CompareToBuilder.reflectionCompare(obj1, obj2);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        CompareToBuilder compareToBuilder2 = new CompareToBuilder();
        compareToBuilder.appendSuper(1);
        compareToBuilder2.appendSuper(2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two CompareToBuilder instances with appended super comparisons should follow the order", comparison, 1);

        comparison = compareToBuilder2.toComparison();
        Assert.assertEquals("Comparison of two CompareToBuilder instances with appended super comparisons should follow the order", comparison, 2);
    }

    @Test
    public void testAppend() {
        Object obj1 = new Object() {
            int i = 5;
            long j = 10L;
            char c = 'A';
            short s = 10;
            byte b = 5;
            double d = 3.14;
            float f = 1.2F;
            boolean bool = true;
        };

        Object obj2 = new Object() {
            int i = 5;
            long j = 10L;
            char c = 'B';
            short s = 11;
            byte b = 5;
            double d = 3.14;
            float f = 1.2F;
            boolean bool = true;
        };

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two objects should be -1, as the first object comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj2, obj1);

        comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of the same objects in reverse order should give a positive value", comparison, 1);
    }

    @Test
    public void testAppendArray() {
        Object[] objArray1 = new Object[]{1, 2, 3};
        Object[] objArray2 = new Object[]{1, 2, 4};

        Comparator<Object[]> comparator = new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                int comparison = Arrays.compare(o1, o2);
                if (comparison == 0) {
                    for (int i = 0; i < o1.length; i++) {
                        comparison = CompareToBuilder.reflectionCompare(o1[i], o2[i]);
                        if (comparison != 0) {
                            break;
                        }
                    }
                }
                return comparison;
            }
        };

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(objArray1, objArray2, comparator);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(objArray1, objArray2, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                int comparison = Arrays.compare(o1, o2);
                if (comparison == 0) {
                    for (int i = 0; i < o1.length; i++) {
                        comparison = CompareToBuilder.reflectionCompare(o1[i], o2[i]);
                        if (comparison != 0) {
                            break;
                        }
                    }
                }
                return comparison;
            }
        });
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendLongArray() {
        long[] longArray1 = new long[]{1, 2, 3};
        long[] longArray2 = new long[]{1, 2, 4};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(longArray1, longArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two long arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(longArray1, longArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendIntArray() {
        int[] intArray1 = new int[]{1, 2, 3};
        int[] intArray2 = new int[]{1, 2, 4};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(intArray1, intArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two int arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(intArray1, intArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendShortArray() {
        short[] shortArray1 = new short[]{1, 2, 3};
        short[] shortArray2 = new short[]{1, 2, 4};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(shortArray1, shortArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two short arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(shortArray1, shortArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendCharArray() {
        char[] charArray1 = new char[]{'A', 'B', 'C'};
        char[] charArray2 = new char[]{'A', 'B', 'D'};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(charArray1, charArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two char arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(charArray1, charArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendByteArray() {
        byte[] byteArray1 = new byte[]{5, 10};
        byte[] byteArray2 = new byte[]{5, 11};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(byteArray1, byteArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two byte arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(byteArray1, byteArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendDoubleArray() {
        double[] doubleArray1 = new double[]{1.2, 3.14, 5.6};
        double[] doubleArray2 = new double[]{1.2, 3.14, 5.7};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(doubleArray1, doubleArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two double arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(doubleArray1, doubleArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendFloatArray() {
        float[] floatArray1 = new float[]{1.2F, 3.1F, 5.6F};
        float[] floatArray2 = new float[]{1.2F, 3.1F, 5.7F};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(floatArray1, floatArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two float arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(floatArray1, floatArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] booleanArray1 = new boolean[]{true, false};
        boolean[] booleanArray2 = new boolean[]{true, false, true};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(booleanArray1, booleanArray2);

        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals("Comparison of two boolean arrays should be -1, as the first array comes before the second", comparison, -1);

        comparison = CompareToBuilder.reflectionCompare(booleanArray1, booleanArray2, null);
        Assert.assertEquals("Reflection compare and builder compare should give the same result", comparison, -1);
    }
}