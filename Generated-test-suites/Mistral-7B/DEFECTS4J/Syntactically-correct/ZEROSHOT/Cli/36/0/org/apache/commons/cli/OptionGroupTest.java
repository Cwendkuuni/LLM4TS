import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 @Test
 public void testOptionGroupInstance() {
 OptionGroup optionGroup = new OptionGroup();
 assertNotNull(optionGroup);
 }

 @Test
 public void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("optionA", "descriptionA");
 Option optionB = new Option("optionB", "descriptionB");

 optionGroup.addOption(option);
 optionGroup.addOption(optionB);

 Collection<Option> options = optionGroup.getOptions();
 assertEquals(2, options.size());
 }

 @Test
 public void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option optionA = new Option("optionA", "descriptionA");
 Option optionB = new Option("optionB", "descriptionB");

 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);

 Collection<String> names = optionGroup.getNames();
 assertEquals(2, names.size());
 }

 @Test
 public void testSetSelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option optionA = new Option("optionA", "descriptionA");
 Option optionB = new Option("optionB", "descriptionB");

 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);

 try {
 optionGroup.setSelected(optionA);
 assertEquals("optionA", optionGroup.getSelected());
 } catch (AlreadySelectedException e) {
 optionGroup.setSelected(optionB);
 assertEquals("optionB", optionGroup.getSelected());
 }
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedAlreadySelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option optionA = new Option("optionA", "descriptionA");
 Option optionB = new Option("optionB", "descriptionB");

 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);

 optionGroup.setSelected(optionA);
 optionGroup.setSelected(optionB);
 }

 @Test
 public void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option optionA = new Option("optionA", "descriptionA");
 Option optionB = new Option("optionB", "descriptionB");
 Option optionC = new Option("optionC", "descriptionC");

 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);
 optionGroup.addOption(optionC);

 assertEquals("[--optionA descriptionA, --optionB descriptionB, --optionC descriptionC]", optionGroup.toString());
 }

 @Test
 public void testIsRequired() {
 OptionGroup optionGroup = new OptionGroup();
 Option optionA = new Option("optionA", "descriptionA");
 Option optionB = new Option("optionB", "descriptionB");

 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);

 assertFalse(optionGroup.isRequired());

 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }
}