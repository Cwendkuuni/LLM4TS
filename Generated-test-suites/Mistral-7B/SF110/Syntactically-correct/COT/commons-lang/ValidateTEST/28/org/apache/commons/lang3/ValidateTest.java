import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
    public void testIsTrueWithBooleanExpressionAndMessage() {
        Validate.isTrue(true, "The validated expression is true");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpressionAndMessage() {
        Validate.isTrue(false, "The validated expression is true");
    }

    @Test
    public void testIsTrueWithBooleanExpressionAndValues() {
        Validate.isTrue(true, "The validated expression is true", 1, 2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpressionAndValues() {
        Validate.isTrue(false, "The validated expression is true", 1, 2, 3);
    }

    @Test
    public void testNotNull() {
        String str = new String("Hello");
        assertNotNull(str);
        Validate.notNull(str);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyArray() {
        String[] arr = new String[]{"Hello", "World"};
        assertNotNull(arr);
        Validate.notEmpty(arr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArray() {
        String[] arr = new String[0];
        Validate.notEmpty(arr);
    }

    @Test
    public void testNotEmptyCollection() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        assertNotNull(list);
        Validate.notEmpty(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithEmptyCollection() {
        ArrayList<String> list = new ArrayList<>();
        Validate.notEmpty(list);
    }

    @Test
    public void testNotEmptyMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Hello", "World");
        assertNotNull(map);
        Validate.notEmpty(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithEmptyMap() {
        HashMap<String, String> map = new HashMap<>();
        Validate.notEmpty(map);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String str = "Hello World";
        assertNotNull(str);
        Validate.notEmpty(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCharSequenceWithEmptyString() {
        String str = "";
        Validate.notEmpty(str);
    }

    @Test
    public void testNoNullElementsArray() {
        String[] arr = new String[]{"Hello", "World"};
        assertNotNull(arr);
        Validate.noNullElements(arr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithNullElement() {
        String[] arr = new String[]{"Hello", null, "World"};
        Validate.noNullElements(arr);
    }

    @Test
    public void testNoNullElementsCollection() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        assertNotNull(list);
        Validate.noNullElements(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsCollectionWithNullElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add(null);
        list.add("World");
        Validate.noNullElements(list);
    }

    @Test
    public void testValidIndexArray() {
        String[] arr = new String[]{"Hello", "World"};
        Validate.validIndex(arr, 0);
        Validate.validIndex(arr, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithInvalidIndex() {
        String[] arr = new String[]{"Hello", "World"};
        Validate.validIndex(arr, 2);
    }

    @Test
    public void testValidIndexCollection() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        Validate.validIndex(list, 0);
        Validate.validIndex(list, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithInvalidIndex() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        Validate.validIndex(list, 2);
    }

    @Test
    public void testValidIndexCharSequence() {
        String str = "Hello World";
        Validate.validIndex(str, 0);
        Validate.validIndex(str, 6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithInvalidIndex() {
        String str = "Hello World";
        Validate.validIndex(str, 7);
    }

    @Test
    public void testValidState() {
        boolean expression = true;
        Validate.validState(expression);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseExpression() {
        boolean expression = false;
        Validate.validState(expression);
    }

    @Test
    public void testValidStateWithMessage() {
        boolean expression = true;
        Validate.validState(expression, "The validated state is true");
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseExpressionAndMessage() {
        boolean expression = false;
        Validate.validState(expression, "The validated state is true");
    }

    @Test
    public void testMatchesPattern() {
        String str = "Hello123World";
        String pattern = "\\w+\\d+\\w+";
        Validate.matchesPattern(str, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithStringNotMatchingPattern() {
        String str = "Hello123World";
        String pattern = "[a-z]+";
        Validate.matchesPattern(str, pattern);
    }

    @Test
    public void testMatchesPatternWithMessage() {
        String str = "Hello123World";
        String pattern = "\\w+\\d+\\w+";
        Validate.matchesPattern(str, pattern, "The string %s does not match the pattern %s", str, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithStringNotMatchingPatternAndMessage() {
        String str = "Hello123World";
        String pattern = "[a-z]+";
        Validate.matchesPattern(str, pattern, "The string %s does not match the pattern %s", str, pattern);
    }

    @Test
    public void testInclusiveBetween() {
        int value = 5;
        byte[] start = {1};
        byte[] end = {10};
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithValueOutsideRange() {
        int value = 15;
        byte[] start = {1};
        byte[] end = {10};
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithValueOutsideRangeAndMessage() {
        int value = 15;
        byte[] start = {1};
        byte[] end = {10};
        Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
    }

    @Test
    public void testExclusiveBetween() {
        int value = 5;
        byte[] start = {1};
        byte[] end = {11};
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithValueInsideRange() {
        int value = 6;
        byte[] start = {1};
        byte[] end = {11};
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithValueInsideRangeAndMessage() {
        int value = 6;
        byte[] start = {1};
        byte[] end = {11};
        Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
    }

    @Test
    public void testExclusiveBetweenWithValueEqualStart() {
        int value = 1;
        byte[] start = {1};
        byte[] end = {2};
        try {
            Validate.exclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testExclusiveBetweenWithValueEqualStartAndMessage() {
        int value = 1;
        byte[] start = {1};
        byte[] end = {2};
        try {
            Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testExclusiveBetweenWithValueEqualEnd() {
        int value = 10;
        byte[] start = {1};
        byte[] end = {10};
        try {
            Validate.exclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testExclusiveBetweenWithValueEqualEndAndMessage() {
        int value = 10;
        byte[] start = {1};
        byte[] end = {10};
        try {
            Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsInstanceOf() {
        String str = "Hello World";
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithNonAssignableType() {
        int num = 10;
        Validate.isInstanceOf(String.class, num);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithNonAssignableTypeAndMessage() {
        int num = 10;
        Validate.isInstanceOf(String.class, num, "Expected type: String, actual: Integer");
    }

    @Test
    public void testIsAssignableFrom() {
        String str = "Hello World";
        Validate.isAssignableFrom(String.class, String[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithNonAssignableTypes() {
        int num = 10;
        Validate.isAssignableFrom(String.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithNonAssignableTypesAndMessage() {
        int num = 10;
        Validate.isAssignableFrom(String.class, Integer.class, "Cannot assign a String to an Integer");
    }
}