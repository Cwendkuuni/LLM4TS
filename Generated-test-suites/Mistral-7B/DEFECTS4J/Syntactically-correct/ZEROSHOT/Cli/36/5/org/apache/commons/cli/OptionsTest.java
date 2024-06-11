import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptions() {
 Options options = new Options();

 options.addOption("a", "alpha", false, "An option with a short name 'a' and a long name 'alpha'.");
 options.addOption("b", "beta", true, "An option with a short name 'b' and a long name 'beta'. It requires an argument.");
 options.addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("c", "char", false, "Option c in group 'group1'.")
 .addOption("d", "digit", true, "Option d in group 'group1'. It requires an argument."));
 options.addOptionGroup(new OptionGroup("group2", "Group 2")
 .addOption("e", "epsilon", false, "Option e in group 'group2'.")
 .addOption("f", "phi", true, "Option f in group 'group2'. It requires an argument."));

 return options;
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();

 assertEquals("alpha", options.getOption("a").getLongOpt());
 assertTrue(options.getOption("a").isRequired());
 assertFalse(options.getOption("a").hasArg());

 assertEquals("An option with a short name 'a' and a long name 'alpha'.", options.getOption("a").getDescription());
 }

 @Test
 public void testAddOptionWithShortArg() {
 Options options = createOptions();

 assertEquals("beta", options.getOption("b").getLongOpt());
 assertFalse(options.getOption("b").isRequired());
 assertTrue(options.getOption("b").hasArg());
 }

 @Test
 public void testAddOptionWithLongArg() {
 Options options = createOptions();

 assertEquals("beta", options.getOption("b").getLongOpt());
 assertFalse(options.getOption("b").isRequired());
 assertTrue(options.getOption("b").hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group1 = options.getOptionGroups().get(0);
 OptionGroup group2 = options.getOptionGroups().get(1);

 assertEquals("group1", group1.getName());
 assertEquals("Group 1", group1.getDescription());

 assertEquals("group2", group2.getName());
 assertEquals("Group 2", group2.getDescription());
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("alpha"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("beta"));
 assertTrue(options.hasOption("c"));
 assertTrue(options.hasOption("char"));
 assertTrue(options.hasOption("d"));
 assertTrue(options.hasOption("digit"));

 assertFalse(options.hasOption("x"));
 assertFalse(options.hasOption("z"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptions();

 assertTrue(options.hasLongOption("alpha"));
 assertTrue(options.hasLongOption("beta"));
 assertTrue(options.hasLongOption("char"));
 assertTrue(options.hasLongOption("digit"));

 assertFalse(options.hasLongOption("x"));
 assertFalse(options.hasLongOption("z"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptions();

 assertTrue(options.hasShortOption("a"));
 assertTrue(options.hasShortOption("b"));
 assertTrue(options.hasShortOption("c"));
 assertTrue(options.hasShortOption("d"));

 assertFalse(options.hasShortOption("x"));
 assertFalse(options.hasShortOption("z"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();

 List<String> matchingOptions = options.getMatchingOptions("ch");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("char"));
 assertTrue(matchingOptions.contains("digit"));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("d");
 Option optionE = options.getOption("e");
 Option optionF = options.getOption("f");

 assertEquals("alpha", optionA.getLongOpt());
 assertFalse(optionA.getArgs().isEmpty());
 assertFalse(optionA.isRequired());
 assertFalse(optionA.hasArg());

 assertEquals("beta", optionB.getLongOpt());
 assertFalse(optionB.getArgs().isEmpty());
 assertTrue(optionB.isRequired());
 assertTrue(optionB.hasArg());

 assertEquals("char", optionC.getLongOpt());
 assertFalse(optionC.getArgs().isEmpty());
 assertFalse(optionC.isRequired());
 assertTrue(optionC.hasArg());

 assertEquals("digit", optionD.getLongOpt());
 assertFalse(optionD.getArgs().isEmpty());
 assertFalse(optionD.isRequired());
 assertTrue(optionD.hasArg());

 assertEquals("epsilon", optionE.getLongOpt());
 assertFalse(optionE.getArgs().isEmpty());
 assertFalse(optionE.isRequired());
 assertFalse(optionE.hasArg());

 assertEquals("phi", optionF.getLongOpt());
 assertFalse(optionF.getArgs().isEmpty());
 assertFalse(optionF.isRequired());
 assertTrue(optionF.hasArg());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptions();

 List<Option> requiredOptions = options.getRequiredOptions();

 assertEquals(0, requiredOptions.size());

 options.addOption("r", true, "Option r is required.");
 requiredOptions = options.getRequiredOptions();

 assertEquals(1, requiredOptions.size());
 assertEquals("r", requiredOptions.get(0).getLongOpt());
 assertTrue(requiredOptions.get(0).isRequired());
 assertFalse(requiredOptions.get(0).hasArg());
 }

 @Test
 public void testParse() {
 Options options = createOptions();
 CommandLineParser parser = new DefaultParser();

 String[] args = new String[]{"myApp", "--alpha", "valueA", "--beta", "valueB", "--group1", "--c", "valueC", "--char", "valueD", "--group2", "--epsilon", "valueE", "--phi", "valueF"};

 try {
 CommandLine parsedArgs = parser.parse(options, args);

 assertEquals(args.length, parsedArgs.getArgs().size());

 for (int i = 0; i < args.length; i++) {
 String arg = args[i];
 String parsedArg = parsedArgs.getArgs().get(i);

 if (i == 0 || i == 1) {
 assertEquals(arg, parsedArg);
 } else {
 Option option = options.getOption(parsedArg);
 assertEquals(parsedArg, option.getLongOpt());
 assertEquals(args[i+1], option.getValue());
 }
 }
 } catch (Exception e) {
 fail("Exception was thrown: " + e.getMessage());
 }
 }
}