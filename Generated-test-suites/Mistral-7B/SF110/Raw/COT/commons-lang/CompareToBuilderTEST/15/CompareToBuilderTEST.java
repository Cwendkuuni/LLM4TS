package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_Objects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals("Objects should be considered unequal", 0, comparison);
    }

    @Test
    public void testReflectionCompare_ObjectsNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        try {
            int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompare_ObjectsDifferentClasses() {
        Object obj1 = new Object1();
        Object obj2 = new Object2();
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals("Objects of different classes should be considered unequal", 0, comparison);
    }

    @Test
    public void testReflectionCompare_ObjectsSameClass() {
        Object obj1 = new Object1();
        Object obj2 = new Object1();
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals("Objects of the same class should be considered equal", 0, comparison);
    }

    @Test
    public void testReflectionCompare_ObjectsWithTransients() {
        Object obj1 = new ObjectWithTransientField();
        Object obj2 = new ObjectWithTransientField();
        obj1.setTransientField(true);
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, true);
        assertEquals("Objects with transient fields should be considered equal if those fields are transient", 0, comparison);
    }

    @Test
    public void testReflectionCompare_ObjectsWithExcludedFields() {
        Object obj1 = new ObjectWithExcludedField();
        Object obj2 = new ObjectWithExcludedField();
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("excludedField").toArray(new String[0]));
        assertEquals("Objects with excluded fields should be compared based on the remaining fields", 0, comparison);
    }

    @Test
    public void testAppend_Objects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Objects should be considered unequal", 0, comparison);
    }

    @Test
    public void testAppend_ObjectsNullLhs() {
        Object obj1 = null;
        Object obj2 = new Object();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Null lhs should result in a negative comparison", -1, comparison);
    }

    @Test
    public void testAppend_ObjectsNullRhs() {
        Object obj1 = new Object();
        Object obj2 = null;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Null rhs should result in a positive comparison", 1, comparison);
    }

    @Test
    public void testAppend_PrimitiveTypes() {
        int i1 = 1;
        int i2 = 2;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(i1, i2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Two integers with different values should have a non-zero comparison", 1, comparison);
    }

    @Test
    public void testAppend_Arrays_Objects() {
        Object[] array1 = new Object[0];
        Object[] array2 = new Object[0];
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(array1, array2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Arrays of objects with no elements should be considered equal", 0, comparison);
    }

    @Test
    public void testAppend_Arrays_ObjectsDifferentLengths() {
        Object[] array1 = new Object[1];
        Object[] array2 = new Object[2];
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(array1, array2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Arrays of different lengths should be considered unequal", 0, comparison);
    }

    @Test
    public void testAppend_Arrays_ObjectsSameLengthsDifferentValues() {
        Object[] array1 = {new Object1(), new Object2()};
        Object[] array2 = {new Object2(), new Object1()};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(array1, array2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Arrays with the same length but different values should be considered unequal", 0, comparison);
    }

    @Test
    public void testAppend_Arrays_ObjectsSameLengthsSameValues() {
        Object[] array1 = {new Object1(), new Object2()};
        Object[] array2 = {new Object1(), new Object2()};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(array1, array2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Arrays with the same length and the same values should be considered equal", 0, comparison);
    }

    @Test
    public void testAppend_Arrays_PrimitiveTypes() {
        int[] intArray1 = {1, 2};
        int[] intArray2 = {2, 1};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(intArray1, intArray2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Two arrays with primitive types and different values should have a non-zero comparison", -1, comparison);
    }

    @Test
    public void testAppend_Arrays_PrimitiveTypesSameValues() {
        int[] intArray1 = {1, 2};
        int[] intArray2 = {1, 2};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(intArray1, intArray2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Two arrays with primitive types and the same values should be considered equal", 0, comparison);
    }

    @Test
    public void testAppend_Arrays_PrimitiveTypesDifferentLengths() {
        int[] intArray1 = {1, 2};
        int[] intArray2 = {1};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(intArray1, intArray2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Two arrays with primitive types and different lengths should be considered unequal", -1, comparison);
    }

    @Test
    public void testAppend_Arrays_PrimitiveTypesSameLengths() {
        int[] intArray1 = {1, 2, 3};
        int[] intArray2 = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(intArray1, intArray2);
        int comparison = compareToBuilder.toComparison();
        assertEquals("Two arrays with primitive types and the same length should be considered equal", 0, comparison);
    }

    static class Object1 {
        private String field1;

        public Object1() {
        }

        public Object1(String field1) {
            this.field1 = field1;
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Object1)) {
                return false;
            }
            Object1 other = (Object1) obj;
            return Objects.equals(this.field1, other.field1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.field1);
        }

        @Override
        public String toString() {
            return "Object1{" +
                    "field1='" + field1 + '\'' +
                    '}';
        }
    }

    static class Object2 {
        private String field2;

        public Object2() {
        }

        public Object2(String field2) {
            this.field2 = field2;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Object2)) {
                return false;
            }
            Object2 other = (Object2) obj;
            return Objects.equals(this.field2, other.field2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.field2);
        }

        @Override
        public String toString() {
            return "Object2{" +
                    "field2='" + field2 + '\'' +
                    '}';
        }
    }

    static class ObjectWithTransientField {
        @Transient
        private String transientField;

        public ObjectWithTransientField() {
        }

        public ObjectWithTransientField(String transientField) {
            this.transientField = transientField;
        }

        public String getTransientField() {
            return transientField;
        }

        public void setTransientField(String transientField) {
            this.transientField = transientField;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ObjectWithTransientField)) {
                return false;
            }
            ObjectWithTransientField other = (ObjectWithTransientField) obj;
            return Objects.equals(this.transientField, other.transientField);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.transientField);
        }

        @Override
        public String toString() {
            return "ObjectWithTransientField{" +
                    "transientField='" + transientField + '\'' +
                    '}';
        }
    }

    static class ObjectWithExcludedField {
        private String excludedField;

        public ObjectWithExcludedField() {
        }

        public ObjectWithExcludedField(String excludedField) {
            this.excludedField = excludedField;
        }

        public String getExcludedField() {
            return excludedField;
        }

        public void setExcludedField(String excludedField) {
            this.excludedField = excludedField;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ObjectWithExcludedField)) {
                return false;
            }
            ObjectWithExcludedField other = (ObjectWithExcludedField) obj;
            return Objects.equals(this.excludedField, other.excludedField);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.excludedField);
        }

        @Override
        public String toString() {
            return "ObjectWithExcludedField{" +
                    "excludedField='" + excludedField + '\'' +
                    '}';
        }
    }
}