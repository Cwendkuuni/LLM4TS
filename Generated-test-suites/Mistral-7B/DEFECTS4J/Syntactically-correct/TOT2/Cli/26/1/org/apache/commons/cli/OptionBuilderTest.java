import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 // Test Method for withLongOpt(String)
 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptTest");
 Option expectedOption = new Option("longOptTest", "Description for longOptTest");
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for hasArg()
 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option expectedOption = Option.builder("argTest").required(false).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for hasArg(boolean)
 @Test
 public void testHasArg_boolean() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
 Option expectedOption = Option.builder("argTest").required(true).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for withArgName(String)
 @Test
 public void testWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argNameTest");
 Option expectedOption = Option.builder("argNameTest").required(false).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for isRequired()
 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option expectedOption = Option.builder("requiredTest").required(true).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for withValueSeparator(char)
 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option expectedOption = Option.builder("valueSeparatorTest").hasArg(true).valueSeparator(':').build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for withValueSeparator()
 @Test
 public void testWithValueSeparator_noArg() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
 Option expectedOption = Option.builder("valueSeparatorTest").hasArg(false).valueSeparator('=').build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for isRequired(boolean)
 @Test
 public void testIsRequired_boolean() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
 Option expectedOption = Option.builder("requiredTest").required(false).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for hasArgs()
 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option expectedOption = Option.builder("argsTest").hasArg(false).isMultipleValuesAllowed(true).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for hasArgs(int)
 @Test
 public void testHasArgs_int() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
 Option expectedOption = Option.builder("argsTest").hasArg(false).isMultipleValuesAllowed(true).required(false).args(2).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for hasOptionalArg()
 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option expectedOption = Option.builder("optionalArgTest").hasArg(true).isRequired(false).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for hasOptionalArgs()
 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option expectedOption = Option.builder("optionalArgsTest").hasArg(false).isMultipleValuesAllowed(true).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for hasOptionalArgs(int)
 @Test
 public void testHasOptionalArgs_int() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
 Option expectedOption = Option.builder("optionalArgsTest").hasArg(false).isMultipleValuesAllowed(true).args(2).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for withType(Object)
 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option expectedOption = Option.builder("typeTest").hasArg(false).isRequired(false).type(Integer.class).build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for withDescription(String)
 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Description for test");
 Option expectedOption = Option.builder("test").description("Description for test").build();
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for create(char)
 @Test
 public void testCreate_char() {
 Option expectedOption = Option.builder("test").description("Description for test").build();
 Option actualOption = OptionBuilder.create('t').create();
 assertEquals(expectedOption, actualOption);
 }

 // Test Method for create()
 @Test(expected = IllegalArgumentException.class)
 public void testCreate() {
 OptionBuilder optionBuilder = OptionBuilder.instance();
 Option actualOption = optionBuilder.create();
 fail("create() with no arguments should throw an IllegalArgumentException");
 }
}