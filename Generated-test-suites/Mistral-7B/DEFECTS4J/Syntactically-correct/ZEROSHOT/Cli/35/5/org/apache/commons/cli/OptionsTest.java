import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();

 // test adding a single character option without argument
 options.addOption("a", "option A");
 Option optA = options.getOption("a");
 assertEquals("option A", optA.getLongOpt());
 assertEquals(false, optA.hasArg());
 assertEquals("", optA.getArgName());

 // test adding a single character option with argument
 options.addOption("b", "option B", true, "argument B");
 Option optB = options.getOption("b");
 assertEquals("option B", optB.getLongOpt());
 assertEquals(true, optB.hasArg());
 assertEquals("argument B", optB.getArgName());

 // test adding a long option without argument
 options.addOption("c", "option C", false, "option C");
 Option optC = options.getOption("c");
 assertEquals("option C", optC.getLongOpt());
 assertEquals(false, optC.hasArg());
 assertEquals("", optC.getArgName());

 // test adding a long option with argument
 options.addOption("d", "option D", true, "argument D");
 Option optD = options.getOption("d");
 assertEquals("option D", optD.getLongOpt());
 assertEquals(true, optD.hasArg());
 assertEquals("argument D", optD.getArgName());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "group1 description");
 Option optE = new Option("e", "option E", false, "option E description");
 Option optF = new Option("f", "option F", false, "option F description");
 group.addOption(optE);
 group.addOption(optF);
 options.addOptionGroup(group);

 // test getOptionGroup
 OptionGroup groupFromOptions = options.getOptionGroup(optE);
 assertEquals("group1", groupFromOptions.getName());

 // test getOptions
 List<Option> optionsList = options.getOptions();
 assertEquals(5, optionsList.size());
 Option optA = optionsList.get(0);
 Option optB = optionsList.get(1);
 Option optC = optionsList.get(2);
 Option optD = optionsList.get(3);
 Option optEFromList = optionsList.get(4);
 assertEquals(optE, optEFromList);

 // test hasOption
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("c"));
 assertTrue(options.hasOption("d"));
 assertTrue(options.hasOption("e"));

 // test getOption
 Option optAFromOptions = options.getOption("a");
 Option optBFromOptions = options.getOption("b");
 Option optCFromOptions = options.getOption("c");
 Option optDFromOptions = options.getOption("d");
 Option optEFromOptions = options.getOption("e");
 assertEquals(optA, optAFromOptions);
 assertEquals(optB, optBFromOptions);
 assertEquals(optC, optCFromOptions);
 assertEquals(optD, optDFromOptions);
 assertEquals(optE, optEFromOptions);
 }

 @Test
 public void testParse() {
 Options options = new Options();
 options.addOption("a", "option A");
 options.addOption("b", "option B", true, "argument B");
 options.addOption("c", "option C", false, "option C");
 options.addOption("d", "option D", true, "argument D");

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "valueA", "-b", "valueB"};
 CommandLine cmd = null;
 try {
 cmd = parser.parse(options, args);
 } catch (ParseException e) {
 fail("parsing failed: " + e.getMessage());
 }

 // test parsed options
 assertEquals(4, cmd.getArgs().length);
 Option optA = options.getOption("a");
 Option optB = options.getOption("b");
 Option optC = options.getOption("c");
 Option optD = options.getOption("d");

 // test parsed values
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 assertNull(cmd.getOptionValue("c"));
 assertNull(cmd.getOptionValue("d"));

 // test parsed boolean options
 assertTrue(Boolean.parseBoolean(cmd.getOptionValue("b")));
 assertFalse(cmd.hasOption("c"));
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidOption() {
 Options options = new Options();
 options.addOption("a", "option A");
 String[] args = {"--invalid"};
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = null;
 try {
 cmd = parser.parse(options, args);
 } catch (ParseException e) {
 // expected
 }

 // test unparsed option
 assertEquals(1, args.length);
 assertNull(cmd.getOptionValue("a"));
 assertNull(cmd.getOptionValue("invalid"));
 }
}