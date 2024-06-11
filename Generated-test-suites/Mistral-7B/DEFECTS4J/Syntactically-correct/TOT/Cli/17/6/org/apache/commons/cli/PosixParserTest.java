package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test Cases for PosixParser class.
 */
public class PosixParserTest {

 /**
  * Test for flattening simple options.
  */
 @Test
 public void testSimpleOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C with argument");
 options.addOption("d", false, "Option D with arguments");
 String[] args = {"-a", "--b", "-c", "--d", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);

 // check the result
 assertEquals(6, result.length);
 assertEquals("-a", result[0]);
 assertEquals("--b", result[1]);
 assertEquals("-c", result[2]);
 assertEquals("--d", result[3]);
 assertEquals("arg1", result[4]);
 assertEquals("arg2", result[5]);
 }

 /**
  * Test for flattening options with arguments.
  */
 @Test
 public void testOptionsWithArguments() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", true, "Option C with required argument");
 options.addOption("d", false, "Option D with optional argument");
 options.addOption("e", false, "Option E with multiple arguments");
 String[] args = {"-a", "--c=arg1", "-b", "--d=arg1", "--d=arg2", "--e=arg1,arg2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);

 // check the result
 assertEquals(7, result.length);
 assertEquals("-a", result[0]);
 assertEquals("--c", result[1]);
 assertEquals("arg1", result[2]);
 assertEquals("-b", result[3]);
 assertEquals("--d", result[4]);
 assertEquals("arg1", result[5]);
 assertEquals("arg2", result[6]);
 }

 /**
  * Test for flattening options with no arguments.
  */
 @Test
 public void testOptionsWithNoArguments() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 String[] args = {"-a", "--b", "-c"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);

 // check the result
 assertEquals(3, result.length);
 assertEquals("-a", result[0]);
 assertEquals("--b", result[1]);
 assertEquals("-c", result[2]);
 }

 /**
  * Test for flattening options with long option names.
  */
 @Test
 public void testLongOptions() {
 Options options = new Options();
 options.addOption("longOptionA", true, "Long Option A");
 options.addOption("longOptionB", false, "Long Option B");
 options.addOption("longOptionC", false, "Long Option C with argument");
 options.addOption("longOptionD", false, "Long Option D with arguments");
 String[] args = {"--longOptionA", "--no-longOptionB", "--longOptionC=arg1", "--longOptionD=arg1,arg2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);

 // check the result
 assertEquals(6, result.length);
 assertEquals("--longOptionA", result[0]);
 assertEquals("", result[1]);
 assertEquals("--longOptionC", result[2]);
 assertEquals("arg1", result[3]);
 assertEquals("--longOptionD", result[4]);
 assertEquals("arg1,arg2", result[5]);
 }

 /**
  * Test for flattening options with stop at non-option.
  */
 @Test
 public void testStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");String[] args = {"abcd", "--no-option"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);

 // check the result
 assertEquals(3, result.length);
 assertEquals("abcd", result[0]);
 assertEquals("--no-option", result[1]);
 assertEquals("", result[2]);
 }

 /**
  * Test for flattening invalid options.
  */
 @Test(expected = ParseException.class)
 public void testInvalidOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 String[] args = {"--invalidOption"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 /**
  * Test for flattening empty options.
  */
 @Test
 public void testEmptyOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 // check the result
 assertEquals(0, result.length);
 }

 /**
  * Test for flattening empty arguments.
  */
 @Test
 public void testEmptyArguments() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] args = {"-a"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 // check the result
 assertEquals(1, result.length);
 assertEquals("-a", result[0]);
 }
}