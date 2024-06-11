package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.ReflectionToStringBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {};
        Object rhs = new Object() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);

        // Test null object comparison
        lhs = null;
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertNotNull(result);
        assertTrue(result < 0);

        // Test different object classes
        lhs = new Object() {};
        rhs = new String("test");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, result);

        // Test excludeFields
        lhs = new ObjectWithField("test1") {};
        rhs = new ObjectWithField("test2") {};
        result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("fieldName").toArray(new String[0]));
        assertEquals(0, result);

        // Test reflectUpToClass
        lhs = new ParentClass() {};
        rhs = new ChildClass() {};
        result = CompareToBuilder.reflectionCompare(lhs, rhs, ParentClass.class);
        assertEquals(0, result);

        // Test compareTransients
        lhs = new TransientObject() {};
        rhs = new TransientObject() {};
        result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, result);
    }

    @Test
    public void testAppend() {
        Object lhs = new ObjectWithField("test1") {};
        Object rhs = new ObjectWithField("test2") {};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(-1, result);

        // Test append with Comparator
        Comparator<Object> comparator = (o1, o2) -> ((String)o1).compareTo((String)o2);
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        result = builder.toComparison();
        assertEquals(1, result);

        // Test append with array types
        long[] longLhs = {1, 2, 3};
        long[] longRhs = {1, 2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(longLhs, longRhs);
        result = builder.toComparison();
        assertEquals(-1, result);

        int[] intLhs = {1, 2};
        int[] intRhs = {1, 3};
        builder = new CompareToBuilder();
        builder.append(intLhs, intRhs);
        result = builder.toComparison();
        assertEquals(-1, result);

        short[] shortLhs = {1, 2, 3};
        short[] shortRhs = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(shortLhs, shortRhs);
        result = builder.toComparison();
        assertEquals(-1, result);

        char[] charLhs = {'a', 'b', 'c'};
        char[] charRhs = {'a', 'b', 'd'};
        builder = new CompareToBuilder();
        builder.append(charLhs, charRhs);
        result = builder.toComparison();
        assertEquals(-1, result);

        byte[] byteLhs = {1, 2, 3};
        byte[] byteRhs = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(byteLhs, byteRhs);
        result = builder.toComparison();
        assertEquals(-1, result);

        double[] doubleLhs = {1.0, 2.0};
        double[] doubleRhs = {1.0, 2.0, 3.0};
        builder = new CompareToBuilder();
        builder.append(doubleLhs, doubleRhs);
        result = builder.toComparison();
        assertEquals(-1, result);

        float[] floatLhs = {1.0f, 2.0f};
        float[] floatRhs = {1.0f, 2.0f, 3.0f};
        builder = new CompareToBuilder();
        builder.append(floatLhs, floatRhs);
        result = builder.toComparison();
        assertEquals(-1, result);

        boolean[] booleanLhs = {true, false};
        boolean[] booleanRhs = {true, false, true};
        builder = new CompareToBuilder();
        builder.append(booleanLhs, booleanRhs);
        result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test
    public void testAppendSuper() {
        ParentClass parentLhs = new ParentClass() {};
        ParentClass parentRhs = new ParentClass() {};
        ChildClass childLhs = new ChildClass() {};
        ChildClass childRhs = new ChildClass() {};

        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(parentLhs, parentRhs));
        builder.append(childLhs, childRhs);
        int result = builder.toComparison();
        assertEquals(0, result);
    }
}

class ObjectWithField {
    private String fieldName;

    public ObjectWithField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

class ParentClass {
}

class ChildClass extends ParentClass {
}

class TransientObject {
    private transient int someTransientField;
}