import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {

    

    // Test alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(0));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
    }

    // Test alwaysFalse()
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(0));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
    }

    // Test isNull()
    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(0));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2));
        assertTrue(isNull.apply(null));
    }

    // Test notNull()
    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(0));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2));
    }

    // Test not()
    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();

        Predicate<Boolean> notTrue = Predicates.not(truePredicate);
        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));

        Predicate<Boolean> notFalse = Predicates.not(falsePredicate);
        assertTrue(notFalse.apply(true));
        assertFalse(notFalse.apply(false));
    }

    // Test and() with Iterable
    @Test
    public void testAndWithIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isGreaterThanZero = Predicates.notNull();

        Predicate<Integer> and = Predicates.and(isEven, isGreaterThanZero);

        assertTrue(and.apply(2));
        assertTrue(and.apply(4));
        assertFalse(and.apply(1));
        assertFalse(and.apply(null));
    }

    // Test and() with varargs
    @Test
    public void testAndWithVarargs() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isGreaterThanZero = Predicates.notNull();

        Predicate<Integer> and = Predicates.and(isEven, isGreaterThanZero);

        assertTrue(and.apply(2));
        assertTrue(and.apply(4));
        assertFalse(and.apply(1));
        assertFalse(and.apply(null));
    }

    // Test and() with two Predicates
    @Test
    public void testAndWithTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isGreaterThanZero = Predicates.notNull();

        Predicate<Integer> and = Predicates.and(isEven, isGreaterThanZero);

        assertTrue(and.apply(2));
        assertTrue(and.apply(4));
        assertFalse(and.apply(1));
        assertFalse(and.apply(null));
    }

    // Test or() with Iterable
    @Test
    public void testOrWithIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isGreaterThanZero = Predicates.notNull();

        Predicate<Integer> or = Predicates.or(isEven, isGreaterThanZero);

        assertTrue(or.apply(2));
        assertTrue(or.apply(4));
        assertTrue(or.apply(1));
        assertTrue(or.apply(0));
    }

    // Test or() with varargs
    @Test
    public void testOrWithVarargs() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isGreaterThanZero = Predicates.notNull();

        Predicate<Integer> or = Predicates.or(isEven, isGreaterThanZero);

        assertTrue(or.apply(2));
        assertTrue(or.apply(4));
        assertTrue(or.apply(1));
        assertTrue(or.apply(0));
    }

    // Test or() with two Predicates
    @Test
    public void testOrWithTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isGreaterThanZero = Predicates.notNull();

        Predicate<Integer> or = Predicates.or(isEven, isGreaterThanZero);

        assertTrue(or.apply(2));
        assertTrue(or.apply(4));
        assertTrue(or.apply(1));
        assertTrue(or.apply(0));
    }

    // Test equalTo() with null target
    @Test
    public void testEqualToWithNullTarget() {
        Predicate<String> equalToNull = Predicates.equalTo(null);
        assertTrue(equalToNull.apply(null));
        assertFalse(equalToNull.apply("foo"));
    }

    // Test equalTo() with non-null target
    @Test
    public void testEqualToWithNonnullTarget() {
        Predicate<String> equalToTarget = Predicates.equalTo("foo");
        assertTrue(equalToTarget.apply("foo"));
        assertFalse(equalToTarget.apply("bar"));
    }

    // Test instanceOf()
    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfInteger = Predicates.instanceOf(Integer.class);
        assertTrue(instanceOfInteger.apply(1));
        assertFalse(instanceOfInteger.apply("foo"));
    }

    // Test assignableFrom()
    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assertTrue(assignableFromString.apply(String[].class));
        assertFalse(assignableFromString.apply(Integer.class));
    }

    // Test in()
    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        Predicate<Integer> inNumbers = Predicates.in(numbers);

        assertTrue(inNumbers.apply(1));
        assertTrue(inNumbers.apply(2));
        assertTrue(inNumbers.apply(3));
        assertTrue(inNumbers.apply(4));
        assertFalse(inNumbers.apply(0));
        assertFalse(inNumbers.apply(5));
    }

    // Test compose()
    @Test
    public void testCompose() {
        Predicate<Boolean> isTrue = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input != null && input;
            }
        };

        Function<Integer, Boolean> toBoolean = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> compose = Predicates.compose(isTrue, toBoolean);

        assertTrue(compose.apply(1));
        assertFalse(compose.apply(-1));
        assertFalse(compose.apply(0));
        assertFalse(compose.apply(null));
    }

    // Test containsPattern() with String pattern
    @Test
    public void testContainsPatternWithStringPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("foo");
        assertTrue(containsPattern.apply("foo"));
        assertFalse(containsPattern.apply("bar"));
        assertFalse(containsPattern.apply("foobar"));
    }

    // Test containsPattern() with Pattern pattern
    @Test
    public void testContainsPatternWithPatternPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern(Pattern.compile("foo"));
        assertTrue(containsPattern.apply("foo"));
        assertFalse(containsPattern.apply("bar"));
        assertFalse(containsPattern.apply("foobar"));
    }

    
}