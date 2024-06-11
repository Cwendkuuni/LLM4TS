import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.fail;

public class ValidateTest {

    @Test
    public void testIsTrueWithMessageAndLong() {
        Validate.isTrue(true, "%s is false", 1L);
    }

    @Test
    public void testIsTrueWithMessageAndDouble() {
        Validate.isTrue(true, "%s is false", 1.0);
    }

    @Test
    public void testIsTrueWithMessageAndValues() {
        Validate.isTrue(true, "Error message: %s %s %s", "apple", "is", "red");
    }

    @Test
    public void testIsTrueWithoutMessageAndFalse() {
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNotNull() {
        Validate.notNull(new Object());
    }

    @Test
    public void testNotNullWithMessageAndValues() {
        try {
            Validate.notNull(null, "The validated object is null", new Object[0]);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testNotEmptyArray() {
        String[] arr = new String[0];
        try {
            Validate.notEmpty(arr, "The validated array is empty", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNotEmptyArrayWithValues() {
        String[] arr = {"apple", "banana", "cherry"};
        Validate.notEmpty(arr, "The validated array is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyCollection() {
        ArrayList<String> coll = new ArrayList<>();
        try {
            Validate.notEmpty(coll, "The validated collection is empty", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNotEmptyCollectionWithValues() {
        ArrayList<String> coll = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        Validate.notEmpty(coll, "The validated collection is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyMap() {
        HashMap<String, String> map = new HashMap<>();
        try {
            Validate.notEmpty(map, "The validated map is empty", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNotEmptyMapWithValues() {
        HashMap<String, String> map = new HashMap<>(Map.of("apple", "red", "banana", "yellow"));
        Validate.notEmpty(map, "The validated map is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String chars = "";
        try {
            Validate.notEmpty(chars, "The validated character sequence is empty", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNotEmptyCharSequenceWithValues() {
        String chars = "apple banana cherry";
        Validate.notEmpty(chars, "The validated character sequence is empty", new Object[0]);
    }

    @Test
    public void testNotBlankCharSequence() {
        String chars = "";
        try {
            Validate.notBlank(chars, "The validated character sequence is blank", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNotBlankCharSequenceWithValues() {
        String chars = "   apple   ";
        Validate.notBlank(chars, "The validated character sequence is blank", new Object[0]);
    }

    @Test
    public void testNoNullElementsArray() {
        String[] arr = new String[2];
        arr[0] = null;
        try {
            Validate.noNullElements(arr, "The validated array contains null element at index: %d", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNoNullElementsArrayWithValues() {
        String[] arr = new String[2];
        arr[0] = "apple";
        arr[1] = null;
        try {
            Validate.noNullElements(arr, "The validated array contains null element at index: %d", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNoNullElementsCollection() {
        List<String> coll = new ArrayList<>();
        coll.add("apple");
        coll.add(null);
        try {
            Validate.noNullElements(coll, "The validated collection contains null element at index: %d", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNoNullElementsCollectionWithValues() {
        List<String> coll = new ArrayList<>(Arrays.asList("apple", "banana", null));
        try {
            Validate.noNullElements(coll, "The validated collection contains null element at index: %d", new Object[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testValidIndexArray() {
        String[] arr = {"apple", "banana", "cherry"};
        try {
            Validate.validIndex(arr, 3, "The validated array index is invalid: %d", new Object[0]);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testValidIndexArrayWithValues() {
        String[] arr = {"apple", "banana", "cherry"};
        Validate.validIndex(arr, 1, "The validated array index is invalid: %d", new Object[0]);
    }

    @Test
    public void testValidIndexCollection() {
        List<String> coll = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        try {
            Validate.validIndex(coll, 3, "The validated collection index is invalid: %d", new Object[0]);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testValidIndexCollectionWithValues() {
        List<String> coll = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        Validate.validIndex(coll, 1, "The validated collection index is invalid: %d", new Object[0]);
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "apple banana cherry";
        try {
            Validate.validIndex(chars, 3, "The validated character sequence index is invalid: %d", new Object[0]);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testValidIndexCharSequenceWithValues() {
        String chars = "apple banana cherry";
        Validate.validIndex(chars, 1, "The validated character sequence index is invalid: %d", new Object[0]);
    }

    @Test
    public void testValidState() {
        try {
            Validate.validState(false);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testValidStateWithMessage() {
        try {
            Validate.validState(false, "The validated state is false");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testMatchesPattern() {
        String input = "apple";
        String pattern = "\\w+";
        try {
            Validate.matchesPattern(input, pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testMatchesPatternWithMessage() {
        String input = "apple";
        String pattern = "\\d+";
        try {
            Validate.matchesPattern(input, pattern, "The input string does not match the pattern: %s", input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testInclusiveBetweenLong() {
        long start = 1;
        long end = 3;
        long value = 2;
        try {
            Validate.inclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongFail() {
        long start = 1;
        long end = 3;
        long value = 4;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test
    public void testInclusiveBetweenLongWithMessage() {
        long start = 1;
        long end = 3;
        long value = 4;
        try {
            Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testInclusiveBetweenDouble() {
        double start = 1.5;
        double end = 3.5;
        double value = 2.5;
        try {
            Validate.inclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleFail() {
        double start = 1.5;
        double end = 3.5;
        double value = 4.5;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test
    public void testInclusiveBetweenDoubleWithMessage() {
        double start = 1.5;
        double end = 3.5;
        double value = 4.5;
        try {
            Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testExclusiveBetweenLong() {
        long start = 1;
        long end = 3;
        long value = 2;
        try {
            Validate.exclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongFail() {
        long start = 1;
        long end = 3;
        long value = 2;
        Validate.exclusiveBetween(start, end, value);
        fail("Expected no exception");
    }

    @Test
    public void testExclusiveBetweenLongWithMessage() {
        long start = 1;
        long end = 3;
        long value = 2;
        try {
            Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testExclusiveBetweenDouble() {
        double start = 1.5;
        double end = 3.5;
        double value = 2.5;
        try {
            Validate.exclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleFail() {
        double start = 1.5;
        double end = 3.5;
        double value = 2.5;
        Validate.exclusiveBetween(start, end, value);
        fail("Expected no exception");
    }

    @Test
    public void testExclusiveBetweenDoubleWithMessage() {
        double start = 1.5;
        double end = 3.5;
        double value = 2.5;
        try {
            Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsInstanceOf() {
        Class<String> type = String.class;
        Object obj = new Object();
        try {
            Validate.isAssignableFrom(String.class, obj);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsAssignableFrom() {
        Class<String> superType = String[].class;
        Class<Integer[]> type = Integer[].class;
        try {
            Validate.isAssignableFrom(superType, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsAssignableFromWithMessage() {
        Class<String> superType = String[].class;
        Class<Integer[]> type = Integer[].class;
        try {
            Validate.isAssignableFrom(superType, type, "Cannot assign a %s to a %s", superType.getName(), type.getName());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}