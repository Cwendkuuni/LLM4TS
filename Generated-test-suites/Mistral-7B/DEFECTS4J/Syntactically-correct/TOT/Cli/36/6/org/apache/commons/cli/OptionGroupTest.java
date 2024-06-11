import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

 @Test
 public void testOptionGroupBasicUsage() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("a", "option1", false, "This is option 1.");
 Option option2 = new Option("b", "option2", false, "This is option 2.");
 Option option3 = new Option("c", "option3", false, "This is option 3.");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-a", "-b"});

 Assert.assertEquals("option1, option2", optionGroup.getSelected());
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("b"));
 Assert.assertFalse(cmd.hasOption("c"));
 } catch (ParseException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testOptionGroupRequiredOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("a", "option1", false, "This is option 1.");
 Option option2 = new Option("b", "option2", false, "This is option 2.");
 Option option3 = new Option("c", "option3", false, "This is option 3.");
 optionGroup.setRequired(true);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-a", "-b", "-c"});

 Assert.fail("Expected ParseException due to required option group.");
 } catch (ParseException e) {
 // Expected exception
 }

 try {
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-a", "-b"});

 Assert.assertEquals("option1, option2", optionGroup.getSelected());
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("b"));
 } catch (ParseException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testOptionGroupMixedUsage() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("a", "option1", false, "This is option 1.");
 Option option2 = new Option("b", "option2", false, "This is option 2.");
 Option option3 = new Option("c", "option3", false, "This is option 3.");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-a", "-c"});

 Assert.assertEquals("option1, option3", optionGroup.getSelected());
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.hasOption("c"));
 Assert.assertFalse(cmd.hasOption("b"));
 } catch (ParseException e) {
 e.printStackTrace();
 }
 }
}