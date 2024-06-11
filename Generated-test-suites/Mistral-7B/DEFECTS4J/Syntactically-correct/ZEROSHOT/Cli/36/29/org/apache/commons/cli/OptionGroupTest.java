import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroupAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testOptionGroupGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Iterator<String> namesIterator = optionGroup.getNames().iterator();
 String name1 = namesIterator.next();
 String name2 = namesiterator.next();

 assertEquals("option1", name1);
 assertEquals("option2", name2);
 }

 @Test
 public void testOptionGroupSetSelectedNull() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);

 assertNull(optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroupSetSelectedAlreadySelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testOptionGroupSetSelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(option1);

 assertEquals("option1", optionGroup.getSelected());
 }

 @Test
 public void testOptionGroupSetRequired() {
 OptionGroup optionGroup = new OptionGroup();

 optionGroup.setRequired(true);

 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testOptionGroupToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[--option1 Option 1, --option2 Option 2]", optionGroup.toString());
 }
}