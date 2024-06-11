package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilder_withLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option expectedOption = new Option("longOpt", "long option");
 Assert.assertEquals(expectedOption.getLongOpt(), optionBuilder.create().getLongOpt());
 }

 @Test
 public void testOptionBuilder_hasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = Option.builder("arg").required(false).build();
 Assert.assertEquals(expectedOption.isRequired(), optionBuilder.create().isRequired());
 Assert.assertEquals(expectedOption.getArgs(), Option.ARG_ONE_VALUE);
 }

 @Test
 public void testOptionBuilder_hasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = Option.builder("arg").required(false).argName("arg").build();
 Assert.assertEquals(expectedOption.isRequired(), optionBuilder.create().isRequired());
 Assert.assertEquals(expectedOption.getArgs(), Option.UNINITIALIZED);
 Assert.assertEquals(expectedOption.hasOptionalArg(), true);
 }

 @Test
 public void testOptionBuilder_isRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = Option.builder("arg").required(true).build();
 Assert.assertEquals(expectedOption.isRequired(), optionBuilder.create().isRequired());
 }

 @Test
 public void testOptionBuilder_hasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = Option.builder("arg").required(false).args(Option.UNLIMITED_VALUES).build();
 Assert.assertEquals(expectedOption.getArgs(), optionBuilder.create().getArgs());
 }

 @Test
 public void testOptionBuilder_hasArgs_withNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
 Option expectedOption = Option.builder("arg")
 .required(false)
 .args(2)
 .build();
 Assert.assertEquals(expectedOption.getArgs(), optionBuilder.create().getArgs());
 }

 @Test
 public void testOptionBuilder_withType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option expectedOption = Option.builder("arg")
 .required(false)
 .args(Option.UNINITIALIZED)
 .type(Integer.class)
 .build();
 Assert.assertEquals(expectedOption.getType(), optionBuilder.create().getType());
 }

 @Test
 public void testOptionBuilder_withDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
 Option expectedOption = Option.builder("arg")
 .required(false)
 .args(Option.UNINITIALIZED)
 .description("description")
 .build();
 Assert.assertEquals(expectedOption.getDescription(), optionBuilder.create().getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilder_create_withoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.create();
 }
}