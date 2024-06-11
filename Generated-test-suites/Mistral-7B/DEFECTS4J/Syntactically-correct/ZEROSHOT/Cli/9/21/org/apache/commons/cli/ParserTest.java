package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.test.ParserTest;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGS = {"--foo", "1", "--bar", "2"};

 @Test
 public void testParse() throws Exception {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("foo", true, "Foo option");
 options.addOption("bar", true, "Bar option");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS, (Properties) null, false);

 assertEquals(2, cmd.getNumArgs());
 assertTrue(cmd.hasOption("foo"));
 assertTrue(cmd.hasOption("bar"));
 assertFalse(cmd.hasOption("baz"));
 assertEquals("1", cmd.getOptionValue("foo"));
 assertEquals("2", cmd.getOptionValue("bar"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws Exception {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("foo", true, "Foo option");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, new String[0], (Properties) null, false);
 fail("Expected ParseException");
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws Exception {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("foo", true, "Foo option");
 options.addRequiredOption("bar", true, "Bar option");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS, (Properties) null, false);
 assertTrue(cmd.hasOption("foo"));
 assertFalse(cmd.hasOption("bar"));
 throw new MissingOptionException("Missing required option: bar");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws Exception {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("foo", true, "Foo option");
 parser.setOptions(options);
 CommandLine cmd = parser.parse(options, ARGS, (Properties) null, false);
 assertFalse(cmd.hasOption("baz"));
 throw new UnrecognizedOptionException("Unrecognized option: baz");
 }

 @Test
 public void testParseWithProperties() throws Exception {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("foo", true, "Foo option");
 parser.setOptions(options);
 Properties props = new Properties();
 props.setProperty("foo", "1");
 CommandLine cmd = parser.parse(options, new String[0], props, false);
 assertTrue(cmd.hasOption("foo"));
 assertEquals("1", cmd.getOptionValue("foo"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws Exception {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("foo", true, "Foo option");
 parser.setOptions(options);
 String[] args = {"--foo", "-n", "arg", "--bar"};
 CommandLine cmd = parser.parse(options, args, (Properties) null, true);
 assertEquals(2, cmd.getNumArgs());
 assertTrue(cmd.hasOption("foo"));
 assertTrue(cmd.hasOption("bar"));
 assertEquals("arg", cmd.getArgList().get(0));
 assertEquals("", cmd.getArgList().get(1));
 }
}