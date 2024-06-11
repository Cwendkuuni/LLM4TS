package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

 private Object[] getTestArguments() {
 return new Object[] { 1, "one", "two", 2.0, 'c', new ContainsExtraTypeInformation<String>() {
 @Override public boolean matches(Object argument) {
 return argument.equals("three");
 }

 @Override public String description() {
 return "contains three";
 }

 @Override public String toString() {
 return "containsThree()";
 }
 }, new ContainsExtraTypeInformation<Integer>() {
 @Override public boolean matches(Object argument) {
 return argument.equals(5);
 }

 @Override public String description() {
 return "contains five";
 }

 @Override public String toString() {
 return "containsFive()";
 }
 }, null };
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
 List<Matcher> matchers = Arrays.asList();
 Object[] arguments = getTestArguments();
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSize() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation<Object>() {
 @Override public boolean matches(Object argument) {
 return true;
 }

 @Override public String description() {
 return "always matches";
 }

 @Override public String toString() {
 return "alwaysMatches()";
 }
 });
 Object[] arguments = getTestArguments();
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsExtraTypeInformation<Integer>() {
 @Override public boolean matches(Object argument) {
 return argument instanceof Integer && ((Integer) argument).intValue() == 1;
 }

 @Override public String description() {
 return "contains one";
 }

 @Override public String toString() {
 return "containsOne()";
 }
 },
 new ContainsExtraTypeInformation<String>() {
 @Override public boolean matches(Object argument) {
 return argument.equals("two");
 }

 @Override public String description() {
 return "contains two";
 }

 @Override public String toString() {
 return "containsTwo()";
 }
 },
 new ContainsExtraTypeInformation<Double>() {
 @Override public boolean matches(Object argument) {
 return argument instanceof Double && ((Double) argument).doubleValue() == 2.0;
 }

 @Override public String description() {
 return "contains two point zero";
 }

 @Override public String toString() {
 return "containsTwoPointZero()";
 }
 },
 new ContainsExtraTypeInformation<Character>() {
 @Override public boolean matches(Object argument) {
 return argument.equals('c');
 }

 @Override public String description() {
 return "contains c";
 }

 @Override public String toString() {
 return "containsC()";
 }
 },
 new ContainsExtraTypeInformation<String>() {
 @Override public boolean matches(Object argument) {
 return argument.equals("three");
 }

 @Override public String description() {
 return "contains three";
 }

 @Override public String toString() {
 return "containsThree()";
 }
 },
 null
 );
 Object[] arguments = getTestArguments();
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[] { 5, 6 }, indexes);
 }
}