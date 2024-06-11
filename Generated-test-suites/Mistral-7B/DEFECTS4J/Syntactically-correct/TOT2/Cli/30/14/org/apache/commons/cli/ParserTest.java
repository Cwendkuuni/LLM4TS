package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addRequiredOption("i", "input", true, "Input file");
 options.addOption("o", "output", true, "Output file");
 options.addOption("h", "help", false, "Print this help message.");

 CommandLine cmd = parser.parse(options, new String[]{"-i", "input.txt", "-o", "output.txt"});

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertFalse(cmd.hasOption("help"));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addRequiredOption("i", "input", true, "Input file");
 options.addOption("o", "output", true, "Output file");

 CommandLine cmd = parser.parse(options, new String[]{"-o", "output.txt"});
 }

 @Test(expected = ParseException.class)
 public void testMissingOptionValue() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addRequiredOption("i", "input", true, "Input file");
 options.addOption("o", "output", true, "Output file");

 CommandLine cmd = parser.parse(options, new String[]{"-i", "--input", "input.txt"});
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption("i", "input", true, "Input file");
 options.addOption("o", "output", true, "Output file");

 CommandLine cmd = parser.parse(options, new String[]{"--invalid"});
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption("i", "input", true, "Input file");
 options.addOption("o", "output", true, "Output file");

 Properties properties = new Properties();
 properties.setProperty("input", "input.txt");
 properties.setProperty("output", "output.txt");

 CommandLine cmd = parser.parse(options, new String[0], properties);

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 }

 @Test
 public void testParseWithMixedArgs() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addRequiredOption("i", "input", true, "Input file");
 options.addOption("o", "output", true, "Output file");

 CommandLine cmd = parser.parse(options, new String[]{"--input", "input.txt", "--output", "output.txt", "arg1", "arg2"});

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertEquals("arg1", cmd.getArg(0));
 assertEquals("arg2", cmd.getArg(1));
 }
}