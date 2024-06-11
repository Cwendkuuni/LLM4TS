package de.beiri22.stringincrementor.test;

import org.junit.*;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

@Test
public void testDiff() {
   String a = "abcdefghijklm";
   String b = "abcdefghijklmnop";
   RelativeString expected = new RelativeString();
   StringLink link = new StringLink(5, 5, 5);
   expected.addLink(link);
   RelativeString result = StringIncrementor.diff(a, b);
   assertEquals(expected, result);
}

@Test
public void testDiffVerbose() {
   String a = "abcdefghijklm";
   String b = "abcdefghijklmnop";
   RelativeString expected = new RelativeString();
   StringLink link = new StringLink(5, 5, 5);
   expected.addLink(link);
   RelativeString result = StringIncrementor.diff(a, b, true);
   assertEquals(expected, result);
}

@Test
public void testPatch() {
   String a = "abcdefghijklm";
   RelativeString r = new RelativeString();
   StringLink link = new StringLink(5, 5, 5);
   r.addLink(link);
   String expected = "abcdefghijklmnop";
   String result = StringIncrementor.patch(a, r);
   assertEquals(expected, result);
}

@Test
public void testPatchVerbose() {
   String a = "abcdefghijklm";
   RelativeString r = new RelativeString();
   StringLink link = new StringLink(5, 5, 5);
   r.addLink(link);
   String expected = "abcdefghijklmnop";
   String result = StringIncrementor.patch(a, r, true);
   assertEquals(expected, result);
}

@Test(expected = IllegalArgumentException.class)
public void testNullDiffInput() {
   String a = null;
   String b = "abcdefghijklm";
   StringIncrementor.diff(a, b);
}

@Test(expected = IllegalArgumentException.class)
public void testNullPatchInput() {
   String a = "abcdefghijklm";
   RelativeString r = null;
   StringIncrementor.patch(a, r);
}

@Test(expected = IllegalArgumentException.class)
public void testLongInput() {
   String a = new String(new byte[10000000]);
   String b = new String(new byte[10000001]);
   try {
      StringIncrementor.diff(a, b);
   } catch (IllegalArgumentException e) {
      // expected
   }
}

@Test(expected = IllegalArgumentException.class)
public void testLongInputPatch() {
   String a = new String(new byte[10000000]);
   RelativeString r = new RelativeString();
   try {
      StringIncrementor.patch(a, r);
   } catch (IllegalArgumentException e) {
      // expected
   }
}

}