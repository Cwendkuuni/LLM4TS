package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

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
        Validate.isTrue(true, "Expected true but got false. Value: %d", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndLongValue() {
        Validate.isTrue(false, "Expected true but got false. Value: %d", 1L);
    }

    @Test
    public void testIsTrueWithObjectArray() {
        Validate.isTrue(new Object[]{true, true, true});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseObjectArray() {
        Validate.isTrue(new Object[]{false, false, false});
    }

    @Test
    public void testIsTrueWithNoArgument() {
        Validate.isTrue();
    }

    @Test
    public void testNotNullWithNotNullObject() {
        Validate.notNull(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyWithNotNullAndNotEmptyArray() {
        Object[] array = new Object[3];
        Validate.notEmpty(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyWithNullArray() {
        Validate.notEmpty(null);
    }

    @Test
    public void testNotEmptyWithEmptyArray() {
        Object[] array = new Object[0];
        Validate.notEmpty(array); // will throw an exception
    }

    @Test
    public void testNotEmptyWithCollection() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Validate.notEmpty(list);
    }

    @Test(expected = NullPointerException.class)
    public void testNotEmptyWithNullCollection() {
        Validate.notEmpty(null);
    }

    @Test
    public void testNotEmptyWithEmptyCollection() {
        List<Integer> list = new ArrayList<>();
        Validate.notEmpty(list); // will throw an exception
    }

    @Test
    public void testNotEmptyWithMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        Validate.notEmpty(map);
    }

    @Test(expected = NullPointerException.class)
    public void testNotEmptyWithNullMap() {
        Validate.notEmpty(null);
    }

    @Test
    public void testNotEmptyWithEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        Validate.notEmpty(map); // will throw an exception
    }

    @Test
    public void testNotBlankWithNotNullAndNotBlankCharSequence() {
        Validate.notBlank("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankWithNullCharSequence() {
        Validate.notBlank(null);
    }

    @Test
    public void testNotBlankWithEmptyCharSequence() {
        Validate.notBlank("");
        // will throw an exception
    }

    @Test
    public void testNoNullElementsWithNotNullAndNonNullArray() {
        String[] array = new String[]{"a", "b", "c"};
        Validate.noNullElements(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithNullArray() {
        Validate.noNullElements(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithArrayWithNullElement() {
        String[] array = new String[]{"a", null, "c"};
        Validate.noNullElements(array);
    }

    @Test
    public void testNoNullElementsWithIterable() {
        List<String> list = Arrays.asList("a", "b", "c");
        Validate.noNullElements(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithIterableWithNullElement() {
        List<String> list = Arrays.asList("a", null, "c");
        Validate.noNullElements(list);
    }

    @Test
    public void testValidIndexWithArray() {
        int[] array = new int[3];
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithArrayOutOfBoundsIndex() {
        int[] array = new int[3];
        Validate.validIndex(array, 3);
    }

    @Test
    public void testValidIndexWithCollection() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Validate.validIndex(list, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithCollectionOutOfBoundsIndex() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Validate.validIndex(list, 2);
    }

    @Test
    public void testValidIndexWithCharSequence() {
        String chars = "abc";
        Validate.validIndex(chars, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithCharSequenceOutOfBoundsIndex() {
        String chars = "abc";
        Validate.validIndex(chars, 3);
    }

    @Test
    public void testValidStateWithTrueBooleanExpression() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseBooleanExpression() {
        Validate.validState(false);
    }

    @Test
    public void testMatchesPatternWithStringInputAndPattern() {
        Validate.matchesPattern("abc", ".*c");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithStringInputAndInvalidPattern() {
        Validate.matchesPattern("abc", ".*d");
    }

    @Test
    public void testMatchesPatternWithStringInputAndMessage() {
        Validate.matchesPattern("abc", ".*c", "The string %s does not match the pattern %s", "abc", ".*c");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithStringInputAndInvalidPatternAndMessage() {
        Validate.matchesPattern("abc", ".*d", "The string %s does not match the pattern %s", "abc", ".*c");
    }

    @Test
    public void testInclusiveBetweenWithIntegerStartAndEndAndIntegerValue() {
        Validate.inclusiveBetween(1, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithIntegerStartAndEndAndIntegerValueOutOfRange() {
        Validate.inclusiveBetween(1, 3, 0);
    }

    @Test
    public void testInclusiveBetweenWithLongStartAndEndAndLongValue() {
        Validate.inclusiveBetween(1L, 3L, 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongStartAndEndAndLongValueOutOfRange() {
        Validate.inclusiveBetween(1L, 3L, 0L);
    }

    @Test
    public void testInclusiveBetweenWithDoubleStartAndEndAndDoubleValue() {
        Validate.inclusiveBetween(1.0, 3.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleStartAndEndAndDoubleValueOutOfRange() {
        Validate.inclusiveBetween(1.0, 3.0, 0.0);
    }

    @Test
    public void testInclusiveBetweenWithIntegerStartAndEndAndComparableValue() {
        Integer i = 2;
        Validate.inclusiveBetween(1, 3, i);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithIntegerStartAndEndAndComparableValueOutOfRange() {
        Integer i = 0;
        Validate.inclusiveBetween(1, 3, i);
    }

    @Test
    public void testExclusiveBetweenWithIntegerStartAndEndAndIntegerValue() {
        Validate.exclusiveBetween(1, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithIntegerStartAndEndAndIntegerValueOutOfRange() {
        Validate.exclusiveBetween(1, 3, 0);
    }

    @Test
    public void testExclusiveBetweenWithLongStartAndEndAndLongValue() {
        Validate.exclusiveBetween(1L, 3L, 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongStartAndEndAndLongValueOutOfRange() {
        Validate.exclusiveBetween(1L, 3L, 0L);
    }

    @Test
    public void testExclusiveBetweenWithDoubleStartAndEndAndDoubleValue() {
        Validate.exclusiveBetween(1.0, 3.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleStartAndEndAndDoubleValueOutOfRange() {
        Validate.exclusiveBetween(1.0, 3.0, 0.0);
    }

    @Test
    public void testExclusiveBetweenWithIntegerStartAndEndAndComparableValue() {
        Integer i = 2;
        Validate.exclusiveBetween(1, 3, i);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithIntegerStartAndEndAndComparableValueOutOfRange() {
        Integer i = 0;
        Validate.exclusiveBetween(1, 3, i);
    }

    @Test
    public void testIsInstanceOfWithClassTypeAndObject() {
        Validate.isInstanceOf(String.class, "abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithClassTypeAndNonInstanceObject() {
        Validate.isInstanceOf(String.class, new Object());
    }

    @Test
    public void testIsAssignableFromWithClassSuperTypeAndClassType() {
        Validate.isAssignableFrom(Number.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithClassSuperTypeAndClassTypeNotAssignable() {
        Validate.isAssignableFrom(String.class, Integer.class);
    }
}