import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B");
 options.addOption("c", 3, "Option C with argument");
 options.addOption("d", null, "Option D with no value");
 options.addOptionGroup(new OptionGroup("group1", "Group 1"));
 options.getOptionGroup("group1").addOption("e", true, "Option E in group 1");
 options.getOptionGroup("group1").addOption("f", 5, "Option F in group 1 with argument");
 }

 @BeforeMethod
 public void setParser() {
 parser = new DefaultParser();
 parser.setOptions(options);
 }

 @Test
 public void testParseSimple() throws ParseException {
 String[] arguments = {"-a", "--b=2", "--c=3"};
 CommandLine cmd = parser.parse(arguments);
 assertTrue(cmd.hasOption("a"));
 assertEquals(true, cmd.getOptionValue("a"));
 assertEquals(2, cmd.getOptionValue("b").intValue());
 assertEquals(3, cmd.getOptionValue("c").intValue());
 assertFalse(cmd.hasOption("d"));
 assertNull(cmd.getOptionValue("d"));
 }

 @Test
 public void testParseWithGroup() throws ParseException {
 String[] arguments = {"--group1", "-e", "-f=5"};
 CommandLine cmd = parser.parse(arguments);
 assertTrue(cmd.hasOption("e"));
 assertEquals(true, cmd.getOptionValue("e"));
 assertEquals(5, cmd.getOptionValue("f").intValue());
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 String[] arguments = {"-x"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 fail("Expected ParseException but got: " + e.getMessage());
 }
 assertEquals("Unrecognized option: x", e.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 String[] arguments = {};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 fail("Expected ParseException but got: " + e.getMessage());
 }
 assertEquals("Option 'a' is required.", e.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testMissingOptionalArg() throws ParseException {
 String[] arguments = {"-c"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 fail("Expected ParseException but got: " + e.getMessage());
 }
 assertEquals("Option 'c' requires an argument.", e.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testInvalidArgType() throws ParseException {
 options.getOption("b").setType(String[].class);
 String[] arguments = {"--b=3"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 fail("Expected ParseException but got: " + e.getMessage());
 }
 assertEquals("Invalid value for option: 'b'. Received: '3'. Expected: java.lang.String[]", e.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testInvalidArgTypeWithValue() throws ParseException {
 options.getOption("b").setType(String[].class);
 String[] arguments = {"--b=1,2"};
 try {
 parser.parse(arguments);
 } catch (ParseException e) {
 fail("Expected ParseException but got: " + e.getMessage());
 }
 assertEquals("Invalid value for option: 'b'. Received: [1, 2]. Expected: java.lang.String[]", e.getMessage());
 }

 @Test
 public void testParseWithArguments() throws ParseException {
 String[] arguments = {"--a", "arg1", "--b=2", "arg2", "--c=3", "arg3"};
 CommandLine cmd = parser.parse(arguments);
 assertTrue(cmd.hasOption("a"));
 assertEquals(true, cmd.getOptionValue("a"));
 assertEquals(2, cmd.getOptionValue("b").intValue());
 assertEquals(3, cmd.getOptionValue("c").intValue());
 assertEquals(3, cmd.getArgs().size());
 assertEquals("arg1", cmd.getArgs().get(0));
 assertEquals("arg2", cmd.getArgs().get(1));
 assertEquals("arg3", cmd.getArgs().get(2));
 }
}