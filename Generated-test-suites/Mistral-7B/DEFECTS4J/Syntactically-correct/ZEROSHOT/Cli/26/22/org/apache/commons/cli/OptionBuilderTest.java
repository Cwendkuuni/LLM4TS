import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testCreateWithDefaults() {
 OptionBuilder ob = new OptionBuilder();
 Option option = ob.create('A');

 assertEquals("Option is not initialized with default long opt value", null, option.getLongOpt());
 assertEquals("Option description is not initialized", "", option.getDescription());
 assertEquals("Argument name is not initialized", "arg", option.getArgName());
 assertFalse("Option is not initialized as required", option.isRequired());
 assertEquals("Number of args is not initialized", Option.UNINITIALIZED, option.getArgs());
 assertNull("Type is not initialized", option.getType());
 assertFalse("Option does not allow optional args by default", option.hasOptionalArg());
 }

 @Test
 public void testCreateWithLongOpt() {
 OptionBuilder ob = new OptionBuilder().withLongOpt("my-long-opt");
 Option option = ob.create('A');

 assertEquals("Long opt value is not set correctly", "my-long-opt", option.getLongOpt());
 }

 @Test
 public void testCreateWithArgument() {
 OptionBuilder ob = new OptionBuilder().hasArg();
 Option option = ob.create('A');

 assertEquals("Number of args is not set correctly", 1, option.getArgs());
 }

 @Test
 public void testCreateWithRequiredArg() {
 OptionBuilder ob = new OptionBuilder().isRequired();
 Option option = ob.create('A');

 assertTrue("Option is not initialized as required", option.isRequired());
 }

 @Test
 public void testCreateWithValueSeparator() {
 OptionBuilder ob = new OptionBuilder().withValueSeparator(':');
 Option option = ob.create('A');

 assertEquals("Value separator is not set correctly", ':', option.getValueSeparator());
 }

 @Test
 public void testCreateWithDescription() {
 OptionBuilder ob = new OptionBuilder().withDescription("My description");
 Option option = ob.create('A');

 assertEquals("Description is not set correctly", "My description", option.getDescription());
 }

 @Test
 public void testCreateWithOptionalArg() {
 OptionBuilder ob = new OptionBuilder().hasOptionalArg();
 Option option = ob.create('A');

 assertTrue("Option is not initialized to allow optional args", option.hasOptionalArg());
 }

 @Test
 public void testCreateWithArgs() {
 OptionBuilder ob = new OptionBuilder().hasArgs(3);
 Option option = ob.create('A');

 assertEquals("Number of args is not set correctly", Option.UNLIMITED_VALUES, option.getArgs());
 assertTrue("Option is not initialized to allow optional args", option.hasOptionalArg());
 }

 @Test
 public void testCreateWithType() {
 OptionBuilder ob = new OptionBuilder().withType(Integer.class);
 Option option = ob.create('A');

 assertEquals("Type is not set correctly", Integer.class, option.getType());
 }
}