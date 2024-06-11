import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 3, "Option C with an argument");
 options.addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("d", false, "Option D in group1")
 .addOption("e", 5, "Option E in group1 with an argument"));

 Parser parser = new DefaultParser();
 String[] arguments = {"-a", "--b", "valueB", "--c", "valueC", "--group1", "--d", "--e", "valueE"};
 CommandLine cmd = parser.parse(options, arguments, null);

 // check options and arguments
 assertEquals(cmd.getArgs().size(), 2);
 assertEquals(cmd.getArgs().get(0), "valueB");
 assertEquals(cmd.getArgs().get(1), "valueC");

 // check option values
 List<Option> optionsList = cmd.getOptions();
 Option optionA = null;
 Option optionB = null;
 Option optionC = null;
 Option optionD = null;
 Option optionE = null;

 for (Option option : optionsList) {
 if (option.getKey().equals("a")) {
 optionA = option;
 assertTrue(option.getValue().equals("true"));
 }
 else if (option.getKey().equals("b")) {
 optionB = option;
 assertEquals(option.getValue().length, 1);
 assertEquals(option.getValue()[0], "valueB");
 }
 else if (option.getKey().equals("c")) {
 optionC = option;
 assertEquals(option.getValue().length, 1);
 assertEquals(option.getValue()[0], "valueC");
 }
 else if (option.getKey().equals("d")) {
 optionD = option;
 assertFalse(option.getValue().isPresent());
 }
 else if (option.getKey().equals("e")) {
 optionE = option;
 assertEquals(option.getValue().length, 1);
 assertEquals(option.getValue()[0], "valueE");
 }
 }

 // check group option
 OptionGroup group1 = options.getOptionGroup("group1");
 Option optionGroup1 = null;
 for (Option option : cmd.getOptions()) {
 if (option.getLongOpt().equals("group1")) {
 optionGroup1 = option;
 break;
 }
 }
 assertEquals(optionGroup1, group1);
 assertTrue(optionGroup1.isSelected());

 // check required options
 List<String> requiredOptions = parser.getParser().getParser().getParserConstants().getRequiredOptions();
 assertTrue(requiredOptions.isEmpty());
 }
}