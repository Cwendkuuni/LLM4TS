package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcherList_ReturnsEmptyArray() {
     Object[] arguments = new Object[]{"arg1", 1, 3.14};
     Assert.assertArrayEquals(new Integer[0], getSuspiciouslyNotMatchingArgsIndexes(null, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatcherList_ReturnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList();
     Object[] arguments = new Object[]{"arg1", 1, 3.14};
     Assert.assertArrayEquals(new Integer[0], getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize_ReturnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(is("arg1"), is(1), is(3.14));
     Object[] arguments = new Object[]{"arg1", "arg2", 3.14};
     Assert.assertArrayEquals(new Integer[0], getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches_ReturnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(is("arg1"), is(1), is(3.14));
     Object[] arguments = new Object[]{"arg1", 1, 3.14};
     Assert.assertArrayEquals(new Integer[0], getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatches_ReturnsNonEmptyArray() {
     List<Matcher> matchers = Arrays.asList(
         is("String").withSource(new StringSource("arg1")),
         is("Integer"),
         is(double.class),
         is("Integer").withSource(new StringSource("arg3"))
     );
     Object[] arguments = new Object[]{"arg1", 1, 3.14, "arg3"};
     Integer[] suspicious = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1, 3}, suspicious);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatches_ReturnsNonEmptyArray_CustomMatcher() {
     List<Matcher> matchers = Arrays.asList(
         new TypeSafeMatcher<String>() {
             @Override
             public boolean matchesSafely(String item) {
                 return item.startsWith("arg");
             }

             @Override
             public void describeTo(StringDescription description) {
                 description.appendText("a string starting with 'arg'");
             }
         },
         is(1),
         is(double.class)
     );
     Object[] arguments = new Object[]{"arg2", 1, 3.14};
     Integer[] suspicious = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0}, suspicious);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatches_ReturnsNonEmptyArray_MismatchedType_CustomMatcher() {
     List<Matcher> matchers = Arrays.asList(
         is("String").withSource(new StringSource("arg1")),
         new TypeSafeMatcher<Integer>() {
             @Override
             public boolean matchesSafely(Integer item) {
                 return item > 0;
             }

             @Override
             public void describeTo(StringDescription description) {
                 description.appendText("an integer greater than 0");
             }
         },
         is(double.class)
     );
     Object[] arguments = new Object[]{"arg1", 0, 3.14};
     Integer[] suspicious = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, suspicious);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_IllegalArgumentException_MismatchedSize() {
     List<Matcher> matchers = Arrays.asList(is("arg1"), is(1), is(3.14));
     Object[] arguments = new Object[]{"arg1", 1, 3.14, "arg3"};
     getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 private static Matcher<Object> is(Object value) {
     return new TypeSafeMatcher<Object>() {
         @Override
         public boolean matchesSafely(Object item) {
             return item != null && item.equals(value);
         }

         @Override
         public void describeTo(StringDescription description) {
             description.appendText(String.valueOf(value));
         }
     };
 }

 private static Matcher<Object> is(Class<? extends Object> clazz) {
     return new TypeSafeMatcher<Object>() {
         @Override
         public boolean matchesSafely(Object item) {
             return clazz.isInstance(item);
         }

         @Override
         public void describeTo(StringDescription description) {
             description.appendText(clazz.getSimpleName());
         }
     };
 }

 static class StringSource {
     private final String source;

     StringSource(String source) {
         this.source = source;
     }

     @Override
     public String toString() {
         return source;
     }
 }
}