import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionsTest {

 private static final String[] TEST_ARGUMENTS = {"-a", "--longOption", "value", "--shortOption", "anotherValue", "--noOption"};

 @Test
 public void testParsing() throws Exception {
 Options options = new Options();

 // add options
 options.addOption("a", false, "Option A");
 options.addOption("l", "longOption", true, "Long option example");
 options.addOption("s", "shortOption", true, "Short option example");

 // parse
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), TEST_ARGUMENTS);

 // assert parsed options
 Assertions.assertFalse(cmd.hasOption("a"));
 Assertions.assertTrue(cmd.hasOption("l"));
 Assertions.assertEquals("value", cmd.getOptionValue("longOption"));
 Assertions.assertTrue(cmd.hasOption("s"));
 Assertions.assertEquals("anotherValue", cmd.getOptionValue("shortOption"));
 Assertions.assertNull(cmd.getOptionValue("nonExistentOption"));
 }

 @Test
 public void testParsingShortOptionsWithArgument() throws Exception {
 Options options = new Options();

 // add options
 options.addOption("a", true, "Option A with argument");

 // parse
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), TEST_ARGUMENTS);

 // assert parsed options
 Assertions.assertEquals("anotherValue", cmd.getOptionValue("a"));
 }

 @Test
 public void testParsingLongOptionsWithArgument() throws Exception {
 Options options = new Options();

 // add options
 options.addOption("longOption", true, "Long option example with argument");

 // parse
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), TEST_ARGUMENTS);

 // assert parsed options
 Assertions.assertEquals("value", cmd.getOptionValue("longOption"));
 }

 @Test
 public void testParsingMissingRequiredOption() throws Exception {
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("requiredGroup", "Required group", true, null, null, new Option[]{}));

 // add required option
 Option option = new Option("r", "required", true, "Required option");
 option.setRequired(true);
 option.setLongOpt("requiredLong");
 options.addOption(option);

 // parse without required option
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), TEST_ARGUMENTS);
 Assertions.fail("Expected IllegalArgumentException when required option is missing");
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }

 @Test
 public void testParsingMissingGroupRequiredOption() throws Exception {
 Options options = new Options();

 // add required option group
 OptionGroup requiredGroup = new OptionGroup("requiredGroup", "Required group", true, null, null, new Option[]{});
 options.addOptionGroup(requiredGroup);

 // add required option
 Option option = new Option("r", "required", true, "Required option");
 option.setRequired(true);
 option.setLongOpt("requiredLong");
 requiredGroup.addOption(option);

 // parse without required option
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), TEST_ARGUMENTS);
 Assertions.fail("Expected IllegalArgumentException when required option is missing");
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }

 @Test
 public void testParsingOptionGroup() throws Exception {
 Options options = new Options();

 // add option group
 OptionGroup group = new OptionGroup("group", "Group", false, null, null, new Option[]{}) {
 @Override
 public boolean isRequired() {
 return false;
 }
 };
 options.addOptionGroup(group);

 // add options in the group
 Option option1 = new Option("g1", "groupOption1", true, "Option in group 1");
 Option option2 = new Option("g2", "groupOption2", true, "Option in group 2");
 group.addOption(option1);
 group.addOption(option2);

 // parse
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), TEST_ARGUMENTS);

 // assert parsed options
 Assertions.assertEquals(2, group.getOptions().size());
 Assertions.assertEquals(option1, group.getOptions().get(0));
 Assertions.assertEquals(option2, group.getOptions().get(1));
 }
}