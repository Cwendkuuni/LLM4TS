package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GnuParserTest {

 @Test
 public void testFlatten_EmptyArguments() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 String[] arguments = {};
 String[] expected = {};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleArgument() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 String[] arguments = {"arg"};
 String[] expected = {"arg"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleOption() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("opt", true, "Option");
 String[] arguments = {"-opt"};
 String[] expected = {"-opt"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_OptionWithValue() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("opt", true, "Option");
 String[] arguments = {"-opt", "value"};
 String[] expected = {"-opt", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleOptions() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("opt1", true, "Option 1");
 options.addOption("opt2", true, "Option 2");
 String[] arguments = {"-opt1", "value1", "-opt2", "value2"};
 String[] expected = {"-opt1", "value1", "-opt2", "value2"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_LongOption() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("longOpt", true, "Long Option");
 String[] arguments = {"--longOpt"};
 String[] expected = {"--longOpt"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_LongOptionWithValue() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("longOpt", true, "Long Option");
 String[] arguments = {"--longOpt", "value"};
 String[] expected = {"--longOpt", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleDash() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 String[] arguments = {"-"};
 String[] expected = {"-"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_DashDash() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 String[] arguments = {"--"};
 String[] expected = {"--"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_NonOption() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 String[] arguments = {"arg"};
 String[] expected = {};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_NonOptionWithArguments() { {GnuParser parser = new GnuParser();Options options = new Options();options.addOption("opt", true, "Option");String[] arguments = {"arg", "value1", "value2"};String[] expected = {};String[] result = parser.flatten(options, arguments, false);assertArrayEquals(expected, result);}}

 @Test
 public void testFlatten_SingleShortOption() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("o", true, "Option");
 String[] arguments = {"-o"};
 String[] expected = {"-o"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithValue() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("o", true, "Option");
 String[] arguments = {"-o", "value"};
 String[] expected = {"-o", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongName() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"-l", "arg", "-o"};
 String[] expected = {"-l", "arg", "-o"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongNameAndValue() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"-l", "arg", "--o", "value"};
 String[] expected = {"-l", "arg", "--o", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongNameAndValue2() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"-l", "arg", "-o", "value"};
 String[] expected = {"-l", "arg", "-o", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongNameAndValue3() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"--", "-l", "arg", "-o", "value"};
 String[] expected = {"--", "-l", "arg", "-o", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongNameAndValue4() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"--", "-l", "arg", "-o", "value", "--"};
 String[] expected = {"--", "-l", "arg", "-o", "value", "--"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongNameAndValue5() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"--", "-l", "arg", "-o", "value", "--arg"};
 String[] expected = {"--", "-l", "arg", "-o", "value", "--arg"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongNameAndValue6() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"--", "-l", "arg", "-o", "value", "--arg", "value"};
 String[] expected = {"--", "-l", "arg", "-o", "value", "--arg", "value"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongNameAndValue7() {
 GnuParser parser = new GnuParser();
 Options options = new Options();
 options.addOption("l", true, "Long Name");
 options.addOption("o", true, "Option");
 String[] arguments = {"--", "-l", "arg", "-o", "value1", "--", "-l", "arg", "-o", "value2"};
 String[] expected = {"--", "-l", "arg", "-o", "value1", "--", "-l", "arg", "-o", "value2"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }
}