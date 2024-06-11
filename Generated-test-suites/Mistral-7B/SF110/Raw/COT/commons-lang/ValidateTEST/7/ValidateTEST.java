import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.fail;

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
    public void testIsTrueWithLongValue() {
        Validate.isTrue(1L > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithNegativeLongValue() {
        Validate.isTrue(-1L > 0);
    }

    @Test
    public void testIsTrueWithDoubleValue() {
        Validate.isTrue(2.0 > 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithNegativeDoubleValue() {
        Validate.isTrue(-2.0 > 1.0);
    }

    @Test
    public void testIsTrueWithObjectArrayAndMessage() {
        String[] objects = new String[]{"apple", "banana", "cherry"};
        Validate.isTrue(objects.length > 0, "The array is empty", "array");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseObjectArrayAndMessage() {
        String[] objects = new String[0];
        Validate.isTrue(objects.length > 0, "The array is empty", "array");
    }

    @Test
    public void testNotNullMethod() {
        String str = new String("test");
        Validate.notNull(str);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullMethodWithNullObject() {
        String str = null;
        Validate.notNull(str, "The validated object is null");
    }

    @Test
    public void testNotEmptyArrayMethod() {
        String[] array = new String[]{"apple", "banana", "cherry"};
        Validate.notEmpty(array, "The validated array is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayMethodWithEmptyArray() {
        String[] array = new String[0];
        Validate.notEmpty(array, "The validated array is empty");
    }

    @Test
    public void testNotEmptyCollectionMethod() {
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        Validate.notEmpty(list, "The validated collection is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionMethodWithEmptyCollection() {
        List<String> list = new ArrayList<>();
        Validate.notEmpty(list, "The validated collection is empty");
    }

    @Test
    public void testNotEmptyMapMethod() {
        Map<String, String> map = new HashMap<>();
        map.put("apple", "red");
        map.put("banana", "yellow");
        Validate.notEmpty(map, "The validated map is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapMethodWithEmptyMap() {
        Map<String, String> map = new HashMap<>();
        Validate.notEmpty(map, "The validated map is empty");
    }

    @Test
    public void testNotEmptyCharSequenceMethod() {
        String chars = "applebanana";
        Validate.notEmpty(chars, "The validated character sequence is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCharSequenceMethodWithEmptyCharSequence() {
        String chars = "";
        Validate.notEmpty(chars, "The validated character sequence is empty");
    }

    @Test
    public void testNotBlankCharSequenceMethod() {
        String chars = "applebanana";
        Validate.notBlank(chars, "The validated character sequence is blank");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankCharSequenceMethodWithEmptyCharSequence() {
        String chars = "";
        Validate.notBlank(chars, "The validated character sequence is blank");
    }

    @Test
    public void testNoNullElementsArrayMethod() {
        String[] array = new String[]{"apple", "banana", "cherry"};
        Validate.noNullElements(array, "The validated array contains null element at index: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayMethodWithNullElementAtIndexZero() {
        String[] array = new String[2];
        array[0] = null;
        Validate.noNullElements(array, "The validated array contains null element at index: %d");
    }

    @Test
    public void testNoNullElementsCollectionMethod() {
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        Validate.noNullElements(list, "The validated collection contains null element at index: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsCollectionMethodWithNullElementAtIndexZero() {
        List<String> list = new ArrayList<>();
        list.add(null);
        Validate.noNullElements(list, "The validated collection contains null element at index: %d");
    }

    @Test
    public void testValidIndexArrayMethod() {
        String[] array = new String[]{"apple", "banana", "cherry"};
        Validate.validIndex(array, 1, "The validated array index is invalid: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayMethodWithInvalidIndex() {
        String[] array = new String[]{"apple", "banana", "cherry"};
        Validate.validIndex(array, 3, "The validated array index is invalid: %d");
    }

    @Test
    public void testValidIndexCollectionMethod() {
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        Validate.validIndex(list, 1, "The validated collection index is invalid: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionMethodWithInvalidIndex() {
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        Validate.validIndex(list, 2, "The validated collection index is invalid: %d");
    }

    @Test
    public void testValidIndexCharSequenceMethod() {
        String chars = "applebanana";
        Validate.validIndex(chars, 1, "The validated character sequence index is invalid: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceMethodWithInvalidIndex() {
        String chars = "applebanana";
        Validate.validIndex(chars, 3, "The validated character sequence index is invalid: %d");
    }

    @Test
    public void testValidStateMethod() {
        boolean validState = true;
        Validate.validState(validState);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateMethodWithInvalidState() {
        boolean validState = false;
        Validate.validState(validState);
    }

    @Test
    public void testValidStateMethodWithMessage() {
        boolean validState = true;
        Validate.validState(validState, "The validated state is false");
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateMethodWithInvalidStateAndMessage() {
        boolean validState = false;
        Validate.validState(validState, "The validated state is false");
    }

    @Test
    public void testMatchesPatternMethodWithMatchingInputAndPattern() {
        String input = "12345";
        String pattern = "\\d{5}";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternMethodWithNonMatchingInputAndPattern() {
        String input = "1234ab";
        String pattern = "\\d{5}";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testMatchesPatternMethodWithMatchingInputAndPatternAndMessage() {
        String input = "12345";
        String pattern = "\\d{5}";
        Validate.matchesPattern(input, pattern, "The string %s does not match the pattern %s", input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternMethodWithNonMatchingInputAndPatternAndMessage() {
        String input = "1234ab";
        String pattern = "\\d{5}";
        Validate.matchesPattern(input, pattern, "The string %s does not match the pattern %s", input, pattern);
    }

    @Test
    public void testInclusiveBetweenMethodWithComparableObjectAndValidValues() {
        Integer validValue = 10;
        Validate.inclusiveBetween(5, 15, validValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenMethodWithComparableObjectAndInvalidValue() {
        Integer validValue = 10;
        Validate.inclusiveBetween(5, 15, 5);
    }

    @Test
    public void testInclusiveBetweenMethodWithLongAndValidValues() {
        long validValue = 10L;
        Validate.inclusiveBetween(5L, 15L, validValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenMethodWithLongAndInvalidValue() {
        long validValue = 10L;
        Validate.inclusiveBetween(5L, 15L, 5L);
    }

    @Test
    public void testInclusiveBetweenMethodWithDoubleAndValidValues() {
        double validValue = 10.5;
        Validate.inclusiveBetween(5.0, 15.0, validValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenMethodWithDoubleAndInvalidValue() {
        double validValue = 10.5;
        Validate.inclusiveBetween(5.0, 15.0, 5.0);
    }

    @Test
    public void testExclusiveBetweenMethodWithComparableObjectAndValidValues() {
        Integer validValue = 10;
        Validate.exclusiveBetween(6, 14, validValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenMethodWithComparableObjectAndInvalidValue() {
        Integer validValue = 10;
        Validate.exclusiveBetween(6, 14, 6);
    }

    @Test
    public void testExclusiveBetweenMethodWithLongAndValidValues() {
        long validValue = 10L;
        Validate.exclusiveBetween(6L, 14L, validValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenMethodWithLongAndInvalidValue() {
        long validValue = 10L;
        Validate.exclusiveBetween(6L, 14L, 6L);
    }

    @Test
    public void testExclusiveBetweenMethodWithDoubleAndValidValues() {
        double validValue = 10.5;
        Validate.exclusiveBetween(6.0, 14.0, validValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenMethodWithDoubleAndInvalidValue() {
        double validValue = 10.5;
        Validate.exclusiveBetween(6.0, 14.0, 6.0);
    }

    @Test
    public void testIsInstanceOfMethod() {
        String str = new String("test");
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfMethodWithInvalidType() {
        String str = new String("test");
        Validate.isInstanceOf(Integer.class, str);
    }

    @Test
    public void testIsAssignableFromMethod() {
        String str = new String("test");
        Validate.isAssignableFrom(String.class.getClassLoader().getSuperclass(), String[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromMethodWithInvalidSuperClass() {
        String str = new String("test");
        Validate.isAssignableFrom(Integer.class, String.class);
    }
}