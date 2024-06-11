import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 // Test for withLongOpt method
 @Test
 public void testWithLongOpt() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.withLongOpt("option1").create();
 assertEquals("option1", option.getLongOpt());
 }

 // Test for hasArg method
 @Test
 public void testHasArg() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.hasArg().create();
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 Option optionArg = ob.hasArg().create();
 assertEquals(1, optionArg.getArgs());
 }

 // Test for hasArg with argument name
 @Test
 public void testHasArgWithArgName() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.hasArg().withArgName("argName").create();
 assertEquals("argName", option.getArgName());
 }

 // Test for isRequired method
 @Test
 public void testIsRequired() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.isRequired().create();
 assertTrue(option.isRequired());
 }

 // Test for withValueSeparator method
 @Test
 public void testWithValueSeparator() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.withValueSeparator(';').create();
 assertEquals(';', option.getValueSeparator());
 }

 // Test for withValueSeparator with no argument
 @Test
 public void testWithValueSeparatorNoArg() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.create();
 assertNull(option.getValueSeparator());
 }

 // Test for hasOptionalArg method
 @Test
 public void testHasOptionalArg() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.hasOptionalArg().create();
 assertTrue(option.isOptional());
 }

 // Test for hasArgs method
 @Test
 public void testHasArgs() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.hasArgs().create();
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 // Test for hasArgs with specified number of arguments
 @Test
 public void testHasArgsWithSpecifiedNumArgs() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.hasArgs(3).create();
 assertEquals(3, option.getArgs());
 }

 // Test for withType method
 @Test
 public void testWithType() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.withType(Integer.class).create();
 assertEquals(Integer.class, option.getType());
 }

 // Test for withDescription method
 @Test
 public void testWithDescription() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.withDescription("Description for OptionBuilder").create();
 assertEquals("Description for OptionBuilder", option.getLongOpt());
 }

 // Test for create method with required argument
 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithRequiredArg() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.create();
 fail("Expected IllegalArgumentException when no long opt is set.");
 }

 // Test for create method with invalid long opt
 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidLongOpt() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.withLongOpt("").create();
 fail("Expected IllegalArgumentException when long opt is empty.");
 }
}