package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValidateTest {

    

    // Test isTrue methods

    @Test
    public void testIsTrueWithBooleanExpressionAndMessage() {
        Validate.isTrue(true, "Expected true", 1L);
    }

    @Test
    public void testIsTrueWithBooleanExpressionAndValue() {
        Validate.isTrue(true, "Expected true value: %d", 1L);
        Validate.isTrue(false, "Expected true value: %d", 1L);
        try {
            Validate.isTrue(false, "Expected true", 1L);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsTrueWithBooleanExpressionAndValues() {
        Validate.isTrue(true, "Expected true", 1L, "Value is false");
        Validate.isTrue(false, "Expected true", 1L, "Value is false");
        try {
            Validate.isTrue(false, "Expected true", 1L);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsTrueWithoutMessage() {
        Validate.isTrue(true);
        Validate.isTrue(false);
        try {
            Validate.isTrue(false);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test notNull method

    @Test
    public void testNotNullWithNotNullObject() {
        Object obj = new Object();
        Validate.notNull(obj);
    }

    @Test
    public void testNotNullWithNullObjectAndMessage() {
        try {
            Validate.notNull(null, "Expected non-null object");
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotNullWithNullObjectAndValues() {
        try {
            Validate.notNull(null, "Expected non-null object", 1L);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Test notEmpty methods for array

    @Test
    public void testNotEmptyArrayWithNotNullAndNonEmptyArray() {
        Object[] array = new Object[2];
        Validate.notEmpty(array);
    }

    @Test
    public void testNotEmptyArrayWithNullArray() {
        try {
            Validate.notEmpty(null, "Expected non-empty array");
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyArrayWithEmptyArray() {
        try {
            Validate.notEmpty(new Object[0], "Expected non-empty array");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test notEmpty methods for collection

    @Test
    public void testNotEmptyCollectionWithNotNullAndNonEmptyCollection() {
        List<String> collection = Arrays.asList("one", "two", "three");
        Validate.notEmpty(collection);
    }

    @Test
    public void testNotEmptyCollectionWithNullCollection() {
        try {
            Validate.notEmpty(null, "Expected non-empty collection");
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyCollectionWithEmptyCollection() {
        try {
            Validate.notEmpty(Collections.emptyList(), "Expected non-empty collection");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test notEmpty methods for map

    @Test
    public void testNotEmptyMapWithNotNullAndNonEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        Validate.notEmpty(map);
    }

    @Test
    public void testNotEmptyMapWithNullMap() {
        try {
            Validate.notEmpty(null, "Expected non-empty map");
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyMapWithEmptyMap() {
        try {
            Validate.notEmpty(new HashMap<>(), "Expected non-empty map");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test noNullElements method for array

    @Test
    public void testNoNullElementsArrayWithNotNullAndNonNullElements() {
        Object[] array = new Object[2];
        array[0] = new Object();
        array[1] = new Object();
        Validate.noNullElements(array);
    }

    @Test
    public void testNoNullElementsArrayWithNullElementAtIndex() {
        Object[] array = new Object[2];
        array[0] = new Object();
        array[1] = null;
        try {
            Validate.noNullElements(array, "Expected non-null element at index: %d", 1);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Test noNullElements method for Iterable

    @Test
    public void testNoNullElementsIterableWithNotNullAndNonNullElements() {
        List<Object> iterable = Arrays.asList(new Object(), new Object());
        Validate.noNullElements(iterable);
    }

    @Test
    public void testNoNullElementsIterableWithNullElementAtIndex() {
        List<Object> iterable = Arrays.asList(new Object(), null);
        try {
            Validate.noNullElements(iterable, "Expected non-null element at index: %d", 1);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Test validIndex methods

    @Test
    public void testValidIndexArrayWithValidIndex() {
        Object[] array = new Object[2];
        Validate.validIndex(array, 0);
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithInvalidIndex() {
        Object[] array = new Object[2];
        Validate.validIndex(array, 3);
    }

    @Test
    public void testValidIndexArrayWithInvalidIndexAndMessage() {
        Object[] array = new Object[2];
        try {
            Validate.validIndex(array, 3, "Expected valid index: %d");
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexCollectionWithValidIndex() {
        List<Object> collection = Arrays.asList(new Object(), new Object());
        Validate.validIndex(collection, 0);
        Validate.validIndex(collection, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithInvalidIndex() {
        List<Object> collection = Arrays.asList(new Object(), new Object());
        Validate.validIndex(collection, 2);
    }

    @Test
    public void testValidIndexCollectionWithInvalidIndexAndMessage() {
        List<Object> collection = Arrays.asList(new Object(), new Object());
        try {
            Validate.validIndex(collection, 2, "Expected valid index: %d");
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexCharSequenceWithValidIndex() {
        String chars = "hello";
        Validate.validIndex(chars, 0);
        Validate.validIndex(chars, 1);
        Validate.validIndex(chars, 2);
        Validate.validIndex(chars, 3);
        Validate.validIndex(chars, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithInvalidIndex() {
        String chars = "hello";
        Validate.validIndex(chars, 5);
    }

    @Test
    public void testValidIndexCharSequenceWithInvalidIndexAndMessage() {
        String chars = "hello";
        try {
            Validate.validIndex(chars, 5, "Expected valid index: %d");
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // Test validState method

    @Test
    public void testValidStateWithTrueState() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseState() {
        Validate.validState(false);
    }

    @Test
    public void testValidStateWithFalseStateAndMessage() {
        try {
            Validate.validState(false, "Expected valid state");
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    // Test matchesPattern method

    @Test
    public void testMatchesPatternWithValidPatternAndInput() {
        Validate.matchesPattern("hello", "^[hH]ello$");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidPatternAndInput() {
        Validate.matchesPattern("hello", "^[hH]elloWorld$");
    }

    @Test
    public void testMatchesPatternWithValidPatternAndInputAndMessage() {
        try {
            Validate.matchesPattern("hello", "^[hH]elloWorld$", "String does not match pattern: %s");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test inclusiveBetween methods

    @Test
    public void testInclusiveBetweenWithLongTypeAndValidValues() {
        Validate.inclusiveBetween(1L, 3L, 2L);
        Validate.inclusiveBetween(1L, 3L, 1L);
        Validate.inclusiveBetween(1L, 3L, 3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongTypeAndInvalidValue() {
        Validate.inclusiveBetween(1L, 3L, 0L);
    }

    @Test
    public void testInclusiveBetweenWithLongTypeAndInvalidValueAndMessage() {
        try {
            Validate.inclusiveBetween(1L, 3L, 0L, "Value is not in the inclusive range of %s to %s");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInclusiveBetweenWithDoubleTypeAndValidValues() {
        Validate.inclusiveBetween(1.0d, 3.0d, 2.0d);
        Validate.inclusiveBetween(1.0d, 3.0d, 1.0d);
        Validate.inclusiveBetween(1.0d, 3.0d, 3.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleTypeAndInvalidValue() {
        Validate.inclusiveBetween(1.0d, 3.0d, 0.0d);
    }

    @Test
    public void testInclusiveBetweenWithDoubleTypeAndInvalidValueAndMessage() {
        try {
            Validate.inclusiveBetween(1.0d, 3.0d, 0.0d, "Value is not in the inclusive range of %s to %s");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test exclusiveBetween methods

    @Test
    public void testExclusiveBetweenWithLongTypeAndValidValues() {
        Validate.exclusiveBetween(1L, 3L, 2L);
        Validate.exclusiveBetween(1L, 3L, 0L);
        Validate.exclusiveBetween(1L, 3L, 3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongTypeAndInvalidValue() {
        Validate.exclusiveBetween(1L, 3L, 1L);
    }

    @Test
    public void testExclusiveBetweenWithLongTypeAndInvalidValueAndMessage() {
        try {
            Validate.exclusiveBetween(1L, 3L, 1L, "Value is not in the exclusive range of %s to %s");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExclusiveBetweenWithDoubleTypeAndValidValues() {
        Validate.exclusiveBetween(1.0d, 3.0d, 2.0d);
        Validate.exclusiveBetween(1.0d, 3.0d, 0.0d);
        Validate.exclusiveBetween(1.0d, 3.0d, 3.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleTypeAndInvalidValue() {
        Validate.exclusiveBetween(1.0d, 3.0d, 1.0d);
    }

    @Test
    public void testExclusiveBetweenWithDoubleTypeAndInvalidValueAndMessage() {
        try {
            Validate.exclusiveBetween(1.0d, 3.0d, 1.0d, "Value is not in the exclusive range of %s to %s");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test isInstanceOf method

    @Test
    public void testIsInstanceOfWithProperTypeAndObject() {
        Validate.isInstanceOf(String.class, "hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithIncorrectTypeAndObject() {
        Validate.isInstanceOf(Integer.class, "hello");
    }

    @Test
    public void testIsInstanceOfWithProperTypeAndObjectAndMessage() {
        try {
            Validate.isInstanceOf(Integer.class, "hello", "Expected type: %s, actual: %s");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test isAssignableFrom method

    @Test
    public void testIsAssignableFromWithProperRelationTypeAndType() {
        Validate.isAssignableFrom(String.class, Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithIncorrectRelationTypeAndType() {
        Validate.isAssignableFrom(Object.class, String.class);
    }

    @Test
    public void testIsAssignableFromWithProperRelationTypeAndTypeAndMessage() {
        try {
            Validate.isAssignableFrom(Object.class, String.class, "Expected type %s to be assignable from type %s");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    
}