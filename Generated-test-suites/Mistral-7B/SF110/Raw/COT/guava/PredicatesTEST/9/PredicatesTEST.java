import org.junit.Assert;
import org.junit.Test;
import com.google.common.base.Predicates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        Assert.assertTrue(alwaysTrue.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        Assert.assertFalse(alwaysFalse.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        Object obj1 = new Object();
        Object obj2 = null;

        Assert.assertFalse(isNull.apply(obj1));
        Assert.assertTrue(isNull.apply(obj2));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        Object obj1 = new Object();
        Object obj2 = null;

        Assert.assertTrue(notNull.apply(obj1));
        Assert.assertFalse(notNull.apply(obj2));
    }

    @Test
    public void testNot() {
        Predicate<Object> originalPredicate = p -> p != null;
        Predicate<Object> negatedPredicate = Predicates.not(originalPredicate);

        Object obj1 = new Object();
        Object obj2 = null;

        Assert.assertTrue(originalPredicate.apply(obj1));
        Assert.assertFalse(negatedPredicate.apply(obj1));
        Assert.assertFalse(negatedPredicate.apply(obj2));
        Assert.assertTrue(originalPredicate.apply(obj2));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = i -> i > 5;
        Predicate<Integer> predicate2 = i -> i < 10;

        Predicate<Integer> andPredicate = Predicates.and(predicate1, predicate2);

        Assert.assertFalse(andPredicate.apply(5));
        Assert.assertFalse(andPredicate.apply(12));
        Assert.assertTrue(andPredicate.apply(6));
        Assert.assertTrue(andPredicate.apply(8));
    }

    @Test(expected = NullPointerException.class)
    public void testAndWithNullComponent() {
        Predicate<Integer> nullComponent = null;
        Predicate<Integer> predicate = Predicates.and(Predicates.alwaysTrue(), nullComponent);
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = i -> i > 5;
        Predicate<Integer> predicate2 = i -> i < 10;

        Predicate<Integer> orPredicate = Predicates.or(predicate1, predicate2);

        Assert.assertTrue(orPredicate.apply(5));
        Assert.assertTrue(orPredicate.apply(6));
        Assert.assertTrue(orPredicate.apply(12));
        Assert.assertFalse(orPredicate.apply(8));
    }

    @Test(expected = NullPointerException.class)
    public void testOrWithNullComponent() {
        Predicate<Integer> nullComponent = null;
        Predicate<Integer> predicate = Predicates.or(Predicates.alwaysTrue(), nullComponent);
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> equalToPredicate = Predicates.equalTo(5);

        Assert.assertFalse(equalToPredicate.apply(3));
        Assert.assertTrue(equalToPredicate.apply(5));
        Assert.assertFalse(equalToPredicate.apply(6));
    }

    @Test
    public void testIsInstance() {
        Class<String> clazz = String.class;
        Predicate<Object> isInstancePredicate = Predicates.instanceOf(clazz);

        String str1 = "Hello";
        Object obj1 = new Object();

        Assert.assertFalse(isInstancePredicate.apply(obj1));
        Assert.assertTrue(isInstancePredicate.apply(str1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceWithInvalidType() {
        Class<Integer> clazz = Integer.class;
        Predicate<Object> isInstancePredicate = Predicates.instanceOf(clazz);
        Object obj = new Object();
        Integer intValue = 1;

        Assert.assertFalse(isInstancePredicate.apply(obj));
        Assert.assertFalse(isInstancePredicate.apply(intValue));
    }

    @Test
    public void testAssignableFrom() {
        Class<Number> superClass = Number.class;
        Class<Integer> subClass = Integer.class;

        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(superClass);

        Assert.assertTrue(assignableFromPredicate.apply(Integer.class));
        Assert.assertTrue(assignableFromPredicate.apply(Double.class));
        Assert.assertFalse(assignableFromPredicate.apply(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssignableFromWithInvalidType() {
        Class<String> superClass = String.class;
        Class<Number> subClass = Integer.class;

        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(subClass);
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inPredicate = Predicates.in(numbers);

        int num1 = 1;
        int num2 = 6;

        Assert.assertTrue(inPredicate.apply(num1));
        Assert.assertTrue(inPredicate.apply(num2));
        Assert.assertFalse(inPredicate.apply(0));
    }

    @Test(expected = NullPointerException.class)
    public void testInWithNullCollection() {
        Predicate<Integer> inPredicate = Predicates.in(null);
    }

    @Test
    public void testContainsPattern() {
        String pattern = ".*Hello.*";
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);

        String str1 = "Hello World";
        String str2 = "Hello Java";
        String str3 = "Goodbye World";

        Assert.assertTrue(containsPatternPredicate.apply(str1));
        Assert.assertTrue(containsPatternPredicate.apply(str2));
        Assert.assertFalse(containsPatternPredicate.apply(str3));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsPatternWithNullPattern() {
        String nullPattern = null;
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(nullPattern);
    }

    @Test
    public void testCompose() {
        Predicate<String> isLongerThanFiveCharsPredicate = s -> s.length() > 5;
        Function<String, Integer> stringToLengthFunction = s -> s.length();

        Predicate<String> composedPredicate = Predicates.compose(isLongerThanFiveCharsPredicate, stringToLengthFunction);

        String str1 = "Hello";
        String str2 = "Predicates";

        Assert.assertFalse(composedPredicate.apply(str1));
        Assert.assertTrue(composedPredicate.apply(str2));
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullPredicate() {
        Predicate<String> nullPredicate = null;
        Function<String, Integer> stringToLengthFunction = s -> s.length();

        Predicate<String> composedPredicate = Predicates.compose(nullPredicate, stringToLengthFunction);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Predicate<String> isLongerThanFiveCharsPredicate = s -> s.length() > 5;
        Function<String, Integer> nullFunction = null;

        Predicate<String> composedPredicate = Predicates.compose(isLongerThanFiveCharsPredicate, nullFunction);
    }

    
}