package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        boolean expression = true;
        Validate.isTrue(expression);
        
        expression = false;
        try {
            Validate.isTrue(expression);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        Validate.isTrue(expression, "Invalid expression: %s", expression);
        try {
            Validate.isTrue(expression, "Invalid expression: %s", "Expected true");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        Validate.isTrue(expression, "Invalid expression: %s", new Object[]{"Expected true"});
        try {
            Validate.isTrue(expression, "Invalid expression: %s", new Object[]{"Expected true", 42});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsTrueWithValues() {
        boolean expression = true;
        Validate.isTrue(expression, "%s is expected to be true", "Expression");
        
        expression = false;
        try {
            Validate.isTrue(expression, "%s is expected to be true", "Expression");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isTrue(expression, "%s is expected to be true: %s", "Expression", "Expected true");
        try {
            Validate.isTrue(expression, "%s is expected to be true: %s", "Expression", "Expected false");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isTrue(expression, "%s is expected to be true: %s", "Expression", new Object[]{"Expected true", 42});
        try {
            Validate.isTrue(expression, "%s is expected to be true: %s", "Expression", new Object[]{"Expected true", 42, "Unexpected value"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotNull() {
        Object object = new Object();
        Validate.notNull(object);

        object = null;
        try {
            Validate.notNull(object);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        Validate.notNull(object, "The validated object is null");
        try {
            Validate.notNull(object, "The validated object is null", new Object[]{"Unexpected value"});
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyArray() {
        Object[] array = new Object[1];
        Validate.notEmpty(array);

        array = new Object[0];
        try {
            Validate.notEmpty(array);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.notEmpty(array, "The validated array is empty");
        try {
            Validate.notEmpty(array, "The validated array is empty", new Object[]{"Unexpected index: 0"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyCollection() {
        List<Object> collection = new ArrayList<>();
        collection.add(new Object());
        Validate.notEmpty(collection);

        collection.clear();
        try {
            Validate.notEmpty(collection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.notEmpty(collection, "The validated collection is empty");
        try {
            Validate.notEmpty(collection, "The validated collection is empty", new Object[]{"Unexpected index: 0"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Key", new Object());
        Validate.notEmpty(map);

        map.clear();
        try {
            Validate.notEmpty(map);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.notEmpty(map, "The validated map is empty");
        try {
            Validate.notEmpty(map, "The validated map is empty", new Object[]{"Unexpected key: Key"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotBlankCharSequence() {
        String chars = "abc";
        Validate.notBlank(chars);

        chars = "";
        try {
            Validate.notBlank(chars);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.notBlank(chars, "The validated character sequence is blank");
        try {
            Validate.notBlank(chars, "The validated character sequence is blank", new Object[]{"Unexpected value: xyz"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNoNullElementsArray() {
        Object[] array = new Object[1];
        array[0] = new Object();
        Validate.noNullElements(array);

        array = new Object[1];
        array[0] = null;
        try {
            Validate.noNullElements(array);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.noNullElements(array, "The validated array contains null element at index: %d");
        try {
            Validate.noNullElements(array, "The validated array contains null element at index: %d", new Object[]{0});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNoNullElementsCollection() {
        List<Object> collection = new ArrayList<>();
        collection.add(new Object());
        Validate.noNullElements(collection);

        collection.add(null);
        try {
            Validate.noNullElements(collection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.noNullElements(collection, "The validated collection contains null element at index: %d");
        try {
            Validate.noNullElements(collection, "The validated collection contains null element at index: %d", new Object[]{0});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexArray() {
        Object[] array = new Object[1];
        Validate.validIndex(array, 0);

        array = new Object[2];
        try {
            Validate.validIndex(array, 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        Validate.validIndex(array, 0, "The validated array index is invalid: %d");
        try {
            Validate.validIndex(array, 3, "The validated array index is invalid: %d");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexCollection() {
        List<Object> collection = new ArrayList<>();
        collection.add(new Object());
        Validate.validIndex(collection, 0);

        collection.add(new Object());
        try {
            Validate.validIndex(collection, 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        Validate.validIndex(collection, 0, "The validated collection index is invalid: %d");
        try {
            Validate.validIndex(collection, 3, "The validated collection index is invalid: %d");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "abc";
        Validate.validIndex(chars, 0);

        chars = "abcdef";
        try {
            Validate.validIndex(chars, 6);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        Validate.validIndex(chars, 0, "The validated character sequence index is invalid: %d");
        try {
            Validate.validIndex(chars, 6, "The validated character sequence index is invalid: %d");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidState() {
        boolean expression = true;
        Validate.validState(expression);

        expression = false;
        try {
            Validate.validState(expression);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        Validate.validState(expression, "The validated state is false");
        try {
            Validate.validState(expression, "The validated state is false", new Object[]{"Expected true"});
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMatchesPattern() {
        String input = "abcd";
        String pattern = ".*d";
        Validate.matchesPattern(input, pattern);

        input = "abcd";
        try {
            Validate.matchesPattern(input, ".*e");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.matchesPattern(input, pattern, "The string %s does not match the pattern %s");
        try {
            Validate.matchesPattern(input, ".*e", "The string %s does not match the pattern %s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInclusiveBetween() {
        Integer value = 5;
        Validate.inclusiveBetween(3, 7, value);

        value = 2;
        try {
            Validate.inclusiveBetween(3, 7, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.inclusiveBetween(3, 7, value, "The value %s is not in the specified inclusive range of %s to %s");
        try {
            Validate.inclusiveBetween(3, 7, 2, "The value %s is not in the specified inclusive range of %s to %s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.inclusiveBetween(3L, 7L, (long) value);
        try {
            Validate.inclusiveBetween(3L, 7L, (long) 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.inclusiveBetween(3.0, 7.0, value, "The value %s is not in the specified inclusive range of %s to %s");
        try {
            Validate.inclusiveBetween(3.0, 7.0, 2.0, "The value %s is not in the specified inclusive range of %s to %s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExclusiveBetween() {
        Integer value = 5;
        Validate.exclusiveBetween(2, 8, value);

        value = 2;
        try {
            Validate.exclusiveBetween(2, 8, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.exclusiveBetween(2, 8, value, "The value %s is not in the specified exclusive range of %s to %s");
        try {
            Validate.exclusiveBetween(2, 8, 2, "The value %s is not in the specified exclusive range of %s to %s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.exclusiveBetween(2L, 8L, (long) value);
        try {
            Validate.exclusiveBetween(2L, 8L, (long) 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.exclusiveBetween(2.0, 8.0, value, "The value %s is not in the specified exclusive range of %s to %s");
        try {
            Validate.exclusiveBetween(2.0, 8.0, 2.0, "The value %s is not in the specified exclusive range of %s to %s");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsInstanceOf() {
        String str = "test";
        Validate.isInstanceOf(String.class, str);

        str = new Object();
        try {
            Validate.isInstanceOf(String.class, str);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isInstanceOf(String.class, str, "Expected type: String, actual: %s");
        try {
            Validate.isInstanceOf(String.class, str, "Expected type: String, actual: %s", new Object[]{"Integer"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAssignableFrom() {
        String str = "test";
        Validate.isAssignableFrom(String.class, Object.class);

        str = new Object();
        try {
            Validate.isAssignableFrom(String.class, str.getClass());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isAssignableFrom(String.class, str.getClass(), "Cannot assign a %s to a %s");
        try {
            Validate.isAssignableFrom(String.class, str.getClass(), "Cannot assign a %s to a %s", new Object[]{"Integer", "String"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}