import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<Option> options = optionGroup.getOptions();
 assertEquals(2, options.size());
 assertEquals(option1, options.iterator().next());
 assertEquals(option2, options.iterator().next());
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<String> names = optionGroup.getNames();
 assertEquals(2, names.size());
 assertTrue(names.contains("option1"));
 assertTrue(names.contains("option2"));
 }

 @Test
 public void testSetSelectedNull() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedSame() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option1", "Option 1");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertEquals(option2.getKey(), optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferent() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals(option1.getKey(), optionGroup.getSelected());

 optionGroup.setSelected(option2);
 }

 @Test
 public void testIsRequiredFalse() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 OptionGroup optionGroup = new OptionGroup();
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1");
 Option option2 = new Option("option2", "Option 2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[--option1 Option 1, --option2 Option 2]", optionGroup.toString());
 }
}