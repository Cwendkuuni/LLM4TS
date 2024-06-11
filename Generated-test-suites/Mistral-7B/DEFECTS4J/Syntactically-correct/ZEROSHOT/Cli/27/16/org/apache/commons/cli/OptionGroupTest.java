import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "option 1 description", true);
 Option option2 = new Option("option2", "option 2 description", true);
 Option option3 = new Option("option3", "option 3 description", false);
 Option option4 = new Option("option4", "option 4 description", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 optionGroup.addOption(option4);
 }

 @Test
 public void testGetNames() {
 List<String> optionsNames = new ArrayList<>(optionGroup.getNames());
 List<String> expectedOptionsNames = Arrays.asList("option1", "option2", "option3", "option4");
 assertEquals(expectedOptionsNames, optionsNames);
 }

 @Test
 public void testGetOptions() {
 List<Option> options = new ArrayList<>(optionGroup.getOptions());
 List<Option> expectedOptions = Arrays.asList(option1, option2, option3, option4);
 assertEquals(expectedOptions, options);
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedOptionAlreadySelected() {
 Option selectedOption = option1;
 optionGroup.setSelected(selectedOption);
 optionGroup.setSelected(selectedOption);
 assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(selectedOption));
 }

 @Test
 public void testSetSelectedOption() {
 Option selectedOption = option1;
 optionGroup.setSelected(selectedOption);
 assertEquals(option1.getOpt(), optionGroup.getSelected());
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

 @Test
 public void testToString() {
 String expectedToString = "[-option1 option 1 description, -option2 option 2 description, --option3 option 3 description, --option4 option 4 description]";
 assertEquals(expectedToString, optionGroup.toString());
 }
}