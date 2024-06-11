import static org.apache.commons.lang3.Validate.*;
import static org.junit.Assert.*;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanExpressionAndMessage() {
        isTrue(true, "The validated expression is true");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpressionAndMessage() {
        isTrue(false, "The validated expression is true");
    }

    @Test
    public void testIsTrueWithBooleanExpression() {
        isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpression() {
        isTrue(false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNotNullWithValidObject() {
        T object = new Object();
        T notNullObject = notNull(object);
        assertEquals(object, notNullObject);
    }

    @Test(expected = NullPointerException.class)
    public <T> void testNotNullWithNullObject() {
        T object = null;
        notNull(object);
    }

    @Test
    public <T> void testNotEmptyArrayWithValidArray() {
        String[] array = {"test1", "test2", "test3"};
        T[] notEmptyArray = notEmpty(array);
        assertEquals(array, notEmptyArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testNotEmptyArrayWithEmptyArray() {
        T[] emptyArray = new T[0];
        notEmpty(emptyArray);
    }

    @Test
    public <T extends Collection<?>> void testNotEmptyCollectionWithValidCollection() {
        List<String> collection = Arrays.asList("test1", "test2", "test3");
        T notEmptyCollection = notEmpty(collection);
        assertEquals(collection, notEmptyCollection);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Collection<?>> void testNotEmptyCollectionWithEmptyCollection() {
        Collection<String> emptyCollection = Collections.emptyList();
        notEmpty(emptyCollection);
    }

    @Test
    public <T extends Map<?, ?>> void testNotEmptyMapWithValidMap() {
        Map<String, String> map = new HashMap<>();
        map.put("test1", "test1Value");
        map.put("test2", "test2Value");
        T notEmptyMap = notEmpty(map);
        assertEquals(map, notEmptyMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Map<?, ?>> void testNotEmptyMapWithEmptyMap() {
        Map<String, String> emptyMap = Collections.emptyMap();
        notEmpty(emptyMap);
    }

    @Test
    public <T extends CharSequence> void testNotEmptyCharSequenceWithValidCharSequence() {
        String chars = "test1test2test3";
        T notEmptyChars = notEmpty(chars);
        assertEquals(chars, notEmptyChars);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends CharSequence> void testNotEmptyCharSequenceWithEmptyCharSequence() {
        String emptyChars = "";
        notEmpty(emptyChars);
    }

    @Test
    public <T> void testNoNullElementsArrayWithValidArray() {
        String[] array = {"test1", "test2", "test3"};
        T[] noNullElementsArray = noNullElements(array);
        assertArrayEquals(array, noNullElementsArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testNoNullElementsArrayWithNullElement() {
        String[] array = {"test1", null, "test3"};
        noNullElements(array);
    }

    @Test
    public <T extends Iterable<?>> void testNoNullElementsIterableWithValidIterable() {
        List<String> iterable = Arrays.asList("test1", "test2", "test3");
        T noNullElementsIterable = noNullElements(iterable);
        assertEquals(iterable, noNullElementsIterable);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Iterable<?>> void testNoNullElementsIterableWithNullElement() {
        List<String> iterable = Arrays.asList("test1", null, "test3");
        noNullElements(iterable);
    }

    @Test
    public <T> void testValidIndexArrayWithValidArrayAndValidIndex() {
        String[] array = {"test1", "test2", "test3"};
        validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public <T> void testValidIndexArrayWithInvalidArrayIndex() {
        String[] array = {"test1", "test2", "test3"};
        validIndex(array, 4);
    }

    @Test
    public <T extends Collection<?>> void testValidIndexCollectionWithValidCollectionAndValidIndex() {
        List<String> collection = Arrays.asList("test1", "test2", "test3");
        validIndex(collection, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public <T extends Collection<?>> void testValidIndexCollectionWithInvalidCollectionIndex() {
        List<String> collection = Arrays.asList("test1", "test2", "test3");
        validIndex(collection, 4);
    }

    @Test
    public <T extends CharSequence> void testValidIndexCharSequenceWithValidCharSequenceAndValidIndex() {
        String chars = "test1test2test3";
        validIndex(chars, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public <T extends CharSequence> void testValidIndexCharSequenceWithInvalidCharSequenceIndex() {
        String chars = "test1test2test3";
        validIndex(chars, 4);
    }

    @Test
    public void testValidStateWithValidState() {
        validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithInvalidState() {
        validState(false);
    }

    @Test
    public void testMatchesPatternWithValidInputAndPattern() {
        matchesPattern("test1test2test3", "test\\d{3}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidInputAndPattern() {
        matchesPattern("test1test2test4", "test\\d{3}");
    }

    @Test
    public void testMatchesPatternWithValidInputAndPatternMessage() {
        matchesPattern("test1test2test3", "test\\d{3}", "The validated character sequence matches the specified pattern");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidInputAndPatternMessage() {
        matchesPattern("test1test2test4", "test\\d{3}", "The validated character sequence matches the specified pattern");
    }

    @Test
    public <T> void testInclusiveBetweenWithValidValueAndStartAndEnd() {
        inclusiveBetween(1, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testInclusiveBetweenWithInvalidValueAndStartAndEnd() {
        inclusiveBetween(1, 3, 0);
    }

    @Test
    public <T> void testInclusiveBetweenWithValidValueAndStartAndEndMessage() {
        inclusiveBetween(1, 3, 2, "The value %s is in the inclusive range of %s to %s");
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testInclusiveBetweenWithInvalidValueAndStartAndEndMessage() {
        inclusiveBetween(1, 3, 0, "The value %s is in the inclusive range of %s to %s");
    }

    @Test
    public void testExclusiveBetweenWithValidValueAndStartAndEnd() {
        exclusiveBetween(1, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidValueAndStartAndEnd() {
        exclusiveBetween(1, 3, 2); // This should not throw an exception since the method signature is different for exclusiveBetween with numbers
        exclusiveBetween(1, 3, 1);
    }

    @Test
    public void testExclusiveBetweenWithValidValueAndStartAndEndMessage() {
        exclusiveBetween(1, 3, 2, "The value %s is not in the exclusive range of %s to %s");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidValueAndStartAndEndMessage() {
        exclusiveBetween(1, 3, 1, "The value %s is not in the exclusive range of %s to %s");
    }

    @Test
    public void testIsInstanceOfWithValidTypeAndObject() {
        isInstanceOf(String.class, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithInvalidTypeAndObject() {
        isInstanceOf(Integer.class, "test");
    }

    @Test
    public void testIsInstanceOfWithValidTypeAndObjectMessage() {
        isInstanceOf(String.class, "test", "Expected type: String, actual: %s");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithInvalidTypeAndObjectMessage() {
        isInstanceOf(Integer.class, "test", "Expected type: Integer, actual: %s");
    }

    @Test
    public void testIsAssignableFromWithValidSuperTypeAndType() {
        isAssignableFrom(Number.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidSuperTypeAndType() {
        isAssignableFrom(String.class, Integer.class);
    }

    @Test
    public void testIsAssignableFromWithValidSuperTypeAndTypeMessage() {
        isAssignableFrom(Number.class, Integer.class, "Cannot assign a %s to a %s");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidSuperTypeAndTypeMessage() {
        isAssignableFrom(String.class, Integer.class, "Cannot assign a %s to a %s");
    }
}