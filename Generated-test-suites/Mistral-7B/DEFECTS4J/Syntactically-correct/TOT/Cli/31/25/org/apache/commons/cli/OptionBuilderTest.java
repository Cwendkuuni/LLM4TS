package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
 Option expectedOption = new Option("longopt", "long option description");
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = new Option("arg", "required argument");
 expectedOption.setRequired(true);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArgWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("name");
 Option expectedOption = new Option("name", "required argument named name");
 expectedOption.setRequired(true);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = new Option("arg", "required argument");
 expectedOption.setRequired(true);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = new Option("arg", "argument with value separator :");
 expectedOption.setValueSeparator(':');
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = new Option("args", "option that accepts multiple arguments");
 expectedOption.setArgs(Option.UNLIMITED_VALUES);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = new Option("optarg", "option with optional argument");
 expectedOption.setOptionalArg(true);
 expectedOption.setArgs(1);
 assertEquals(expectedOption, optionBuilder.create());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.create();
 fail("expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("123");
 optionBuilder.create();
 fail("expected IllegalArgumentException");
 }

 @Test
 public void testCreateWithOptionalArgAndNoArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 }

 @Test
 public void testCreateWithOptionalArgAndOneArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 String arg = "arg";
 option.setValue(arg);
 assertEquals(arg, option.getValue());
 }

 @Test
 public void testCreateWithOptionalArgAndMultipleArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option option = optionBuilder.create();
 String[] args = {"arg1", "arg2"};
 option.setArgs(args);
 assertEquals(args, option.getValues());
 }

 @Test
 public void testCreateWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option expectedOption = Option.builder("arg").longOpt("longopt")
 .desc("option with integer argument").isRequired(false)
 .argName("arg").build().valueSeparator(':')
 .type(Integer.class);
 assertEquals(expectedOption, optionBuilder.create());
 }
}