import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

 @Test
 public void testOptionGroupWithRequiredOptions() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "Description for option1");
 Option option2 = new Option("o2", "option2", true, "Description for option2");
 group.addOption(option1).addOption(option2).setRequired(true);

 try {
 group.setSelected(option1);
 assertEquals("option1", group.getSelected());
 } catch (AlreadySelectedException e) {
 // This should not be thrown as options are required
 fail("Should not reach this point");
 }

 try {
 group.setSelected(option2);
 assertEquals("option2", group.getSelected());
 } catch (AlreadySelectedException e) {
 // This should not be thrown as options are required
 fail("Should not reach this point");
 }

 try {
 group.setSelected(null);
 fail("Should not be able to set selected to null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testOptionGroupWithOptionalOptions() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 group.addOption(option1).addOption(option2);

 try {
 group.setSelected(option1);
 assertEquals("option1", group.getSelected());
 } catch (AlreadySelectedException e) {
 // This should not be thrown
 }

 try {
 group.setSelected(option2);
 assertEquals("option2", group.getSelected());
 } catch (AlreadySelectedException e) {
 // This should not be thrown
 }

 try {
 group.setSelected(null);
 fail("Should not be able to set selected to null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testOptionGroupWithDuplicateSelectedOption() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o1", "option1", false, "Description for option1");
 group.addOption(option1).addOption(option2);

 try {
 group.setSelected(option1);
 assertEquals("option1", group.getSelected());
 } catch (AlreadySelectedException e) {
 fail("Should not reach this point");
 }

 try {
 group.setSelected(option2);
 fail("Should not be able to select the same option twice");
 } catch (AlreadySelectedException e) {
 // Expected
 }
 }
}