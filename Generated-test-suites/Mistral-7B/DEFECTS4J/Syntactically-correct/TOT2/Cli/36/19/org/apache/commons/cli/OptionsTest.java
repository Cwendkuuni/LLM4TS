import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 // Basic test cases for addOption(String opt, String description) method
 @Test
 public void testAddOptionStringDescription() {
 Options options = new Options();
 Option option = options.addOption("a", "option A");
 assertEquals("option A", option.getLongOpt());
 assertEquals("option A", option.getKey());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 }

 @Test
 public void testAddOptionStringDescriptionWithArg() {
 Options options = new Options();
 Option option = options.addOption("a", "option A", true, "arg");
 assertEquals("option A", option.getLongOpt());
 assertEquals("a", option.getKey());
 assertEquals("arg", option.getArgName());
 assertTrue(option.isRequired());
 }

 // Basic test cases for addOption(String opt, String longOpt, boolean hasArg, String description) method
 @Test
 public void testAddOptionStringLongOptBooleanDescription() {
 Options options = new Options();
 Option option = options.addOption("a", "option A", false, "option A description");
 assertEquals("option A", option.getLongOpt());
 assertEquals("a", option.getKey());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals("option A description", option.getDescription());
 }

 @Test
 public void testAddOptionStringLongOptBooleanDescriptionWithArg() {
 Options options = new Options();
 Option option = options.addOption("a", "option A", true, "option A description", "arg");
 assertEquals("option A", option.getLongOpt());
 assertEquals("a", option.getKey());
 assertEquals("arg", option.getArgName());
 assertTrue(option.isRequired());
 assertEquals("option A description", option.getDescription());
 }

 // Test cases for addOptionGroup(OptionGroup group) method
 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "group description");
 Option option = new Option("g", "group option", false, "group option description");
 group.addOption(option);
 options.addOptionGroup(group);
 assertEquals("group", group.getKey());
 assertEquals(1, options.getOptions().size());
 assertEquals("group description", group.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupNullGroup() {
 Options options = new Options();
 options.addOptionGroup(null);
 }

 // Edge cases and error scenarios tests
 @Test
 public void testAddOptionWithNullDescription() {
 Options options = new Options();
 Option option = options.addOption("a", null);
 assertNull(option.getDescription());
 }

 @Test
 public void testAddOptionWithEmptyDescription() {
 Options options = new Options();
 Option option = options.addOption("a", "");
 assertNull(option.getDescription());
 }

 @Test
 public void testAddOptionWithEmptyKey() {
 Options options = new Options();
 Option option = options.addOption(null, "option A", false, "option A description");
 assertNull(option.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullLongOpt() {
 Options options = new Options();
 Option option = options.addOption(null, "option A", false, "option A description");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescriptionAndNullKey() {
 Options options = new Options();
 Option option = options.addOption(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescriptionAndLongOpt() {
 Options options = new Options();
 Option option = options.addOption("a", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithNullKey() {
 Options options = new Options();
 OptionGroup group = new OptionGroup(null, "group description");
 options.addOptionGroup(group);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithEmptyDescription() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "");
 options.addOptionGroup(group);
 }

 @Test
 public void testAddOptionWithDuplicateKey() {
 Options options = new Options();
 Option option1 = options.addOption("a", "option A");
 Option option2 = options.addOption("a", "option A");
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroupWithDuplicateKey() {
 Options options = new Options();
 OptionGroup group1 = new OptionGroup("group1", "group description 1");
 OptionGroup group2 = new OptionGroup("group1", "group description 2");
 options.addOptionGroup(group1);
 options.addOptionGroup(group2);
 assertEquals(1, options.getOptionGroups().size());
 }

 @Test
 public void testAddOptionWithDuplicateLongOpt() {
 Options options = new Options();
 Option option1 = options.addOption("a", "option A");
 Option option2 = options.addOption("a", "option A");
 assertEquals(1, options.getOptions().size());
 }
}