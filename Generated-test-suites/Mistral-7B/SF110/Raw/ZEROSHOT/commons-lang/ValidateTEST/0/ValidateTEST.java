package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 final boolean expressionTrue = true;
 final boolean expressionFalse = false;
 Validate.isTrue(expressionTrue);
 Validate.isTrue(expressionTrue, "Expected true but got: %s", expressionFalse);
 Validate.isTrue(expressionTrue, "Expected true", (Object[]) new Object[] { expressionFalse });
 Validate.isTrue(expressionTrue);

 Validate.isTrue(expressionFalse, "Expected false but got: %s", expressionTrue);
 Validate.isTrue(expressionFalse, "Expected false", (Object[]) new Object[] { expressionTrue });
 Validate.isTrue(!expressionTrue);
 Validate.isTrue(!expressionFalse);

 final long valueTrue = 10;
 final double valueTrueDouble = 10.0;
 Validate.isTrue(expressionTrue, "Expected %s but got: %s", valueTrue, valueTrueDouble);
 Validate.isTrue(expressionTrue, "Expected %s", valueTrueDouble);
 Validate.isTrue(expressionTrue, "Expected %s, %s", valueTrue, valueTrueDouble);

 final long valueFalse = 5;
 final double valueFalseDouble = 5.0;
 Validate.isTrue(expressionFalse, "Expected %s but got: %s", valueFalse, valueFalseDouble);
 Validate.isTrue(expressionFalse, "Expected %s", valueFalseDouble);
 Validate.isTrue(expressionFalse, "Expected %s, %s", valueFalse, valueFalseDouble);
 }

 @Test
 public void testNotNull() {
 final Object obj1 = new Object();
 final Object obj2 = null;

 Validate.notNull(obj1);
 Validate.notNull(obj1, "Expected non-null but got null");
 Validate.notNull(obj1, "Expected non-null", (Object[]) new Object[] { obj2 });

 Validate.notNull(obj2, "Expected null but got %s", obj1);
 Validate.notNull(obj2, "Expected null", (Object[]) new Object[] { obj1 });
 }

 @Test
 public void testNotEmpty() {
 final Object[] arrayEmpty = new Object[0];
 final Object[] arrayNotEmpty = { "one", "two", "three" };
 final List<String> listEmpty = new ArrayList<>();
 final List<String> listNotEmpty = Arrays.asList("one", "two", "three");
 final Map<String, String> mapEmpty = new HashMap<>();
 final Map<String, String> mapNotEmpty = new LinkedHashMap<>();
 mapNotEmpty.put("key1", "value1");
 mapNotEmpty.put("key2", "value2");

 Validate.notEmpty(arrayEmpty, "Expected non-empty but got empty array");
 Validate.notEmpty(arrayEmpty, "Expected non-empty", (Object[]) new Object[] { arrayNotEmpty });
 Validate.notEmpty(arrayNotEmpty);

 Validate.notEmpty(listEmpty, "Expected non-empty but got empty list");
 Validate.notEmpty(listEmpty, "Expected non-empty", (Object[]) new Object[] { listNotEmpty });
 Validate.notEmpty(listNotEmpty);

 Validate.notEmpty(mapEmpty, "Expected non-empty but got empty map");
 Validate.notEmpty(mapEmpty, "Expected non-empty", (Object[]) new Object[] { mapNotEmpty });
 Validate.notEmpty(mapNotEmpty);
 }

 @Test
 public void testNotBlank() {
 final String charsEmpty = "";
 final String charsNotEmpty = "one two three";

 Validate.notBlank(charsEmpty, "Expected non-blank but got blank");
 Validate.notBlank(charsEmpty, "Expected non-blank", (Object[]) new Object[] { charsNotEmpty });
 Validate.notBlank(charsNotEmpty);
 }

 @Test
 public void testNoNullElements() {
 final Object[] arrayNull = { null, "one", "two" };
 final Object[] arrayNoNull = { "one", "two", "three" };
 final List<String> listNull = Arrays.asList(null, "one", "two");
 final List<String> listNoNull = Arrays.asList("one", "two", "three");

 Validate.noNullElements(arrayNull, "Expected no null elements but got %s at index: %d", null, 1);
 Validate.noNullElements(arrayNull, "Expected no null elements but got null at index: %d", null, 0);
 Validate.noNullElements(arrayNull, "Expected no null elements", (Object[]) new Object[] { arrayNoNull });
 Validate.noNullElements(arrayNoNull);

 Validate.noNullElements(listNull, "Expected no null elements but got %s at index: %d", null, 1);
 Validate.noNullElements(listNull, "Expected no null elements but got null at index: %d", null, 0);
 Validate.noNullElements(listNull, "Expected no null elements", (Object[]) new Object[] { listNoNull });
 Validate.noNullElements(listNoNull);
 }

 @Test
 public void testValidIndex() {
 final Object[] array = new Object[3];
 Validate.validIndex(array, 0);
 Validate.validIndex(array, 1);
 Validate.validIndex(array, 2);

 Validate.validIndex(array, 3, "Expected valid index but got: %d", 3);
 Validate.validIndex(array, -1, "Expected valid index but got: %d", -1);
 }

 @Test
 public void testValidIndexCharSequence() {
 final String chars = "one two three";
 Validate.validIndex(chars, 0);
 Validate.validIndex(chars, 1);
 Validate.validIndex(chars, 2);

 Validate.validIndex(chars, 3, "Expected valid index but got: %d", 3);
 Validate.validIndex(chars, -1, "Expected valid index but got: %d", -1);
 }

 @Test
 public void testValidIndexCollection() {
 final List<String> list = Arrays.asList("one", "two", "three");
 Validate.validIndex(list, 0);
 Validate.validIndex(list, 1);
 Validate.validIndex(list, 2);

 Validate.validIndex(list, 3, "Expected valid index but got: %d", 3);
 Validate.validIndex(list, -1, "Expected valid index but got: %d", -1);
 }

 @Test
 public void testValidState() {
 final boolean expressionTrue = true;
 final boolean expressionFalse = false;
 Validate.validState(expressionTrue);
 Validate.validState(expressionFalse, "Expected valid state but got: %b", expressionFalse);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPattern() {
 final String input = "";
 final String pattern = "[a-z]+";
 Validate.matchesPattern(input, pattern);
 Validate.matchesPattern(input, pattern, "Expected string to match pattern: %s", pattern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween() {
 final long value = 5;
 final long start = 10;
 final long end = 15;
 Validate.inclusiveBetween(start, end, value);
 Validate.inclusiveBetween(start, end, value, "Expected value %d to be in inclusive range %d to %d", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween() {
 final long value = 5;
 final long start = 10;
 final long end = 15;
 Validate.exclusiveBetween(start, end, value);
 Validate.exclusiveBetween(start, end, value, "Expected value %d to be in exclusive range %d to %d", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOf() {
 final Object obj = new Object();
 final String str = "test";
 Validate.isInstanceOf(String.class, obj);
 Validate.isInstanceOf(String.class, obj, "Expected %s but got %s", String.class.getName(), obj.getClass().getName());
 Validate.isInstanceOf(String.class, str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFrom() {
 final Object obj = new Object();
 final String str = "test";
 Validate.isAssignableFrom(String.class.getClass(), obj.getClass());
 Validate.isAssignableFrom(String.class.getClass(), obj.getClass(), "Expected %s to be assignable from %s", String.class.getName(), obj.getClass().getName());
 Validate.isAssignableFrom(String[].class, str.getClass());
 }
}