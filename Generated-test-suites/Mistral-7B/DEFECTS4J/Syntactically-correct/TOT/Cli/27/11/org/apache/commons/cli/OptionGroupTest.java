import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

 @Test
 public void testOptionGroup_SimpleUsage() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 Option option3 = new Option("o3", "option3", false, "Description for option3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Option parsedOption = null;
 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-o1", "--o2"});
 parsedOption = cmd.getOption("o1");
 } catch (ParseException e) {
 // handle parse errors here
 }

 assertEquals("option1", parsedOption.getOpt());
 assertEquals("Description for option1", parsedOption.getDescription());
 assertNull(cmd.getOption("o3"));
 }

 @Test(expected = AlreadySelectedException.class)
 public void testOptionGroup_OptionAlreadySelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 try {
 optionGroup.setSelected(option1);
 optionGroup.setSelected(option2);
 } catch (AlreadySelectedException e) {
 // expected exception
 }
 }

 @Test
 public void testOptionGroup_RequiredOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "Description for option1");
 Option option2 = new Option("o2", "option2", false, "Description for option2");
 Option option3 = new Option("o3", "option3", false, "Description for option3");
 optionGroup.setRequired(true);
 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Option parsedOption = null;
 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(optionGroup.getOptions(), new String[]{"-o1"});
 parsedOption = cmd.getOption("o1");
 } catch (ParseException e) {
 // handle parse errors here
 }

 assertEquals("option1", parsedOption.getOpt());
 assertEquals("Description for option1", parsedOption.getDescription());
 assertNull(cmd.getOption("o2"));
 assertNull(cmd.getOption("o3"));
 }
}