import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithLongArguments() {
        try {
            Validate.isTrue(false, "Expression is false with long message and value: %d", 1L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isTrue(true, "Expression is true");
        Validate.isTrue(true, "Expression is true with long message and value: %d", 2L);
    }

    @Test
    public void testIsTrueWithDoubleArguments() {
        try {
            Validate.isTrue(false, "Expression is false with double message and value: %f", 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isTrue(true, "Expression is true");
        Validate.isTrue(true, "Expression is true with double message and value: %f", 2.0);
    }

    @Test
    public void testIsTrueWithObjectsArguments() {
        try {
            Validate.isTrue(false, "Expression is false with message and values: %s", new Object[]{"Message with objects"});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isTrue(true, "Expression is true");
        Validate.isTrue(true, "Expression is true with message and values: %s", new Object[]{"Message with objects"});
    }

    @Test
    public void testIsTrueWithoutMessage() {
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isTrue(true);
    }

    @Test
    public void testNotNull() {
        Object obj = new Object();
        Validate.notNull(obj, "Object should not be null");

        try {
            Validate.notNull(null, "Object should not be null");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyArray() {
        String[] array = new String[0];
        try {
            Validate.notEmpty(array, "Array should not be empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String[] notEmptyArray = new String[]{"String1", "String2"};
        Validate.notEmpty(notEmptyArray, "Array should not be empty");
    }

    @Test
    public void testNotEmptyCollection() {
        ArrayList<String> collection = new ArrayList<>();
        try {
            Validate.notEmpty(collection, "Collection should not be empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ArrayList<String> notEmptyCollection = new ArrayList<>(Arrays.asList("String1", "String2"));
        Validate.notEmpty(notEmptyCollection, "Collection should not be empty");
    }

    @Test
    public void testNotEmptyMap() {
        HashMap<String, Integer> map = new HashMap<>();
        try {
            Validate.notEmpty(map, "Map should not be empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        HashMap<String, Integer> notEmptyMap = new HashMap<>(Map.of("String1", 1, "String2", 2));
        Validate.notEmpty(notEmptyMap, "Map should not be empty");
    }

    @Test
    public void testNotBlankCharSequence() {
        String emptyString = "";
        try {
            Validate.notBlank(emptyString, "Character sequence should not be blank");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String notBlankString = "String1";
        Validate.notBlank(notBlankString, "Character sequence should not be blank");
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = new String[2];
        array[0] = null;
        try {
            Validate.noNullElements(array, "Array should not contain null elements");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String[] notNullArray = new String[2];
        notNullArray[0] = "String1";
        notNullArray[1] = "String2";
        Validate.noNullElements(notNullArray, "Array should not contain null elements");
    }

    @Test
    public void testNoNullElementsIterable() {
        List<String> list = Arrays.asList("String1", null, "String2");
        try {
            Validate.noNullElements(list, "Iterable should not contain null elements");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<String> notNullList = Arrays.asList("String1", "String2");
        Validate.noNullElements(notNullList, "Iterable should not contain null elements");
    }

    @Test
    public void testValidIndexArray() {
        String[] array = new String[2];
        try {
            Validate.validIndex(array, 3, "Array index is invalid: %d", 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        String[] array2 = new String[2];
        array2[0] = "String1";
        array2[1] = "String2";
        Validate.validIndex(array2, 0, "Array index is invalid: %d", 0);
    }

    @Test
    public void testValidIndexCollection() {
        ArrayList<String> collection = new ArrayList<>();
        try {
            Validate.validIndex(collection, 3, "Collection index is invalid: %d", 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        ArrayList<String> collection2 = new ArrayList<>(Arrays.asList("String1", "String2"));
        Validate.validIndex(collection2, 0, "Collection index is invalid: %d", 0);
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "String1String2";
        try {
            Validate.validIndex(chars, 3, "Character sequence index is invalid: %d", 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        String chars2 = "String1String2";
        Validate.validIndex(chars2, 0, "Character sequence index is invalid: %d", 0);
    }

    @Test
    public void testValidState() {
        boolean state = false;
        try {
            Validate.validState(state);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        boolean state2 = true;
        Validate.validState(state2);
    }

    @Test
    public void testMatchesPattern() {
        String input = "String123";
        String pattern = "^[A-Za-z0-9]+$";
        try {
            Validate.matchesPattern(input, pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String input2 = "String123";
        String pattern2 = "^[A-Za-z0-9]+$";
        Validate.matchesPattern(input2, pattern2);
    }

    @Test
    public void testInclusiveBetweenLong() {
        long value = 1L;
        long start = 2L;
        long end = 3L;
        try {
            Validate.inclusiveBetween(value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long value2 = 2L;
        Validate.inclusiveBetween(value2, start, end);
    }

    @Test
    public void testInclusiveBetweenLongWithMessage() {
        long value = 1L;
        long start = 2L;
        long end = 3L;
        try {
            Validate.inclusiveBetween(value, start, end, "Value out of range: %d, start: %d, end: %d", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long value2 = 2L;
        Validate.inclusiveBetween(value2, start, end, "Value out of range: %d, start: %d, end: %d", value2, start, end);
    }

    @Test
    public void testInclusiveBetweenDouble() {
        double value = 1.0;
        double start = 2.0;
        double end = 3.0;
        try {
            Validate.inclusiveBetween(value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double value2 = 2.0;
        Validate.inclusiveBetween(value2, start, end);
    }

    @Test
    public void testInclusiveBetweenDoubleWithMessage() {
        double value = 1.0;
        double start = 2.0;
        double end = 3.0;
        try {
            Validate.inclusiveBetween(value, start, end, "Value out of range: %f, start: %f, end: %f", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double value2 = 2.0;
        Validate.inclusiveBetween(value2, start, end, "Value out of range: %f, start: %f, end: %f", value2, start, end);
    }

    @Test
    public void testExclusiveBetweenLong() {
        long value = 1L;
        long start = 2L;
        long end = 3L;
        try {
            Validate.exclusiveBetween(value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long value2 = 2L;
        Validate.exclusiveBetween(value2, start, end);
    }

    @Test
    public void testExclusiveBetweenLongWithMessage() {
        long value = 1L;
        long start = 2L;
        long end = 3L;
        try {
            Validate.exclusiveBetween(value, start, end, "Value out of range: %d, start: %d, end: %d", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long value2 = 2L;
        Validate.exclusiveBetween(value2, start, end, "Value out of range: %d, start: %d, end: %d", value2, start, end);
    }

    @Test
    public void testExclusiveBetweenDouble() {
        double value = 1.0;
        double start = 2.0;
        double end = 3.0;
        try {
            Validate.exclusiveBetween(value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double value2 = 2.0;
        Validate.exclusiveBetween(value2, start, end);
    }

    @Test
    public void testExclusiveBetweenDoubleWithMessage() {
        double value = 1.0;
        double start = 2.0;
        double end = 3.0;
        try {
            Validate.exclusiveBetween(value, start, end, "Value out of range: %f, start: %f, end: %f", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double value2 = 2.0;
        Validate.exclusiveBetween(value2, start, end, "Value out of range: %f, start: %f, end: %f", value2, start, end);
    }

    @Test
    public void testIsInstanceOf() {
        String str = "String1";
        Class<String> stringClass = String.class;
        try {
            Validate.isInstanceOf(String.class, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isInstanceOf(stringClass, str);
    }

    @Test
    public void testIsInstanceOfWithMessage() {
        String str = "String1";
        Class<String> stringClass = String.class;
        try {
            Validate.isInstanceOf(String.class, str, "Expected type: %s, actual: %s", String.class, "Integer");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isInstanceOf(stringClass, str, "Expected type: %s, actual: %s", String.class, String.class);
    }

    @Test
    public void testIsAssignableFrom() {
        Class<? super String> superClass = Object.class;
        Class<String> stringClass = String.class;
        try {
            Validate.isAssignableFrom(String.class, int.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isAssignableFrom(superClass, stringClass);
    }

    @Test
    public void testIsAssignableFromWithMessage() {
        Class<? super String> superClass = Object.class;
        Class<String> stringClass = String.class;
        try {
            Validate.isAssignableFrom(String.class, int.class, "Expected super type: %s, actual type: %s", String.class, int.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isAssignableFrom(superClass, stringClass, "Expected super type: %s, actual type: %s", Object.class, String.class);
    }
}