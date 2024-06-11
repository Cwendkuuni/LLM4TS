import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 @Test
 public void testOptionGroupAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<Option> options = optionGroup.getOptions();

 Assert.assertEquals(2, options.size());
 Assert.assertTrue(options.contains(option1));
 Assert.assertTrue(options.contains(option2));
 }

 @Test
 public void testOptionGroupGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<String> names = optionGroup.getNames();

 Assert.assertEquals(2, names.size());
 Assert.assertTrue(names.contains("o1"));
 Assert.assertTrue(names.contains("o2"));
 }

 @Test
 public void testOptionGroupSetSelected() throws Exception {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 Option selectedOption = new Option("o1", "option1", true, "Description for option1");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(selectedOption);

 Assert.assertEquals("option1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroupSetSelectedAlreadySelected() throws Exception {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 Option selectedOption = new Option("o1", "option1", true, "Description for option1");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(selectedOption);
 optionGroup.setSelected(selectedOption);
 }

 @Test(expected = NullPointerException.class)
 public void testOptionGroupSetSelectedNull() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option selectedOption = null;

 optionGroup.addOption(option1);
 optionGroup.setSelected(selectedOption);
 }

 @Test
 public void testOptionGroupToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 String result = optionGroup.toString();
 String expected = "[-o1 Description for option1, --option1 Description for option1, -o2 Description for option2, --option2 Description for option2]";

 Assert.assertEquals(expected, result);
 }
}