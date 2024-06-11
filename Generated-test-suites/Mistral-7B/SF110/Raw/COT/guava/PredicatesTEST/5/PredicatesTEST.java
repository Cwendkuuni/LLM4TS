package com.example.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class PredicatesTest {

  @Test
  public void testAlwaysTrue() {
    Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
    assertTrue(alwaysTrue.apply(null));
    assertTrue(alwaysTrue.apply(new Object()));
  }

  @Test
  public void testAlwaysFalse() {
    Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
    assertFalse(alwaysFalse.apply(null));
    assertFalse(alwaysFalse.apply(new Object()));
  }

  @Test
  public void testIsNull() {
    Predicate<Object> isNull = Predicates.isNull();
    assertNull(isNull.apply(null));
    assertNull(isNull.apply(new Object()));
    assertTrue(isNull.apply(null));
  }

  @Test
  public void testIsNotNull() {
    Predicate<Object> isNotNull = Predicates.notNull();
    assertNotNull(isNotNull.apply(null));
    assertNotNull(isNotNull.apply(new Object()));
    assertFalse(isNotNull.apply(null));
  }

  @Test
  public void testNot() {
    Predicate<Object> predicate = mock(Predicate.class);
    Predicate<Object> notPredicate = Predicates.not(predicate);

    when(predicate.apply(null)).thenReturn(true);
    assertFalse(notPredicate.apply(null));

    when(predicate.apply(new Object())).thenReturn(false);
    assertTrue(notPredicate.apply(new Object()));
  }

  @Test
  public void testAnd() {
    Predicate<String> first = Predicates.isNull();
    Predicate<String> second = Predicates.equalTo("test");

    Predicate<String> andPredicate = Predicates.and(first, second);

    assertFalse(andPredicate.apply(null));
    assertFalse(andPredicate.apply(""));
    assertTrue(andPredicate.apply("test"));
  }

  @Test
  public void testAndWithIterable() {
    Predicate<String> first = Predicates.isNull();
    Predicate<String> second = Predicates.notNull();
    Predicate<String> third = Predicates.equalTo("test");

    List<Predicate<String>> components = Arrays.asList(first, second, third);
    Predicate<String> andPredicate = Predicates.and(components);

    assertFalse(andPredicate.apply(null));
    assertFalse(andPredicate.apply(""));
    assertFalse(andPredicate.apply(""));
    assertTrue(andPredicate.apply("test"));
  }

  @Test
  public void testAndWithArray() {
    Predicate<String> first = Predicates.isNull();
    Predicate<String> second = Predicates.notNull();
    Predicate<String> third = Predicates.equalTo("test");

    Predicate<String> andPredicate = Predicates.and(first, second, third);

    assertFalse(andPredicate.apply(null));
    assertFalse(andPredicate.apply(""));
    assertFalse(andPredicate.apply(""));
    assertTrue(andPredicate.apply("test"));
  }

  @Test
  public void testOr() {
    Predicate<String> first = Predicates.isNull();
    Predicate<String> second = Predicates.notNull();

    Predicate<String> orPredicate = Predicates.or(first, second);

    assertFalse(orPredicate.apply(null));
    assertTrue(orPredicate.apply(new Object()));
    assertTrue(orPredicate.apply(""));
  }

  @Test
  public void testOrWithIterable() {
    Predicate<String> first = Predicates.isNull();
    Predicate<String> second = Predicates.notNull();
    Predicate<String> third = Predicates.equalTo("test");

    List<Predicate<String>> components = Arrays.asList(first, second, third);
    Predicate<String> orPredicate = Predicates.or(components);

    assertFalse(orPredicate.apply(null));
    assertTrue(orPredicate.apply(""));
    assertTrue(orPredicate.apply("test"));
  }

  @Test
  public void testOrWithArray() {
    Predicate<String> first = Predicates.isNull();
    Predicate<String> second = Predicates.notNull();
    Predicate<String> third = Predicates.equalTo("test");

    Predicate<String> orPredicate = Predicates.or(first, second, third);

    assertFalse(orPredicate.apply(null));
    assertTrue(orPredicate.apply(""));
    assertTrue(orPredicate.apply("test"));
  }

  @Test
  public void testInstanceOf() {
    Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);

    assertTrue(instanceOfPredicate.apply(""));
    assertFalse(instanceOfPredicate.apply(new Object()));
    assertFalse(instanceOfPredicate.apply(new Integer(1)));
  }

  @Test
  public void testAssignableFrom() {
    Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(String.class);

    assertTrue(assignableFromPredicate.apply(String.class));
    assertTrue(assignableFromPredicate.apply(StringBuilder.class));
    assertFalse(assignableFromPredicate.apply(Integer.class));
  }

  @Test
  public void testIn() {
    Predicate<Integer> inPredicate = Predicates.in(Arrays.asList(1, 2, 3));

    assertFalse(inPredicate.apply(-1));
    assertTrue(inPredicate.apply(1));
    assertTrue(inPredicate.apply(2));
    assertTrue(inPredicate.apply(3));
  }

  @Test(expected = NullPointerException.class)
  public void testInWithNullList() {
    Predicate<Integer> inPredicate = Predicates.in(null);
  }

  @Test
  public void testInWithEmptyList() {
    Predicate<Integer> inPredicate = Predicates.in(Collections.emptyList());

    assertFalse(inPredicate.apply(-1));
    assertFalse(inPredicate.apply(0));
    assertFalse(inPredicate.apply(1));
  }

  @Test
  public void testCompose1() {
    Predicate<String> predicate = Predicates.isNull();
    Function<String, Boolean> function = s -> s.length() > 0;

    Predicate<String> composedPredicate = Predicates.compose(predicate, function);

    assertFalse(composedPredicate.apply(null));
    assertFalse(composedPredicate.apply(""));
    assertTrue(composedPredicate.apply("test"));
  }

  @Test
  public void testCompose2() {
    Predicate<String> predicate = Predicates.notNull();
    Function<String, Integer> function = s -> s.length();

    Predicate<String> composedPredicate = Predicates.compose(predicate, function);

    assertNotNull(composedPredicate.apply("test"));
    assertNull(composedPredicate.apply(null));
  }

  @Test(expected = NullPointerException.class)
  public void testComposeWithNullFunction() {
    Predicate<String> predicate = Predicates.notNull();
    Function<String, Integer> function = null;

    Predicate<String> composedPredicate = Predicates.compose(predicate, function);
  }

  @Test
  public void testContainsPattern() {
    Predicate<String> containsPatternPredicate = Predicates.containsPattern("test.*");

    assertFalse(containsPatternPredicate.apply(""));
    assertFalse(containsPatternPredicate.apply("hello"));
    assertTrue(containsPatternPredicate.apply("test"));
    assertTrue(containsPatternPredicate.apply("test123"));
  }

  @Test
  public void testContainsPatternFromString() {
    Predicate<String> containsPatternPredicate = Predicates.containsPattern("test.*");

    assertTrue(containsPatternPredicate.apply("test"));
    assertTrue(containsPatternPredicate.apply("test123"));
  }
}