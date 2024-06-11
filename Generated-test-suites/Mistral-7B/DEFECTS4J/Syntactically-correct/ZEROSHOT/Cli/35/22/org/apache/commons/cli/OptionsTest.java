import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 Assert.assertEquals(options.getOptions().size(), 1);
 }

 @Test
 public void testAddOptionWithArgument() {
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 Assert.assertEquals(options.getOptions().size(), 1);
 }

 @Test
 public void testAddOptionWithLongName() {
 Option option = new Option("o", "output-file-path", false, "Output file path");
 options.addOption(option);
 Assert.assertEquals(options.getOptions().size(), 1);
 }

 @Test
 public void testAddOptionWithLongNameAndShortName() {
 Option option = new Option("o", "output-file-path, o", false, "Output file path");
 options.addOption(option);
 Assert.assertEquals(options.getOptions().size(), 1);
 }

 @Test
 public void testAddMultipleOptions() {
 Option option1 = new Option("o", "output1", false, "Output file path 1");
 Option option2 = new Option("o", "output2", false, "Output file path 2");
 options.addOption(option1);
 options.addOption(option2);
 Assert.assertEquals(options.getOptions().size(), 2);
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Test group", true);
 Option option = new Option("g", "group-option", false, "Test group option");
 group.addOption(option);
 options.addOptionGroup(group);
 Assert.assertEquals(options.getOptions().size(), 1);
 Assert.assertEquals(options.getOptionGroups().size(), 1);
 }

 @Test
 public void testHasOption() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("o"));
 Assert.assertTrue(options.hasOption("output"));
 Assert.assertFalse(options.hasOption("nonexistent"));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 Assert.assertEquals(options.getOption("o"), option);
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionWhenAddingInvalidOption() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 options.addOption(option); // Duplicate option
 }

 @Test
 public void testToString() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 Assert.assertTrue(options.toString().contains("Options: [ short"));
 }
}