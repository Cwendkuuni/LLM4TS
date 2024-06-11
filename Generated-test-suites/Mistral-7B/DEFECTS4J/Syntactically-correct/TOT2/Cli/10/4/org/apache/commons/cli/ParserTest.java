package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] INVALID_ARGUMENTS = new String[]{"-t", "--invalid"};
 private static final String[] VALID_ARGUMENTS_NO_OPTIONS = new String[]{"--", "-h", "-t", "--test"};
 private static final String[] VALID_ARGUMENTS_WITH_OPTIONS = new String[]{"--test", "-t", "--foo=bar"};
 private static final String[] VALID_ARGUMENTS_WITH_OPTIONS_AND_ARGUMENT = new String[]{"--test", "--foo=bar", "arg"};

 @Test
 public void testParserEmpty() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 CommandLine cmd = parser.parse(options, new String[0]);
 Assert.assertEquals(0, cmd.getArgs().size());
 Assert.assertNull(cmd.getOptionValues("test"));
 Assert.assertNull(cmd.getOptionValues("foo"));
 }

 @Test(expected = ParseException.class)
 public void testParserMissedRequiredOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("t", "test", true, "Test option");
 CommandLine cmd = parser.parse(options, VALID_ARGUMENTS_NO_OPTIONS);
 }

 @Test(expected = ParseException.class)
 public void testParserInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("i", "invalid", false, "Invalid option");
 CommandLine cmd = parser.parse(options, INVALID_ARGUMENTS);
 }

 @Test(expected = ParseException.class)
 public void testParserMissingArgument() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("t", "test", true, "Test option");
 CommandLine cmd = parser.parse(options, VALID_ARGUMENTS_NO_OPTIONS);
 Assert.assertNull(cmd.getOptionValues("test"));
 Assert.assertThrows(MissingArgumentException.class, () -> cmd.getOptionValues("arg"));
 }

 @Test
 public void testParserValidArguments() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("t", "test", false, "Test option");
 CommandLine cmd = parser.parse(options, VALID_ARGUMENTS_WITH_OPTIONS);
 Assert.assertEquals(2, cmd.getArgs().size());
 Assert.assertEquals(new String[]{"--test"}, cmd.getArgList().toArray(new String[0]));
 Assert.assertArrayEquals(new String[]{"foo"}, cmd.getOptionValues("foo"));
 }

 @Test
 public void testParserValidArgumentsWithArg() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("t", "test", false, "Test option");
 CommandLine cmd = parser.parse(options, VALID_ARGUMENTS_WITH_OPTIONS_AND_ARGUMENT);
 Assert.assertEquals(3, cmd.getArgs().size());
 Assert.assertEquals(new String[]{"--test"}, cmd.getArgList().toArray(new String[0]));
 Assert.assertArrayEquals(new String[]{"foo=bar"}, cmd.getOptionValues("foo"));
 Assert.assertEquals("arg", cmd.getArgs()[2]);
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParserUnrecognizedOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("t", "test", false, "Test option");
 CommandLine cmd = parser.parse(options, new String[]{"--unknown"});
 }

 @Test
 public void testParserOptionsGroup() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("g", "group");
 group.setRequired(true);
 options.addOptionGroup(group);
 group.addOption("t", "test", false, "Test option");
 CommandLine cmd = parser.parse(options, VALID_ARGUMENTS_NO_OPTIONS);
 Assert.assertEquals(1, cmd.getOptions().size());
 Assert.assertTrue(group.isSelected());
 }
}