import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testOptionsCreation() {
 Options options = new Options();
 assertEquals(0, options.size());
 }

 @Test
 public void testAddOptionShort() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 assertEquals(1, options.size());
 Option optionA = options.getOption("a");
 assertNotNull(optionA);
 assertEquals("Option A", optionA.getLongOpt());
 assertEquals(false, optionA.hasArg());
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "Option A with argument");
 assertEquals(1, options.size());
 Option optionA = options.getOption("a");
 assertNotNull(optionA);
 assertEquals("optionA", optionA.getOpt());
 assertEquals("Option A with argument", optionA.getDescription());
 assertTrue(optionA.hasArg());
 }

 @Test
 public void testAddOptionLong() {
 Options options = new Options();
 options.addOption("myOption", "mylongOption", false, "My Option");
 assertEquals(1, options.size());
 Option option = options.getOption("myOption");
 assertNotNull(option);
 assertEquals("mylongOption", option.getLongOpt());
 assertEquals("My Option", option.getDescription());
 assertEquals(false, option.hasArg());
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = new Options();
 options.addOption("myOption", null, true, "My Option with argument");
 assertEquals(1, options.size());
 Option option = options.getOption("myOption");
 assertNotNull(option);
 assertNull(option.getOpt());
 assertEquals("My Option with argument", option.getDescription());
 assertTrue(option.hasArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortKey() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("a", "optionB", false, "Option B");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongKey() {
 Options options = new Options();
 options.addOption("myOption", "optionA", false, "Option A");
 options.addOption("myOption", "optionB", false, "Option B");
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "My Group");
 options.addOptionGroup(optionGroup);
 assertEquals(1, options.size());
 assertEquals(0, optionGroup.getOptions().size());
 }

 @Test
 public void testAddOptionToOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "My Group");
 Option option = new Option("o", "option", false, "Option with group");
 option.setOptionGroup(optionGroup);
 options.addOptionGroup(optionGroup);
 options.addOption(option);
 assertEquals(1, options.size());
 assertEquals(1, optionGroup.getOptions().size());
 }

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B with argument");

 String[] args = new String[]{"-a", "valueA", "-b", "valueB"};
 CommandLine line = new DefaultParser().parse(options, args);
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");

 assertEquals(2, options.size());
 assertNotNull(line);
 assertTrue(line.hasOption("a"));
 assertTrue(line.hasOption("b"));
 assertEquals("valueA", line.getOptionValue("a"));
 assertEquals("valueB", line.getOptionValue("b"));
 assertEquals(optionA, line.getOption("a"));
 assertEquals(optionB, line.getOption("b"));
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B with argument");
 options.addOptionGroup(new OptionGroup("group", "Group"));
 options.addOption("c", "optionC", true, "Option C with argument");
 options.addOption("d", "optionD", true, "Option D with argument");
 options.addOption("e", "optionE", true, "Option E with argument");
 options.addOption("f", "optionF", true, "Option F with argument");
 options.addOption("g", "optionG", true, "Option G with argument");
 options.addOption("h", "optionH", true, "Option H with argument");
 options.addOption("i", "optionI", true, "Option I with argument");
 options.addOption("j", "optionJ", true, "Option J with argument");
 options.addOption("k", "optionK", true, "Option K with argument");
 options.addOption("l", "optionL", true, "Option L with argument");
 options.addOption("m", "optionM", true, "Option M with argument");
 options.addOption("n", "optionN", true, "Option N with argument");
 options.addOption("o", "optionO", true, "Option O with argument");
 options.addOption("p", "optionP", true, "Option P with argument");
 options.addOption("q", "optionQ", true, "Option Q with argument");
 options.addOption("r", "optionR", true, "Option R with argument");
 options.addOption("s", "optionS", true, "Option S with argument");
 options.addOption("t", "optionT", true, "Option T with argument");
 options.addOption("u", "optionU", true, "Option U with argument");
 options.addOption("v", "optionV", true, "Option V with argument");
 options.addOption("w", "optionW", true, "Option W with argument");
 options.addOption("x", "optionX", true, "Option X with argument");
 options.addOption("y", "optionY", true, "Option Y with argument");
 options.addOption("z", "optionZ", true, "Option Z with argument");

 String[] args = new String[]{"-a", "valueA"};
 try {
 CommandLine line = new DefaultParser().parse(options, args);
 } catch (ParseException e) {
 assertEquals("Missing required options: [-c, -d, -e, -f, -g, -h, -i, -j, -k, -l, -m, -n, -o, -p, -q, -r, -s, -t, -u, -v, -w, -x, -y, -z]", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B with argument");
 options.addOption("c", "optionC", true, "Option C with argument");

 String[] args = new String[]{"-x", "value"};
 try {
 CommandLine line = new DefaultParser().parse(options, args);
 } catch (ParseException e) {
 assertEquals("Unrecognized option: -x", e.getMessage());
 }
 }
}