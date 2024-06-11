package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

 @Test
 public void testFlattenEmptyArguments() {
 Options options = new Options();
 String[] arguments = new String[0];
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testFlattenSingleArgument() {
 Options options = new Options();
 String[] arguments = {"arg"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"arg"}, result);
 }

 @Test
 public void testFlattenDoubleDash() {
 Options options = new Options();
 String[] arguments = {"--arg"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--arg"}, result);
 }

 @Test
 public void testFlattenSingleHyphen() {
 Options options = new Options();
 String[] arguments = {"-"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-"}, result);
 }

 @Test
 public void testFlattenSingleOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] arguments = {"-a"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a"}, result);
 }

 @Test
 public void testFlattenSingleOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", 1, "Option C");
 String[] arguments = {"-a", "valueA", "-b", "valueB", "--", "-c", "valueC"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a", "valueA", "-b", "valueB", "--", "-c", "valueC"}, result);
 }

 @Test
 public void testFlattenMultipleOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 String[] arguments = {"-a", "-b"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a", "-b"}, result);
 }

 @Test
 public void testFlattenMultipleOptionsWithArgs() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 String[] arguments = {"-a", "valueA", "-b", "valueB", "--"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"-a", "valueA", "-b", "valueB", "--"}, result);
 }

 @Test
 public void testFlattenMultipleOptionsWithLongNames() {
 Options options = new Options();
 options.addOption("long-option-a", true, "Long Option A");
 options.addOption("long-option-b", true, "Long Option B");
 String[] arguments = {"--long-option-a", "--long-option-b"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option-a", "--long-option-b"}, result);
 }

 @Test
 public void testFlattenMultipleOptionsWithLongNamesAndArgs() {
 Options options = new Options();
 options.addOption("long-option-a", true, "Long Option A");
 options.addOption("long-option-b", true, "Long Option B");
 String[] arguments = {"--long-option-a", "valueA", "--long-option-b", "valueB", "--"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option-a", "valueA", "--long-option-b", "valueB", "--"}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] arguments = {"-x"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOptionWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 String[] arguments = {"--x"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, true);
 }

 @Test
 public void testFlattenSingleLongOption() {
 Options options = new Options();
 options.addOption("long-option", true, "Long Option");
 String[] arguments = {"--long-option"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option"}, result);
 }

 @Test
 public void testFlattenSingleLongOptionWithArg() {
 Options options = new Options();
 options.addOption("long-option", true, "Long Option");
 options.addOption("short-option", true, "Short Option");
 String[] arguments = {"--long-option=value", "--short-option=value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option=value", "--short-option=value"}, result);
 }

 @Test
 public void testFlattenSingleLongOptionWithTwoArgs() {
 Options options = new Options();
 options.addOption("long-option", true, "Long Option");
 options.addOption("short-option", true, "Short Option");
 String[] arguments = {"--long-option=value1=value2", "--short-option=value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option=value1=value2", "--short-option=value"}, result);
 }

 @Test
 public void testFlattenMultipleLongOptionsWithArgs() {
 Options options = new Options();
 options.addOption("long-option1", true, "Long Option 1");
 options.addOption("long-option2", true, "Long Option 2");
 options.addOption("short-option", true, "Short Option");
 String[] arguments = {"--long-option1=value1", "--long-option2=value2", "--short-option=value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option1=value1", "--long-option2=value2", "--short-option=value"}, result);
 }

 @Test
 public void testFlattenLongOptionWithSingleValue() {
 Options options = new Options();
 options.addOption("long-option", true, "Long Option");
 String[] arguments = {"--long-option=value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option=value"}, result);
 }

 @Test
 public void testFlattenLongOptionWithMultipleValues() {
 Options options = new Options();
 options.addOption("long-option", true, "Long Option");
 String[] arguments = {"--long-option=value1 value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--long-option=value1", "--long-option=value2"}, result);
 }
}