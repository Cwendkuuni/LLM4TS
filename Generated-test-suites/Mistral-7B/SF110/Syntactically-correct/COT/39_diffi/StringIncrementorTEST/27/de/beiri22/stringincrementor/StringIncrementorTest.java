package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest {

private static final String STRING_A = "The quick brown fox jumps over the lazy dog";
private static final String STRING_B = "The quick red fox jumps over the lazy dog";
private static final String STRING_C = "The quick brown fox jumps over the swift dog";
private static final String STRING_D = "The quick brown fox jumps over the lazy cat";

@Test
public void testDiff_sameStrings() {
RelativeString expected = new RelativeString();
assertEquals(expected, StringIncrementor.diff(STRING_A, STRING_A, true));
}

@Test
public void testDiff_identicalSubstrings() {
RelativeString result = StringIncrementor.diff(STRING_A, STRING_B, true);
RelativeString expected = new RelativeString();
expected.addLink(new StringLink(11, 11, 1));
expected.addLink(new StringLink(13, 13, 1));
assertEquals(expected, result);
}

@Test
public void testDiff_noDifferences() {
RelativeString result = StringIncrementor.diff(STRING_A, STRING_C, true);
assertNull(result);
}

@Test(expected = IllegalArgumentException.class)
public void testDiff_nullStrings() {
StringIncrementor.diff(null, STRING_B, true);
}

@Test(expected = IllegalArgumentException.class)
public void testDiff_nullStringB() {
StringIncrementor.diff(STRING_A, null, true);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testDiff_emptyStringA() {
StringIncrementor.diff("", STRING_B, true);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testDiff_emptyStringB() {
StringIncrementor.diff(STRING_A, "", true);
}

@Test(expected = ArrayIndexOutOfBoundsException.class)
public void testDiff_largeString() {
StringIncrementor.diff(STRING_A, STRING_D, true);
}

@Test
public void testPatch_emptyString() {
String expected = STRING_A;
String result = StringIncrementor.patch(expected, new RelativeString(), true);
assertEquals(expected, result);
}

@Test
public void testPatch_noDifferences() {
String expected = STRING_A;
String result = StringIncrementor.patch(expected, new RelativeString(), true);
assertEquals(expected, result);
}

@Test
public void testPatch_singleDifference() {
String result = StringIncrementor.patch(STRING_A, new RelativeString().addLink(new StringLink(11, 12, 1)), true);
assertEquals(STRING_B, result);
}

@Test(expected = IllegalArgumentException.class)
public void testPatch_nullString() {
StringIncrementor.patch(null, new RelativeString(), true);
}

@Test(expected = IllegalArgumentException.class)
public void testPatch_nullPatchString() {
StringIncrementor.patch(STRING_A, null, true);
}

@Test(expected = IllegalArgumentException.class)
public void testPatch_nullAbsoluteString() {
StringIncrementor.patch(STRING_A, new RelativeString(), null);
}
}