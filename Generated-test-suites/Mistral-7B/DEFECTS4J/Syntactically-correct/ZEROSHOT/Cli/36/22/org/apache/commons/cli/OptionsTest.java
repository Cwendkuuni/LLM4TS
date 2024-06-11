import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OptionsTest {

 private Options options;

 @BeforeEach
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option option = new Option("a", "alert", false, "Display an alert message.");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithArgument() {
 Option option = new Option("b", "base", true, "Base directory for data files.");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertTrue(option.isRequired());
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Option option = new Option("c", "color", false, "Set the color scheme.");
 option.setLongOpt("colors");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("g", "Group");
 Option option1 = new Option("h", "help", false, "Display this help message.");
 Option option2 = new Option("i", "info", false, "Display info message.");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 assertEquals(3, options.getOptions().size());
 }

 @Test
 public void testHasOption() {
 Option option = new Option("j", "justify", false, "Justify the text.");
 options.addOption(option);
 assertTrue(options.hasOption("j"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("k", "keep", false, "Keep the files.");
 options.addOption(option);
 assertTrue(options.hasLongOption("keep"));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("l", "list", false, "List the files.");
 options.addOption(option);
 assertTrue(options.hasShortOption("l"));
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("m", "maximum", true, "The maximum size.");
 Option option2 = new Option("minimum", false, "The minimum size.");
 Option option3 = new Option("max", "maximum", true, "The maximum size.");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> matchingOptions = options.getMatchingOptions("ma");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("maximum"));
 assertTrue(matchingOptions.contains("max"));
 }

 @Test
 public void testParse() throws Exception {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(), new String[]{"-a", "-b", "/home/user", "-c", "red"});
 assertEquals(3, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("b"));
 assertEquals("red", cmd.getOptionValue("c"));
 }

 @Test
 public void testParseWithOptionGroup() throws Exception {
 OptionGroup group = new OptionGroup("g", "Group");
 Option option1 = new Option("h", "help", false, "Display this help message.");
 Option option2 = new Option("i", "info", false, "Display info message.");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(), new String[]{"--help"});
 assertTrue(cmd.hasOption("help"));
 }

 @AfterEach
 public void tearDown() {
// Clear the options to prepare for the next test
 options.getOptions().clear();
 }
}