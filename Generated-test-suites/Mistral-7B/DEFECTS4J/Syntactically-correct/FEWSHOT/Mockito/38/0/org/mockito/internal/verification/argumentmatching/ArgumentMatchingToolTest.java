package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.type.IsInstanceOf.instanceOf;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyListAndArray_ReturnsEmptyArray() {
 List<Matcher> emptyList = Arrays.asList();
 Object[] emptyArray = new Object[0];
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(emptyList, emptyArray);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedListAndArrayLengths_ReturnsEmptyArray() {
 List<Matcher> mismatchedList = Arrays.asList(instanceOf(String.class), instanceOf(Integer.class));
 Object[] mismatchedArray = new Object[]{"foo", 1};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(mismatchedList, mismatchedArray);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches_ReturnsEmptyArray() {
 List<Matcher> noSuspiciousList = Arrays.asList(instanceOf(String.class), instanceOf(String.class));
 Object[] noSuspiciousArray = new Object[]{"foo", "bar"};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(noSuspiciousList, noSuspiciousArray);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_OneSuspiciousMatch_ReturnsCorrectIndex() {
 List<Matcher> suspiciousList = Arrays.asList(not(instanceOf(String.class)), instanceOf(Integer.class));
 Object[] suspiciousArray = new Object[]{"bar", 1};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(suspiciousList, suspiciousArray);
 assertArrayEquals(new Integer[]{0}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspiciousMatches_ReturnsCorrectIndexes() {
 List<Matcher> multipleSuspiciousList = Arrays.asList(not(instanceOf(List.class)), instanceOf(List.class), not(instanceOf(List.class)), instanceOf(List.class));
 Object[] multipleSuspiciousArray = new Object[]{new LinkedList<>(), Arrays.asList(1, 2), null, Arrays.asList(3, 4)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(multipleSuspiciousList, multipleSuspiciousArray);
 assertArrayEquals(new Integer[]{0, 2}, result);
 }
}