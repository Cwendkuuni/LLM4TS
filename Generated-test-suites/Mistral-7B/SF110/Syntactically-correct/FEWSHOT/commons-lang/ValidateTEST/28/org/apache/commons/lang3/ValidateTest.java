package org.apache.commons.lang3;

import static org.junit.Assert.fail;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueBooleanMessage() {
        Validate.isTrue(true, "Expected true but was false", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueFalseMessage() {
        Validate.isTrue(false, "Expected true but was false", 1L);
    }

    @Test
    public void testIsTrueDoubleMessage() {
        Validate.isTrue(true, "Expected true but was false", 1.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueFalseDoubleMessage() {
        Validate.isTrue(false, "Expected true but was false", 1.5);
    }

    @Test
    public void testIsTrueObjectMessage() {
        Validate.isTrue(true, "Expected true but was false", new Object[]{"Test", 1, 2.0f});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueFalseObjectMessage() {
        Validate.isTrue(false, "Expected true but was false", new Object[]{"Test", 1, 2.0f});
    }

    @Test
    public void testIsTrueNoMessage() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueFalseNoMessage() {
        Validate.isTrue(false);
    }

    @Test
    public void testNotNullObject() {
        Object obj = new Object();
        Validate.notNull(obj);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyArray() {
        int[] array = {1, 2, 3};
        Validate.notEmpty(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyArray() {
        Validate.notEmpty(new int[0]);
    }

    @Test
    public void testNotEmptyCollection() {
        List<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Validate.notEmpty(collection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyCollection() {
        List<Integer> collection = Collections.emptyList();
        Validate.notEmpty(collection);
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        Validate.notEmpty(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyMap() {
        Map<String, Integer> map = Collections.emptyMap();
        Validate.notEmpty(map);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String chars = "Test";
        Validate.notEmpty(chars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyCharSequence() {
        String chars = "";
        Validate.notEmpty(chars);
    }

    @Test
    public void testNotBlankCharSequence() {
        String chars = "Test";
        Validate.notBlank(chars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankEmptyCharSequence() {
        String chars = "";
        Validate.notBlank(chars);
    }

    @Test
    public void testNoNullElementsArray() {
        int[] array = {1, 2, 3};
        Validate.noNullElements(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsNullElementArray() {
        int[] array = {1, null, 3};
        Validate.noNullElements(array);
    }

    @Test
    public void testNoNullElementsCollection() {
        List<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Validate.noNullElements(collection.toArray(new Integer[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsNullElementCollection() {
        List<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(null);
        Validate.noNullElements(collection.toArray(new Integer[0]));
    }

    @Test
    public void testValidIndexArray() {
        int[] array = {1, 2, 3};
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexArray() {
        int[] array = {1, 2, 3};
        Validate.validIndex(array, 4);
    }

    @Test
    public void testValidIndexCollection() {
        List<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Validate.validIndex(collection, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexCollection() {
        List<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Validate.validIndex(collection, 3);
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "Test";
        Validate.validIndex(chars, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexCharSequence() {
        String chars = "Test";
        Validate.validIndex(chars, 4);
    }

    @Test
    public void testValidState() {
        boolean state = true;
        Validate.validState(state);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidState() {
        boolean state = false;
        Validate.validState(state);
    }

    @Test
    public void testMatchesPattern() {
        String input = "Test";
        String pattern = "^Test$";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoesNotMatchPattern() {
        String input = "Test1";
        String pattern = "^Test$";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testInclusiveBetweenLong() {
        long value = 5L;
        Validate.inclusiveBetween(1L, 10L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongOutOfRange() {
        long value = 15L;
        Validate.inclusiveBetween(1L, 10L, value);
    }

    @Test
    public void testInclusiveBetweenLongMessage() {
        long value = 5L;
        Validate.inclusiveBetween(1L, 10L, value, "Invalid long value: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongMessageOutOfRange() {
        long value = 15L;
        Validate.inclusiveBetween(1L, 10L, value, "Invalid long value: %d");
    }

    @Test
    public void testInclusiveBetweenDouble() {
        double value = 5.5;
        Validate.inclusiveBetween(1.0, 10.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleOutOfRange() {
        double value = 15.5;
        Validate.inclusiveBetween(1.0, 10.0, value);
    }

    @Test
    public void testInclusiveBetweenDoubleMessage() {
        double value = 5.5;
        Validate.inclusiveBetween(1.0, 10.0, value, "Invalid double value: %f");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleMessageOutOfRange() {
        double value = 15.5;
        Validate.inclusiveBetween(1.0, 10.0, value, "Invalid double value: %f");
    }

    @Test
    public void testExclusiveBetweenLong() {
        long value = 4L;
        Validate.exclusiveBetween(1L, 10L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongOutOfRangeMin() {
        long value = 0L;
        Validate.exclusiveBetween(1L, 10L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongOutOfRangeMax() {
        long value = 11L;
        Validate.exclusiveBetween(1L, 10L, value);
    }

    @Test
    public void testExclusiveBetweenLongMessage() {
        long value = 4L;
        Validate.exclusiveBetween(1L, 10L, value, "Invalid long value: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongMessageOutOfRangeMin() {
        long value = 0L;
        Validate.exclusiveBetween(1L, 10L, value, "Invalid long value: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongMessageOutOfRangeMax() {
        long value = 11L;
        Validate.exclusiveBetween(1L, 10L, value, "Invalid long value: %d");
    }

    @Test
    public void testExclusiveBetweenDouble() {
        double value = 4.5;
        Validate.exclusiveBetween(1.0, 10.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleOutOfRangeMin() {
        double value = 0.0;
        Validate.exclusiveBetween(1.0, 10.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleOutOfRangeMax() {
        double value = 11.0;
        Validate.exclusiveBetween(1.0, 10.0, value);
    }

    @Test
    public void testExclusiveBetweenDoubleMessage() {
        double value = 4.5;
        Validate.exclusiveBetween(1.0, 10.0, value, "Invalid double value: %f");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleMessageOutOfRangeMin() {
        double value = 0.0;
        Validate.exclusiveBetween(1.0, 10.0, value, "Invalid double value: %f");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleMessageOutOfRangeMax() {
        double value = 11.0;
        Validate.exclusiveBetween(1.0, 10.0, value, "Invalid double value: %f");
    }

    @Test
    public void testIsInstanceOfClass() {
        Object obj = new String("Test");
        Validate.isInstanceOf(String.class, obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfClassWrongType() {
        Object obj = new Integer(5);
        Validate.isInstanceOf(String.class, obj);
    }

    @Test
    public void testIsAssignableFromClass() {
        Class<String> superType = String.class;
        Class<Integer> type = Integer.class;
        Validate.isAssignableFrom(superType, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromClassWrongSuperType() {
        Class<String> superType = String.class;
        Class<Float> type = Float.class;
        Validate.isAssignableFrom(superType, type);
    }
}