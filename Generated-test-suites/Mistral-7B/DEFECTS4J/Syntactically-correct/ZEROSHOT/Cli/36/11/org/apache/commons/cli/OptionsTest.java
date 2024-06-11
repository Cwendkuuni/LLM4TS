import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
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
 public void testAddOptionShort() {
 Option opt = new Option("o", "opt", false, "Option short");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 assertEquals(opt, options.getOption("o"));
 }

 @Test
 public void testAddOptionLong() {
 Option opt = new Option("option", "opt", false, "Option long");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 assertEquals(opt, options.getOption("option"));
 }

 @Test
 public void testAddOptionShortWithValue() {
 Option opt = new Option("o", "opt", true, "Option short with value");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 assertEquals(opt, options.getOption("o"));
 }

 @Test
 public void testAddOptionLongWithValue() {
 Option opt = new Option("option", "opt", true, "Option long with value");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 assertEquals(opt, options.getOption("option"));
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group");
 Option opt = new Option("o", "opt", false, "Option in group", group);
 options.addOptionGroup(group);
 assertEquals(1, options.getOptions().size());
 assertEquals(group, options.getOptionGroup("group"));
 assertEquals(1, group.getOptions().size());
 assertEquals(opt, group.getOption("opt"));
 }

 @Test
 public void testAddOptionGroupWithRequiredOption() {
 OptionGroup group = new OptionGroup("group", "Group");
 Option opt = new Option("o", "opt", true, "Option in group", group);
 group.setRequired(true);
 options.addOptionGroup(group);
 assertEquals(1, options.getOptions().size());
 assertEquals(group, options.getOptionGroup("group"));
 assertEquals(1, group.getOptions().size());
 assertEquals(opt, group.getOption("opt"));
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testGetRequiredOptions() {
 OptionGroup group = new OptionGroup("group", "Group");
 Option opt = new Option("o", "opt", true, "Option in group", group);
 group.setRequired(true);
 options.addOptionGroup(group);
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals(opt, requiredOptions.get(0));
 }

 @Test
 public void testHasOption() {
 Option opt = new Option("o", "opt", false, "Option");
 options.addOption(opt);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("opt"));
 }

 @Test
 public void testHasLongOption() {
 Option opt = new Option("option", "opt", false, "Option long");
 options.addOption(opt);
 assertTrue(options.hasLongOption("option"));
 }

 @Test
 public void testHasShortOption() {
 Option opt = new Option("o", "opt", false, "Option short");
 options.addOption(opt);
 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group");
 Option opt = new Option("o", "opt", false, "Option in group", group);
 options.addOptionGroup(group);
 OptionGroup result = options.getOptionGroup("group");
 assertEquals(group, result);
 }

 @Test
 public void testGetMatchingOptions() {
 Option opt1 = new Option("longOption1", "longOpt1", false, "Option1 long");
 Option opt2 = new Option("longOption2", "longOpt2", false, "Option2 long");
 options.addOption(opt1);
 options.addOption(opt2);
 List<String> result = options.getMatchingOptions("longOpt");
 assertEquals(2, result.size());
 assertTrue(result.contains("longOption1"));
 assertTrue(result.contains("longOption2"));
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option opt1 = new Option("o1", "opt1", false, "Option1");
 Option opt2 = new Option("o2", "opt2", false, "Option2");
 options.addOption(opt1);
 options.addOption(opt2);
 String result = options.toString();
 assertTrue(result.contains("short"));
 assertTrue(result.contains("long"));
 }
}