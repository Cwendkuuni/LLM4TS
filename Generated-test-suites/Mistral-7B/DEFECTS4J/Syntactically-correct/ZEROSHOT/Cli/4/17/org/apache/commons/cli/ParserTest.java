package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 // Create a new Parser and set up some test options
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 3, "Option C with an integer argument");
 options.addOption("d", null, "Option D with an optional string argument");
 options.addRequiredOption("e", true, "Required Option E");
 options.addRequiredOption("f", true, "Required Option F");

 // Test parsing with no arguments
 CommandLine cmd = parser.parse(options, new String[0]);
 Assert.assertEquals(0, cmd.getArgs().size());
 Assert.assertEquals(0, cmd.getOptions().size());

 // Test parsing with all required options
 String[] args = {"-e", "-f"};
 cmd = parser.parse(options, args);
 Assert.assertEquals(0, cmd.getArgs().size());
 Assert.assertEquals(2, cmd.getOptions().size());

 // Test parsing with all required and non-required options
 args = new String[]{"-a", "-b", "1", "-c", "3", "-d", "value"};
 cmd = parser.parse(options, args);
 Assert.assertEquals(1, cmd.getArgs().size());
 Assert.assertEquals(5, cmd.getOptions().size());

 // Test parsing with an unrecognized option
 args = new String[]{"-x", "-a", "-b", "1", "-c", "3"};
 Assert.assertTrue(parser.hasErrorMessage());
 Assert.assertEquals("Unrecognized option: x", parser.getErrorMessage());

 // Test parsing with an option with an invalid integer argument
 args = new String[]{"-b", "abcd"};
 Assert.assertTrue(parser.hasErrorMessage());
 Assert.assertEquals("Invalid value for option: 'b'. ", "Invalid value for option: 'b'. Expected: a number, received: abcd", parser.getErrorMessage());

 // Test parsing with an option with an invalid string argument
 args = new String[]{"-d", "abc"};
 Assert.assertTrue(parser.hasErrorMessage());
 Assert.assertEquals("Invalid value for option: 'd'. ", "Invalid value for option: 'd'. Expected: a String, received: abc", parser.getErrorMessage());
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 // Create a new Parser and set up some test options
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("p", true, "Option P");
 options.addOption("q", 1, "Option Q with an integer argument");
 options.addOption("r", null, "Option R with an optional string argument");

 // Create some test properties
 Properties properties = new Properties();
 properties.setProperty("p", "value");
 properties.setProperty("q", "123");
 properties.setProperty("r", "default");

 // Test parsing with no arguments
 CommandLine cmd = parser.parse(options, new String[0], properties);
 Assert.assertEquals(0, cmd.getArgs().size());
 Assert.assertEquals(0, cmd.getOptions().size());

 // Test parsing with all required options
 String[] args = {"-e", "-f"};
 cmd = parser.parse(options, args, properties);
 Assert.assertEquals(0, cmd.getArgs().size());
 Assert.assertEquals(2, cmd.getOptions().size());

 // Test parsing with all required and non-required options
 args = new String[]{"-a", "-b", "1", "-c", "3", "-d", "value"};
 cmd = parser.parse(options, args, properties);
 Assert.assertEquals(1, cmd.getArgs().size());
 Assert.assertEquals(5, cmd.getOptions().size());

 // Test parsing with an unrecognized option from properties
 properties.setProperty("x", "x");
 cmd = parser.parse(options, new String[0], properties);
 Assert.assertTrue(parser.hasErrorMessage());
 Assert.assertEquals("Unrecognized option: x", parser.getErrorMessage());

 // Test parsing with an option with an invalid integer argument from properties
 properties.setProperty("q", "abcd");
 cmd = parser.parse(options, new String[0], properties);
 Assert.assertTrue(parser.hasErrorMessage());
 Assert.assertEquals("Invalid value for option: 'q'. ", "Invalid value for option: 'q'. Expected: a number, received: abcd", parser.getErrorMessage());

 // Test parsing with an option with an invalid string argument from properties
 properties.setProperty("r", "invalid");
 cmd = parser.parse(options, new String[0], properties);
 Assert.assertTrue(parser.hasErrorMessage());
 Assert.assertEquals("Invalid value for option: 'r'. ", "Invalid value for option: 'r'. Expected: a String, received: invalid", parser.getErrorMessage());
 }
}