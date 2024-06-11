import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {
    

    // Test isTrue methods

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueBooleanExpressionFalseLongValue() {
        Validate.isTrue(false, "%s", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueBooleanExpressionFalseDoubleValue() {
        Validate.isTrue(false, "%s", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueBooleanExpressionFalseArrayValues() {
        Validate.isTrue(false, "%s", new Object[]{"error", "message"});
    }

    @Test
    public void testIsTrueBooleanExpressionTrue() {
        Validate.isTrue(true);
    }

    // Test notNull methods

    @Test(expected = NullPointerException.class)
    public void testNotNullNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotNullNotNullObject() {
        Object obj = new Object();
        Validate.notNull(obj);
    }

    // Test notEmpty methods for arrays

    @Test(expected = NullPointerException.class)
    public void testNotEmptyNullArray() {
        Validate.notEmpty((T[]) null, "The validated array is empty", new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyArray() {
        String[] array = new String[0];
        Validate.notEmpty(array, "The validated array is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyNonEmptyArray() {
        String[] array = {"apple", "banana", "cherry"};
        Validate.notEmpty(array);
    }

    // Test notEmpty methods for collections

    @Test(expected = NullPointerException.class)
    public void testNotEmptyNullCollection() {
        Validate.notEmpty((T) null, "The validated collection is empty", new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyCollection() {
        ArrayList<String> collection = new ArrayList<>();
        Validate.notEmpty(collection, "The validated collection is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyNonEmptyCollection() {
        ArrayList<String> collection = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        Validate.notEmpty(collection);
    }

    // Test notEmpty methods for maps

    @Test(expected = NullPointerException.class)
    public void testNotEmptyNullMap() {
        Validate.notEmpty((T) null, "The validated map is empty", new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyMap() {
        HashMap<String, String> map = new HashMap<>();
        Validate.notEmpty(map, "The validated map is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyNonEmptyMap() {
        HashMap<String, String> map = new HashMap<>(Arrays.asList(
                Map.entry("apple", "red"),
                Map.entry("banana", "yellow"),
                Map.entry("cherry", "red")
        ));
        Validate.notEmpty(map);
    }

    // Test notEmpty methods for CharSequences

    @Test(expected = NullPointerException.class)
    public void testNotEmptyNullCharSequence() {
        Validate.notEmpty((T) null, "The validated character sequence is empty", new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyEmptyCharSequence() {
        String chars = "";
        Validate.notEmpty(chars, "The validated character sequence is empty", new Object[0]);
    }

    @Test
    public void testNotEmptyNonEmptyCharSequence() {
        String chars = "hello world";
        Validate.notEmpty(chars);
    }

    // Test notBlank methods for CharSequences

    @Test(expected = NullPointerException.class)
    public void testNotBlankNullCharSequence() {
        Validate.notBlank((T) null, "The validated character sequence is blank", new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankEmptyCharSequence() {
        String chars = "";
        Validate.notBlank(chars, "The validated character sequence is blank", new Object[0]);
    }

    @Test
    public void testNotBlankNonBlankCharSequence() {
        String chars = "hello world";
        Validate.notBlank(chars);
    }

    // Test noNullElements methods for arrays

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithNullElement() {
        String[] array = {"apple", null, "cherry"};
        Validate.noNullElements(array, "The validated array contains null element at index: %d", new Object[0]);
    }

    @Test
    public void testNoNullElementsArrayWithoutNullElement() {
        String[] array = {"apple", "banana", "cherry"};
        Validate.noNullElements(array);
    }

    // Test noNullElements methods for iterable collections

    @Test
    public void testNoNullElementsIterableCollectionWithoutNullElement() {
        List<String> collection = Arrays.asList("apple", "banana", "cherry");
        Validate.noNullElements(collection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsIterableCollectionWithNullElement() {
        List<String> collection = Arrays.asList("apple", null, "cherry");
        Validate.noNullElements(collection, "The validated collection contains null element at index: %d", new Object[0]);
    }

    // Test validIndex methods for arrays

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayNegativeIndex() {
        String[] array = {"apple", "banana", "cherry"};
        Validate.validIndex(array, -1, "The validated array index is invalid: %d", new Object[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayIndexBiggerThanArraySize() {
        String[] array = {"apple", "banana", "cherry"};
        Validate.validIndex(array, 3, "The validated array index is invalid: %d", new Object[0]);
    }

    @Test
    public void testValidIndexArrayValidIndex() {
        String[] array = {"apple", "banana", "cherry"};
        Validate.validIndex(array, 1);
    }

    // Test validIndex methods for collections

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionNegativeIndex() {
        List<String> collection = Arrays.asList("apple", "banana", "cherry");
        Validate.validIndex(collection, -1, "The validated collection index is invalid: %d", new Object[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionIndexBiggerThanCollectionSize() {
        List<String> collection = Arrays.asList("apple", "banana", "cherry");
        Validate.validIndex(collection, 3, "The validated collection index is invalid: %d", new Object[0]);
    }

    @Test
    public void testValidIndexCollectionValidIndex() {
        List<String> collection = Arrays.asList("apple", "banana", "cherry");
        Validate.validIndex(collection, 1);
    }

    // Test validIndex methods for CharSequences

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceNegativeIndex() {
        String chars = "hello world";
        Validate.validIndex(chars, -1, "The validated character sequence index is invalid: %d", new Object[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceIndexBiggerThanCharSequenceSize() {
        String chars = "hello world";
        Validate.validIndex(chars, 11, "The validated character sequence index is invalid: %d", new Object[0]);
    }

    @Test
    public void testValidIndexCharSequenceValidIndex() {
        String chars = "hello world";
        Validate.validIndex(chars, 5);
    }

    // Test validState methods

    @Test(expected = IllegalStateException.class)
    public void testValidStateFalse() {
        Validate.validState(false);
    }

    // Test validState methods with message

    @Test(expected = IllegalStateException.class)
    public void testValidStateFalseWithMessage() {
        Validate.validState(false, "The validated state is false");
    }

    // Test matchesPattern methods

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternInvalidPatternAndInput() {
        Validate.matchesPattern("hello", "[^h]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternInvalidPatternAndInputWithMessage() {
        Validate.matchesPattern("hello", "[^h]", "The string %s does not match the pattern %s", "hello", "[^h]");
    }

    @Test
    public void testMatchesPatternValidPatternAndInput() {
        Validate.matchesPattern("hello", "h.llo");
    }

    // Test inclusiveBetween methods for Comparable types

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenInvalidValueForStartAndEnd() {
        Integer value = 5;
        Validate.inclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenInvalidValueForStartAndEndWithMessage() {
        Integer value = 5;
        Validate.inclusiveBetween(1, 3, value, "The value %s is not in the specified inclusive range of %s to %s", value, 1, 3);
    }

    @Test
    public void testInclusiveBetweenValidValueForStartAndEnd() {
        Integer value = 3;
        Validate.inclusiveBetween(1, 3, value);
    }

    // Test inclusiveBetween methods for long type

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenInvalidValueForStartAndEndLong() {
        long value = 5L;
        Validate.inclusiveBetween(1L, 3L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenInvalidValueForStartAndEndLongWithMessage() {
        long value = 5L;
        Validate.inclusiveBetween(1L, 3L, value, "The value %s is not in the specified inclusive range of %s to %s", value, 1L, 3L);
    }

    @Test
    public void testInclusiveBetweenValidValueForStartAndEndLong() {
        long value = 3L;
        Validate.inclusiveBetween(1L, 3L, value);
    }

    // Test inclusiveBetween methods for double type

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenInvalidValueForStartAndEndDouble() {
        double value = 5.5;
        Validate.inclusiveBetween(1.0, 3.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenInvalidValueForStartAndEndDoubleWithMessage() {
        double value = 5.5;
        Validate.inclusiveBetween(1.0, 3.0, value, "The value %s is not in the specified inclusive range of %s to %s", value, 1.0, 3.0);
    }

    @Test
    public void testInclusiveBetweenValidValueForStartAndEndDouble() {
        double value = 3.0;
        Validate.inclusiveBetween(1.0, 3.0, value);
    }

    // Test exclusiveBetween methods for Comparable types

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenInvalidValueForStartAndEnd() {
        Integer value = 5;
        Validate.exclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenInvalidValueForStartAndEndWithMessage() {
        Integer value = 5;
        Validate.exclusiveBetween(1, 3, value, "The value %s is not in the specified exclusive range of %s to %s", value, 1, 3);
    }

    @Test
    public void testExclusiveBetweenValidValueForStartAndEnd() {
        Integer value = 2;
        Validate.exclusiveBetween(1, 3, value);
    }

    // Test exclusiveBetween methods for long type

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenInvalidValueForStartAndEndLong() {
        long value = 5L;
        Validate.exclusiveBetween(1L, 3L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenInvalidValueForStartAndEndLongWithMessage() {
        long value = 5L;
        Validate.exclusiveBetween(1L, 3L, value, "The value %s is not in the specified exclusive range of %s to %s", value, 1L, 3L);
    }

    @Test
    public void testExclusiveBetweenValidValueForStartAndEndLong() {
        long value = 2L;
        Validate.exclusiveBetween(1L, 3L, value);
    }

    // Test exclusiveBetween methods for double type

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenInvalidValueForStartAndEndDouble() {
        double value = 5.5;
        Validate.exclusiveBetween(1.0, 3.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenInvalidValueForStartAndEndDoubleWithMessage() {
        double value = 5.5;
        Validate.exclusiveBetween(1.0, 3.0, value, "The value %s is not in the specified exclusive range of %s to %s", value, 1.0, 3.0);
    }

    @Test
    public void testExclusiveBetweenValidValueForStartAndEndDouble() {
        double value = 2.5;
        Validate.exclusiveBetween(1.0, 3.0, value);
    }

    // Test isInstanceOf methods

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfInvalidTypes() {
        Validate.isInstanceOf(String.class, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfInvalidTypesWithMessage() {
        Validate.isInstanceOf(String.class, new Object(), "Expected type: %s, actual: %s", "String", new Object().getClass().getName());
    }

    @Test
    public void testIsInstanceOfValidTypes() {
        Validate.isInstanceOf(String.class, "test");
    }

    // Test isAssignableFrom methods

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromInvalidTypes() {
        Validate.isAssignableFrom(String.class.getClass(), Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromInvalidTypesWithMessage() {
        Validate.isAssignableFrom(String.class.getClass(), Integer.class, "Cannot assign a %s to a %s", "Class<String>", "Class<Integer>");
    }

    @Test
    public void testIsAssignableFromValidTypes() {
        Validate.isAssignableFrom(Number.class, Integer.class);
    }

    
}