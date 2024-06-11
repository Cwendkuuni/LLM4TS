package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
 Option option = optionBuilder.create();
 Assert.assertEquals("testLongOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create();
 Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
 }

 @Test
 public void testOptionBuilderHasArgWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("testArgName");
 Option option = optionBuilder.create();
 Assert.assertEquals("testArgName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create();
 Assert.assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 Assert.assertTrue(option.isOptional());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = optionBuilder.create();
 Assert.assertEquals(Integer.class, option.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create();
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create();
 Assert.assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create();
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testOptionBuilderHasArgsWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs().withArgName("testArgName");
 Option option = optionBuilder.create();
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 Assert.assertEquals("testArgName", option.getArgName());
 }

 @Test
 public void testOptionBuilderHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option option = optionBuilder.create();
 Assert.assertTrue(option.isOptional());
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testOptionBuilderHasOptionalArgsWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs().withArgName("testArgName");
 Option option = optionBuilder.create();
 Assert.assertTrue(option.isOptional());
 Assert.assertEquals("testArgName", option.getArgName());
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testOptionBuilderHasOptionalArgsWithNumArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
 Option option = optionBuilder.create();
 Assert.assertEquals(3, option.getArgs().length);
 Assert.assertTrue(option.isOptional());
 }
}