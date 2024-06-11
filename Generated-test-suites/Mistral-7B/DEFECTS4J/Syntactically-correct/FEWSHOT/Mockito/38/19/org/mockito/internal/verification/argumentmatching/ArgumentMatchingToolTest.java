package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 private static class TestMatcher extends TypeSafeMatcher<Object> {
  private final Class<?> expectedType;

  TestMatcher(Class<?> expectedType) {
    this.expectedType = expectedType;
  }

  @Override
  protected boolean matchesSafely(Object item) {
    return expectedType.isInstance(item);
  }

  @Override
  public String toString() {
    return "a matcher for type " + expectedType.getName();
  }
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes() {
  List<Matcher<Object>> matchers = new LinkedList<>();
  matchers.add(new TestMatcher(String.class));
  matchers.add(new TestMatcher(Integer.class));

  Object[] args = new Object[] {"foo", 1};

  Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
  assertArrayEquals(new Integer[0], result);

  args = new Object[] {" foo", 1};
  result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
  assertArrayEquals(new Integer[0], result);

  args = new Object[] {"foo", 1L};
  result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
  assertArrayEquals(new Integer[0], result);

  args = new Object[] {"foo", new TestMatcher(String.class)};
  result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
  assertArrayEquals(new Integer[]{0}, result);

  args = new Object[] {"foo", new Object() {}};
  result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
  assertArrayEquals(new Integer[]{1}, result);

  args = new Object[] {"foo", new Object() {}, "bar"};
  result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
  assertArrayEquals(new Integer[0], result);

  matchers.set(0, new ContainsExtraTypeInformation(String.class, String[].class));
  result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[] {"foo", "bar"});
  assertArrayEquals(new Integer[0], result);

  matchers.set(0, new ContainsExtraTypeInformation(String.class, String[].class) {
    @Override
    public boolean typeMatches(Object argument) {
      return argument instanceof String[];
    }
  });
  result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[] {"foo", "bar"});
  assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArgs() {
  List<Matcher<Object>> matchers = new LinkedList<>();
  matchers.add(new TestMatcher(String.class));
  matchers.add(new TestMatcher(Integer.class));

  Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, (Object[]) null);
  assertNull(result);
 }
}