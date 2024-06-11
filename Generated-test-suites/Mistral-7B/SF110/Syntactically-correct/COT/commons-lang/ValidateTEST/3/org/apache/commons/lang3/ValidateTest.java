import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidateTest {

 @Test
 public void testIsTrueBooleanExpression() {
 Validate.isTrue(true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueFalseExpression() {
 Validate.isTrue(false);
 }

 @Test
 public void testIsTrueBooleanExpressionWithMessage() {
 Validate.isTrue(true, "The validated expression is true");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueFalseExpressionWithMessage() {
 Validate.isTrue(false, "The validated expression is true");
 }

 @Test
 public void testIsTrueBooleanExpressionWithValues() {
 Validate.isTrue(true, "The validated expression is true", 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueFalseExpressionWithValues() {
 Validate.isTrue(false, "The validated expression is true", 10);
 }

 @Test
 public void testIsTrueBooleanExpressionWithMessageAndValues() {
 Validate.isTrue(true, "The validated expression is true", 10, "Message with values");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueFalseExpressionWithMessageAndValues() {
 Validate.isTrue(false, "The validated expression is true", 10, "Message with values");
 }

 @Test
 public void testNotNullObject() {
 Object obj = new Object();
 Validate.notNull(obj);
 }

 @Test(expected = NullPointerException.class)
 public void testNullObject() {
 Validate.notNull(null);
 }

 @Test
 public void testNotEmptyArray() {
 Integer[] arr = new Integer[5];
 Validate.notEmpty(arr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyArray() {
 Integer[] arr = new Integer[0];
 Validate.notEmpty(arr);
 }

 @Test
 public void testNotEmptyArrayWithMessage() {
 Integer[] arr = new Integer[5];
 Validate.notEmpty(arr, "Array is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyArrayWithMessageAndValues() {
 Integer[] arr = new Integer[0];
 Validate.notEmpty(arr, "Array is empty", 0);
 }

 @Test
 public void testNotEmptyCollection() {
 List<String> list = Arrays.asList("String1", "String2");
 Validate.notEmpty(list);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCollection() {
 List<String> list = Collections.emptyList();
 Validate.notEmpty(list);
 }

 @Test
 public void testNotEmptyCollectionWithMessage() {
 List<String> list = Collections.emptyList();
 Validate.notEmpty(list, "Collection is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCollectionWithMessageAndValues() {
 List<String> list = Collections.emptyList();
 Validate.notEmpty(list, "Collection is empty", 0);
 }

 @Test
 public void testNotEmptyMap() {
 Map<String, Integer> map = new HashMap<>();
 map.put("StringKey1", 1);
 map.put("StringKey2", 2);
 Validate.notEmpty(map);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyMap() {
 Map<String, Integer> map = new HashMap<>();
 Validate.notEmpty(map);
 }

 @Test
 public void testNotEmptyMapWithMessage() {
 Map<String, Integer> map = new HashMap<>();
 Validate.notEmpty(map, "Map is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyMapWithMessageAndValues() {
 Map<String, Integer> map = new HashMap<>();
 Validate.notEmpty(map, "Map is empty", 0);
 }

 @Test
 public void testNotEmptyCharSequence() {
 String chars = "CharSequence";
 Validate.notEmpty(chars);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCharSequence() {
 String chars = "";
 Validate.notEmpty(chars);
 }

 @Test
 public void testNotEmptyCharSequenceWithMessage() {
 String chars = "";
 Validate.notEmpty(chars, "Character sequence is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyCharSequenceWithMessageAndValues() {
 String chars = "";
 Validate.notEmpty(chars, "Character sequence is empty", 0);
 }

 @Test
 public void testNotBlankCharSequence() {
 String chars = "Character sequence";
 Validate.notBlank(chars);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlankCharSequence() {
 String chars = "";
 Validate.notBlank(chars);
 }

 @Test
 public void testNotBlankCharSequenceWithMessage() {
 String chars = "";
 Validate.notBlank(chars, "Character sequence is blank");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlankCharSequenceWithMessageAndValues() {
 String chars = "";
 Validate.notBlank(chars, "Character sequence is blank", 0);
 }

 @Test
 public void testNoNullElementsArray() {
 Integer[] arr = new Integer[] {1, 2, 3};
 Validate.noNullElements(arr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayWithNullElement() {
 Integer[] arr = new Integer[] {1, null, 3};
 Validate.noNullElements(arr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayWithNullElementWithMessage() {
 Integer[] arr = new Integer[] {1, null, 3};
 Validate.noNullElements(arr, "Array contains null element");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayWithNullElementWithMessageAndValues() {
 Integer[] arr = new Integer[] {1, null, 3};
 Validate.noNullElements(arr, "Array contains null element", 1);
 }

 @Test
 public void testNoNullElementsCollection() {
 List<String> list = Arrays.asList("String1", "String2", null);
 Validate.noNullElements(list);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectionWithNullElement() {
 List<String> list = Arrays.asList("String1", "String2", null);
 Validate.noNullElements(list);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectionWithNullElementWithMessage() {
 List<String> list = Arrays.asList("String1", "String2", null);
 Validate.noNullElements(list, "Collection contains null element");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectionWithNullElementWithMessageAndValues() {
 List<String> list = Arrays.asList("String1", "String2", null);
 Validate.noNullElements(list, "Collection contains null element", 2);
 }

 @Test
 public void testValidIndexArray() {
 Integer[] arr = new Integer[5];
 Validate.validIndex(arr, 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexArray() {
 Integer[] arr = new Integer[5];
 Validate.validIndex(arr, 5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexArrayWithMessage() {
 Integer[] arr = new Integer[5];
 Validate.validIndex(arr, 5, "Invalid array index");
 }

 @Test
 public void testValidIndexCollection() {
 List<String> list = Arrays.asList("String1", "String2");
 Validate.validIndex(list, 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexCollection() {
 List<String> list = Arrays.asList("String1", "String2");
 Validate.validIndex(list, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexCollectionWithMessage() {
 List<String> list = Arrays.asList("String1", "String2");
 Validate.validIndex(list, 2, "Invalid collection index");
 }

 @Test
 public void testValidIndexCharSequence() {
 String chars = "Character sequence";
 Validate.validIndex(chars, 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexCharSequence() {
 String chars = "Character sequence";
 Validate.validIndex(chars, 5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndexCharSequenceWithMessage() {
 String chars = "Character sequence";
 Validate.validIndex(chars, 5, "Invalid character sequence index");
 }

 @Test
 public void testValidStateBoolean() {
 boolean state = true;
 Validate.validState(state);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidStateBoolean() {
 boolean state = false;
 Validate.validState(state);
 }

 @Test
 public void testValidStateBooleanWithMessage() {
 boolean state = true;
 Validate.validState(state, "Invalid state");
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidStateBooleanWithMessage() {
 boolean state = false;
 Validate.validState(state, "Invalid state");
 }

 @Test
 public void testMatchesPatternCharSequence() {
 String chars = "ABCDEFG";
 Validate.matchesPattern(chars, ".*F.*");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoesNotMatchPatternCharSequence() {
 String chars = "ABCDEFG";
 Validate.matchesPattern(chars, ".*J.*");
 }

 @Test
 public void testMatchesPatternCharSequenceWithMessage() {
 String chars = "ABCDEFG";
 Validate.matchesPattern(chars, ".*F.*", "Invalid pattern match");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoesNotMatchPatternCharSequenceWithMessage() {
 String chars = "ABCDEFG";
 Validate.matchesPattern(chars, ".*J.*", "Invalid pattern match");
 }

 @Test
 public void testInclusiveBetweenComparable() {
 Integer num = 5;
 Validate.inclusiveBetween(5, 10, num);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenComparable() {
 Integer num = 5;
 Validate.exclusiveBetween(5, 10, num);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenComparableWithMessage() {
 Integer num = 5;
 Validate.inclusiveBetween(5, 10, num, "Invalid number range");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenComparableWithMessage() {
 Integer num = 5;
 Validate.exclusiveBetween(5, 10, num, "Invalid number range");
 }

 @Test
 public void testInclusiveBetweenLong() {
 long num = 5L;
 Validate.inclusiveBetween(5L, 10L, num);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenLong() {
 long num = 5L;
 Validate.exclusiveBetween(5L, 10L, num);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenLongWithMessage() {
 long num = 5L;
 Validate.inclusiveBetween(5L, 10L, num, "Invalid number range");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenLongWithMessage() {
 long num = 5L;
 Validate.exclusiveBetween(5L, 10L, num, "Invalid number range");
 }

 @Test
 public void testInclusiveBetweenDouble() {
 double num = 5.5;
 Validate.inclusiveBetween(5.5, 10.5, num);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenDouble() {
 double num = 5.5;
 Validate.exclusiveBetween(5.5, 10.5, num);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenDoubleWithMessage() {
 double num = 5.5;
 Validate.inclusiveBetween(5.5, 10.5, num, "Invalid number range");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenDoubleWithMessage() {
 double num = 5.5;
 Validate.exclusiveBetween(5.5, 10.5, num, "Invalid number range");
 }

 @Test
 public void testIsInstanceOfClass() {
 String str = "String";
 Validate.isInstanceOf(String.class, str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotInstanceOfClass() {
 String str = "String";
 Validate.isInstanceOf(Integer.class, str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotInstanceOfClassWithMessage() {
 String str = "String";
 Validate.isInstanceOf(Integer.class, str, "Invalid class type");
 }

 @Test
 public void testIsAssignableFromClass() {
 String str1 = "String1";
 String str2 = "String2";
 Validate.isAssignableFrom(String.class, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotAssignableFromClass() {
 String str1 = "String1";
 Integer num = 5;
 Validate.isAssignableFrom(String.class, num.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotAssignableFromClassWithMessage() {
 String str1 = "String1";
 Integer num = 5;
 Validate.isAssignableFrom(String.class, num.getClass(), "Invalid class type");
 }
}