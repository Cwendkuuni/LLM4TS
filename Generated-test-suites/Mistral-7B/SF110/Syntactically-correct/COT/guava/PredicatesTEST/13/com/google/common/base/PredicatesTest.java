import static org.junit.Assert.*;

import com.google.common.base.Predicates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    

    // alwaysTrue test
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2.5));
        assertTrue(alwaysTrue.apply("abc"));
    }

    // alwaysFalse test
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2.5));
        assertFalse(alwaysFalse.apply("abc"));
    }

    // isNull test
    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2.5));
        assertFalse(isNull.apply("abc"));
        assertTrue(isNull.apply(null));
    }

    // notNull test
    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2.5));
        assertTrue(notNull.apply("abc"));
    }

    // not test
    @Test
    public void testNot() {
        Predicate<Boolean> predicate = Predicates.alwaysTrue();
        Predicate<Boolean> notPredicate = Predicates.not(predicate);

        assertFalse(notPredicate.apply(true));
        assertTrue(notPredicate.apply(false));
    }

    // and test with Iterable
    @Test
    public void testAndIterable() {
        Predicate<Integer> isGreaterThanOne = (Predicate<Integer>)Predicates.alwaysFalse();
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> andPredicate = Predicates.and(Arrays.asList(isGreaterThanOne, isEven));

        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
    }

    // and test with Predicate...
    @Test
    public void testAndPredicate() {
        Predicate<Integer> isGreaterThanOne = Predicates.alwaysFalse();
        Predicate<Integer> isEven = Predicates.alwaysFalse();
        Predicate<Integer> andPredicate = Predicates.and(isGreaterThanOne, isEven);

        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
    }

    // and test with two Predicates
    @Test
    public void testAndTwoPredicates() {
        Predicate<Integer> isGreaterThanOne = Predicates.alwaysFalse();
        Predicate<Integer> isEven = Predicates.alwaysFalse();
        Predicate<Integer> andPredicate = Predicates.and(isGreaterThanOne, isEven);

        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
    }

    // or test with Iterable
    @Test
    public void testOrIterable() {
        Predicate<Integer> isGreaterThanOne = Predicates.alwaysFalse();
        Predicate<Integer> isEven = Predicates.alwaysTrue();
        Predicate<Integer> orPredicate = Predicates.or(Arrays.asList(isGreaterThanOne, isEven));

        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    // or test with Predicate...
    @Test
    public void testOrPredicate() {
        Predicate<Integer> isGreaterThanOne = Predicates.alwaysFalse();
        Predicate<Integer> isEven = Predicates.alwaysTrue();
        Predicate<Integer> orPredicate = Predicates.or(isGreaterThanOne, isEven);

        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    // or test with two Predicates
    @Test
    public void testOrTwoPredicates() {
        Predicate<Integer> isGreaterThanOne = Predicates.alwaysFalse();
        Predicate<Integer> isEven = Predicates.alwaysTrue();
        Predicate<Integer> orPredicate = Predicates.or(isGreaterThanOne, isEven);

        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    // equalTo test
    @Test
    public void testEqualToNull() {
        Predicate<Integer> equalToNull = Predicates.equalTo(null);
        assertFalse(equalToNull.apply(1));
        assertFalse(equalToNull.apply(2));
        assertFalse(equalToNull.apply(3));
        assertTrue(equalToNull.apply(null));
    }

    // instanceOf test
    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceOfUnsupported() {
        Predicate<String> instanceOfInt = Predicates.instanceOf(Integer.class);
        instanceOfInt.apply("abc");
    }

    // assignableFrom test
    @Test(expected = UnsupportedOperationException.class)
    public void testAssignableFromUnsupported() {
        Predicate<String> assignableFromNumber = Predicates.assignableFrom(Number.class);
        assignableFromNumber.apply("abc");
    }

    // in test
    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(list);

        assertFalse(inPredicate.apply(0));
        assertFalse(inPredicate.apply(4));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
    }

    // compose test
    @Test
    public void testCompose() {
        Predicate<String> isEmpty = Predicates.alwaysFalse();
        Function<String, Integer> lengthFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };
        Predicate<String> composePredicate = Predicates.compose(isEmpty, lengthFunction);

        assertFalse(composePredicate.apply(""));
        assertFalse(composePredicate.apply("abc"));
        assertFalse(composePredicate.apply("123"));
        assertTrue(composePredicate.apply(" "));
    }

    // containsPattern test
    @Test(expected = IllegalArgumentException.class)
    public void testContainsPatternInvalidPattern() {
        Predicate<String> containsPatternInvalid = Predicates.containsPattern("(.*\\)");
        containsPatternInvalid.apply("abc");
    }

    // containsPattern test with String
    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPatternFromString = Predicates.containsPattern("abc");
        assertTrue(containsPatternFromString.apply("abc"));
        assertFalse(containsPatternFromString.apply("def"));
    }

    // containsPattern test with Pattern
    @Test
    public void testContainsPattern() {
        Pattern pattern = Pattern.compile("abc");
        Predicate<String> containsPattern = Predicates.containsPattern(pattern);
        assertTrue(containsPattern.apply("abc"));
        assertFalse(containsPattern.apply("def"));
    }

    
}