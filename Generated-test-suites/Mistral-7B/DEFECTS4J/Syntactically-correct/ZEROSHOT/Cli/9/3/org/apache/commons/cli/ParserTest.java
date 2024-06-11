package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Parser parser = new DefaultParser();

 // test parsing with no options or arguments
 String[] args = new String[0];
 Options options = new Options();
 CommandLine cmd = parser.parse(options, args, null, false);
 assertEquals(0, cmd.getArgs().size());
 assertFalse(cmd.hasOption("option1"));
 assertFalse(cmd.hasOption("option2"));

 // test parsing with options but no arguments
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 args = new String[]{"-o1", "-o2"};
 cmd = parser.parse(options, args, null, false);
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertFalse(cmd.hasOption("option3"));
 assertFalse(cmd.getArgs().size() > 0);

 // test parsing with options and arguments
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 options.addOption("option3", true, "o3");
 options.addOption("option4", "arg1,arg2");
 args = new String[]{"-o1", "-o2", "--option3", "arg3", "-o4", "arg4"};
 cmd = parser.parse(options, args, null, false);
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertTrue(cmd.hasOption("option3"));
 assertTrue(cmd.hasOption("option4"));
 assertEquals(2, cmd.getArgs().size());
 assertEquals("arg3", cmd.getArgs().get(0));
 assertEquals("arg4", cmd.getArgs().get(1));

 // test parsing with properties
 Properties props = new Properties();
 props.setProperty("option1", "value1");
 props.setProperty("option2", "value2");
 args = new String[0];
 cmd = parser.parse(options, args, props, false);
 assertTrue(cmd.hasOption("option1"));
 assertEquals("value1", cmd.getOptionValue("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertEquals("value2", cmd.getOptionValue("option2"));

 // test parsing with required options
 options.addRequiredOption("option1", true, "o1");
 args = new String[0];
 try {
 cmd = parser.parse(options, args, null, false);
 fail("MissingRequiredOptionException expected");
 } catch (MissingOptionException e) {
 // expected
 }
 args = new String[]{"-o2"};
 try {
 cmd = parser.parse(options, args, null, false);
 fail("MissingRequiredOptionException expected");
 } catch (MissingOptionException e) {
 // expected
 }
 args = new String[]{"-o1"};
 cmd = parser.parse(options, args, null, false);
 assertTrue(cmd.hasOption("option1"));

 // test parsing with optional group options
 Options groupOptions = new Options();
 groupOptions.addOptionGroup(new OptionGroup("group1", "Group 1"));
 groupOptions.addOption(new Option("o1", "opt1").setLongOpt("longOpt1")
 .setOptionalArg(true).setDescription("Option 1"));
 groupOptions.addOption(new Option("o2", "opt2").setLongOpt("longOpt2")
 .setOptionalArg(true).setDescription("Option 2"));
 groupOptions.addOption(new Option("o3", "opt3").setLongOpt("longOpt3")
 .setOptionalArg(true).setDescription("Option 3"));
 groupOptions.addOption(null, "group", false, "g", "Group");
 args = new String[]{"--group", "--o1=val1", "--o2=val2", "--o3=val3"};
 cmd = parser.parse(options, args, null, false);
 assertTrue(cmd.hasOption("group"));
 assertTrue(cmd.hasOption("o1"));
 assertTrue(cmd.hasOption("o2"));
 assertTrue(cmd.hasOption("o3"));
 assertEquals("val1", cmd.getOptionValue("o1"));
 assertEquals("val2", cmd.getOptionValue("o2"));
 assertEquals("val3", cmd.getOptionValue("o3"));
 }

 @Test(expected = ParseException.class)
 public void testParseException() throws ParseException {
 Parser parser = new DefaultParser();

 // test parsing with invalid option
 String[] args = new String[]{"--invalid"};
 Options options = new Options();
 options.addOption("option1", true, "o1");
 cmd = parser.parse(options, args, null, false);
 fail("ParseException expected");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOptionException() throws ParseException {
 Parser parser = new DefaultParser();

 // test unrecognized option
 String[] args = new String[]{"--unrecognized"};
 Options options = new Options();
 options.addOption("option1", true, "o1");
 cmd = parser.parse(options, args, null, false);
 fail("UnrecognizedOptionException expected");
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgumentException() throws ParseException {
 Parser parser = new DefaultParser();

 // test missing argument
 String[] args = new String[]{"-o1"};
 Options options = new Options();
 options.addOption("option1", true, "o1").hasArg();
 cmd = parser.parse(options, args, null, false);
 fail("MissingArgumentException expected");
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingOptionException() throws ParseException {
 Parser parser = new DefaultParser();

 // test missing required option
 String[] args = new String[0];
 Options options = new Options();
 options.addRequiredOption("option1", true, "o1");
 cmd = parser.parse(options, args, null, false);
 fail("MissingOptionException expected");
 }
}