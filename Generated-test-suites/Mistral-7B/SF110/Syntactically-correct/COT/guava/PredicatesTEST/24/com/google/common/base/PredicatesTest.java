package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PredicatesTest {

    private Predicate predicate;
    private Object testObject;

    @Parameters
    public static List<Object[]> data() {
        Object[][] data = new Object[][] {
                {Predicates.alwaysTrue(), null},
                {Predicates.alwaysTrue(), new Object()},
                {Predicates.alwaysFalse(), null},
                {Predicates.alwaysFalse(), new Object()},
                {Predicates.isNull(), null},
                {Predicates.isNull(), new Object()},
                {Predicates.notNull(), null},
                {Predicates.notNull(), new Object()},
                {Predicates.not(Predicates.isNull()), new Object()},
                {Predicates.not(Predicates.notNull()), null},
                {Predicates.and(Predicates.isNull(), Predicates.isNull()), null},
                {Predicates.and(Predicates.isNull(), Predicates.isNull()), new Object()},
                {Predicates.and(Predicates.isNull(), Predicates.notNull()), null},
                {Predicates.and(Predicates.isNull(), Predicates.notNull()), new Object()},
                {Predicates.or(Predicates.isNull(), Predicates.isNull()), null},
                {Predicates.or(Predicates.isNull(), Predicates.isNull()), new Object()},
                {Predicates.or(Predicates.isNull(), Predicates.notNull()), null},
                {Predicates.or(Predicates.isNull(), Predicates.notNull()), new Object()},
                {Predicates.equalTo(null), null},
                {Predicates.equalTo(null), new Object()},
                {Predicates.equalTo(new Object()), null},
                {Predicates.equalTo(new Object()), new Object()},
                {Predicates.instanceOf(String.class), ""},
                {Predicates.instanceOf(String.class), new String()},
                {Predicates.assignableFrom(String.class), Integer.class},
                {Predicates.assignableFrom(String.class), String.class},
                {Predicates.in(Arrays.asList(1, 2, 3)), 1},
                {Predicates.in(Arrays.asList(1, 2, 3)), 2},
                {Predicates.in(Arrays.asList(1, 2, 3)), 3},
                {Predicates.in(Arrays.asList(1, 2, 3)), 4},
                {Predicates.compose(Predicates.isNull(), (Function<Object, Integer>) o -> 1), null},
                {Predicates.compose(Predicates.isNull(), (Function<Object, Integer>) o -> 1), new Object()},
                {Predicates.containsPattern(".*\\d.*"), "12345"},
                {Predicates.containsPattern(".*\\d.*"), "abcd"},
                {Predicates.containsPatternFromString(".*\\d.*"), "12345"},
                {Predicates.containsPatternFromString(".*\\d.*"), "abcd"}
        };
        return Arrays.asList(data);
    }

    public PredicatesTest(Predicate predicate, Object testObject) {
        this.predicate = predicate;
        this.testObject = testObject;
    }

    @Test
    public void test() {
        boolean result = predicate.apply(testObject);
        if (predicate.getClass().getName().contains("NotPredicate")) {
            assertFalse("Expected predicate to return false but got: " + result, result);
        } else if (predicate.getClass().getName().contains("AndPredicate")) {
            Predicate<Object> andPredicate = (AndPredicate<Object>) predicate;
            for (Predicate<Object> predicate : andPredicate.getComponents()) {
                assertTrue("Expected at least one predicate to return true but none did. Predicates: " + andPredicate.getComponents(), predicate.apply(testObject));
            }
        } else if (predicate.getClass().getName().contains("OrPredicate")) {
            Predicate<Object> orPredicate = (OrPredicate<Object>) predicate;
            for (Predicate<Object> predicate : orPredicate.getComponents()) {
                assertTrue("Expected at least one predicate to return true but none did. Predicates: " + orPredicate.getComponents(), predicate.apply(testObject) || result);
            }
        } else {
            assertTrue("Expected predicate to return true but got: " + result, result);
        }
    }
}