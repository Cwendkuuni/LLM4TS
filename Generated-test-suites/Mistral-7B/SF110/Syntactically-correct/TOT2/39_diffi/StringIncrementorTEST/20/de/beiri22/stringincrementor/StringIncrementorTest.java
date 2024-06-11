package de.beiri22.stringincrementor;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

@Test
public void testDiff() {
String a = "abcdabcdabcd";
String b = "efghdefghdefgh";
RelativeString result = StringIncrementor.diff(a, b);
assertNotNull(result);
// Add more assertions based on the expected output
}

@Test(expected = IllegalArgumentException.class)
public void testDiffEmptyStrings() {
String a = "";
String b = "";
StringIncrementor.diff(a, b);
}

@Test
public void testDiffStringsDifferentLengths() {
String a = "abcd";
String b = "efghdefgh";
RelativeString result = StringIncrementor.diff(a, b);
assertNotNull(result);
// Add more assertions based on the expected output
}

@Test
public void testDiffStringsSpecialCharacters() {
String a = "abcd !@#$% abcd";
String b = "efgh!@#$% defgh !@#$% defgh";
RelativeString result = StringIncrementor.diff(a, b);
assertNotNull(result);
// Add more assertions based on the expected output
}

@Test
public void testDiffStringsIdenticalSubstrings() {
String a = "abcdabcdabcd";
String b = "efghdefghdefgh";
RelativeString result = StringIncrementor.diff(a, b);
assertNotNull(result);
// Add more assertions based on the expected output
}

@Test(expected = NullPointerException.class)
public void testDiffNullStrings() {
String a = null;
String b = "efghdefgh";
StringIncrementor.diff(a, b);
}

@Test(expected = NullPointerException.class)
public void testDiffNullStrings2() {
String a = "efghdefgh";
String b = null;
StringIncrementor.diff(a, b);
}
}