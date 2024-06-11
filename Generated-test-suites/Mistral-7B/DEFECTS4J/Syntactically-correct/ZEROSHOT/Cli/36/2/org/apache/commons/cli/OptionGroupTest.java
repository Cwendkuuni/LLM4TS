import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for option1");
 Option option2 = new Option("option2", "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for option1");
 Option option2 = new Option("option2", "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<String> names = optionGroup.getNames();

 assertEquals(2, names.size());
 assertTrue(names.contains("option1"));
 assertTrue(names.contains("option2"));
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for option1");
 Option option2 = new Option("option2", "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertEquals(option2.getKey(), optionGroup.getSelected());

 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for option1");
 Option option2 = new Option("option2", "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testSetRequired() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertEquals(true, optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Description for option1");
 Option option2 = new Option("option2", "Description for option2");
 Option option3 = new Option("option3", "Description for option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 assertEquals("[--option1 Description for option1, --option2 Description for option2, --option3 Description for option3]", optionGroup.toString());
 }
}