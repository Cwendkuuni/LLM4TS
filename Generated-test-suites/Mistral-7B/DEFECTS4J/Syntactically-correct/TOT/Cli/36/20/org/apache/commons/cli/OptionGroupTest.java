import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroup() {
 OptionGroup optionGroup = new OptionGroup();

 Option option1 = new Option("a", "option1", false, "This is option 1");
 Option option2 = new Option("b", "option2", false, "This is option 2");
 Option option3 = new Option("c", "option3", false, "This is option 3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 // test getNames method
 Collection<String> names = optionGroup.getNames();
 assertEquals(3, names.size());
 assertTrue(names.contains("a"));
 assertTrue(names.contains("b"));
 assertTrue(names.contains("c"));

 // test getOptions method
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(3, options.size());
 assertEquals(option1, options.iterator().next());
 assertEquals(option2, options.iterator().next());
 assertEquals(option3, options.iterator().next());

 // test setSelected method with valid option
 optionGroup.setSelected(option1);
 assertEquals("a", optionGroup.getSelected());

 // test setSelected method with already selected option
 optionGroup.setSelected(option1);
 try {
 optionGroup.setSelected(option2);
 fail();
 } catch (AlreadySelectedException e) {
 // expected
 }

 // test setSelected method with null option
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());

 // test isRequired method with required group
 OptionGroup requiredGroup = new OptionGroup();
 requiredGroup.setRequired(true);
 assertTrue(requiredGroup.isRequired());

 // test isRequired method with non-required group
 OptionGroup nonRequiredGroup = new OptionGroup();
 assertFalse(nonRequiredGroup.isRequired());
 }

 @Test(expected = ParseException.class)
 public void testSetSelectedTwiceWithSameOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("a", "option1", false, "This is option 1");
 optionGroup.addOption(option1);
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option1);
 }
}