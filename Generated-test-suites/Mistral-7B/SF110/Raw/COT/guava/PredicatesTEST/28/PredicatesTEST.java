import static junit.framework.Assert.*;
import org.junit.Test;
import com.google.common.base.Predicates;

public class PredicatesTest {

    // Test methods for alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(null));
        assertTrue(alwaysTruePredicate.apply(1));
        assertTrue(alwaysTruePredicate.apply(3.14));
    }

    // Test methods for alwaysFalse()
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(null));
        assertFalse(alwaysFalsePredicate.apply(1));
        assertFalse(alwaysFalsePredicate.apply(3.14));
    }

    // Test methods for isNull()
    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertFalse(isNullPredicate.apply(new Object()));
        assertTrue(isNullPredicate.apply(null));
    }

    // Test methods for notNull()
    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();
        assertFalse(notNullPredicate.apply(null));
        assertTrue(notNullPredicate.apply(new Object()));
    }

    // Test methods for not()
    @Test
    public void testNot() {
        Predicate<Boolean> alwaysTruePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> alwaysFalsePredicate = Predicates.alwaysFalse();

        Predicate<Boolean> notTruePredicate = Predicates.not(alwaysTruePredicate);
        Predicate<Boolean> notFalsePredicate = Predicates.not(alwaysFalsePredicate);

        assertFalse(notTruePredicate.apply(true));
        assertTrue(notTruePredicate.apply(false));

        assertFalse(notFalsePredicate.apply(false));
        assertTrue(notFalsePredicate.apply(true));
    }

    // Test methods for and()
    @Test
    public void testAnd() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> greaterThanZeroPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(evenPredicate, greaterThanZeroPredicate);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-1));

        Predicate<? super String> nullPredicate = Predicates.isNull();
        Predicate<? super String> nonEmptyStringPredicate = new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input != null && !input.isEmpty();
            }
        };

        Predicate<? super String> andStringPredicate = Predicates.and(nullPredicate, nonEmptyStringPredicate);

        assertTrue(andStringPredicate.apply(null));
        assertFalse(andStringPredicate.apply(""));
        assertTrue(andStringPredicate.apply("Test"));

        List<Predicate<? super Integer>> predicates = Arrays.asList(evenPredicate, greaterThanZeroPredicate);
        Predicate<Integer> andPredicate2 = Predicates.and(predicates);

        assertTrue(andPredicate2.apply(2));
        assertFalse(andPredicate2.apply(1));
        assertFalse(andPredicate2.apply(-1));
    }

    // Test methods for or()
    @Test
    public void testOr() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> greaterThanZeroPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(evenPredicate, greaterThanZeroPredicate);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(3));

        List<Predicate<? super Integer>> predicates = Arrays.asList(evenPredicate, greaterThanZeroPredicate);
        Predicate<Integer> orPredicate2 = Predicates.or(predicates);

        assertTrue(orPredicate2.apply(2));
        assertTrue(orPredicate2.apply(1));
        assertTrue(orPredicate2.apply(3));

        Predicate<Integer> evenAndGreaterThanZeroPredicate = Predicates.and(evenPredicate, greaterThanZeroPredicate);
        Predicate<Integer> orPredicate3 = Predicates.or(evenAndGreaterThanZeroPredicate, Predicates.notNull());

        assertTrue(orPredicate3.apply(2));
        assertFalse(orPredicate3.apply(null));
        assertTrue(orPredicate3.apply(1));
    }

    // Test methods for equalTo()
    @Test
    public void testEqualTo() {
        Predicate<Integer> equalToFivePredicate = Predicates.equalTo(5);

        assertTrue(equalToFivePredicate.apply(5));
        assertFalse(equalToFivePredicate.apply(4));
        assertFalse(equalToFivePredicate.apply(6));

        Predicate<String> equalToEmptyStringPredicate = Predicates.equalTo("");

        assertTrue(equalToEmptyStringPredicate.apply(""));
        assertFalse(equalToEmptyStringPredicate.apply("Test"));
        assertFalse(equalToEmptyStringPredicate.apply("Hello"));
    }

    // Test methods for instanceOf() (GWT-incompatible)
    @Test(expected = java.lang.RuntimeException.class)
    public void testInstanceOfException() {
        Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(String.class);

        Predicate<Integer> integerPredicate = Predicates.alwaysTrue();

        try {
            Predicates.compose(instanceOfStringPredicate, integerPredicate);
        } catch (Exception e) {
            throw new RuntimeException("Expected IllegalArgumentException, but got " + e.getClass().getName(), e);
        }
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(String.class);

        assertTrue(instanceOfStringPredicate.apply("Test"));
        assertFalse(instanceOfStringPredicate.apply(new Object()));
        assertFalse(instanceOfStringPredicate.apply(123));

        Predicate<Object> instanceOfIntegerPredicate = Predicates.instanceOf(Integer.class);

        assertTrue(instanceOfIntegerPredicate.apply(123));
        assertFalse(instanceOfIntegerPredicate.apply("Test"));
        assertFalse(instanceOfIntegerPredicate.apply(new Object()));
    }

    // Test methods for assignableFrom() (GWT-incompatible)
    @Test(expected = java.lang.RuntimeException.class)
    public void testAssignableFromException() {
        Predicate<Class<?>> assignableFromStringClassPredicate = Predicates.assignableFrom(String.class);

        Predicate<Class<?>> assignableFromIntegerClassPredicate = Predicates.assignableFrom(Integer.TYPE.getClass());

        try {
            Predicates.compose(assignableFromStringClassPredicate, assignableFromIntegerClassPredicate);
        } catch (Exception e) {
            throw new RuntimeException("Expected IllegalArgumentException, but got " + e.getClass().getName(), e);
        }
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromStringClassPredicate = Predicates.assignableFrom(String.class);

        assertTrue(assignableFromStringClassPredicate.apply(String.class));
        assertFalse(assignableFromStringClassPredicate.apply(Integer.TYPE.getClass()));

        Predicate<Class<?>> assignableFromIntegerClassPredicate = Predicates.assignableFrom(Integer.TYPE.getClass());

        assertTrue(assignableFromIntegerClassPredicate.apply(Integer.TYPE.getClass()));
        assertFalse(assignableFromIntegerClassPredicate.apply(String.class));

        Predicate<Class<?>> assignableFromAnyClassPredicate = Predicates.assignableFrom(Object.class);

        assertTrue(assignableFromAnyClassPredicate.apply(String.class));
        assertTrue(assignableFromAnyClassPredicate.apply(Integer.TYPE.getClass()));
        assertTrue(assignableFromAnyClassPredicate.apply(Object.class));
    }

    // Test methods for in()
    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> inPredicate = Predicates.in(numbers);

        assertTrue(inPredicate.apply(2));
        assertFalse(inPredicate.apply(6));
        assertTrue(inPredicate.apply(3));

        List<String> strings = Arrays.asList("Test1", "Test2", "Test3");

        Predicate<String> inPredicateString = Predicates.in(strings);

        assertTrue(inPredicateString.apply("Test2"));
        assertFalse(inPredicateString.apply("Test4"));
        assertTrue(inPredicateString.apply("Test3"));

        List<String> emptyList = Collections.emptyList();

        Predicate<String> inPredicateEmptyList = Predicates.in(emptyList);

        assertFalse(inPredicateEmptyList.apply("Test1"));
        assertFalse(inPredicateEmptyList.apply("Test2"));
        assertFalse(inPredicateEmptyList.apply("Test3"));
    }

    // Test methods for compose()
    @Test
    public void testCompose() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> addOneFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Predicate<Integer> composePredicate = Predicates.compose(isEvenPredicate, addOneFunction);

        assertTrue(composePredicate.apply(2));
        assertTrue(composePredicate.apply(4));
        assertFalse(composePredicate.apply(1));

        Function<Integer, Integer> subtractOneFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input - 1;
            }
        };

        Predicate<Integer> composePredicate2 = Predicates.compose(isEvenPredicate, subtractOneFunction);

        assertTrue(composePredicate2.apply(2));
        assertTrue(composePredicate2.apply(4));
        assertFalse(composePredicate2.apply(1));
    }

    // Test methods for containsPattern() (GWT-incompatible)
    @Test(expected = java.lang.RuntimeException.class)
    public void testContainsPatternException() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(".*\\d.*");

        try {
            Predicates.compose(containsPatternPredicate, Predicates.alwaysTrue());
        } catch (Exception e) {
            throw new RuntimeException("Expected IllegalArgumentException, but got " + e.getClass().getName(), e);
        }
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(".*\\d.*");

        assertTrue(containsPatternPredicate.apply("Test123"));
        assertFalse(containsPatternPredicate.apply("Test1"));
        assertFalse(containsPatternPredicate.apply("Test"));

        Predicate<String> containsPatternPredicate2 = Predicates.containsPattern(".*Test.*");

        assertTrue(containsPatternPredicate2.apply("Test123"));
        assertTrue(containsPatternPredicate2.apply("TestTest"));
        assertFalse(containsPatternPredicate2.apply("Test1"));
    }

    // Test methods for containsPatternFromString() (GWT-incompatible)
    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPatternFromStringPredicate = Predicates.containsPatternFromString(".*\\d.*");

        assertTrue(containsPatternFromStringPredicate.apply("Test123"));
        assertFalse(containsPatternFromStringPredicate.apply("Test1"));
        assertFalse(containsPatternFromStringPredicate.apply("Test"));

        Predicate<String> containsPatternFromStringPredicate2 = Predicates.containsPatternFromString(".*Test.*");

        assertTrue(containsPatternFromStringPredicate2.apply("Test123"));
        assertTrue(containsPatternFromStringPredicate2.apply("TestTest"));
        assertFalse(containsPatternFromStringPredicate2.apply("Test1"));
    }
}