package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithSimpleOption() {
 OptionBuilder ob = new OptionBuilder();
 OptionOption = ob.withLongOpt("opt")
 .hasArg()
 .isRequired()
 .withType(String.class)
 .withDescription("A test option")
 .create('o');

 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o", "test"};
 CommandLine line;
 try {
 line = parser.parse(options, args);
 } catch (ParseException e) {
 throw new RuntimeException("Error parsing command line.", e);
 }
 String optionValue = line.getOptionValue("o");
 assertEquals("test", optionValue);
 }

 @Test
 public void testOptionBuilderWithOptionalArg() {
 OptionBuilder ob = new OptionBuilder();
 OptionOption = ob.withLongOpt("opt")
 .hasArg(false)
 .isRequired(false)
 .withDescription("A test option with optional argument")
 .create('o');

 CommandLineParser parser = new DefaultParser();
 String[] args1 = new String[]{"-o", "test"};
 CommandLine line1;
 try {
 line1 = parser.parse(options, args1);
 } catch (ParseException e) {
 // optional arg, no error
 }
 String optionValue1 = line1.getOptionValue("o");
 assertNull(optionValue1);

 String[] args2 = new String[]{"-o", "test", "arg"};
 CommandLine line2;
 try {
 line2 = parser.parse(options, args2);
 } catch (ParseException e) {
 throw new RuntimeException("Error parsing command line.", e);
 }
 String optionValue2 = line2.getOptionValue("o");
 assertEquals("test", optionValue2);
 String optionValue3 = line2.getArgs()[0];
 assertEquals("arg", optionValue3);
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder ob = new OptionBuilder();
 OptionOption = ob.withLongOpt("opt")
 .hasArg()
 .isRequired()
 .withDescription("A test option with value separator")
 .withValueSeparator(':')
 .create('o');

 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o", "key:value"};
 CommandLine line;
 try {
 line = parser.parse(options, args);
 } catch (ParseException e) {
 throw new RuntimeException("Error parsing command line.", e);
 }
 String optionValue = line.getOptionValue("o");
 assertEquals("key:value", optionValue);
 }

 @Test(expected = ParseException.class)
 public void testOptionBuilderWithInvalidOption() {
 OptionBuilder ob = new OptionBuilder();
 OptionOption = ob.withLongOpt("")
 .create('o');
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o"};
 CommandLine line;
 try {
 line = parser.parse(options, args);
 } catch (ParseException e) {
 // expected
 }
 }
}