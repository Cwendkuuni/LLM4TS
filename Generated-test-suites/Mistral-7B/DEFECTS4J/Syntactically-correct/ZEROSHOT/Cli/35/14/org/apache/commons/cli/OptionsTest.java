import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @After
 public void tearDown() {
 options = null;
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "output", false, "Output file path.");
 options.addOption(option);
 assertEquals("Output file path.", option.getDescription());
 assertEquals(false, option.isRequired());
 assertFalse(option.hasArg());
 assertNull(option.getLongOpt());
 assertEquals('o', option.getKey().charAt(0));
 assertNull(option.getValues());
 }

 @Test
 public void testAddOptionWithAlias() {
 Option option = new Option("o", null, false, "Output file path.");
 options.addOption(option);
 option.setLongOpt("outputFile");
 assertEquals("Output file path.", option.getDescription());
 assertEquals(false, option.isRequired());
 assertFalse(option.hasArg());
 assertEquals("outputFile", option.getLongOpt());
 assertEquals('o', option.getKey().charAt(0));
 assertNull(option.getValues());
 }

 @Test
 public void testAddOptionWithArgument() {
 Option option = new Option("o", "output", true, "Output file path.");
 options.addOption(option);
 assertEquals("Output file path.", option.getDescription());
 assertEquals(true, option.isRequired());
 assertTrue(option.hasArg());
 assertNull(option.getLongOpt());
 assertEquals('o', option.getKey().charAt(0));
 assertNull(option.getValues());
 }

 @Test
 public void testAddOptionWithLongOption() {
 Option option = new Option("o", null, false, "Output file path.");
 options.addOption(option);
 option.setLongOpt("outputFile");
 assertEquals("Output file path.", option.getDescription());
 assertEquals(false, option.isRequired());
 assertFalse(option.hasArg());
 assertEquals("outputFile", option.getLongOpt());
 assertEquals(' ', option.getKey().charAt(0));
 assertNull(option.getValues());
 }

 @Test
 public void testAddOptionWithLongOptionAndArgument() {
 Option option = new Option("o", "output", true, "Output file path.");
 options.addOption(option);
 option.setLongOpt("outputFile");
 assertEquals("Output file path.", option.getDescription());
 assertEquals(true, option.isRequired());
 assertTrue(option.hasArg());
 assertEquals("outputFile", option.getLongOpt());
 assertEquals(' ', option.getKey().charAt(0));
 assertEquals(new String[]{}, option.getValues());
 }

 @Test
 public void testAddOptionWithValue() {
 Option option = new Option("o", null, false, "Output file path.", "path");
 options.addOption(option);
 assertEquals("Output file path.", option.getDescription());
 assertEquals(false, option.isRequired());
 assertFalse(option.hasArg());
 assertEquals(new String[]{"path"}, option.getValues());
 assertEquals(' ', option.getKey().charAt(0));
 assertNull(option.getLongOpt());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("group", "Group description.");
 Option option = new Option("g", null, false, "Group option.", true);
 option.setLongOpt("groupOption");
 optionGroup.addOption(option);
 options.addOptionGroup(optionGroup);
 assertEquals("Group description.", optionGroup.getDescription());
 assertEquals(true, optionGroup.isRequired());
 assertEquals(1, optionGroup.getOptions().size());
 assertEquals("group", optionGroup.getKey());
 assertEquals("groupOption", option.getLongOpt());
 }

 @Test
 public void testGetOptions() {
 assertEquals(0, options.getOptions().size());
 Option option = new Option("o", null, false, "Output file path.");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testHasOption() {
 Option option = new Option("o", null, false, "Output file path.");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("o"));
 assertFalse(options.hasOption("n"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("o", null, false, "Output file path.");
 options.addOption(option);
 option.setLongOpt("outputFile");
 assertTrue(options.hasLongOption("outputFile"));
 assertTrue(options.hasLongOption("outputFile"));
 assertFalse(options.hasLongOption("outputFile1"));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("o", null, false, "Output file path.");
 options.addOption(option);
 assertTrue(options.hasShortOption("o"));
 assertTrue(options.hasShortOption("o"));
 assertFalse(options.hasShortOption("n"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("group", "Group description.");
 Option option = new Option("g", null, false, "Group option.", true);
 option.setLongOpt("groupOption");
 optionGroup.addOption(option);
 options.addOptionGroup(optionGroup);
 OptionGroup retrievedOptionGroup = options.getOptionGroup("group");
 assertEquals(optionGroup, retrievedOptionGroup);
 }

 @Test
 public void testGetRequiredOptions() {
 Option option = new Option("r", null, false, "Required option.", true);
 options.addOption(option);
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals(option, requiredOptions.get(0));
 }

 @Test
 public void testParseArguments() {
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-o", "test.txt", "--outputFile", "output.txt"};
 try {
 CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args));
 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertEquals("output.txt", cmd.getOptionValue("outputFile"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Failed to parse arguments correctly.");
 }
 }

}