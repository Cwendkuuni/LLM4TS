package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

 /**
 * Test flatten method with valid options and arguments.
 */
 @Test
 public void testFlattenValidOptionsAndArguments() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Print debug information.");
 options.addOption("o", "output", true, "Output file name.");
 String[] arguments = {"-D", "debug", "--", "input.txt", "-o", "output.txt", "--"};

 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"-D", "debug", "--", "input.txt", "-o", "output.txt", "--"};
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with invalid option.
 */
 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] arguments = {"-X", "arg"};

 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }

 /**
 * Test flatten method with no options or arguments.
 */
 @Test
 public void testFlattenNoOptionsOrArguments() {
 Options options = new Options();
 String[] arguments = {};

 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {};
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with long option value.
 */
 @Test
 public void testFlattenLongOptionValue() {
 Options options = new Options();
 options.addOption("l", "logfile", true, "Log file name.");
 String[] arguments = {"-l", "long-option-value-that-can-be-very-long"};

 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"-l", "long-option-value-that-can-be-very-long"};
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with stopAtNonOption.
 */
 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Print debug information.");
 String[] arguments = {"-D", "debug", "nonOption", "--"};

 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, true);
 String[] expected = {"-D", "debug"};
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with double hyphen.
 */
 @Test
 public void testFlattenDoubleHyphen() {
 Options options = new Options();
 String[] arguments = {"--", "-o", "output.txt", "--"};

 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"--", "-o", "output.txt", "--"};
 assertArrayEquals(expected, result);
 }
}