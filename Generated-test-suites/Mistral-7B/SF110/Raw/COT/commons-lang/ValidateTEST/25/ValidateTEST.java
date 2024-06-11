import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpression() {
        Validate.isTrue(false);
    }

    @Test
    public void testIsTrueWithStringMessageAndLongValue() {
        Validate.isTrue(true, "The validated expression is true for value: 1", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndLongValue() {
        Validate.isTrue(false, "The validated expression is true for value: 1", 1);
    }

    @Test
    public void testIsTrueWithObjectArray() {
        Object[] objArray = new Object[2];
        Validate.isTrue(Arrays.asList(objArray).contains(objArray[0]), "The list contains the first element", objArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseObjectArray() {
        Object[] objArray = new Object[2];
        Validate.isTrue(Arrays.asList(objArray).contains(objArray[1]), "The list contains the first element", objArray);
    }

    @Test
    public void testNotNull() {
        String notNullString = new String("Hello World");
        Validate.notNull(notNullString);
    }

    @Test(expected = NullPointerException.class)
    public void testNullNotNull() {
        String nullString = null;
        Validate.notNull(nullString);
    }

    @Test
    public void testNotEmptyArray() {
        String[] notEmptyArray = new String[2];
        notEmptyArray[0] = "Hello";
        Validate.notEmpty(notEmptyArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArray() {
        String[] emptyArray = new String[0];
        Validate.notEmpty(emptyArray);
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> notEmptyList = new ArrayList<>();
        notEmptyList.add("Hello");
        Validate.notEmpty(notEmptyList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCollection() {
        List<String> emptyList = Collections.emptyList();
        Validate.notEmpty(emptyList);
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, Integer> notEmptyMap = new HashMap<>();
        notEmptyMap.put("Hello", 1);
        Validate.notEmpty(notEmptyMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap() {
        Map<String, Integer> emptyMap = Collections.emptyMap();
        Validate.notEmpty(emptyMap);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String notEmptyString = "Hello World";
        Validate.notEmpty(notEmptyString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharSequence() {
        String emptyString = "";
        Validate.notEmpty(emptyString);
    }

    @Test
    public void testNoNullElementsArray() {
        String[] noNullArray = new String[2];
        noNullArray[0] = "Hello";
        Validate.noNullElements(noNullArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayWithNullElement() {
        String[] arrayWithNull = new String[2];
        arrayWithNull[0] = "Hello";
        arrayWithNull[1] = null;
        Validate.noNullElements(arrayWithNull);
    }

    @Test
    public void testNoNullElementsIterable() {
        List<String> noNullList = new ArrayList<>();
        noNullList.add("Hello");
        Validate.noNullElements(noNullList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterableWithNullElement() {
        List<String> iterableWithNull = new ArrayList<>();
        iterableWithNull.add("Hello");
        iterableWithNull.add(null);
        Validate.noNullElements(iterableWithNull);
    }

    @Test
    public void testValidIndexArray() {
        String[] array = new String[2];
        Validate.validIndex(array, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexArray() {
        String[] array = new String[2];
        Validate.validIndex(array, 3);
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        Validate.validIndex(list, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexCollection() {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        Validate.validIndex(list, 2);
    }

    @Test
    public void testValidIndexCharSequence() {
        String string = "Hello World";
        Validate.validIndex(string, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexCharSequence() {
        String string = "Hello World";
        Validate.validIndex(string, 10);
    }

    @Test
    public void testValidState() {
        assertTrue(true);
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidState() {
        assertFalse(false);
        Validate.validState(false);
    }

    @Test
    public void testMatchesPattern() {
        String input = "Hello World";
        String pattern = "\\w+\\s\\w+";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedPattern() {
        String input = "Hello World";
        String pattern = "\\d+";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testInclusiveBetween() {
        Integer value = 5;
        Validate.inclusiveBetween(3, 7, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOutsideInclusiveRange() {
        Integer value = 1;
        Validate.inclusiveBetween(3, 7, value);
    }

    @Test
    public void testLongInclusiveBetween() {
        long value = 5;
        Validate.inclusiveBetween(3, 7, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongValueOutsideInclusiveRange() {
        long value = 1;
        Validate.inclusiveBetween(3, 7, value);
    }

    @Test
    public void testDoubleInclusiveBetween() {
        double value = 5.5;
        Validate.inclusiveBetween(3.0, 7.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleValueOutsideInclusiveRange() {
        double value = 1.5;
        Validate.inclusiveBetween(3.0, 7.0, value);
    }

    @Test
    public void testExclusiveBetween() {
        Integer value = 5;
        Validate.exclusiveBetween(2, 6, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOutsideExclusiveRange() {
        Integer value = 1;
        Validate.exclusiveBetween(2, 6, value);
    }

    @Test
    public void testLongExclusiveBetween() {
        long value = 5;
        Validate.exclusiveBetween(2, 6, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongValueOutsideExclusiveRange() {
        long value = 1;
        Validate.exclusiveBetween(2, 6, value);
    }

    @Test
    public void testDoubleExclusiveBetween() {
        double value = 5.5;
        Validate.exclusiveBetween(2.0, 6.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleValueOutsideExclusiveRange() {
        double value = 1.5;
        Validate.exclusiveBetween(2.0, 6.0, value);
    }

    @Test
    public void testIsInstanceOf() {
        String testString = "Hello World";
        Validate.isInstanceOf(String.class, testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotInstanceOf() {
        String testString = "Hello World";
        Validate.isInstanceOf(Integer.class, testString);
    }

    @Test
    public void testIsAssignableFrom() {
        String testString = "Hello World";
        Validate.isAssignableFrom(String.class, StringBuffer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotAssignableFrom() {
        String testString = "Hello World";
        Validate.isAssignableFrom(StringBuffer.class, String.class);
    }
}