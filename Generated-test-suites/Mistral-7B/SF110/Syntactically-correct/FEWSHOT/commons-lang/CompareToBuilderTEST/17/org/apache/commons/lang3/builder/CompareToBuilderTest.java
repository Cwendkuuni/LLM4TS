package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testComparisonOfTwoObjects() {
        Integer lhs = 1;
        Integer rhs = 2;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoObjectsWithSameValue() {
        Integer lhs = 1;
        Integer rhs = 1;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoNullObjects() {
        Integer lhs = null;
        Integer rhs = null;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfAnArrayOfTwoObjects() {
        Object[] lhs = {1, 2};
        Object[] rhs = {1, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithSameValues() {
        Object[] lhs = {1, 2};
        Object[] rhs = {1, 2};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithDifferentLengths() {
        Object[] lhs = {1, 2};
        Object[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithSameLengthButDifferentValues() {
        Object[] lhs = {1, 2};
        Object[] rhs = {1, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithNullValues() {
        Object[] lhs = {null, null};
        Object[] rhs = {null, null};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithNullValuesAndDifferentLengths() {
        Object[] lhs = {null, null};
        Object[] rhs = {null, null, null};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithNullValuesAndSameLengthButDifferentValues() {
        Object[] lhs = {null, null};
        Object[] rhs = {null, ""};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithNullValuesAndSameLengthButSameValues() {
        Object[] lhs = {null, null};
        Object[] rhs = {null, null};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithPrimitiveValues() {
        int[] lhs = {1, 2};
        int[] rhs = {1, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoArraysWithPrimitiveValuesAndSameValues() {
        int[] lhs = {1, 2};
        int[] rhs = {1, 2};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test(expected = NullPointerException.class)
    public void testComparisonOfTwoObjectsWithDifferentTypes() {
        Integer lhs = 1;
        String rhs = "1";
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
    }

    @Test
    public void testComparisonOfTwoObjectsWithTransientFields() {
        class TestClass {
            private int transientField = 1;
            private transient int nonTransientField = 2;

            @Override
            public String toString() {
                return "TestClass{" +
                        "transientField=" + transientField +
                        ", nonTransientField=" + nonTransientField +
                        '}';
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, true);
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoObjectsWithExcludedFields() {
        class TestClass {
            private int excludedField = 1;
            private int includedField = 2;

            @Override
            public String toString() {
                return "TestClass{" +
                        "excludedField=" + excludedField +
                        ", includedField=" + includedField +
                        '}';
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new String[]{"excludedField"});
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoObjectsWithExcludedFieldsAndDifferentValues() {
        class TestClass {
            private int excludedField = 1;
            private int includedField = 2;

            @Override
            public String toString() {
                return "TestClass{" +
                        "excludedField=" + excludedField +
                        ", includedField=" + includedField +
                        '}';
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        lhs.excludedField = 3;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new String[]{"excludedField"});
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoObjectsWithExcludedFieldsAndSameValues() {
        class TestClass {
            private int excludedField = 1;
            private int includedField = 2;

            @Override
            public String toString() {
                return "TestClass{" +
                        "excludedField=" + excludedField +
                        ", includedField=" + includedField +
                        '}';
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new String[]{"excludedField"});
        assertEquals(Integer.valueOf(0), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoObjectsWithExcludedFieldsAndNullValues() {
        class TestClass {
            private int excludedField = 1;
            private int includedField = 2;

            @Override
            public String toString() {
                return "TestClass{" +
                        "excludedField=" + excludedField +
                        ", includedField=" + includedField +
                        '}';
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = null;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new String[]{"excludedField"});
        assertEquals(Integer.valueOf(1), compareToBuilder.build());
    }

    @Test
    public void testComparisonOfTwoObjectsWithExcludedFieldsAndNullValuesAndSameTypes() {
        class TestClass {
            private int excludedField = 1;
            private int includedField = 2;

            @Override
            public String toString() {
                return "TestClass{" +
                        "excludedField=" + excludedField +
                        ", includedField=" + includedField +
                        '}';
            }
        }

        TestClass lhs = null;
        TestClass rhs = new TestClass();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new String[]{"excludedField"});
        assertEquals(Integer.valueOf(-1), compareToBuilder.build());
    }
}