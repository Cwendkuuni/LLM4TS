package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class PredicatesTest {

    private Predicate<Object> predicate;
    private Object object;

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { Predicates.alwaysTrue(), null }, // alwaysTrue()
                { Predicates.alwaysTrue(), new Object() }, // alwaysTrue()
                { Predicates.alwaysFalse(), null }, // alwaysFalse()
                { Predicates.alwaysFalse(), new Object() }, // alwaysFalse()
                { Predicates.isNull(), null }, // isNull()
                { Predicates.isNull(), new Object() }, // isNull()
                { Predicates.notNull(), null }, // notNull()
                { Predicates.notNull(), new Object() }, // notNull()
                { Predicates.not(Predicates.alwaysTrue()), null }, // not()
                { Predicates.not(Predicates.alwaysFalse()), null }, // not()
                { Predicates.and(Predicates.isNull(), Predicates.isNull()), null }, // and()
                { Predicates.and(Predicates.instanceOf(String.class), Predicates.isNull()), null }, // and()
                { Predicates.and(Predicates.instanceOf(String.class), Predicates.notNull()), new String() }, // and()
                { Predicates.or(Predicates.isNull(), Predicates.isNull()), null }, // or()
                { Predicates.or(Predicates.isNull(), Predicates.notNull()), null }, // or()
                { Predicates.in(Arrays.asList(1, 2, 3)), 1 }, // in()
                { Predicates.in(Arrays.asList(1, 2, 3)), 4 }, // in()
                { Predicates.equalTo(null), null }, // equalTo()
                { Predicates.equalTo("hello"), "hello" }, // equalTo()
                { Predicates.instanceOf(String.class), "hello" }, // instanceOf()
                { Predicates.assignableFrom(String.class), Number.class }, // assignableFrom()
                { Predicates.not(Predicates.instanceOf(Number.class)), "hello" }, // not() + instanceOf()
                { Predicates.compose(Predicates.instanceOf(Number.class), new Function<Integer, String>() {
                    @Override
                    public String apply(Integer input) {
                        return String.valueOf(input);
                    }
                }), 5 }, // compose()
                { Predicates.containsPattern(".*hello.*"), "hello world" }, // containsPattern()
                { Predicates.containsPattern(".*world.*"), "hello world" } // containsPattern()
        });
    }

    @Test
    public void testPredicateFunctionality() {
        this.predicate.apply(this.object);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPredicate() {
        this.predicate = null;
        this.predicate.apply(this.object);
    }

    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        this.predicate = Predicates.alwaysTrue();
        this.predicate.apply(null);
    }

    @Test
    public void testAndPredicates() {
        List<Predicate<Object>> andComponents = new ArrayList<>();
        andComponents.add(Predicates.notNull());
        andComponents.add(Predicates.instanceOf(String.class));
        Predicate<Object> andPredicate = Predicates.and(andComponents);
        Object object = new String("hello");
        assertTrue(andPredicate.apply(object));
    }

    @Test
    public void testOrPredicates() {
        List<Predicate<Object>> orComponents = new ArrayList<>();
        orComponents.add(Predicates.isNull());
        orComponents.add(Predicates.notNull());
        Predicate<Object> orPredicate = Predicates.or(orComponents);
        Object object = null;
        assertFalse(orPredicate.apply(object));
        object = new Object();
        assertTrue(orPredicate.apply(object));
    }

    @Test
    public void testInPredicate() {
        Collection<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Predicate<Integer> inPredicate = Predicates.in(collection);
        Integer number = 1;
        assertTrue(inPredicate.apply(number));
        number = 3;
        assertFalse(inPredicate.apply(number));
    }

    @Test(expected = ClassCastException.class)
    public void testInPredicateWrongType() {
        Collection<Integer> collection = new ArrayList<>();
        collection.add(1);
        Predicate<String> inPredicate = Predicates.in(collection);
        String string = "hello";
        inPredicate.apply(string);
    }

    @Test(expected = NullPointerException.class)
    public void testInPredicateNullCollection() {
        Predicate<Object> inPredicate = Predicates.in(null);
        Object object = new Object();
        inPredicate.apply(object);
    }

    @Test
    public void testCompositionPredicate() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        Predicate<Integer> predicate = Predicates.instanceOf(Integer.class);
        Predicate<String> compositionPredicate = Predicates.compose(predicate, function);
        Integer number = 5;
        String string = compositionPredicate.apply(number);
        assertEquals("5", string);
    }

    @Test
    public void testContainsPatternPredicate() {
        Predicate<CharSequence> patternPredicate = Predicates.containsPattern(".*hello.*");
        String string = "hello world";
        assertTrue(patternPredicate.apply(string));
        string = "hello there";
        assertTrue(patternPredicate.apply(string));
        string = "world hello";
        assertTrue(patternPredicate.apply(string));
        string = "there hello";
        assertTrue(patternPredicate.apply(string));
        String emptyString = "";
        assertFalse(patternPredicate.apply(emptyString));
        string = "hello world!";
        assertFalse(patternPredicate.apply(string));
    }
}