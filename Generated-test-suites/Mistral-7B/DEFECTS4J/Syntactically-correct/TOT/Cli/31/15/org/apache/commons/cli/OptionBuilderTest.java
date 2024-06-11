package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testLongOpt() {
 OptionBuilder builder = OptionBuilder.withLongOpt("myLongOpt");
 Option option = builder.create();
 assertEquals("myLongOpt", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder builder = OptionBuilder.hasArg();
 Option option = builder.create();
 assertEquals(Option.UNINITIALIZED, option.getArgs());

 OptionBuilder hasArgBuilder = OptionBuilder.hasArg();
 Option hasArgOption = hasArgBuilder.create();
 assertEquals(1, hasArgOption.getArgs().length);
 }

 @Test
 public void testHasArgWithArgName() {
 OptionBuilder builder = OptionBuilder.withArgName("argName");
 Option option = builder.create();
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder builder = OptionBuilder.isRequired();
 Option option = builder.create();
 assertTrue(option.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder builder = OptionBuilder.withValueSeparator(':');
 Option option = builder.create();
 assertEquals(':', option.getValueSeparator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithNoSettings() {
 OptionBuilder.reset();
 Option option = OptionBuilder.create();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithNoLongOpt() {
 OptionBuilder.reset();
 OptionBuilder builder = OptionBuilder.create();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCreateWithOptionalArg() {
 OptionBuilder builder = OptionBuilder.hasOptionalArg();
 Option option = builder.create();
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testCreateWithUnlimitedArgs() {
 OptionBuilder builder = OptionBuilder.hasArgs();
 Option option = builder.create();
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testCreateWithSpecifiedNumArgs() {
 OptionBuilder builder = OptionBuilder.hasArgs(2);
 Option option = builder.create();
 assertEquals(2, option.getArgs().length);
 }

 @Test
 public void testCreateWithType() {
 OptionBuilder builder = OptionBuilder.withType(Integer.TYPE);
 Option option = builder.create();
 assertEquals(Integer.TYPE, option.getType());
 }

 @Test
 public void testCreateWithDescription() {
 OptionBuilder builder = OptionBuilder.withDescription("My Option Description");
 Option option = builder.create();
 assertEquals("My Option Description", option.getDescription());
 }
}