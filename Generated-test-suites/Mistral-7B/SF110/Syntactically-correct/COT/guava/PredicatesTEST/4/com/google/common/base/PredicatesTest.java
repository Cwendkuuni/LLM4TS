import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertNull(isNullPredicate.apply(null));
        assertFalse(isNullPredicate.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();
        assertNull(notNullPredicate.apply(null));
        assertNotNull(notNullPredicate.apply(new Object()));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();
        Predicate<Boolean> notTruePredicate = Predicates.not(truePredicate);
        Predicate<Boolean> notFalsePredicate = Predicates.not(falsePredicate);

        assertTrue(notTruePredicate.apply(false));
        assertFalse(notTruePredicate.apply(true));

        assertTrue(notFalsePredicate.apply(true));
        assertFalse(notFalsePredicate.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> oddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 != 0;
            }
        };

        Predicate<Integer> andEvenOddPredicate = Predicates.and(evenPredicate, oddPredicate);

        assertFalse(andEvenOddPredicate.apply(1));
        assertTrue(andEvenOddPredicate.apply(2));
        assertFalse(andEvenOddPredicate.apply(3));

        Predicate<Integer> andEvenEvenPredicate = Predicates.and(evenPredicate, evenPredicate);
        assertTrue(andEvenEvenPredicate.apply(2));
        assertTrue(andEvenEvenPredicate.apply(4));
    }

    @Test
    public void testOr() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> oddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 != 0;
            }
        };

        Predicate<Integer> orEvenOddPredicate = Predicates.or(evenPredicate, oddPredicate);

        assertFalse(orEvenOddPredicate.apply(1));
        assertTrue(orEvenOddPredicate.apply(2));
        assertTrue(orEvenOddPredicate.apply(3));

        Predicate<Integer> orEvenEvenPredicate = Predicates.or(evenPredicate, evenPredicate);
        assertTrue(orEvenEvenPredicate.apply(2));
        assertTrue(orEvenEvenPredicate.apply(4));
    }

    @Test
    public void testEqualTo() {
        Predicate<Object> equalsNullPredicate = Predicates.equalTo(null);
        assertFalse(equalsNullPredicate.apply(new Object()));
        assertTrue(equalsNullPredicate.apply(null));

        Object obj1 = new Object();
        Predicate<Object> equalsObjectPredicate = Predicates.equalTo(obj1);
        assertFalse(equalsObjectPredicate.apply(new Object()));
        assertTrue(equalsObjectPredicate.apply(obj1));
    }

    @Test(expected = AssertionError.class)
    public void testInstanceOfNull() {
        Predicate<Object> instanceOfNullPredicate = Predicates.instanceOf(null);
        instanceOfNullPredicate.apply(new Object());
    }

    @Test
    public void testInstanceOfString() {
        Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(String.class);
        assertTrue(instanceOfStringPredicate.apply(""));
        assertFalse(instanceOfStringPredicate.apply(1));
    }

    @Test
    public void testInstanceOfInteger() {
        Predicate<Object> instanceOfIntegerPredicate = Predicates.instanceOf(Integer.class);
        assertTrue(instanceOfIntegerPredicate.apply(1));
        assertFalse(instanceOfIntegerPredicate.apply(""));
    }

    @Test(expected = AssertionError.class)
    public void testAssignableFromNull() {
        Predicate<Class<?>> assignableFromNullPredicate = Predicates.assignableFrom(null);
        assignableFromNullPredicate.apply(String.class);
    }

    @Test
    public void testAssignableFromStringToObject() {
        Predicate<Class<?>> assignableFromStringToObjectPredicate = Predicates.assignableFrom(String.class);
        assertTrue(assignableFromStringToObjectPredicate.apply(String.class));
        assertFalse(assignableFromStringToObjectPredicate.apply(Integer.class));
    }

    @Test
    public void testAssignableFromStringToInteger() {
        Predicate<Class<?>> assignableFromStringToIntegerPredicate = Predicates.assignableFrom(String.class);
        assertFalse(assignableFromStringToIntegerPredicate.apply(String.class));
        assertTrue(assignableFromStringToIntegerPredicate.apply(Integer.class.getClass()));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        Predicate<Integer> inPredicate = Predicates.in(numbers);

        assertFalse(inPredicate.apply(0));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertFalse(inPredicate.apply(4));
        assertFalse(inPredicate.apply(5));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> squareFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * integer;
            }
        };
        Predicate<Integer> composedPredicate = Predicates.compose(isEvenPredicate, squareFunction);

        assertFalse(composedPredicate.apply(1));
        assertTrue(composedPredicate.apply(4));
        assertTrue(composedPredicate.apply(9));
        assertFalse(composedPredicate.apply(16));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsPatternWithEmptyPattern() {
        Predicate<String> containsPatternEmptyPredicate = Predicates.containsPattern("");
        containsPatternEmptyPredicate.apply("hello");
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab");
        assertTrue(containsPatternPredicate.apply("hello world, abracadabra"));
        assertFalse(containsPatternPredicate.apply("hello world"));
    }
}