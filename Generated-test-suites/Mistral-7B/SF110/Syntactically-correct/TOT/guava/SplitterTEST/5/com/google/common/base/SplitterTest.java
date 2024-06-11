package com.google.common.base;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 private Splitter separatorMatcherSplitter;
 private Splitter separatorStringSplitter;
 private Splitter separatorPatternSplitter;
 private Splitter fixedLengthSplitter;

 @Before
 public void setup() {
 separatorMatcherSplitter = Splitter.on(CharMatcher.is('-'));
 separatorStringSplitter = Splitter.on("\\-");
 separatorPatternSplitter = Splitter.onPattern("\\-");
 fixedLengthSplitter = Splitter.fixedLength(2);
 }

 @Test
 public void testEmptyString() {
 Iterable<String> emptyStringSplit = Splitter.on("").split(CharSequence.class.cast(""));
 List<String> emptyList = fixedLengthSplitter.splitToList(CharSequence.class.cast(""));
 assertEquals(0, emptyStringSplit.iterator().nextCount());
 assertEquals(0, emptyList.size());
 }

 @Test
 public void testNullString() {
 try {
 Splitter.on((CharSequence)null).split(CharSequence.class.cast(""));
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }

 try {
 fixedLengthSplitter.splitToList(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testSingleCharacterInput() {
 Iterable<String> singleCharacterSplit = separatorMatcherSplitter.split(CharSequence.class.cast("abc-"));
 List<String> fixedLengthSingleCharacterSplit = fixedLengthSplitter.splitToList(CharSequence.class.cast("abc-"));
 List<String> separatorStringSingleCharacterSplit = separatorStringSplitter.splitToList(CharSequence.class.cast("abc-"));
 List<String> separatorPatternSingleCharacterSplit = separatorPatternSplitter.splitToList(CharSequence.class.cast("abc-"));

 assertEquals(Arrays.asList("abc", "-"), Arrays.asList(singleCharacterSplit.iterator().next()));
 assertEquals(Arrays.asList("ab", "c"), fixedLengthSingleCharacterSplit);
 assertEquals(Arrays.asList("ab", "", "c"), separatorStringSingleCharacterSplit);
 assertEquals(Arrays.asList("ab", ""), separatorPatternSingleCharacterSplit);
 }

 @Test
 public void testMultipleCharacterInput() {
 Iterable<String> multipleCharacterInputSplit = separatorMatcherSplitter.split(CharSequence.class.cast("abc--def--ghi"));
 List<String> fixedLengthMultipleCharacterInputSplit = fixedLengthSplitter.splitToList(CharSequence.class.cast("abc--def--ghi"));
 List<String> separatorStringMultipleCharacterInputSplit = separatorStringSplitter.splitToList(CharSequence.class.cast("abc--def--ghi"));
 List<String> separatorPatternMultipleCharacterInputSplit = separatorPatternSplitter.splitToList(CharSequence.class.cast("abc--def--ghi"));

 assertEquals(Arrays.asList("abc", "--", "def", "--", "ghi"), multipleCharacterInputSplit.iterator().next());
 assertEquals(Arrays.asList("ab", "c--", "d--", "f--", "g--", "hi"), fixedLengthMultipleCharacterInputSplit);
 assertEquals(Arrays.asList("ab", "", "c--", "", "d--", "", "f--", "", "g--", "", "hi"), separatorStringMultipleCharacterInputSplit);
 assertEquals(Arrays.asList("ab", "", "c--", "", "d--", "", "f--", "", "g--", "", "hi"), separatorPatternMultipleCharacterInputSplit);
 }

 @Test
 public void testEmptyStringAtEnd() {
 Iterable<String> emptyStringAtEndSplit = separatorMatcherSplitter.split(CharSequence.class.cast("abc-"));
 List<String> emptyStringAtEndFixedLengthSplit = fixedLengthSplitter.splitToList(CharSequence.class.cast("abc-"));

 assertEquals(Arrays.asList("abc"), emptyStringAtEndSplit.iterator().next());
 assertEquals(Arrays.asList("ab", "c"), emptyStringAtEndFixedLengthSplit);
 }

 @Test
 public void testLimit() {
 Iterable<String> limitSplit = separatorMatcherSplitter.limit(2).split(CharSequence.class.cast("abc-def-ghi-jkl-mno"));
 List<String> limitFixedLengthSplit = fixedLengthSplitter.limit(2).splitToList(CharSequence.class.cast("abc-def-ghi-jkl-mno"));

 assertEquals(Arrays.asList("ab", "cd"), limitSplit.iterator().next());
 assertEquals(Arrays.asList("ab", "cd"), limitFixedLengthSplit);
 }

 @Test
 public void testTrimming() {
 Iterable<String> trimmedSplit = separatorMatcherSplitter.trimResults().split(CharSequence.class.cast(" abc--def ghi "));
 List<String> trimmedFixedLengthSplit = fixedLengthSplitter.trimResults().splitToList(CharSequence.class.cast(" abc--def ghi "));
 List<String> trimmedFixedLengthPatternSplit = separatorPatternSplitter.trimResults().splitToList(CharSequence.class.cast(" abc--def ghi "));
 List<String> trimmedStringSplit = separatorStringSplitter.trimResults().splitToList(CharSequence.class.cast(" abc--def ghi "));

 assertEquals(Arrays.asList("abc--def", "ghi"), trimmedSplit.iterator().next());
 assertEquals(Arrays.asList("ab", "cd", "ef"), trimmedFixedLengthSplit);
 assertEquals(Arrays.asList("ab", "", "cd", "", "ef", ""), trimmedFixedLengthPatternSplit);
 assertEquals(Arrays.asList("ab", "", "cd", "", "ef", ""), trimmedStringSplit);
 }

 @Test
 public void testOmittingEmptyStrings() {
 Iterable<String> omitEmptyStringsSplit = separatorMatcherSplitter.omitEmptyStrings().split(CharSequence.class.cast("abc--def--ghi--"));
 List<String> omitEmptyStringsFixedLengthSplit = fixedLengthSplitter.omitEmptyStrings().splitToList(CharSequence.class.cast("abc--def--ghi--"));

 assertEquals(Arrays.asList("def", "ghi"), omitEmptyStringsSplit.iterator().next());
 assertEquals(Arrays.asList("cd", "ef"), omitEmptyStringsFixedLengthSplit);
 }

 @Test
 public void testPatternWithEmptyString() {
 Iterable<String> patternWithEmptyStringSplit = separatorPatternSplitter.split(CharSequence.class.cast("abc--def--ghi--"));

 assertEquals(Arrays.asList("abc--def", "ghi"), patternWithEmptyStringSplit.iterator().next());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 separatorPatternSplitter = Splitter.onPattern("");
 }
}