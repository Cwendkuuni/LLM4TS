import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 private OptionGroup optionGroup;
 private Option option1;
 private Option option2;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 option1 = new Option("o1", "Option 1", true, "o1-arg");
 option2 = new Option("o2", "Option 2", false, "o2-arg");
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals(2, names.size());
 assertEquals("o1", names.iterator().next());
 assertEquals("o2", names.iterator().next());
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(2, options.size());
 assertEquals(option1, options.iterator().next());
 assertEquals(option2, options.iterator().next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedSameOption() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedDifferentOption() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 }

 @Test
 public void testGetSelectedNull() {
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedAndGetSelected() {
 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test
 public void testIsRequiredFalse() {
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }
}