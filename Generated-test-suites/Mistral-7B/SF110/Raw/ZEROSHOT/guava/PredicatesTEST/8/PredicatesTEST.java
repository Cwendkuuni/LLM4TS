package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;
import static org.junit.Assert.*;;

@RunWith(Parameterized.class)
public class PredicatesTest {

 @Parameters(name = "{index}: testPredicate{0} applied to {1} = {2}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // alwaysTrue
 {Predicates.alwaysTrue(), null, true},
 {Predicates.alwaysTrue(), new Object(), true},
 // alwaysFalse
 {Predicates.alwaysFalse(), null, false},
 {Predicates.alwaysFalse(), new Object(), false},
 // isNull
 {Predicates.isNull(), null, true},
 {Predicates.isNull(), new Object(), false},
 // notNull
 {Predicates.notNull(), null, false},
 {Predicates.notNull(), new Object(), true},
 // not
 {Predicates.not(Predicates.alwaysTrue()), null, false},
 {Predicates.not(Predicates.alwaysTrue()), new Object(), true},
 // and
 {Predicates.and(Predicates.isNull(), Predicates.alwaysFalse()), null, false},
 {Predicates.and(Predicates.isNull(), Predicates.alwaysFalse()), new Object(), false},
 {Predicates.and(Predicates.isNull(), Predicates.notNull()), null, false},
 {Predicates.and(Predicates.isNull(), Predicates.notNull()), new Object(), false},
 {Predicates.and(Predicates.isNull(), Predicates.notNull(), Predicates.notNull()), null, false},
 {Predicates.and(Predicates.isNull(), Predicates.notNull(), Predicates.notNull()), new Object(), false},
 // or
 {Predicates.or(Predicates.isNull(), Predicates.isNull()), null, true},
 {Predicates.or(Predicates.isNull(), Predicates.isNull()), new Object(), true},
 {Predicates.or(Predicates.isNull(), Predicates.notNull()), null, true},
 {Predicates.or(Predicates.isNull(), Predicates.notNull()), new Object(), false},
 {Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysFalse()), null, true},
 {Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysFalse()), new Object(), false},
 // equalTo
 {Predicates.equalTo(null), null, true},
 {Predicates.equalTo(null), new Object(), false},
 {Predicates. equalTo('a'), 'a', true},
 {Predicates.equalTo('a'), 'b', false},
 {Predicates.equalTo("foo"), "foo", true},
 {Predicates.equalTo("foo"), "bar", false},
 // instanceOf
 {Predicates.instanceOf(String.class), "foo", true},
 {Predicates.instanceOf(String.class), 1, false},
 {Predicates.instanceOf(Number.class), 1, true},
 {Predicates.instanceOf(Number.class), "foo", false},
 // assignableFrom
 {Predicates.assignableFrom(String.class), Number.class, true},
 {Predicates.assignableFrom(String.class), String.class, true},
 {Predicates.assignableFrom(String.class), Object.class, true},
 {Predicates.assignableFrom(Number.class), String.class, false},
 // in
 {Predicates.in(Arrays.asList("foo", "bar")), "foo", true},
 {Predicates.in(Arrays.asList("foo", "bar")), "baz", false},
 {Predicates.in(Arrays.asList(1, 2, 3)), 1, true},
 {Predicates.in(Arrays.asList(1, 2, 3)), 4, false},
 // compose
 {Predicates.compose(Predicates.isNull(), s -> s.length() == 0), "", true},
 {Predicates.compose(Predicates.isNull(), s -> s.length() == 0), "foobar", false},
 // containsPattern
 {Predicates.containsPattern("foo.*"), "foo", true},
 {Predicates.containsPattern("foo.*"), "bar", false},
 {Predicates.containsPattern(".*bar.*"), "foobar", true},
 {Predicates.containsPattern(".*bar.*"), "foo", false}
 });
 }

 @Parameterized.Parameter(value = 0)
 public Predicate<?> predicate;

 @Parameterized.Parameter(value = 1)
 public Object testObject;

 @Parameterized.Parameter(value = 2, nullable = false)
 public boolean expectedResult;

 @Test
 public void testPredicate() {
 assertFalse("Unexpected result", !this.predicate.apply(this.testObject));
 assertEquals("Unexpected result", this.expectedResult, this.predicate.apply(this.testObject));
 }
}