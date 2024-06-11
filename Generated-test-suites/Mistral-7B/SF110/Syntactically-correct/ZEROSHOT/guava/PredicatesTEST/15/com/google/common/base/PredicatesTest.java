import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static com.google.common.base.Predicates.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = alwaysTrue();
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = alwaysFalse();
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(2));
        assertFalse(predicate.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> predicate = isNull();
        assertFalse(predicate.apply(1));
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> predicate = notNull();
        assertFalse(predicate.apply(null));
        assertTrue(predicate.apply(1));
    }

    @Test
    public void testNot() {
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = notNull();
        Predicate<Integer> notPredicate = not(predicate1);
        Predicate<Integer> equalPredicate = isNull();

        assertFalse(notPredicate.apply(null));
        assertTrue(notPredicate.apply(1));
        assertTrue(equalPredicate.apply(null));
        assertFalse(equalPredicate.apply(1));
        assertTrue(not(equalPredicate).apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = alwaysTrue();
        Predicate<Integer> predicate2 = isNull();
        Predicate<Integer> andPredicate = and(predicate1, predicate2);

        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(null));
        assertFalse(andPredicate.apply(new Object()));

        List<Predicate<Integer>> list = Arrays.asList(predicate1, predicate2);
        Predicate<Integer> andListPredicate = and(list);
        assertTrue(andListPredicate.apply(1));
        assertTrue(andListPredicate.apply(null));
        assertFalse(andListPredicate.apply(new Object()));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = alwaysTrue();
        Predicate<Integer> orPredicate = or(predicate1, predicate2);

        assertTrue(orPredicate.apply(null));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(new Object()));

        List<Predicate<Integer>> list = Arrays.asList(predicate1, predicate2);
        Predicate<Integer> orListPredicate = or(list);
        assertTrue(orListPredicate.apply(null));
        assertTrue(orListPredicate.apply(1));
        assertTrue(orListPredicate.apply(new Object()));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> predicate = equalTo(1);

        assertTrue(predicate.apply(1));
        assertFalse(predicate.apply(2));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> predicate = instanceOf(Integer.class);

        assertTrue(predicate.apply(1));
        assertFalse(predicate.apply("1"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> predicate = assignableFrom(Integer.class);

        assertTrue(predicate.apply(Integer.class));
        assertTrue(predicate.apply(Long.class));
        assertFalse(predicate.apply(String.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> predicate = in(collection);

        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
        assertFalse(predicate.apply(4));
    }

    @Test
    public void testCompose() {
        Function<Integer, Boolean> function = i -> i > 0;
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = alwaysTrue();

        Predicate<Integer> composedPredicate = compose(predicate2, function);
        assertTrue(composedPredicate.apply(1));
        assertTrue(composedPredicate.apply(2));
        assertFalse(composedPredicate.apply(0));

        Predicate<Integer> andPredicate = and(predicate1, predicate2);
        Predicate<Integer> composedAndPredicate = compose(andPredicate, function);
        assertFalse(composedAndPredicate.apply(1));
        assertFalse(composedAndPredicate.apply(0));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> predicate = containsPattern("ab.*");

        assertTrue(predicate.apply("abcd"));
        assertFalse(predicate.apply("xyz"));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<String> predicate = containsPatternFromString("ab.*");

        assertTrue(predicate.apply("abcd"));
        assertFalse(predicate.apply("xyz"));
    }

    @Test
    public void testNotPredicate() {
        Predicate<Integer> predicate = isNull();
        Predicate<Integer> notPredicate = not(predicate);

        assertFalse(notPredicate.apply(null));
        assertTrue(notPredicate.apply(1));
    }

    @Test
    public void testAndPredicate() {
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = alwaysTrue();
        Predicate<Integer> andPredicate = and(predicate1, predicate2);

        assertFalse(andPredicate.apply(null));
        assertTrue(andPredicate.apply(1));
    }

    @Test
    public void testOrPredicate() {
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = alwaysTrue();
        Predicate<Integer> orPredicate = or(predicate1, predicate2);

        assertFalse(orPredicate.apply(null));
        assertTrue(orPredicate.apply(1));
    }

    @Test
    public void testIsEqualToPredicate() {
        Predicate<Integer> predicate = equalTo(1);

        assertTrue(predicate.apply(1));
        assertFalse(predicate.apply(2));
    }

    @Test
    public void testInstanceOfPredicate() {
        Predicate<Object> predicate = instanceOf(Integer.class);

        assertTrue(predicate.apply(1));
        assertFalse(predicate.apply("1"));
    }

    @Test
    public void testAssignableFromPredicate() {
        Predicate<Class<?>> predicate = assignableFrom(Integer.class);

        assertTrue(predicate.apply(Integer.class));
        assertTrue(predicate.apply(Long.class));
        assertFalse(predicate.apply(String.class));
    }

    @Test
    public void testInPredicate() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> predicate = in(collection);

        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
        assertFalse(predicate.apply(4));
    }

    @Test
    public void testCompositionPredicate() {
        Function<Integer, Boolean> function = i -> i > 0;
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = alwaysTrue();

        Predicate<Integer> composedPredicate = compose(predicate2, function);
        assertTrue(composedPredicate.apply(1));
        assertTrue(composedPredicate.apply(2));
        assertFalse(composedPredicate.apply(0));

        Predicate<Integer> andPredicate = and(predicate1, predicate2);
        Predicate<Integer> composedAndPredicate = compose(andPredicate, function);
        assertFalse(composedAndPredicate.apply(1));
        assertFalse(composedAndPredicate.apply(0));
    }

    @Test
    public void testContainsPatternPredicate() {
        Predicate<String> predicate = containsPattern("ab.*");

        assertTrue(predicate.apply("abcd"));
        assertFalse(predicate.apply("xyz"));
    }

    @Test
    public void testContainsPatternPredicateFail() {
        Predicate<String> predicate = containsPattern("ab.*");

        assertFalse(predicate.apply(null));
        try {
            predicate.apply("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}