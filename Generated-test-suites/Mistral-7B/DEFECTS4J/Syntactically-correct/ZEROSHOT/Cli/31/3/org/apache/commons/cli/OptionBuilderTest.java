package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaultValues() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.create('optionName');

 assertEquals("optionName is the default argument name", "optionName", option.getArgName());
 assertEquals("longopt is null by default", null, option.getLongOpt());
 assertNull("description is null by default", option.getDescription());
 assertFalse("required is false by default", option.isRequired());
 assertEquals("numberOfArgs is uninitialized by default", Option.UNINITIALIZED, option.getArgs());
 assertNull("type is null by default", option.getType());
 assertFalse("optionalArg is false by default", option.hasOptionalArg());
 assertNull("valueSeparator is null by default", option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithCustomValues() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();

 Option option = optionBuilder.hasArg()
 .withArgName("argName")
 .withDescription("description")
 .withLongOpt("longOpt")
 .isRequired()
 .withType(String.class)
 .hasOptionalArg()
 .withValueSeparator(':')
 .create('option');

 assertEquals("optionName is argName", "argName", option.getArgName());
 assertEquals("longOpt is longOpt", "longOpt", option.getLongOpt());
 assertEquals("description is description", "description", option.getDescription());
 assertTrue("required is true", option.isRequired());
 assertEquals("numberOfArgs is 1", Option.UNINITIALIZED, option.getArgs());
 assertEquals("type is String.class", String.class, option.getType());
 assertTrue("optionalArg is true", option.hasOptionalArg());
 assertEquals("valueSeparator is :", ':', option.getValueSeparator());
 }
}