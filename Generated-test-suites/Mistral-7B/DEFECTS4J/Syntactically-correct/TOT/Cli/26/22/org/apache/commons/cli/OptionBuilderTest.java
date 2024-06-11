package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithSingleLongOption() {
 Options options = new Options();
 OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOption")
 .hasArg()
 .withDescription("A test option");
 Option option = optBuilder.create('L');
 options.addOption(option);

 CommandLine parsed = null;
 try {
 parsed = new DefaultParser().parse(options, new String[]{"-L", "testValue"});
 } catch (ParseException e) {
 fail("Parsing failed. " + e.getMessage());
 }

 assertEquals("testValue", parsed.getOptionValue("longOption"));
 }

 @Test
 public void testOptionBuilderWithMultipleLongOptions() {
 Options options = new Options();
 OptionBuilder optBuilder1 = OptionBuilder.withLongOpt("longOption1")
 .hasArg()
 .withDescription("Option 1 test");
 Option option1 = optBuilder1.create('L');
 options.addOption(option1);

 OptionBuilder optBuilder2 = OptionBuilder.withLongOpt("longOption2")
 .hasArg()
 .withDescription("Option 2 test");
 Option option2 = optBuilder2.create('M');
 options.addOption(option2);

 CommandLine parsed = null;
 try {
 parsed = new DefaultParser().parse(options, new String[]{"-M", "value1", "-L", "value2"});
 } catch (ParseException e) {
 fail("Parsing failed. " + e.getMessage());
 }

 assertEquals("value1", parsed.getOptionValue("longOption2"));
 assertEquals("value2", parsed.getOptionValue("longOption1"));
 }

 @Test
 public void testOptionBuilderWithNoArg() {
 Options options = new Options();
 OptionBuilder optBuilder = OptionBuilder.withLongOpt("noArgOption")
 .withDescription("A test option with no argument");
 Option option = optBuilder.create();
 options.addOption(option);

 CommandLine parsed = null;
 try {
 parsed = new DefaultParser().parse(options, new String[]{"-n"});
 } catch (ParseException e) {
 fail("Parsing failed. " + e.getMessage());
 }

 assertTrue(parsed.hasOption("noArgOption"));
 }

 @Test
 public void testOptionBuilderWithRequiredArg() {
 Options options = new Options();
 OptionBuilder optBuilder = OptionBuilder.withLongOpt("requiredArgOption")
 .isRequired()
 .withDescription("A test option with required argument");
 Option option = optBuilder.create('R');
 options.addOption(option);

 CommandLine parsed = null;
 try {
 parsed = new DefaultParser().parse(options, new String[]{"-R"});
 } catch (ParseException e) {
 fail("Parsing failed. " + e.getMessage());
 }

 assertNotNull(parsed.getOptionValues("requiredArgOption"));
 assertFalse(parsed.getOptions().isEmpty());
 }

 @Test
 public void testOptionBuilderWithOptionalArg() {
 Options options = new Options();
 OptionBuilder optBuilder = OptionBuilder.withLongOpt("optionalArgOption")
 .hasArg(true)
 .withDescription("A test option with optional argument");
 Option option = optBuilder.create('O');
 options.addOption(option);

 CommandLine parsed = null;
 try {
 parsed = new DefaultParser().parse(options, new String[]{"-O", "testValue"});
 } catch (ParseException e) {
 fail("Parsing failed. " + e.getMessage());
 }

 assertEquals("testValue", parsed.getOptionValue("optionalArgOption"));

 CommandLine parsed2 = null;
 try {
 parsed2 = new DefaultParser().parse(options, new String[]{"-O"});
 } catch (ParseException e) {
 // expected
 }

 assertNull(parsed2.getOptionValue("optionalArgOption"));
 }

 @Test
 public void testOptionBuilderWithMultipleValues() {
 Options options = new Options();
 OptionBuilder optBuilder = OptionBuilder.withLongOpt("multipleValuesOption")
 .hasArgs()
 .withDescription("A test option with multiple values");
 Option option = optBuilder.create('V');
 options.addOption(option);

 CommandLine parsed = null;
 try {
 parsed = new DefaultParser().parse(options, new String[]{"-V", "value1", "-V", "value2"});
 } catch (ParseException e) {
 fail("Parsing failed. " + e.getMessage());
 }

 String[] values = parsed.getOptionValues("multipleValuesOption");
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testOptionBuilderWithoutLongOpt() {
 Options options = new Options();
 OptionBuilder optBuilder = OptionBuilder.hasArg();
 Option option = optBuilder.create();
 options.addOption(option);

 fail("Should not be able to create an option without specifying a long opt.");
 }

 @Test (expected = IllegalArgumentException.class)
 public void testOptionBuilderWithInvalidValueSeparator() {
 OptionBuilder optBuilder = OptionBuilder.withValueSeparator('^');
 optBuilder.create('T');
 fail("Should not be able to create an option with an invalid value separator.");
 }
}