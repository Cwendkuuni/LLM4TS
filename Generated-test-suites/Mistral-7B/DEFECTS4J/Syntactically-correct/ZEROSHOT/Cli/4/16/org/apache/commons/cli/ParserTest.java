package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParserTest {

 @Test
 public void testParseWithNoArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLine cmd = parser.parse(options, new String[0]);
 assertEquals(0, cmd.getArgs().length);
 assertEquals("output", cmd.getOptionValue("o"));
 }

 @Test
 public void testParseWithSingleArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLine cmd = parser.parse(options, new String[]{"input.txt"});
 assertEquals(1, cmd.getArgs().length);
 assertEquals("input.txt", cmd.getArgs()[0]);
 assertEquals("output", cmd.getOptionValue("o"));
 }

 @Test
 public void testParseWithMultipleArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLine cmd = parser.parse(options, new String[]{"input.txt", "output.txt"});
 assertEquals(1, cmd.getArgs().length);
 assertEquals("output.txt", cmd.getArgs()[0]);
 assertEquals("output", cmd.getOptionValue("o"));
 }

 @Test
 public void testParseWithSingleRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("o", "output", true, "output file path");
 CommandLine cmd = parser.parse(options, new String[0]);
 throw new AssertionError("Expected MissingOptionException");
 }

 @Test
 public void testParseWithMultipleRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addRequiredOption("o1", "output1", true, "output1 file path");
 options.addRequiredOption("o2", "output2", true, "output2 file path");
 CommandLine cmd = parser.parse(options, new String[0]);
 throw new AssertionError("Expected MissingOptionException with message: 'Missing options: [o1, o2]'");
 }

 @Test
 public void testParseWithMixedOptionsAndArguments() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 options.addOption("a", "append", false, "append to output file");
 CommandLine cmd = parser.parse(options, new String[]{"-a", "input.txt", "output.txt"});
 assertEquals(1, cmd.getArgs().length);
 assertEquals("input.txt", cmd.getArgs()[0]);
 assertEquals("output.txt", cmd.getOptionValue("o"));
 assertEquals("append", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseWithOptionGroup() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "This is a group.");
 group.addOption("o1", "output1", true, "output file path 1");
 group.addOption("o2", "output2", true, "output file path 2");
 options.addOptionGroup(group);
 CommandLine cmd = parser.parse(options, new String[0]);
 throw new AssertionError("Expected MissingOptionException with message: 'Missing options: [o1, o2]'");
 }

 @Test
 public void testParseWithSelectedOptionGroup() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "This is a group.");
 group.addOption("o1", "output1", true, "output file path 1");
 group.addOption("o2", "output2", true, "output file path 2");
 options.addOptionGroup(group);
 group.setSelected(options.getOption("o1"));
 CommandLine cmd = parser.parse(options, new String[0]);
 assertEquals("output1", cmd.getOptionValue("o1"));
 }

 @Test
 public void testParseWithInvalidOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLine cmd = parser.parse(options, new String[]{"-x"});
 assertEquals(0, cmd.getArgs().length);
 }

 @Test
 public void testParseWithUnrecognizedOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("o", "output", true, "output file path");
 CommandLine cmd = parser.parse(options, new String[]{"--unrecognized"});
 throw new AssertionError("Expected UnrecognizedOptionException with message: 'Unrecognized option: unrecognized'");
 }

 @Test
 public void testParseWithLongOptionName() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addLongOption("a-very-long-option-name", "longOption", true, "a very long option name");
 CommandLine cmd = parser.parse(options, new String[]{"--a-very-long-option-name", "value"});
 assertEquals("value", cmd.getOptionValue("longOption"));
 }

 @Test
 public void testParseWithShortOptionName() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("a", "append", false, "append to output file");
 CommandLine cmd = parser.parse(options, new String[]{"-a"});
 assertTrue(cmd.hasOption("append"));
 }
}