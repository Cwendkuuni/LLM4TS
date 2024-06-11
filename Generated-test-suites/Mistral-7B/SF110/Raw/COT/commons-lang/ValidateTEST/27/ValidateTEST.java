import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.fail;

public class ValidateTest {

    @Test
    public void testIsTrueBooleanExpression() {
        Validate.isTrue(true, "Expected true but was false");
    }

    @Test
    public void testIsTrueBooleanExpressionWithMessageAndValue() {
        Validate.isTrue(false, "Expected true but was false with message: Expected true but was false, value: 42", 42);
    }

    @Test
    public void testIsTrueBooleanExpressionWithValues() {
        Validate.isTrue(false, "Expected true but was false with values: {1, 2, 3}", new Object[]{1, 2, 3});
    }

    @Test
    public void testIsTrueBooleanExpressionNoMessage() {
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testNotNullObject() {
        String object = new String("hello");
        Validate.notNull(object);
    }

    @Test
    public void testNotNullObjectWithMessageAndValues() {
        Object object = null;
        try {
            Validate.notNull(object, "Expected not null but was null with message: Expected not null but was null, values: {1, 2, 3}", new Object[]{1, 2, 3});
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testNotEmptyArray() {
        String[] array = new String[]{"hello", "world"};
        Validate.notEmpty(array, "Expected not empty but was empty with message: Expected not empty but was empty, values: {0}", new Object[]{0});
    }

    @Test
    public void testNotEmptyArrayNoMessage() {
        String[] array = new String[0];
        try {
            Validate.notEmpty(array);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testNotEmptyCollection() {
        ArrayList<String> collection = new ArrayList<>();
        collection.add("hello");
        Validate.notEmpty(collection, "Expected not empty but was empty with message: Expected not empty but was empty, values: {0}", new Object[]{0});
    }

    @Test
    public void testNotEmptyCollectionNoMessage() {
        ArrayList<String> collection = new ArrayList<>();
        try {
            Validate.notEmpty(collection);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsInArray() {
        String[] array = new String[]{"hello", null};
        Validate.noNullElements(array, "Expected no null elements but found null at index: {1}", new Object[]{1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsInCollection() {
        ArrayList<String> collection = new ArrayList<>();
        collection.add("hello");
        collection.add(null);
        Validate.noNullElements(collection, "Expected no null elements but found null at index: {1}", new Object[]{1});
    }

    @Test
    public void testValidIndexArray() {
        int[] array = new int[5];
        Validate.validIndex(array, 2, "Expected valid index but was invalid with message: Expected valid index but was invalid, values: {1}", new Object[]{1});
    }

    @Test
    public void testValidIndexArrayWithIndexOutOfBoundsException() {
        int[] array = new int[5];
        try {
            Validate.validIndex(array, 5, "Expected valid index but was invalid with message: Expected valid index but was invalid, values: {1}", new Object[]{1});
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void testValidIndexCollection() {
        ArrayList<String> collection = new ArrayList<>();
        collection.add("hello");
        Validate.validIndex(collection, 0, "Expected valid index but was invalid with message: Expected valid index but was invalid, values: {1}", new Object[]{1});
    }

    @Test
    public void testValidIndexCollectionWithIndexOutOfBoundsException() {
        ArrayList<String> collection = new ArrayList<>();
        collection.add("hello");
        try {
            Validate.validIndex(collection, 1, "Expected valid index but was invalid with message: Expected valid index but was invalid, values: {1}", new Object[]{1});
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "hello";
        Validate.validIndex(chars, 2, "Expected valid index but was invalid with message: Expected valid index but was invalid, values: {1}", new Object[]{1});
    }

    @Test
    public void testValidIndexCharSequenceWithIndexOutOfBoundsException() {
        String chars = "hello";
        try {
            Validate.validIndex(chars, 5, "Expected valid index but was invalid with message: Expected valid index but was invalid, values: {1}", new Object[]{1});
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void testValidStateBooleanExpression() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateBooleanExpressionFalse() {
        Validate.validState(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidStateBooleanExpressionWithMessage() {
        Validate.validState(false, "Expected valid state but was invalid with message: Expected valid state but was invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPattern() {
        String input = "invalid";
        String pattern = "\\w+";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithMessageAndValues() {
        String input = "invalid";
        String pattern = "\\w+";
        Validate.matchesPattern(input, pattern, "Expected string to match pattern but did not match with message: Expected string to match pattern but did not match, values: {0}", new Object[]{"Invalid input"});
    }

    @Test
    public void testInclusiveBetweenLong() {
        long value = 42;
        long start = 40;
        long end = 45;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongOutOfRange() {
        long value = 39;
        long start = 40;
        long end = 45;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongWithMessage() {
        long value = 39;
        long start = 40;
        long end = 45;
        Validate.inclusiveBetween(start, end, value, "Expected value to be in inclusive range but was out of range with message: Expected value to be in inclusive range but was out of range");
    }

    @Test
    public void testExclusiveBetweenLong() {
        long value = 42;
        long start = 40;
        long end = 45;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongOutOfRange() {
        long value = 41;
        long start = 40;
        long end = 45;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongWithMessage() {
        long value = 41;
        long start = 40;
        long end = 45;
        Validate.exclusiveBetween(start, end, value, "Expected value to be in exclusive range but was in range with message: Expected value to be in exclusive range but was in range");
    }

    @Test
    public void testInclusiveBetweenDouble() {
        double value = 42.0;
        double start = 40.0;
        double end = 45.0;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleOutOfRange() {
        double value = 39.0;
        double start = 40.0;
        double end = 45.0;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleWithMessage() {
        double value = 39.0;
        double start = 40.0;
        double end = 45.0;
        Validate.inclusiveBetween(start, end, value, "Expected value to be in inclusive range but was out of range with message: Expected value to be in inclusive range but was out of range");
    }

    @Test
    public void testExclusiveBetweenDouble() {
        double value = 42.0;
        double start = 40.0;
        double end = 45.0;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleOutOfRange() {
        double value = 41.0;
        double start = 40.0;
        double end = 45.0;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleWithMessage() {
        double value = 41.0;
        double start = 40.0;
        double end = 45.0;
        Validate.exclusiveBetween(start, end, value, "Expected value to be in exclusive range but was in range with message: Expected value to be in exclusive range but was in range");
    }

    @Test
    public void testIsInstanceOfClass() {
        Object obj = new String("hello");
        Validate.isInstanceOf(String.class, obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfClassWrongType() {
        Object obj = new String("hello");
        Validate.isInstanceOf(Integer.class, obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfClassWithMessage() {
        Object obj = new String("hello");
        Validate.isInstanceOf(Integer.class, obj, "Expected instance of class Integer but was instance of class String with message: Expected instance of class Integer but was instance of class String");
    }

    @Test
    public void testIsAssignableFromClass() {
        Class<? extends Object> superType = Object.class;
        Class<? extends Object> type = String.class;
        Validate.isAssignableFrom(superType, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromClassWrongType() {
        Class<? extends Object> superType = Object.class;
        Class<? extends Object> type = Integer.class;
        Validate.isAssignableFrom(superType, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromClassWithMessage() {
        Class<? extends Object> superType = Object.class;
        Class<? extends Object> type = Integer.class;
        Validate.isAssignableFrom(superType, type, "Expected super type to be assignable from sub type but was not with message: Expected super type to be assignable from sub type but was not");
    }
}