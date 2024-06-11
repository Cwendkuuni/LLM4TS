package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {

  @Test
  public void testAlwaysTrue() {
    Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();

    // Typical use case
    assertTrue(alwaysTrue.apply(42));

    // Edge case: null input
    assertFalse(alwaysTrue.apply(null));

    // Error scenario: passing an incorrect type
    try {
      Predicate<String> alwaysTrueString = Predicates.alwaysTrue();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}