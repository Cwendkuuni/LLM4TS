import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithExpressionAndValues() {
        Validate.isTrue(true, "%s is false", "expected value");
        Validate.isTrue(false, "%s is false", "expected value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseAndMessage() {
        Validate.isTrue(false, "The validated expression is false");
    }

    @Test
    public void testIsTrueWithExpression() {
        Validate.isTrue(true);
    }

    @Test
    public void testNotNullWithObject() {
        Validate.notNull(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyArray() {
        String[] array = new String[]{"test1", "test2"};
        Validate.notEmpty(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArray() {
        String[] array = new String[0];
        Validate.notEmpty(array);
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        Validate.notEmpty(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithEmptyCollection() {
        List<String> list = new ArrayList<>();
        Validate.notEmpty(list);
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        Validate.notEmpty(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithEmptyMap() {
        Map<String, String> map = new HashMap<>();
        Validate.notEmpty(map);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String chars = "test";
        Validate.notEmpty(chars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCharSequenceWithEmptyChars() {
        String chars = "";
        Validate.notEmpty(chars);
    }

    @Test
    public void testNotBlankCharSequence() {
        String chars = "test";
        Validate.notBlank(chars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankCharSequenceWithBlankChars() {
        String chars = "";
        Validate.notBlank(chars);
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = new String[]{"test1", "test2"};
        Validate.noNullElements(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithNullElement() {
        String[] array = new String[]{"test1", null, "test3"};
        Validate.noNullElements(array);
    }

    @Test
    public void testNoNullElementsIterable() {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        Validate.noNullElements(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsIterableWithNullElement() {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add(null);
        Validate.noNullElements(list);
    }

    @Test
    public void testValidIndexArray() {
        String[] array = new String[]{"test1", "test2"};
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithInvalidIndex() {
        String[] array = new String[]{"test1", "test2"};
        Validate.validIndex(array, 2);
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        Validate.validIndex(list, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithInvalidIndex() {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        Validate.validIndex(list, 2);
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "test1test2";
        Validate.validIndex(chars, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithInvalidIndex() {
        String chars = "test1test2";
        Validate.validIndex(chars, 2);
    }

    @Test
    public void testValidState() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalse() {
        Validate.validState(false);
    }

    @Test
    public void testMatchesPattern() {
        String input = "test123";
        String pattern = "\\d{3}";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidInput() {
        String input = "test12";
        String pattern = "\\d{3}";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidPattern() {
        String input = "test123";
        String pattern = "\\d{4}";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testInclusiveBetweenWithComparable() {
        Integer value = 10;
        Validate.inclusiveBetween(5, 15, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithComparableOutOfRange() {
        Integer value = 15;
        Validate.inclusiveBetween(5, 15, value);
    }

    @Test
    public void testInclusiveBetweenWithLong() {
        long value = 10L;
        Validate.inclusiveBetween(5L, 15L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongOutOfRange() {
        long value = 15L;
        Validate.inclusiveBetween(5L, 15L, value);
    }

    @Test
    public void testInclusiveBetweenWithDouble() {
        double value = 10.5;
        Validate.inclusiveBetween(5.0, 15.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleOutOfRange() {
        double value = 15.0;
        Validate.inclusiveBetween(5.0, 15.0, value);
    }

    @Test
    public void testExclusiveBetweenWithComparable() {
        Integer value = 10;
        Validate.exclusiveBetween(4, 14, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithComparableEqual() {
        Integer value = 14;
        Validate.exclusiveBetween(4, 14, value);
    }

    @Test
    public void testExclusiveBetweenWithLong() {
        long value = 10L;
        Validate.exclusiveBetween(4L, 14L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongEqual() {
        long value = 14L;
        Validate.exclusiveBetween(4L, 14L, value);
    }

    @Test
    public void testExclusiveBetweenWithDouble() {
        double value = 10.5;
        Validate.exclusiveBetween(4.0, 14.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleEqual() {
        double value = 14.0;
        Validate.exclusiveBetween(4.0, 14.0, value);
    }

    @Test
    public void testIsInstanceOf() {
        String str = "test";
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithInvalidType() {
        String str = "test";
        Validate.isInstanceOf(Integer.class, str);
    }

    @Test
    public void testIsAssignableFrom() {
        String str = "test";
        Validate.isAssignableFrom(String.class, StringBuffer.class);
        Validate.isAssignableFrom(StringBuffer.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidTypes() {
        String str = "test";
        Validate.isAssignableFrom(String.class, Integer.class);
    }
}