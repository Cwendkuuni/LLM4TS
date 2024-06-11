package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

 @Test
 public void testDefaultOption() {
 Option option = OptionBuilder.create();
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionWithLongOpt() {
 Option option = OptionBuilder.withLongOpt("testLongOpt")
 .create();
 assertEquals("testLongOpt", option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionWithDescription() {
 Option option = OptionBuilder.withDescription("test option description")
 .create();
 assertEquals("test option description", option.getDescription());
 assertNull(option.getLongOpt());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionWithArgName() {
 Option option = OptionBuilder.withArgName("testArgName")
 .create();
 assertEquals("testArgName", option.getArgName());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionRequired() {
 Option option = OptionBuilder.isRequired()
 .create();
 assertTrue(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionHasArg() {
 Option option = OptionBuilder.hasArg()
 .create();
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionWithValueSeparator() {
 Option option = OptionBuilder.withValueSeparator(',')
 .create();
 assertEquals(',', option.getValueSeparator());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 }

 @Test
 public void testOptionWithType() {
 Option option = OptionBuilder.withType(Boolean.class)
 .create();
 assertEquals(Boolean.class, option.getType());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs().getValue());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionHasArgs() {
 Option option = OptionBuilder.hasArgs(3)
 .create();
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getValue());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionHasOptionalArgs() {
 Option option = OptionBuilder.hasOptionalArgs()
 .create();
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().getValue());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }

 @Test
 public void testOptionHasOptionalArgsWithNumber() {
 Option option = OptionBuilder.hasOptionalArgs(5)
 .create();
 assertEquals(5, option.getArgs().getValue());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertNull(option.getValueSeparator());
 }
}