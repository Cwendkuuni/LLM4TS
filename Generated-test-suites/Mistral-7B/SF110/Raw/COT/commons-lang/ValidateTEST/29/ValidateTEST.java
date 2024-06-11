import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValidateTest {

    @Test
    public void testIsTrueWithMessageAndLong() {
        try {
            Validate.isTrue(false, "%s is not true", 1L);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("1 is not true"));
        }
    }

    @Test
    public void testIsTrueWithMessageAndDouble() {
        try {
            Validate.isTrue(false, "%s is not true", 1.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("1.0 is not true"));
        }
    }

    @Test
    public void testIsTrueWithMessageAndObjects() {
        try {
            Validate.isTrue(false, "%s is not true", "hello", "world");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("hello is not true world"));
        }
    }

    @Test
    public void testIsTrueWithoutMessageAndBoolean() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithoutMessageAndFalse() {
        Validate.isTrue(false);
    }

    @Test
    public void testNotNullWithMessageAndObject() {
        Validate.notNull(null, "%s is null");
        try {
            Validate.notNull(null, "%s is null", "hello");
            fail();
        } catch (NullPointerException e) {
            //expected
        }
    }

    @Test
    public void testNotNullWithObject() {
        Validate.notNull(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNull() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyArrayWithMessageAndObjects() {
        try {
            Validate.notEmpty((Object[]) null, "%s is null or empty");
            fail();
        } catch (NullPointerException e) {
            //expected
        }

        try {
            Validate.notEmpty(new Object[0], "%s is empty");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("is empty"));
        }

        Object[] array = new Object[1];
        array[0] = new Object();
        Validate.notEmpty(array);
    }

    @Test
    public void testNotEmptyArrayWithObjects() {
        Validate.notEmpty(new Object[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithNullElement() {
        Object[] array = new Object[1];
        array[0] = null;
        Validate.notEmpty(array);
    }

    @Test
    public void testNotEmptyCollectionWithMessageAndObjects() {
        try {
            Validate.notEmpty(Collections.emptyList(), "%s is null or empty");
            fail();
        } catch (NullPointerException e) {
            //expected
        }

        try {
            Validate.notEmpty(Collections.emptyList(), "%s is empty");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("is empty"));
        }

        List<Object> list = Arrays.asList(new Object[1]);
        Validate.notEmpty(list);
    }

    @Test
    public void testNotEmptyCollectionWithObjects() {
        Validate.notEmpty(Arrays.asList(new Object[1]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithNullElement() {
        List<Object> list = Arrays.asList(null);
        Validate.notEmpty(list);
    }

    @Test
    public void testNotEmptyMapWithMessageAndObjects() {
        Map<String, Object> map = new HashMap<>();
        try {
            Validate.notEmpty(map);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("The validated map is empty"));
        }

        map.put("key1", new Object());
        Validate.notEmpty(map);
    }

    @Test
    public void testNotEmptyMapWithObjects() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", new Object());
        Validate.notEmpty(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithEmptyMap() {
        Map<String, Object> map = new HashMap<>();
        Validate.notEmpty(map);
    }

    @Test
    public void testNotEmptyCharSequenceWithMessageAndObjects() {
        CharSequence chars = "";
        try {
            Validate.notEmpty(chars, "%s is null or empty");
            fail();
        } catch (NullPointerException e) {
            //expected
        }

        try {
            Validate.notEmpty(chars, "%s is empty");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("is empty"));
        }

        CharSequence chars1 = "hello";
        Validate.notEmpty(chars1);
    }

    @Test
    public void testNotEmptyCharSequenceWithObjects() {
        CharSequence chars = "hello";
        Validate.notEmpty(chars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCharSequenceWithEmptyCharSequence() {
        CharSequence chars = "";
        Validate.notEmpty(chars);
    }

    @Test
    public void testNoNullElementsWithMessageAndObjects() {
        Object[] array = new Object[1];
        array[0] = null;
        try {
            Validate.noNullElements(array, "%s contains a null element at index: %d");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("contains a null element at index: 0"));
        }

        Object[] array1 = new Object[1];
        array1[0] = new Object();
        Validate.noNullElements(array1);
    }

    @Test
    public void testNoNullElementsWithObjects() {
        Object[] array = new Object[1];
        array[0] = new Object();
        Validate.noNullElements(array);
    }

    @Test
    public void testNoNullElementsWithCollection() {
        List<Object> list = Arrays.asList(new Object[1]);
        Validate.noNullElements(list);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalse() {
        Validate.validState(false);
    }

    @Test
    public void testValidStateWithMessageAndObjects() {
        Validate.validState(true, "state is false");
    }

    @Test
    public void testMatchesPatternWithCharSequenceAndPattern() {
        String input = "abcdef";
        String pattern = "[a-z]+";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithCharSequenceAndInvalidPattern() {
        String input = "abcdef";
        String pattern = "[x-z]+";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithCharSequenceAndMessage() {
        String input = "abcdef";
        String pattern = "[x-z]+";
        Validate.matchesPattern(input, pattern, "The string %s does not match the pattern %s", input, pattern);
    }

    @Test
    public void testInclusiveBetweenWithComparableAndValues() {
        Integer value = 2;
        Integer start = 1;
        Integer end = 4;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithComparableAndValuesOutOfRange() {
        Integer value = 2;
        Integer start = 5;
        Integer end = 4;
        try {
            Validate.inclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("The value 2 is not in the specified inclusive range of 5 to 4"));
        }
    }

    @Test
    public void testInclusiveBetweenWithLongAndValues() {
        long value = 2;
        long start = 1;
        long end = 4;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongAndValuesOutOfRange() {
        long value = 2;
        long start = 5;
        long end = 4;
        try {
            Validate.inclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("The value 2 is not in the specified inclusive range of 5 to 4"));
        }
    }

    @Test
    public void testInclusiveBetweenWithDoubleAndValues() {
        double value = 2.5;
        double start = 1.5;
        double end = 4.5;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleAndValuesOutOfRange() {
        double value = 2.5;
        double start = 5.5;
        double end = 4.5;
        try {
            Validate.inclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("The value 2.5 is not in the specified inclusive range of 5.5 to 4.5"));
        }
    }

    @Test
    public void testExclusiveBetweenWithComparableAndValues() {
        Integer value = 2;
        Integer start = 1;
        Integer end = 4;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithComparableAndValuesOutOfRange() {
        Integer value = 2;
        Integer start = 5;
        Integer end = 4;
        try {
            Validate.exclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("The value 2 is not in the specified exclusive range of 5 to 4"));
        }
    }

    @Test
    public void testExclusiveBetweenWithLongAndValues() {
        long value = 2;
        long start = 1;
        long end = 4;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongAndValuesOutOfRange() {
        long value = 2;
        long start = 5;
        long end = 4;
        try {
            Validate.exclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("The value 2 is not in the specified exclusive range of 5 to 4"));
        }
    }

    @Test
    public void testExclusiveBetweenWithDoubleAndValues() {
        double value = 2.5;
        double start = 1.5;
        double end = 4.5;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleAndValuesOutOfRange() {
        double value = 2.5;
        double start = 5.5;
        double end = 4.5;
        try {
            Validate.exclusiveBetween(start, end, value);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("The value 2.5 is not in the specified exclusive range of 5.5 to 4.5"));
        }
    }

    @Test
    public void testIsInstanceOfWithClassAndObject() {
        Validate.isInstanceOf(String.class, "hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithClassAndInvalidObject() {
        Validate.isInstanceOf(String.class, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithMessageAndClassAndObject() {
        Validate.isInstanceOf(String.class, "hello", "Expected type: java.lang.String, actual: java.lang.Integer");
    }

    @Test
    public void testIsAssignableFromWithClasses() {
        Validate.isAssignableFrom(String.class, Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidClasses() {
        Validate.isAssignableFrom(String.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithMessageAndClasses() {
        Validate.isAssignableFrom(String.class, Integer.class, "Cannot assign a java.lang.String to a java.lang.Integer");
    }
}