import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", true, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals(2, optionGroup.getOptions().size());
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", true, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 Collection<String> names = optionGroup.getNames();

 assertTrue(names.containsAll(Arrays.asList("option1", "option2")));
 }

 @Test
 public void testSetSelected() throws AlreadySelectedException {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", true, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());

 optionGroup.setSelected(option2);
 assertEquals("o2", optionGroup.getSelected());

 try {
 optionGroup.setSelected(option1);
 fail("AlreadySelectedException expected");
 } catch (AlreadySelectedException e) {
 // expected
 }
 }

 @Test
 public void testIsRequired() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", true, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());

 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "Option 1", true, "o1");
 Option option2 = new Option("option2", "Option 2", false, "o2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 assertEquals("[--option1, --option2]", optionGroup.toString());
 }
}