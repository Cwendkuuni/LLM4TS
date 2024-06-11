package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertMapEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrowsExactly;
import static org.junit.Assert.assertMatches;

import java.util.*;
import java.util.regex.Pattern;

import org.junit.Test;

public class ValidateTest {

    // Test isTrue methods

    @Test
    public void testIsTrueWithBooleanExpressionAndMessage() {
        Validate.isTrue(true, "Expected true", (Object[]) null);
    }

    @Test
    public void testIsTrueWithBooleanExpressionAndMessageAndValues() {
        Validate.isTrue(true, "Expected true for value %s", new Object[] {"value1"});
    }

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
    }

    @Test
    public void testIsTrueWithBooleanExpressionAndMessageAndValuesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Expected true for value %s", new Object[] {"value1"}));
    }

    // Test isTrue with different types (long, double, Object[])

    @Test
    public void testIsTrueWithLongExpressionAndMessage() {
        Validate.isTrue(1L, "Expected true for value %s", (Object[]) null);
    }

    @Test
    public void testIsTrueWithLongExpressionAndMessageAndValues() {
        Validate.isTrue(1L, "Expected true for value %s", new Object[] {"value1"});
    }

    @Test
    public void testIsTrueWithLongExpression() {
        Validate.isTrue(1L);
    }

    @Test
    public void testIsTrueWithLongExpressionAndMessageAndValuesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(0L, "Expected true for value %s", new Object[] {"value1"}));
    }

    @Test
    public void testIsTrueWithDoubleExpressionAndMessage() {
        Validate.isTrue(1.0, "Expected true for value %s", (Object[]) null);
    }

    @Test
    public void testIsTrueWithDoubleExpressionAndMessageAndValues() {
        Validate.isTrue(1.0, "Expected true for value %s", new Object[] {"value1"});
    }

    @Test
    public void testIsTrueWithDoubleExpression() {
        Validate.isTrue(1.0);
    }

    @Test
    public void testIsTrueWithDoubleExpressionAndMessageAndValuesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(0.0, "Expected true for value %s", new Object[] {"value1"}));
    }

    @Test
    public void testIsTrueWithObjectArrayExpressionAndMessage() {
        Object[] objectArray = new Object[] {"value1", "value2"};
        Validate.isTrue(objectArray, "Expected true for object array", (Object[]) null);
    }

    @Test
    public void testIsTrueWithObjectArrayExpressionAndMessageAndValues() {
        Object[] objectArray = new Object[] {"value1", "value2"};
        Validate.isTrue(objectArray, "Expected true for object array with values %s and %s", new Object[] {"value3", "value4"});
    }

    @Test
    public void testIsTrueWithObjectArrayExpression() {
        Object[] objectArray = new Object[] {"value1", "value2"};
        Validate.isTrue(objectArray);
    }

    @Test
    public void testIsTrueWithObjectArrayExpressionAndValuesThrowsException() {
        Object[] objectArray = new Object[] {"value1", "value2"};
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(new Object[] {}, "Expected true for object array with values %s and %s", new Object[] {"value3", "value4"}));
    }

    // Test notNull methods

    @Test
    public void testNotNullWithObject() {
        Object obj = new Object();
        Validate.notNull(obj);
    }

    @Test
    public void testNotNullWithObjectAndMessage() {
        Object obj = new Object();
        Validate.notNull(obj, "Expected non-null object");
    }

    @Test
    public void testNotNullWithObjectAndMessageAndValues() {
        Object obj = new Object();
        Validate.notNull(obj, "Expected non-null object for property %s", new Object[] {"propertyName"});
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null, "Expected non-null object", (Object[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObjectAndMessage() {
        Validate.notNull(null, "Expected non-null object for property %s", new Object[] {"propertyName"});
    }

    // Test notEmpty methods for arrays

    @Test
    public void testNotEmptyArrayWithArray() {
        Object[] array = new Object[] {"value1", "value2"};
        Validate.notEmpty(array);
    }

    @Test
    public void testNotEmptyArrayWithArrayAndMessage() {
        Object[] array = new Object[] {"value1", "value2"};
        Validate.notEmpty(array, "Expected non-empty array");
    }

    @Test
    public void testNotEmptyArrayWithArrayAndMessageAndValues() {
        Object[] array = new Object[] {"value1", "value2"};
        Validate.notEmpty(array, "Expected non-empty array with values %s and %s", new Object[] {"value3", "value4"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArray() {
        Validate.notEmpty(new Object[0], "Expected non-empty array", (Object[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArrayAndMessage() {
        Validate.notEmpty(new Object[0], "Expected non-empty array with values %s and %s", new Object[] {"value3", "value4"});
    }

    // Test noNullElements methods for arrays

    @Test
    public void testNoNullElementsArrayWithArray() {
        Object[] array = new Object[] {"value1", "value2"};
        Validate.noNullElements(array);
    }

    @Test
    public void testNoNullElementsArrayWithArrayAndMessage() {
        Object[] array = new Object[] {"value1", null, "value3"};
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(array, "Array contains null elements", (Object[]) null));
    }

    @Test
    public void testNoNullElementsArrayWithArrayAndMessageAndValues() {
        Object[] array = new Object[] {"value1", null, "value3"};
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(array, "Array contains null elements at indices %d and %d", new Object[] {0, 1}));
    }

    // Test noNullElements methods for Iterable

    @Test
    public void testNoNullElementsIterableWithIterable() {
        List<Object> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        Validate.noNullElements(list);
    }

    @Test
    public void testNoNullElementsIterableWithIterableAndMessage() {
        List<Object> list = new ArrayList<>();
        list.add(null);
        list.add("value2");
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(list, "Iterable contains null elements", (Object[]) null));
    }

    @Test
    public void testNoNullElementsIterableWithIterableAndMessageAndValues() {
        List<Object> list = new ArrayList<>();
        list.add(null);
        list.add("value2");
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(list, "Iterable contains null elements at indices %d and %d", new Object[] {0, 1}));
    }

    // Test validIndex methods for arrays

    @Test
    public void testValidIndexArrayWithArray() {
        Object[] array = new Object[] {"value1", "value2"};
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithArrayAndInvalidIndex() {
        Object[] array = new Object[] {"value1", "value2"};
        Validate.validIndex(array, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithArrayAndInvalidIndexAndMessage() {
        Object[] array = new Object[] {"value1", "value2"};
        Validate.validIndex(array, -1, "Invalid index");
    }

    // Test validIndex methods for CharSequence

    @Test
    public void testValidIndexCharSequenceWithCharSequence() {
        String charSequence = "value1value2";
        Validate.validIndex(charSequence, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithCharSequenceAndInvalidIndex() {
        String charSequence = "value1value2";
        Validate.validIndex(charSequence, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithCharSequenceAndInvalidIndexAndMessage() {
        String charSequence = "value1value2";
        Validate.validIndex(charSequence, 10, "Invalid index");
    }

    // Test validIndex methods for Collection

    @Test
    public void testValidIndexCollectionWithCollection() {
        List<Object> collection = new ArrayList<>();
        collection.add("value1");
        collection.add("value2");
        Validate.validIndex(collection, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithCollectionAndInvalidIndex() {
        List<Object> collection = new ArrayList<>();
        collection.add("value1");
        collection.add("value2");
        Validate.validIndex(collection, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithCollectionAndInvalidIndexAndMessage() {
        List<Object> collection = new ArrayList<>();
        collection.add("value1");
        collection.add("value2");
        Validate.validIndex(collection, 2, "Invalid index");
    }

    // Test matchesPattern methods

    @Test
    public void testMatchesPatternWithCharSequenceAndPattern() {
        String charSequence = "value1value2";
        Validate.matchesPattern(charSequence, "value.+value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithCharSequenceAndInvalidPattern() {
        String charSequence = "value1value2";
        Validate.matchesPattern(charSequence, "value.+value2");
    }

    @Test
    public void testMatchesPatternWithCharSequenceAndPatternAndMessage() {
        String charSequence = "value1value2";
        Validate.matchesPattern(charSequence, "value.+value", "String does not match pattern");
    }

    // Test inclusiveBetween methods

    @Test
    public void testInclusiveBetweenWithComparableAndValues() {
        Integer value = 1;
        Validate.inclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithComparableAndValuesAndMessage() {
        Integer value = 0;
        Validate.inclusiveBetween(1, 3, value, "Value %s is not within inclusive range [%s, %s]");
    }

    @Test
    public void testInclusiveBetweenWithLongAndValues() {
        long value = 1;
        Validate.inclusiveBetween(1L, 3L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongAndValuesAndMessage() {
        long value = 0;
        Validate.inclusiveBetween(1L, 3L, value, "Value %s is not within inclusive range [%s, %s]");
    }

    @Test
    public void testInclusiveBetweenWithDoubleAndValues() {
        double value = 1.0;
        Validate.inclusiveBetween(1.0, 3.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleAndValuesAndMessage() {
        double value = 0.0;
        Validate.inclusiveBetween(1.0, 3.0, value, "Value %s is not within inclusive range [%s, %s]");
    }

    // Test exclusiveBetween methods

    @Test
    public void testExclusiveBetweenWithComparableAndValues() {
        Integer value = 1;
        Validate.exclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithComparableAndValuesAndMessage() {
        Integer value = 1;
        Validate.exclusiveBetween(1, 3, value, "Value %s is not within exclusive range [%s, %s]");
    }

    @Test
    public void testExclusiveBetweenWithLongAndValues() {
        long value = 1;
        Validate.exclusiveBetween(1L, 3L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongAndValuesAndMessage() {
        long value = 1;
        Validate.exclusiveBetween(1L, 3L, value, "Value %s is not within exclusive range [%s, %s]");
    }

    @Test
    public void testExclusiveBetweenWithDoubleAndValues() {
        double value = 1.0;
        Validate.exclusiveBetween(1.0, 3.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleAndValuesAndMessage() {
        double value = 1.0;
        Validate.exclusiveBetween(1.0, 3.0, value, "Value %s is not within exclusive range [%s, %s]");
    }

    // Test isInstanceOf methods

    @Test
    public void testIsInstanceOfWithClassAndObject() {
        Validate.isInstanceOf(String.class, "value1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithClassAndObjectAndMessage() {
        Validate.isInstanceOf(String.class, new Object(), "Expected String, but received Object");
    }

    @Test
    public void testIsInstanceOfWithClassAndObjectAndMessageAndValues() {
        Validate.isInstanceOf(String.class, new Object(), "Expected String for property %s, but received Object", new Object[] {"propertyName"});
    }

    // Test isAssignableFrom methods

    @Test
    public void testIsAssignableFromWithClassAndClass() {
        Validate.isAssignableFrom(String.class, StringBuffer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithClassAndClassAndMessage() {
        Validate.isAssignableFrom(String.class, Integer.class, "Expected class %s to be assignable from %s", new Object[] {String.class.getName(), Integer.class.getName()});
    }
}