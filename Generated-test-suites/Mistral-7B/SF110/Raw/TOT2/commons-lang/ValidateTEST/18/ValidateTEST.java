import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.*;

public class ValidateTest {
    @Test
    public void testIsTrueTrue() {
        Validate.isTrue(true);
    }

    @Test
    public void testIsTrueFalseMessage() {
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::isTrue)
                .withArgument(false)
                .verify();
    }

    @Test
    public void testIsTrueFalseMessageValues() {
        ExpectException.throwException(IllegalArgumentException.class)
                .withMessage("The validated expression is false")
                .when(Validate::isTrue)
                .withArgument(false)
                .verify();
    }

    @Test
    public void testIsTrueFalseOtherTypeMessage() {
        ExpectException.throwException(IllegalArgumentException.class)
                .withMessage("The validated expression is false")
                .when(Validate::isTrue)
                .withArgument(123)
                .verify();
    }

    @Test
    public void testNotNullNotNull() {
        Validate.notNull(new Object());
    }

    @Test
    public void testNotNullNullMessage() {
        ExpectException.throwException(NullPointerException.class)
                .when(Validate::notNull)
                .withArgument(null)
                .verify();
    }

    @Test
    public void testNotNullNullMessageValues() {
        ExpectException.throwException(NullPointerException.class)
                .withMessage("The validated object is null")
                .when(Validate::notNull)
                .withArgument(null)
                .verify();
    }

    @Test
    public void testNotEmptyArrayNotNull() {
        Validate.notEmpty(new String[0], "The validated array is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyArrayNull() {
        ExpectException.throwException(NullPointerException.class)
                .when(Validate::notEmpty)
                .withArgument(null)
                .verify();
    }

    @Test
    public void testNotEmptyArrayEmpty() {
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::notEmpty)
                .withArgument(new String[0])
                .verify();
    }

    @Test
    public void testNotEmptyCollectionNotNull() {
        Validate.notEmpty(Collections.emptyList(), "The validated collection is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyCollectionNull() {
        ExpectException.throwException(NullPointerException.class)
                .when(Validate::notEmpty)
                .withArgument(null)
                .verify();
    }

    @Test
    public void testNotEmptyCollectionEmpty() {
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::notEmpty)
                .withArgument(Collections.emptyList())
                .verify();
    }

    @Test
    public void testInclusiveBetweenLongValid() {
        long start = 1;
        long end = 3;
        long value = 2;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test
    public void testInclusiveBetweenLongInvalidLow() {
        long start = 1;
        long end = 3;
        long value = 0;
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::inclusiveBetween)
                .withArguments(start, end, value)
                .verify();
    }

    @Test
    public void testInclusiveBetweenLongInvalidHigh() {
        long start = 1;
        long end = 3;
        long value = 4;
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::inclusiveBetween)
                .withArguments(start, end, value)
                .verify();
    }

    @Test
    public void testExclusiveBetweenLongValid() {
        long start = 1;
        long end = 3;
        long value = 2;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test
    public void testExclusiveBetweenLongInvalidLow() {
        long start = 1;
        long end = 3;
        long value = 0;
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::exclusiveBetween)
                .withArguments(start, end, value)
                .verify();
    }

    @Test
    public void testExclusiveBetweenLongInvalidHigh() {
        long start = 1;
        long end = 3;
        long value = 4;
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::exclusiveBetween)
                .withArguments(start, end, value)
                .verify();
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(String.class, StringBuffer.class);
    }

    @Test
    public void testIsAssignableFromInvalid() {
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::isAssignableFrom)
                .withArguments(String.class, Integer.class)
                .verify();
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, new String("test"));
    }

    @Test
    public void testIsInstanceOfInvalid() {
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::isInstanceOf)
                .withArguments(String.class, new Integer(42))
                .verify();
    }

    @Test
    public void testMatchesPatternValide() {
        String pattern = "^[a-zA-Z0-9]+$";
        String input = "test";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testMatchesPatternInvalid() {
        String pattern = "^[a-zA-Z0-9]+$";
        String input = "test123!";
        ExpectException.throwException(IllegalArgumentException.class)
                .when(Validate::matchesPattern)
                .withArguments(input, pattern)
                .verify();
    }

    @Test
    public void testValidIndexArrayValid() {
        int[] arr = {1, 2, 3};
        Validate.validIndex(arr, 1);
    }

    @Test
    public void testValidIndexArrayInvalid() {
        int[] arr = {1, 2, 3};
        ExpectException.throwException(IndexOutOfBoundsException.class)
                .when(Validate::validIndex)
                .withArguments(arr, -1)
                .verify();
    }

    @Test
    public void testValidIndexCollectionValid() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Validate.validIndex(list, 1);
    }

    @Test
    public void testValidIndexCollectionInvalid() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ExpectException.throwException(IndexOutOfBoundsException.class)
                .when(Validate::validIndex)
                .withArguments(list, -1)
                .verify();
    }

    @Test
    public void testValidIndexCharSequenceValid() {
        String str = "test";
        Validate.validIndex(str, 0);
    }

    @Test
    public void testValidIndexCharSequenceInvalid() {
        String str = "test";
        ExpectException.throwException(IndexOutOfBoundsException.class)
                .when(Validate::validIndex)
                .withArguments(str, -1)
                .verify();
    }
}