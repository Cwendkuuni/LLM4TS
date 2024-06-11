import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.Assert.*;

public class ValidateTest {

 // Test for isTrue method with boolean expression and int value
 @Test
 public void testIsTrueWithBooleanExpressionAndIntValue() {
 Validate.isTrue(true, "Expected true but was false", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseBooleanExpressionAndIntValue() {
 Validate.isTrue(false, "Expected true but was false", 1);
 }

 // Test for isTrue method with boolean expression and double value
 @Test
 public void testIsTrueWithBooleanExpressionAndDoubleValue() {
 Validate.isTrue(true, "Expected true but was false", 2.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseBooleanExpressionAndDoubleValue() {
 Validate.isTrue(false, "Expected true but was false", 2.5);
 }

 // Test for isTrue method with boolean expression and Object array
 @Test
 public void testIsTrueWithBooleanExpressionAndObjectArray() {
 Validate.isTrue(true, "Expected true but was false", new Object[] {"test", 1, 2.5});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseBooleanExpressionAndObjectArray() {
 Validate.isTrue(false, "Expected true but was false", new Object[] {"test", 1, 2.5});
 }

 // Test for isTrue method with boolean expression only
 @Test
 public void testIsTrueWithBooleanExpressionOnly() {
 Validate.isTrue(true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseBooleanExpressionOnly() {
 Validate.isTrue(false);
 }

 // Test for notNull method with null object
 @Test(expected = NullPointerException.class)
 public void testNotNullWithNullObject() {
 Validate.notNull(null);
 }

 // Test for notNull method with non-null object
 @Test
 public void testNotNullWithNonNullObject() {
 Validate.notNull(new Object());
 }

 // Test for notEmpty method with empty array
 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyWithEmptyArray() {
 Validate.notEmpty(new Object[0], "Expected non-empty array but was empty");
 }

 // Test for notEmpty method with non-empty array
 @Test
 public void testNotEmptyWithNonEmptyArray() {
 Validate.notEmpty(new Object[] {"test1", "test2", "test3"});
 }

 // Test for notEmpty method with Iterable instead of array
 @Test
 public void testNotEmptyWithIterable() {
 Validate.noNullElements(Arrays.asList("test1", "test2", "test3"), "Expected non-null elements in the collection");
 }

 // Test for notEmpty method with null Iterable
 @Test(expected = NullPointerException.class)
 public void testNotEmptyWithNullIterable() {
 Validate.noNullElements(null, "Expected non-null elements in the collection");
 }

 // Test for validIndex method with valid index in array
 @Test
 public void testValidIndexWithValidIndexInArray() {
 Validate.validIndex(new Object[] {"test1", "test2", "test3"}, 1, "Expected valid index in the array");
 }

 // Test for validIndex method with invalid index in array
 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexWithInvalidIndexInArray() {
 Validate.validIndex(new Object[] {"test1", "test2", "test3"}, 4, "Expected valid index in the array");
 }

 // Test for validIndex method with valid index in CharSequence
 @Test
 public void testValidIndexWithValidIndexInCharSequence() {
 Validate.validIndex("test1test2test3", 5, "Expected valid index in the CharSequence");
 }

 // Test for validIndex method with invalid index in CharSequence
 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexWithInvalidIndexInCharSequence() {
 Validate.validIndex("test1test2test3", 10, "Expected valid index in the CharSequence");
 }

 // Test for validState method with valid state
 @Test
 public void testValidStateWithValidState() {
 Validate.validState(true);
 }

 @Test(expected = IllegalStateException.class)
 // Test for validState method with invalid state
 public void testValidStateWithInvalidState() {
 Validate.validState(false);
 }

 // Test for matchesPattern method with matching pattern and input
 @Test
 public void testMatchesPatternWithMatchingPatternAndInput() {
 String regex = "[a-z]+";
 Validate.matchesPattern("[abc]", regex);
 }

 // Test for matchesPattern method with non-matching pattern and input
 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternWithNonMatchingPatternAndInput() {
 String regex = "[a-z]+";
 Validate.matchesPattern("[123]", regex);
 }

 // Test for inclusiveBetween method with inclusive value within range
 @Test
 public void testInclusiveBetweenWithInclusiveValueWithinRange() {
 Validate.inclusiveBetween(1, 3, 2);
 }

 // Test for inclusiveBetween method with exclusive value within range
 @Test
 public void testInclusiveBetweenWithExclusiveValueWithinRange() {
 Validate.inclusiveBetween(1, 3, 2.5);
 }

 // Test for inclusiveBetween method with value below range start
 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenWithValueBelowRangeStart() {
 Validate.inclusiveBetween(1, 3, 0.5);
 }

 // Test for inclusiveBetween method with value above range end
 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenWithValueAboveRangeEnd() {
 Validate.inclusiveBetween(1, 3, 4.5);
 }

 // Test for exclusiveBetween method with exclusive value within range
 @Test
 public void testExclusiveBetweenWithExclusiveValueWithinRange() {
 Validate.exclusiveBetween(1, 3, 2);
 }

 // Test for exclusiveBetween method with inclusive value within range
 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithInclusiveValueWithinRange() {
 Validate.exclusiveBetween(1, 3, 2.5);
 }

 // Test for exclusiveBetween method with value below range start
 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithValueBelowRangeStart() {
 Validate.exclusiveBetween(1, 3, 0.5);
 }

 // Test for exclusiveBetween method with value above range end
 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithValueAboveRangeEnd() {
 Validate.exclusiveBetween(1, 3, 4.5);
 }

 // Test for isAssignableFrom method with assignable types
 @Test
 public void testIsAssignableFromWithAssignableTypes() {
 Validate.isAssignableFrom(String.class, StringBuilder.class);
 }

 // Test for isAssignableFrom method with non-assignable types
 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromWithNonAssignableTypes() {
 Validate.isAssignableFrom(String.class, Integer.class);
 }

 // Test for isInstanceOf method with instance of expected type
 @Test
 public void testIsInstanceOfWithInstanceOfExpectedType() {
 Validate.isInstanceOf(StringBuilder.class, new StringBuilder());
 }

 // Test for isInstanceOf method with not instance of expected type
 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfWithNotInstanceOfExpectedType() {
 Validate.isInstanceOf(StringBuilder.class, new Integer(1));
 }
}