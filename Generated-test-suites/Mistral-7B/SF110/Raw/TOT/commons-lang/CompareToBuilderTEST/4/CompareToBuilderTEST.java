package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.ReflectionUtils;
import org.opetcode.junit.lombok.TestBuilder;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompareToBuilderTest {

    @TestBuilder
    public static class ReflectionCompareTest {

        @Test
        public void testReflectionCompareObjects() {
            Object lhs = new Object();
            Object rhs = new Object();
            int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
            assertEquals(0, comparison);
        }

        @Test
        public void testReflectionCompareObjectsWithTransients() {
            class TestClass {
                private transient int comparison;

                public int getComparison() {
                    return comparison;
                }
            }

            TestClass lhs = new TestClass();
            TestClass rhs = new TestClass();
            int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
            assertEquals(0, comparison);
        }

        @Test
        public void testReflectionCompareObjectsWithExcludedFields() {
            class TestClass {
                private int comparison;
                private String excludedField;

                public int getComparison() {
                    return comparison;
                }

                public String getExcludedField() {
                    return excludedField;
                }
            }

            TestClass lhs = new TestClass();
            TestClass rhs = new TestClass();
            int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, CompareToBuilder.class.getName(), new String[]{"excludedField"});
            assertEquals(0, comparison);
        }

        @Test
        public void testReflectionCompareObjectsWithExcludedFieldArray() {
            class TestClass {
                private int comparison;
                private String excludedField;

                public int getComparison() {
                    return comparison;
                }

                public String getExcludedField() {
                    return excludedField;
                }
            }

            TestClass lhs = new TestClass();
            TestClass rhs = new TestClass();
            int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, CompareToBuilder.class.getName(), new String[]{"excludedField"}, new String[0]);
            assertEquals(0, comparison);
        }

        @Test
        public void testReflectionCompareObjectsWithParentClass() {
            class ParentClass {
                private int comparison;
            }

            class ChildClass extends ParentClass {
                private int childComparison;
            }

            ChildClass lhs = new ChildClass();
            ChildClass rhs = new ChildClass();
            int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, ParentClass.class, null);
            assertEquals(0, comparison);
        }

        @Test(expected = NullPointerException.class)
        public void testReflectionCompareNullObjects() {
            Object lhs = null;
            Object rhs = null;
            CompareToBuilder.reflectionCompare(lhs, rhs);
        }

        @Test(expected = ClassCastException.class)
        public void testReflectionCompareObjectsWithIncompatibleTypes() {
            Object lhs = new String("test");
            Object rhs = new Integer(1);
            CompareToBuilder.reflectionCompare(lhs, rhs);
        }
    }

    @TestBuilder
    public static class AppendTest {

        @Test
        public void testAppendObjects() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object lhs = new Object();
            Object rhs = new Object();
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendArrays() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object[] lhs = {1, 2, 3};
            Object[] rhs = {1, 2, 3};
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendArraysDifferentLength() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object[] lhs = {1, 2, 3};
            Object[] rhs = {1, 2};
            compareToBuilder.append(lhs, rhs);
            assertEquals(-1, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendArraysNull() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object[] lhs = {1, 2, 3};
            Object[] rhs = null;
            compareToBuilder.append(lhs, rhs);
            assertEquals(-1, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendArraysNullElements() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object[] lhs = {1, null, 3};
            Object[] rhs = {1, null, 3};
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendArraysDifferentTypes() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object[] lhs = {1, "test"};
            Object[] rhs = {1, 2};
            compareToBuilder.append(lhs, rhs);
            assertEquals(-1, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendLongs() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            long lhs = 1L;
            long rhs = 1L;
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendInts() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            int lhs = 1;
            int rhs = 1;
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendShorts() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            short lhs = 1;
            short rhs = 1;
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendChars() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            char lhs = 'a';
            char rhs = 'a';
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendBytes() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            byte lhs = 1;
            byte rhs = 1;
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendDoubles() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            double lhs = 1.0;
            double rhs = 1.0;
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendFloats() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            float lhs = 1.0f;
            float rhs = 1.0f;
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendBooleans() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            boolean lhs = true;
            boolean rhs = true;
            compareToBuilder.append(lhs, rhs);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendBooleansDifferent() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            boolean lhs = true;
            boolean rhs = false;
            compareToBuilder.append(lhs, rhs);
            assertEquals(-1, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendObjectsWithComparator() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object lhs = new Object();
            Object rhs = new Object();
            Comparator<Object> comparator = (o1, o2) -> o1.toString().compareTo(o2.toString());
            compareToBuilder.append(lhs, rhs, comparator);
            assertEquals(0, compareToBuilder.toComparison());
        }

        @Test
        public void testAppendObjectsWithCustomComparator() {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            Object lhs = new Object();
            Object rhs = new Object();
            class CustomComparator implements Comparator<Object> {
                @Override
                public int compare(Object o1, Object o2) {
                    return o1.getClass().getName().compareTo(o2.getClass().getName());
                }
            }
            compareToBuilder.append(lhs, rhs, new CustomComparator());
            assertEquals(0, compareToBuilder.toComparison());
        }
    }

    @Test
    public void testBuild() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(1, 1);
        assertEquals(0, compareToBuilder.toComparison());
    }
}