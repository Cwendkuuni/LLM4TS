package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

 @Test
 public void testDiffWithEmptyStrings() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "";
 String b = "";
 RelativeString expected = new RelativeString();
 RelativeString result = stringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffWithIdenticalStrings() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "teststring";
 String b = "teststring";
 RelativeString expected = new RelativeString();
 RelativeString result = stringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffWithSingleCharacterDifference() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "teststring";
 String b = "teststring1";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(a.lastIndexOf("t"), b.lastIndexOf("t"), 1));
 StringLink link = expected.getLink(0);
 assertEquals(link.getLen(), 1);
 RelativeString result = stringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffWithMultipleCharacterDifferences() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "teststring123";
 String b = "teststring456";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(a.lastIndexOf("123"), b.lastIndexOf("456"), 3));
 StringLink link1 = expected.getLink(0);
 assertEquals(link1.getLen(), 3);
 expected.addLink(new StringLink(a.lastIndexOf("t"), b.lastIndexOf("t"), 1));
 StringLink link2 = expected.getLink(1);
 assertEquals(link2.getLen(), 1);
 RelativeString result = stringIncrementor.diff(a, b);
 assertEquals(expected, result);
 }

 @Test
 public void testDiffWithVerboseMode() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "teststring123";
 String b = "teststring456";
 RelativeString expected = new RelativeString();
 expected.addLink(new StringLink(a.lastIndexOf("123"), b.lastIndexOf("456"), 3));
 StringLink link1 = expected.getLink(0);
 assertEquals(link1.getLen(), 3);
 expected.addLink(new StringLink(a.lastIndexOf("t"), b.lastIndexOf("t"), 1));
 StringLink link2 = expected.getLink(1);
 assertEquals(link2.getLen(), 1);
 StringIncrementor.DiffOptions options = new StringIncrementor.DiffOptions();
 options.setVerbose(true);
 RelativeString result = stringIncrementor.diff(a, b, options);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchWithEmptyStrings() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "";
 String patch = "";
 String expected = "";
 String result = stringIncrementor.patch(a, null);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchWithIdenticalStrings() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "teststring";
 String patch = "";
 String expected = "teststring";
 String result = stringIncrementor.patch(a, null);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchWithSingleCharacterDifference() {
 StringIncrementor stringIncrementor = new StringIncrementor();String a = "teststring";
 String patch = new RelativeString();
 patch.addLink(new StringLink(a.lastIndexOf("t"), 0, 1));
 patch.setAbsolute("1");
 String expected = "teststring1";
 String result = stringIncrementor.patch(a, patch);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchWithMultipleCharacterDifferences() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "teststring123";
 String patch = new RelativeString();
 patch.addLink(new StringLink(a.lastIndexOf("123"), 11, 3));
 patch.setAbsolute("456");
 String expected = "teststring456";
 String result = stringIncrementor.patch(a, patch);
 assertEquals(expected, result);
 }

 @Test
 public void testPatchWithMixedDifferences() {
 StringIncrementor stringIncrementor = new StringIncrementor();
 String a = "teststring123";
 String patch = new RelativeString();
 patch.addLink(new StringLink(a.lastIndexOf("123"), 11, 3));
 patch.setAbsolute("456");
 patch.addLink(new StringLink(a.lastIndexOf("t"), 0, 1));
 patch.setAbsolute("1");
 String expected = "teststring451";
 String result = stringIncrementor.patch(a, patch);
 assertEquals(expected, result);
 }
}