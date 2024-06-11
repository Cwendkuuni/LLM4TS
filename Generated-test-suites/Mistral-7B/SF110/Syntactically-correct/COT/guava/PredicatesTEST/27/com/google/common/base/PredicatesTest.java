import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class PredicatesTest {

    

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(100));
        assertTrue(alwaysTrue.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(100));
        assertFalse(alwaysFalse.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(""));
        assertFalse(isNull.apply(new Object()));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(""));
        assertTrue(notNull.apply(new Object()));
        assertFalse(notNull.apply(null));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = alwaysTrue();
        Predicate<Boolean> falsePredicate = alwaysFalse();
        Predicate<Boolean> notTrue = not(truePredicate);
        Predicate<Boolean> notFalse = not(falsePredicate);

        assertTrue(notTrue.apply(false));
        assertTrue(notTrue.apply(true));
        assertFalse(notFalse.apply(false));
        assertFalse(notFalse.apply(true));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> andPredicate = and(isEven, isPositive);

        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
    }

    @Test
    public void testAndWithArrays() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> andPredicate = and(isEven, isPositive);

        Predicate<Integer>[] andPredicates = new Predicate[] { isEven, isPositive };
        Predicate<Integer> andPredicate2 = and(andPredicates);

        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate2.apply(2));
        assertTrue(andPredicate2.apply(4));
        assertFalse(andPredicate2.apply(1));
        assertFalse(andPredicate2.apply(-1));
        assertFalse(andPredicate2.apply(0));
    }

    @Test
    public void testAndWithOne() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> andPredicate = and(isEven);

        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAndWithNull() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> nullPredicate = null;
        and(isEven, nullPredicate);
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> orPredicate = or(isEven, isPositive);

        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(4));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
    }

    @Test
    public void testOrWithArrays() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> orPredicate = or(isEven, isPositive);

        Predicate<Integer>[] orPredicates = new Predicate[] { isEven, isPositive };
        Predicate<Integer> orPredicate2 = or(orPredicates);

        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(4));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate2.apply(1));
        assertTrue(orPredicate2.apply(2));
        assertTrue(orPredicate2.apply(4));
        assertFalse(orPredicate2.apply(-1));
        assertFalse(orPredicate2.apply(0));
    }

    @Test
    public void testOrWithOne() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> orPredicate = or(isEven);

        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(4));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
    }

    @Test(expected = NullPointerException.class)
    public void testOrWithNull() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> nullPredicate = null;
        or(isEven, nullPredicate);
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> equalToThree = equalTo(3);
        Predicate<Integer> equalToFour = equalTo(4);
        Predicate<Integer> equalToFive = equalTo(5);

        assertTrue(equalToThree.apply(3));
        assertFalse(equalToThree.apply(4));
        assertFalse(equalToThree.apply(5));
        assertFalse(equalToThree.apply(null));

        assertTrue(equalToFour.apply(4));
        assertFalse(equalToFour.apply(3));
        assertFalse(equalToFour.apply(5));
        assertFalse(equalToFour.apply(null));

        assertFalse(equalToFive.apply(3));
        assertFalse(equalToFive.apply(4));
        assertTrue(equalToFive.apply(5));
        assertFalse(equalToFive.apply(null));

        Predicate<String> equalToString = equalTo("test");
        Predicate<String> equalToNull = equalTo(null);

        assertFalse(equalToString.apply("Hello"));
        assertFalse(equalToString.apply("test"));
        assertTrue(equalToString.apply("test"));
        assertFalse(equalToString.apply(null));

        assertFalse(equalToNull.apply("test"));
        assertFalse(equalToNull.apply("Hello"));
        assertFalse(equalToNull.apply(null));
        assertTrue(equalToNull.apply(null));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfString = instanceOf(String.class);
        Predicate<Object> instanceOfNumber = instanceOf(Number.class);

        assertTrue(instanceOfString.apply("test"));
        assertFalse(instanceOfString.apply(1));
        assertFalse(instanceOfString.apply(null));

        assertTrue(instanceOfNumber.apply(1));
        assertFalse(instanceOfNumber.apply("test"));
        assertFalse(instanceOfNumber.apply(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceOfWithIllegalArgument() {
        Predicate<Object> instanceOfString = instanceOf(String.class);
        instanceOf(int.class);
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = assignableFrom(String.class);
        Predicate<Class<?>> assignableFromNumber = assignableFrom(Number.class);
        Predicate<Class<?>> assignableFromObject = assignableFrom(Object.class);

        assertTrue(assignableFromString.apply(String.class));
        assertTrue(assignableFromString.apply(StringBuilder.class));
        assertFalse(assignableFromString.apply(Number.class));
        assertFalse(assignableFromString.apply(Object.class));
        assertFalse(assignableFromString.apply(int.class));

        assertTrue(assignableFromNumber.apply(Number.class));
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Long.class));
        assertFalse(assignableFromNumber.apply(String.class));
        assertFalse(assignableFromNumber.apply(Object.class));
        assertFalse(assignableFromNumber.apply(int[].class));

        assertTrue(assignableFromObject.apply(Object.class));
        assertTrue(assignableFromObject.apply(String.class));
        assertTrue(assignableFromObject.apply(Number.class));
        assertFalse(assignableFromObject.apply(StringBuilder.class));
        assertFalse(assignableFromObject.apply(int[].class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAssignableFromWithIllegalArgument() {
        Predicate<Class<?>> assignableFromString = assignableFrom(String.class);
        assignableFrom(int.class);
    }

    @Test
    public void testIn() {
        Predicate<Integer> inList = in(Arrays.asList(1, 2, 3, 4, 5));
        Predicate<Integer> inList2 = in(Arrays.asList(3, 4, 5, 1));
        Predicate<Integer> inNull = in(null);

        assertTrue(inList.apply(1));
        assertTrue(inList.apply(2));
        assertTrue(inList.apply(3));
        assertTrue(inList.apply(4));
        assertTrue(inList.apply(5));
        assertFalse(inList.apply(6));
        assertFalse(inList.apply(null));

        assertTrue(inList2.apply(3));
        assertTrue(inList2.apply(4));
        assertTrue(inList2.apply(5));
        assertFalse(inList2.apply(1));
        assertFalse(inList2.apply(2));
        assertFalse(inList2.apply(6));
        assertFalse(inList2.apply(null));

        assertFalse(inNull.apply(1));
        assertFalse(inNull.apply(2));
        assertFalse(inNull.apply(3));
        assertFalse(inNull.apply(4));
        assertFalse(inNull.apply(5));
        assertFalse(inNull.apply(6));
        assertTrue(inNull.apply(null));
    }

    @Test(expected = NullPointerException.class)
    public void testInWithNullCollection() {
        Predicate<Integer> inNullList = in(null);
        inNullList.apply(1);
    }

    @Test(expected = ClassCastException.class)
    public void testInWithNonList() {
        Predicate<Integer> inInteger = in(1);
        inInteger.apply(1);
        inInteger.apply('a');
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input + 1;
            }
        };
        Predicate<Integer> composePredicate = compose(isEven, addOne);

        assertTrue(composePredicate.apply(2));
        assertTrue(composePredicate.apply(4));
        assertFalse(composePredicate.apply(1));
        assertFalse(composePredicate.apply(-1));
        assertFalse(composePredicate.apply(0));

        Predicate<Integer> isOdd = not(isEven);
        Predicate<Integer> composePredicate2 = compose(isOdd, addOne);

        assertFalse(composePredicate2.apply(2));
        assertFalse(composePredicate2.apply(4));
        assertTrue(composePredicate2.apply(1));
        assertFalse(composePredicate2.apply(-1));
        assertFalse(composePredicate2.apply(0));
    }

    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPatternString = containsPattern("test");
        Predicate<CharSequence> containsPatternPattern = containsPattern(Pattern.compile("(test)"));

        assertTrue(containsPatternString.apply("test"));
        assertTrue(containsPatternString.apply("test test"));
        assertFalse(containsPatternString.apply("hello"));
        assertFalse(containsPatternString.apply(null));

        assertTrue(containsPatternPattern.apply("test"));
        assertTrue(containsPatternPattern.apply("test test"));
        assertFalse(containsPatternPattern.apply("hello"));
        assertFalse(containsPatternPattern.apply(null));
    }

    @Test
    public void testContainsPatternWithEmptyString() {
        Predicate<CharSequence> containsPatternEmptyString = containsPattern("");
        assertTrue(containsPatternEmptyString.apply(""));
        assertFalse(containsPatternEmptyString.apply("test"));
        assertFalse(containsPatternEmptyString.apply(null));
    }

    
}