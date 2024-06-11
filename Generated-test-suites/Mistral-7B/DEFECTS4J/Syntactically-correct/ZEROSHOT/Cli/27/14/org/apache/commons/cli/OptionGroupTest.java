import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroupAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testOptionGroupGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Set<String> names = optionGroup.getNames();

 assertTrue(names.contains("opt1"));
 assertTrue(names.contains("opt2"));
 }

 @Test
 public void testOptionGroupSetSelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals("opt1", optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertEquals("opt2", optionGroup.getSelected());

 optionGroup.setSelected(option1);
 assertEquals("opt1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroupSetSelectedAlreadySelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testOptionGroupSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);

 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testOptionGroupIsRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(false);

 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testOptionGroupToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[ -opt1 Option 1, --opt2 Option 2 ]", optionGroup.toString());
 }
}