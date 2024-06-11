package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParsingArgumentType;
import org.junit.Test;

public class ParserTest {

 private CommandLineParser parser;

 @Test
 public void testParserWithSimpleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with integer value");

 parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = {"-a", "--b", "5"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals(5, cmd.getOptionValue("b", 0));
 }

 @Test
 public void testParserWithBooleanOptions() throws ParseException {
 Options options = new Options();
 options.addOption("v", false, "Verbose output");
 options.addOption("d", false, "Debug output");

 parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = {"-v", "-d"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("v"));
 assertTrue(cmd.hasOption("d"));
 }

 @Test
 public void testParserWithMandatoryOptions() throws ParseException {
 Options options = new Options();
 options.addOption("m", true, "Mandatory option");
 options.addRequiredOption("r", false, "Required option");

 parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = {"-m"};

 try {
 parser.parse(options, args);
 fail("Expected MissingOptionException but none was thrown");
 } catch (MissingOptionException e) {
 // expected
 }

 args = new String[]{"-r"};
 try {
 parser.parse(options, args);
 fail("Expected MissingOptionException but none was thrown");
 } catch (MissingOptionException e) {
 // expected
 }

 args = new String[]{"-m", "-r"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("m"));
 assertTrue(cmd.hasOption("r"));
 }

 @Test
 public void testParserWithOptionGroups() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 group.addOption("g1", true, "Option in group 1");
 group.addOption("g2", true, "Option in group 2");
 options.addOptionGroup(group);
 options.addOption("o", true, "Option outside group");

 parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = {"--group", "--g1", "--g2", "-o"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("g1"));
 assertTrue(cmd.hasOption("g2"));
 assertTrue(cmd.hasOption("o"));
 assertFalse(cmd.getOptions().get(0) instanceof OptionGroup);
 }

 @Test
 public void testParserWithValueOptions() throws ParseException {
 Options options = new Options();
 options.addOption("i", true, "Input file")
 .setValueSeparator('=')
 .setArgName("file")
 .setType(new ParsingArgumentType<File>() {
 @Override
 public File parse(String value) throws ParseException {
 return new File(value);
 }
 });

 parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = {"-i=input.txt"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("i"));
 assertNotNull(cmd.getOptionValue("i"));
 assertEquals(new File("input.txt"), cmd.getOptionValue("i"));
 }

 @Test
 public void testParserWithLongOptions() throws ParseException {
 Options options = new Options();
 options.addOption("long-option-a", true, "Long option A");
 options.addOption("long-option-b", 1, "Long option B with integer value");

 parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = {"--long-option-a", "--long-option-b=5"};
 CommandLine cmd = parser.parse(options, args);

 assertTrue(cmd.hasOption("long-option-a"));
 assertTrue(cmd.hasOption("long-option-b"));
 assertEquals(5, cmd.getOptionValue("long-option-b", 0));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParserWithUnknownOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 parser = new DefaultParser();
 parser.setOptions(options);
 String[] args = {"-x"};
 CommandLine cmd = parser.parse(options, args);
 fail("Expected UnrecognizedOptionException but none was thrown");
 }
}