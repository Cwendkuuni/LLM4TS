import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {

    

    // Test alwaysTrue() method
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2.0));
        assertTrue(alwaysTrue.apply("test"));
    }

    // Test alwaysFalse() method
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2.0));
        assertFalse(alwaysFalse.apply("test"));
    }

    // Test isNull() method
    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply(0));
        assertTrue(isNull.apply(null));
    }

    // Test notNull() method
    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(0));
        assertTrue(notNull.apply("test"));
    }

    // Test not() method
    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> notEven = Predicates.not(isEven);
        assertFalse(notEven.apply(0));
        assertTrue(notEven.apply(1));
    }

    // Test and() method with Iterable
    @Test
    public void testAndIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> and = Predicates.and(isEven, isPositive);
        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertTrue(and.apply(1));
        assertTrue(and.apply(2));
    }

    // Test and() method with varargs
    @Test
    public void testAndVarargs() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> and = Predicates.and(isEven, isPositive);
        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertTrue(and.apply(1));
        assertTrue(and.apply(2));
    }

    // Test and() method with two Predicates
    @Test
    public void testAndTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> and = Predicates.and(isEven, isPositive);
        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertTrue(and.apply(1));
        assertTrue(and.apply(2));
    }

    // Test or() method with Iterable
    @Test
    public void testOrIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> or = Predicates.or(isEven, isOdd);
        assertFalse(or.apply(-1));
        assertFalse(or.apply(0));
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));
    }

    // Test or() method with varargs
    @Test
    public void testOrVarargs() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> or = Predicates.or(isEven, isOdd);
        assertFalse(or.apply(-1));
        assertFalse(or.apply(0));
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));
    }

    // Test or() method with two Predicates
    @Test
    public void testOrTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> or = Predicates.or(isEven, isOdd);
        assertFalse(or.apply(-1));
        assertFalse(or.apply(0));
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));
    }

    // Test equalTo() method
    @Test
    public void testEqualTo() {
        Predicate<String> equalToNull = Predicates.equalTo(null);
        assertFalse(equalToNull.apply("test"));
        assertTrue(equalToNull.apply(null));
        Predicate<String> equalToTest = Predicates.equalTo("test");
        assertFalse(equalToTest.apply(null));
        assertFalse(equalToTest.apply("otherTest"));
        assertTrue(equalToTest.apply("test"));
    }

    // Test instanceOf() method
    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceOfThrowsException() {
        Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);
        assertFalse(instanceOfString.apply(new Object()));
    }

    // Test instanceOf() method with valid type
    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);
        assertFalse(instanceOfString.apply(new Integer(1)));
        assertTrue(instanceOfString.apply("test"));
    }

    // Test assignableFrom() method
    @Test(expected = UnsupportedOperationException.class)
    public void testAssignableFromThrowsException() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assertFalse(assignableFromString.apply(Integer.class));
    }

    // Test assignableFrom() method with valid types
    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assertFalse(assignableFromString.apply(Integer.class));
        assertTrue(assignableFromString.apply(StringBuilder.class));
    }

    // Test in() method
    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Predicate<Integer> in = Predicates.in(numbers);
        assertFalse(in.apply(-1));
        assertFalse(in.apply(0));
        assertTrue(in.apply(1));
        assertTrue(in.apply(2));
        assertTrue(in.apply(3));
    }

    // Test compose() method
    @Test
    public void testCompose() {
        Function<Integer, Boolean> isEven = input -> input % 2 == 0;
        Predicate<Integer> isEvenPredicate = Predicates.compose(Predicates.alwaysFalse(), isEven);
        assertFalse(isEvenPredicate.apply(-1));
        assertFalse(isEvenPredicate.apply(0));
        assertFalse(isEvenPredicate.apply(1));
        assertTrue(isEvenPredicate.apply(2));
    }

    // Test containsPattern() method
    @Test
    public void testContainsPatternWithPatternObject() {
        Predicate<String> containsPattern = Predicates.containsPattern(Pattern.compile("\\w+e\\w+"));
        assertFalse(containsPattern.apply("test"));
        assertTrue(containsPattern.apply("testString"));
    }

    // Test containsPattern() method with pattern string
    @Test
    public void testContainsPatternWithPatternString() {
        Predicate<String> containsPattern = Predicates.containsPattern("\\w+e\\w+");
        assertFalse(containsPattern.apply("test"));
        assertTrue(containsPattern.apply("testString"));
    }

    // Test edge cases for in() method with null collection
    @Test(expected = NullPointerException.class)
    public void testInNullCollection() {
        Predicate<Integer> in = Predicates.in(null);
        in.apply(1);
    }

    // Test edge cases for in() method with empty collection
    @Test
    public void testInEmptyCollection() {
        List<Integer> numbers = Collections.emptyList();
        Predicate<Integer> in = Predicates.in(numbers);
        assertFalse(in.apply(-1));
        assertFalse(in.apply(0));
        assertFalse(in.apply(1));
        assertFalse(in.apply(2));
    }

    // Test edge cases for and() method with null components
    @Test(expected = NullPointerException.class)
    public void testAndNullComponents() {
        Predicate<Integer> and = Predicates.and(null, isEven);
        and.apply(1);
    }

    // Test edge cases for and() method with empty components
    @Test
    public void testAndEmptyComponents() {
        Predicate<Integer> and = Predicates.and(Collections.emptyList().toArray(new Predicate[0]));
        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertFalse(and.apply(1));
        assertFalse(and.apply(2));
    }

    // Test edge cases for or() method with null components
    @Test(expected = NullPointerException.class)
    public void testOrNullComponents() {
        Predicate<Integer> or = Predicates.or(null, isEven);
        or.apply(1);
    }

    // Test edge cases for or() method with empty components
    @Test
    public void testOrEmptyComponents() {
        Predicate<Integer> or = Predicates.or(Collections.emptyList().toArray(new Predicate[0]));
        assertFalse(or.apply(-1));
        assertFalse(or.apply(0));
        assertFalse(or.apply(1));
        assertFalse(or.apply(2));
    }

    // Test edge cases for and() method with null list
    @Test(expected = NullPointerException.class)
    public void testAndNullList() {
        Predicate<Integer> and = Predicates.and((List)null, isEven);
        and.apply(1);
    }

    // Test edge cases for and() method with empty list
    @Test
    public void testAndEmptyList() {
        Predicate<Integer> and = Predicates.and(Collections.emptyList(), isEven);
        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertFalse(and.apply(1));
        assertFalse(and.apply(2));
    }

    // Test edge cases for or() method with null list
    @Test(expected = NullPointerException.class)
    public void testOrNullList() {
        Predicate<Integer> or = Predicates.or((List)null, isEven);
        or.apply(1);
    }

    // Test edge cases for or() method with empty list
    @Test
    public void testOrEmptyList() {
        Predicate<Integer> or = Predicates.or(Collections.emptyList(), isEven);
        assertFalse(or.apply(-1));
        assertFalse(or.apply(0));
        assertFalse(or.apply(1));
        assertFalse(or.apply(2));
    }

    
}