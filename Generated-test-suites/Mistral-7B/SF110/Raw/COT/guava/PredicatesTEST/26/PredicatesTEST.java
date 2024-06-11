import com.google.common.base.Predicates;
import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    

    // Testing alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply("test"));
        assertTrue(alwaysTrue.apply(123));
    }

    // Testing alwaysFalse()
    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply("test"));
        assertFalse(alwaysFalse.apply(123));
    }

    // Testing isNull()
    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply("test"));
        assertTrue(isNull.apply(null));
    }

    // Testing notNull()
    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply("test"));
        assertTrue(notNull.apply(123));
    }

    // Testing not()
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

    // Testing and() with Iterable
    @Test
    public void testAndIterable() {
        Predicate<Integer> isZero = Predicates.alwaysFalse();
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and((Iterable<? extends Predicate<? super Integer>>)Arrays.asList(isZero, isEven));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(3));
        assertTrue(andPredicate.apply(0));
        assertTrue(andPredicate.apply(2));
    }

    // Testing and() with array
    @Test
    public void testAndArray() {
        Predicate<Integer> isZero = Predicates.alwaysFalse();
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(isZero, isEven);
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(3));
        assertTrue(andPredicate.apply(0));
        assertTrue(andPredicate.apply(2));
    }

    // Testing and() with two Predicates
    @Test
    public void testAndTwoPredicates() {
        Predicate<Integer> isZero = Predicates.alwaysFalse();
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(isZero, isEven);
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(3));
        assertTrue(andPredicate.apply(0));
        assertTrue(andPredicate.apply(2));
    }

    // Testing or() with Iterable
    @Test
    public void testOrIterable() {
        Predicate<Integer> isZero = Predicates.alwaysFalse();
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

        Predicate<Integer> orPredicate = Predicates.or((Iterable<? extends Predicate<? super Integer>>)Arrays.asList(isZero, isEven, isOdd));
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    // Testing or() with array
    @Test
    public void testOrArray() {
        Predicate<Integer> isZero = Predicates.alwaysFalse();
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

        Predicate<Integer> orPredicate = Predicates.or(isZero, isEven, isOdd);
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    // Testing or() with two Predicates
    @Test
    public void testOrTwoPredicates() {
        Predicate<Integer> isZero = Predicates.alwaysFalse();
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(isZero, isEven);
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    // Testing equalTo() with null
    @Test(expected = NullPointerException.class)
    public void testEqualToNull() {
        Predicate<Object> equalToNull = Predicates.equalTo(null);
        Predicates.alwaysFalse().apply(null);
        equalToNull.apply(null); // this line should throw NPE
    }

    // Testing equalTo() with non-null
    @Test
    public void testEqualToNonNull() {
        Predicate<Object> equalToNonNull = Predicates.equalTo("test");
        assertTrue(equalToNonNull.apply("test"));
        assertFalse(equalToNonNull.apply(123));
    }

    // Testing instanceOf() with valid class
    @Test
    public void testInstanceOfValidClass() {
        Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);
        assertTrue(instanceOfString.apply("test"));
        assertFalse(instanceOfString.apply(123));
    }

    // Testing instanceOf() with invalid class
    @Test
    public void testInstanceOfInvalidClass() {
        Predicate<Object> instanceOfInteger = Predicates.instanceOf(Integer.class);
        assertFalse(instanceOfInteger.apply("test"));
        assertFalse(instanceOfInteger.apply(123));
    }

    // Testing assignableFrom() with valid class
    @Test
    public void testAssignableFromValidClass() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assertTrue(assignableFromString.apply(String.class));
        assertFalse(assignableFromString.apply(Integer.class));
    }

    // Testing assignableFrom() with invalid class
    @Test
    public void testAssignableFromInvalidClass() {
        Predicate<Class<?>> assignableFromInteger = Predicates.assignableFrom(Integer.class);
        assertFalse(assignableFromInteger.apply(String.class));
        assertFalse(assignableFromInteger.apply(Integer.class));
    }

    // Testing in() with null Collection
    @Test(expected = NullPointerException.class)
    public void testInNullCollection() {
        Predicate<Object> inNullCollection = Predicates.in(null);
        inNullCollection.apply(null); // this line should throw NPE
    }

    // Testing in() with valid Collection
    @Test
    public void testInValidCollection() {
        Collection<Object> collection = Arrays.asList(1, "test", null);
        Predicate<Object> inCollection = Predicates.in(collection);
        assertTrue(inCollection.apply(1));
        assertTrue(inCollection.apply("test"));
        assertFalse(inCollection.apply(2));
        assertFalse(inCollection.apply(null));
    }

    // Testing compose() with null Predicate
    @Test(expected = NullPointerException.class)
    public void testComposeNullPredicate() {
        Function<Integer, Boolean> function = i -> i > 10;
        Predicate<Integer> nullPredicate = null;
        Predicate<Integer> compositionPredicate = Predicates.compose(nullPredicate, function);
        nullPredicate.apply(null); // this line should throw NPE
    }

    // Testing compose() with valid Predicate and Function
    @Test
    public void testComposeValidPredicateAndFunction() {
        Function<Integer, Boolean> function = i -> i > 10;
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> compositionPredicate = Predicates.compose(isEven, function);
        assertFalse(compositionPredicate.apply(1));
        assertFalse(compositionPredicate.apply(2));
        assertFalse(compositionPredicate.apply(3));
        assertTrue(compositionPredicate.apply(4));
        assertTrue(compositionPredicate.apply(5));
        assertTrue(compositionPredicate.apply(6));
        assertTrue(compositionPredicate.apply(7));
        assertTrue(compositionPredicate.apply(8));
        assertTrue(compositionPredicate.apply(9));
        assertTrue(compositionPredicate.apply(10));
        assertFalse(compositionPredicate.apply(11));
        assertFalse(compositionPredicate.apply(12));
    }

    
}