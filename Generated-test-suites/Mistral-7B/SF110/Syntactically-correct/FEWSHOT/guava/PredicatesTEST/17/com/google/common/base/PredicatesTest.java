package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class PredicatesTest {

    private List<Predicate<?>> predicates;

    @Before
    public void setUp() {
        predicates = Arrays.asList(Predicates.<Object>alwaysTrue(), Predicates.<Object>alwaysFalse(), Predicates.<Object>isNull(), Predicates.<Object>notNull());
    }

    @Test
    public void testAlwaysTrue() {
        for (Predicate<?> predicate : predicates) {
            Object obj = new Object();
            assertTrue(predicate.apply(obj));
        }
    }

    @Test
    public void testAlwaysFalse() {
        for (Predicate<?> predicate : predicates) {
            Object obj = new Object();
            assertFalse(predicate.apply(obj));
        }
    }

    @Test
    public void testIsNull() {
        Object obj1 = null;
        for (Predicate<?> predicate : predicates) {
            if (predicate instanceof Predicates.IsNull) {
                assertTrue(predicate.apply(obj1));
                break;
            }
            Object obj2 = new Object();
            assertFalse(predicate.apply(obj2));
        }
    }

    @Test
    public void testNotNull() {
        Object obj = new Object();
        for (Predicate<?> predicate : predicates) {
            if (predicate instanceof Predicates.NotNull) {
                assertTrue(predicate.apply(obj));
                break;
            }
            assertFalse(predicate.apply(null));
        }
    }

    @Test
    public void testNot() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.not(predicate1);
        Object obj1 = null;
        Object obj2 = new Object();
        assertFalse(predicate1.apply(obj1));
        assertFalse(predicate2.apply(obj1));
        assertTrue(predicate1.apply(obj2));
        assertTrue(predicate2.apply(obj2));

        Predicate<Object> predicate3 = Predicates.not(Predicates.notNull());
        assertNull(predicate3.apply(null));
        assertNotNull(predicate3.apply(new Object()));
    }

    @Test
    public void testAnd() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Predicate<Object> predicate3 = Predicates.equalTo(new Object());
        Predicate<Object> predicate4 = Predicates.not(predicate3);
        Predicate<Object> predicate5 = Predicates.and(predicate1, predicate2);
        Predicate<Object> predicate6 = Predicates.and(predicate1, predicate3);
        Predicate<Object> predicate7 = Predicates.and(predicate1, predicate4);
        Object obj1 = null;
        Object obj2 = new Object();
        assertFalse(predicate5.apply(obj1));
        assertFalse(predicate6.apply(obj1));
        assertFalse(predicate7.apply(obj1));
        assertFalse(predicate5.apply(obj2));
        assertFalse(predicate6.apply(obj2));
        assertFalse(predicate3.apply(obj2));
        assertFalse(predicate4.apply(obj2));
        assertFalse(predicate5.apply(obj2));
        assertFalse(predicate6.apply(obj2));
        assertFalse(predicate7.apply(obj2));
        assertTrue(predicate5.apply(null));
        assertTrue(predicate5.apply(obj1));
        assertFalse(predicate5.apply(obj2));
        assertFalse(predicate6.apply(null));
        assertFalse(predicate6.apply(obj1));
        assertFalse(predicate6.apply(obj2));
        assertFalse(predicate7.apply(null));
        assertFalse(predicate7.apply(obj1));
        assertTrue(predicate7.apply(obj2));
    }

    @Test
    public void testOr() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Predicate<Object> predicate3 = Predicates.equalTo(new Object());
        Predicate<Object> predicate4 = Predicates.not(predicate3);
        Predicate<Object> predicate5 = Predicates.or(predicate1, predicate2);
        Predicate<Object> predicate6 = Predicates.or(predicate1, predicate3);
        Predicate<Object> predicate7 = Predicates.or(predicate1, predicate4);
        Object obj1 = null;
        Object obj2 = new Object();
        assertFalse(predicate5.apply(obj1));
        assertFalse(predicate5.apply(obj2));
        assertTrue(predicate6.apply(obj1));
        assertFalse(predicate6.apply(obj2));
        assertFalse(predicate7.apply(obj1));
        assertTrue(predicate7.apply(obj2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Object> predicate1 = Predicates.equalTo(null);
        Predicate<Object> predicate2 = Predicates.equalTo(new Object());
        Object obj1 = null;
        Object obj2 = new Object();
        assertFalse(predicate1.apply(obj1));
        assertFalse(predicate1.apply(obj2));
        assertFalse(predicate2.apply(obj1));
        assertFalse(predicate2.apply(null));
        assertTrue(predicate2.apply(obj2));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> predicate1 = Predicates.instanceOf(Object.class);
        Predicate<Object> predicate2 = Predicates.instanceOf(String.class);
        Object obj1 = new Object();
        String obj2 = "test";
        assertFalse(predicate1.apply(obj1));
        assertFalse(predicate1.apply(obj2));
        assertFalse(predicate2.apply(obj1));
        assertTrue(predicate2.apply(obj2));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> predicate1 = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> predicate2 = Predicates.assignableFrom(Object.class);
        assertFalse(predicate1.apply(Integer.class));
        assertFalse(predicate1.apply(Object.class));
        assertTrue(predicate1.apply(String.class));
        assertFalse(predicate2.apply(Integer.class));
        assertTrue(predicate2.apply(Object.class));
    }

    @Test
    public void testIn() {
        Collection<Object> collection1 = Arrays.asList(new Object(), new Object());
        Predicate<Object> predicate1 = Predicates.in(collection1);
        Object obj1 = new Object();
        Object obj2 = collection1.get(0);
        assertFalse(predicate1.apply(obj1));
        assertTrue(predicate1.apply(obj2));
    }

    @Test
    public void testCompose() {
        Predicate<String> predicate1 = Predicates.isNull();
        Function<String, Boolean> function1 = new Function<String, Boolean>() {
            @Override
            public Boolean apply(String input) {
                return input.isEmpty();
            }
        };
        Predicate<String> predicate2 = Predicates.compose(predicate1, function1);
        String str1 = null;
        String str2 = "";
        assertFalse(predicate1.apply(str1));
        assertFalse(predicate1.apply(str2));
        assertFalse(predicate2.apply(str1));
        assertTrue(predicate2.apply(str2));
    }

    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> predicate1 = Predicates.containsPattern(Pattern.compile("\\w+"));
        CharSequence str1 = "test";
        CharSequence str2 = "testing";
        assertFalse(predicate1.apply(str1));
        assertTrue(predicate1.apply(str2));
    }
}