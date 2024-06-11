import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setup() {
 Option option1 = new Option("o1", "option1", true, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");
 Option option3 = new Option("o3", "option3", false, "This is option3");

 optionGroup = new OptionGroup("testGroup", "Test Group", true);
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals(3, names.size());
 assertTrue(names.contains("o1"));
 assertTrue(names.contains("o2"));
 assertTrue(names.contains("o3"));
 }

 @Test
 public void testGetOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(3, options.size());
 assertTrue(options.contains(option1));
 assertTrue(options.contains(option2));
 assertTrue(options.contains(option3));
 }

 @Test
 public void testSetSelected_ValidOption_SetsSelected() {
 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelected_AlreadySelectedOption_ThrowsException() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }

 @Test
 public void testSetSelected_NullOption_ResetsSelected() {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testIsRequired() {
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[-o1 This is option1, -o2 This is option2, --option3 This is option3]";
 assertEquals(expected, optionGroup.toString());
 }

 @Test(expected = ParseException.class)
 public void testSetSelected_InvalidOption_ThrowsParseException() {
 Option invalidOption = new Option("oInvalid", "invalidOption", false, "This is invalid option");
 optionGroup.setSelected(invalidOption);
 }
}