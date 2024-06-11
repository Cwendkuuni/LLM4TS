import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 Option option1 = new Option("o1", "option 1", true, "default1");
 Option option2 = new Option("o2", "option 2", false, "default2");
 Option option3 = new Option("o3", "option 3", false, "default3");
 optionGroup = new OptionGroup("groupName", "Option group description", true).addOption(option1).addOption(option2).addOption(option3);
 }

 @Test
 public void testAddOptions() {
 Collection<Option> options = optionGroup.getOptions();
 assertEquals(3, options.size());
 }

 @Test
 public void testGetNames() {
 Collection<String> names = optionGroup.getNames();
 assertEquals(3, names.size());
 }

 @Test
 public void testSetSelectedOption() {
 Option selectedOption = optionGroup.getOption("o1");
 optionGroup.setSelected(selectedOption);
 assertEquals("default1", optionGroup.getSelected());
 }

 @Test(expected = AlreadySelectedException.class)
 public void testSetSelectedOptionTwice() {
 Option selectedOption = optionGroup.getOption("o1");
 optionGroup.setSelected(selectedOption);
 optionGroup.setSelected(selectedOption);
 }

 @Test
 public void testIsRequired() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 assertEquals("[--groupName Option group description --o1 option 1 \n--o1 default1]", optionGroup.toString());
 }

 @Test
 public void testParse() {
 CommandLine parsed = null;
 try {
 parsed = optionGroup.parse(new String[]{"--groupName", "--o1"});
 } catch (ParseException e) {
 e.printStackTrace();
 }
 assertEquals("default1", parsed.getOptionValue("o1"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithDifferentOption() {
 CommandLine parsed = null;
 try {
 parsed = optionGroup.parse(new String[]{"--groupName", "--o4"});
 } catch (ParseException e) {
 assertEquals("Unrecognized option: o4", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithRequiredOptionMissing() {
 CommandLine parsed = null;
 try {
 parsed = optionGroup.parse(new String[0]);
 } catch (ParseException e) {
 assertEquals("Missing required option 'groupName'", e.getMessage());
 }
 }

 @Test
 public void testParseWithBothOptions() {
 OptionGroup optionGroup = new OptionGroup("groupName", "Option group description", true)
 .addOption(new Option("o1", "option 1", true, "default1"))
 .addOption(new Option("o2", "option 2", false, "default2"));
 CommandLine parsed = null;
 try {
 parsed = optionGroup.parse(new String[]{"--groupName", "--o1", "--o2"});
 } catch (ParseException e) {
 e.printStackTrace();
 }
 assertEquals("default1", parsed.getOptionValue("o1"));
 assertEquals("default2", parsed.getOptionValue("o2"));
 }
}