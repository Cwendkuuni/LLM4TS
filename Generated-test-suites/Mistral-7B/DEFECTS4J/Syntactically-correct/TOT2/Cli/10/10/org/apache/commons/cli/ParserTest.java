package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Parser class.
 */
public class ParserTest {

 private CommandLineParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 options.addRequiredOption("o", "output", true, "Output file path");
 parser = new DefaultParser();
 }

 /**
 * Test parsing with required options.
 */
 @Test
 public void testParseWithRequiredOptions() throws ParseException {
 String[] args = new String[]{"-i", "input.txt", "-o", "output.txt"};
 CommandLine cmd = parser.parse(options, args);
 Assert.assertNotNull(cmd);
 Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
 }

 /**
 * Test parsing with multiple values for an option.
 */
 @Test
 public void testParseWithMultipleValuesForOption() throws ParseException {
 options.addOption("a", "args", true, "Arguments", true);
 String[] args = new String[]{"-a", "arg1", "--", "arg2", "arg3"};
 CommandLine cmd = parser.parse(options, args);
 List<String> argsList = cmd.getArgs();
 Assert.assertEquals(3, argsList.size());
 Assert.assertEquals("arg1", argsList.get(0));
 Assert.assertEquals("arg2", argsList.get(1));
 Assert.assertEquals("arg3", argsList.get(2));
 }

 /**
 * Test parsing with an unrecognized option.
 */
 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnrecognizedOption() throws ParseException {
 String[] args = new String[]{"--unknown-option"};
 parser.parse(options, args);
 }

 /**
 * Test parsing with missing required options.
 */
 @Test(expected = MissingArgumentException.class)
 public void testParseWithMissingRequiredOption() throws ParseException {
 String[] args = new String[]{"-o"};
 parser.parse(options, args);
 }

 /**
 * Test parsing with mixed options and arguments.
 */
 @Test
 public void testParseWithMixedOptionsAndArguments() throws ParseException {
 String[] args = new String[]{"-i", "input.txt", "-o", "output.txt", "--", "arg1", "arg2"};
 CommandLine cmd = parser.parse(options, args);
 Assert.assertNotNull(cmd);
 Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
 List<String> argsList = cmd.getArgs();
 Assert.assertEquals(2, argsList.size());
 Assert.assertEquals("arg1", argsList.get(0));
 Assert.assertEquals("arg2", argsList.get(1));
 }

 /**
 * Test parsing with properties.
 */
 @Test
 public void testParseWithProperties() throws ParseException {
 Properties properties = new Properties();
 properties.setProperty("input", "input.txt");
 properties.setProperty("output", "output.txt");
 CommandLine cmd = parser.parse(options, null, properties);
 Assert.assertNotNull(cmd);
 Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
 Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
 }
}