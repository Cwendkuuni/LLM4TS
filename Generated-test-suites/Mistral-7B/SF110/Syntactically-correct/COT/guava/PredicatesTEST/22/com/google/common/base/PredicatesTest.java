import static org.junit.Assert.*;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {
    

    // Test for alwaysTrue() method
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(10));
    }

    // Test for alwaysFalse() method
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(10));
    }

    // Test for isNull() method
    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertNull(isNull.apply(null)); // Test with an Object
        assertNull(isNull.apply((Object)123));
        assertFalse(isNull.apply(new String("Test")));
    }

    // Test for notNull() method
    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNotNull(notNull.apply(null)); // Should throw NullPointerException, not tested here
        assertNotNull(notNull.apply(new Object()));
        assertNotNull(notNull.apply("Test String"));
    }

    // Test for not() method
    @Test
    public void testNot() {
        Predicate<Boolean> isTrue = Predicates.alwaysTrue();
        Predicate<Boolean> isFalse = Predicates.alwaysFalse();

        Predicate<Boolean> notTrue = Predicates.not(isTrue);
        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));

        Predicate<Boolean> notFalse = Predicates.not(isFalse);
        assertTrue(notFalse.apply(true));
        assertFalse(notFalse.apply(false));
    }

    // Test for and() method with Iterable
    @Test
    public void testAndWithIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull());

        Predicate<Integer> andPredicate = Predicates.and(Predicates.asIterable(isEven, isPositive));

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(null));
    }

    // Test for and() method with Predicate array
    @Test
    public void testAndWithPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull());

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(null));
    }

    // Test for and() method with two Predicates
    @Test
    public void testAndWithTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull());

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(null));
    }

    // Test for or() method with Iterable
    @Test
    public void testOrWithIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(Predicates.asIterable(isEven, isOdd));

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(null));
    }

    // Test for or() method with Predicate array
    @Test
    public void testOrWithPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(null));
    }

    // Test for or() method with two Predicates
    @Test
    public void testOrWithTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(null));
    }

    // Test for equalTo() method
    @Test
    public void testEqualTo() {
        Predicate<String> equalToNull = Predicates.equalTo(null);
        assertTrue(equalToNull.apply(null));
        assertFalse(equalToNull.apply("Test"));

        Predicate<String> equalToTest = Predicates.equalTo("Test");
        assertTrue(equalToTest.apply("Test"));
        assertFalse(equalToTest.apply("AnotherTest"));
    }

    // Test for instanceOf() method
    @Test
    public void testInstanceOf() {
        Predicate<String> isString = Predicates.instanceOf(String.class);
        assertTrue(isString.apply("Test"));
        assertFalse(isString.apply(123));

        Predicate<Number> isInt = Predicates.instanceOf(int.class);
        assertFalse(isInt.apply("Test"));
        assertTrue(isInt.apply(123));
    }

    // Test for assignableFrom() method
    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assertTrue(assignableFromString.apply(String.class));
        assertTrue(assignableFromString.apply(StringBuilder.class));
        assertFalse(assignableFromString.apply(Integer.class));

        Predicate<Class<?>> assignableFromNumber = Predicates.assignableFrom(Number.class);
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Double.class));
        assertFalse(assignableFromNumber.apply(String.class));
    }

    // Test for in() method
    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> inPredicate = Predicates.in(numbers);

        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertTrue(inPredicate.apply(4));
        assertTrue(inPredicate.apply(5));
        assertFalse(inPredicate.apply(6));
    }

    // Test for compose() method
    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * input;
            }
        };

        Predicate<Integer> composedPredicate = Predicates.compose(isEven, square);

        assertTrue(composedPredicate.apply(4));
        assertFalse(composedPredicate.apply(2));
        assertFalse(composedPredicate.apply(1));
    }

    // Test for containsPattern() method
    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("Test.*");

        assertTrue(containsPattern.apply("Test"));
        assertTrue(containsPattern.apply("Testing"));
        assertFalse(containsPattern.apply("TestFail"));
    }

    // Test for containsPattern() method with Pattern
    @Test
    public void testContainsPatternWithPattern() {
        Pattern pattern = Pattern.compile("Test.*");
        Predicate<String> containsPattern = Predicates.contains(pattern);

        assertTrue(containsPattern.apply("Test"));
        assertTrue(containsPattern.apply("Testing"));
        assertFalse(containsPattern.apply("TestFail"));
    }

    // Test for edge cases and exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testDefensiveCopyNullIterable() {
        Predicates.defensiveCopy(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDefensiveCopyNullElement() {
        List<String> list = Collections.singletonList(null);
        Predicates.defensiveCopy(list);
    }

    @Test(expected = NullPointerException.class)
    public void testDefensiveCopyNullArray() {
        Predicates.defensiveCopy(new Object[0], (Predicate<? super Object>[])null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefensiveCopyNullArrayComponent() {
        Object[] array = new Object[]{null};
        Predicates.defensiveCopy(Arrays.asList(array), (Predicate<? super Object>[])null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefensiveCopyNullPredicate() {
        Predicate<? super Object> nullPredicate = null;
        Predicates.and((List)Arrays.asList(Predicates.isNull(), nullPredicate));
    }
}
