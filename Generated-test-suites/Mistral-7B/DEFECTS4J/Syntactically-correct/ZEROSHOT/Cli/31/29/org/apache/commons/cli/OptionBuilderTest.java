import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
 Option option = optionBuilder.create('o');
 assertEquals("optionLongName", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('a');
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('b');
 assertTrue(option.getOptionalArg());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('c');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
 Option option = optionBuilder.create('d');
 assertEquals(3, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = optionBuilder.create('e');
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('r');
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
 Option option = optionBuilder.create('v');
 assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = optionBuilder.create('i');
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option Description");
 Option option = optionBuilder.create('t');
 assertEquals("Test Option Description", option.getDescription());
 }

 @Test
 public void testCreateOptionWithDefaultSettings() {
 Option option = OptionBuilder.create();
 assertNull(option.getLongOpt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithoutLongOptSet() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create();
 fail("IllegalArgumentException expected");
 }
}