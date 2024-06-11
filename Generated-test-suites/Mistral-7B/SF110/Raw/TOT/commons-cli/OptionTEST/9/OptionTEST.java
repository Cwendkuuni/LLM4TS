import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreationWithNoArgs() {
 OptionalOption opt = new OptionalOption("opt1", "Description for opt1");
 assertEquals("opt1", opt.getOpt());
 assertEquals("Description for opt1", opt.getDescription());
 assertFalse(opt.hasArg());
 assertFalse(opt.hasOptionalArg());
 assertNull(opt.getValue());
 assertNull(opt.getValues());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionCreationWithInvalidOpt() {
 Option invalidOption = new Option("", "Description for invalid option");
 }

 @Test
 public void testOptionCreationWithSingleValueArg() {
 SingleValueOption opt = new SingleValueOption("opt2", "Description for opt2", "argName");
 assertEquals("opt2", opt.getOpt());
 assertEquals("Description for opt2", opt.getDescription());
 assertEquals("argName", opt.getArgName());
 assertFalse(opt.hasOptionalArg());
 assertFalse(opt.hasArgs());
 assertNull(opt.getValue());
 assertNull(opt.getValues());
 }

 @Test
 public void testOptionCreationWithMultipleValuesArg() {
 MultipleValuesOption opt = new MultipleValuesOption("opt3", "Description for opt3", "argName");
 assertEquals("opt3", opt.getOpt());
 assertEquals("Description for opt3", opt.getDescription());
 assertEquals("argName", opt.getArgName());
 assertFalse(opt.hasOptionalArg());
 assertTrue(opt.hasArgs());
 assertNull(opt.getValue());
 assertNotNull(opt.getValues());
 opt.addValue("value1");
 opt.addValue("value2");
 assertEquals(2, opt.getValues().size());
 assertEquals("value1", opt.getValues().get(0));
 assertEquals("value2", opt.getValues().get(1));
 }

 @Test
 public void testOptionCreationWithLongOpt() {
 LongOptOption opt = new LongOptOption("longOpt1", "Description for long opt1", "longArgName");
 assertEquals("longOpt1", opt.getLongOpt());
 assertEquals("Description for long opt1", opt.getDescription());
 assertFalse(opt.hasArg());
 assertFalse(opt.hasOptionalArg());
 assertNull(opt.getValue());
 assertNull(opt.getValues());
 }

 @Test
 public void testParseOption() throws ParseException {
 Options options = new Options();
 Option opt1 = new SingleValueOption("opt1", "Description for opt1", "argName1");
 Option opt2 = new MultipleValuesOption("opt2", "Description for opt2", "argName2");
 Option opt3 = new LongOptOption("longOpt3", "Description for long opt3", "longArgName3");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 String[] args = new String[]{"-opt1", "arg1", "--longOpt3", "arg3", "--opt2", "arg2", "arg3"};
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args);
 Option parsedOpt1 = cmd.getOption("opt1");
 Option parsedOpt2 = cmd.getOption("opt2");
 Option parsedOpt3 = cmd.getOption("longOpt3");
 assertEquals("arg1", parsedOpt1.getValue());
 assertEquals(2, parsedOpt2.getValues().size());
 assertEquals("arg2", parsedOpt2.getValues().get(0));
 assertEquals("arg3", parsedOpt2.getValues().get(1));
 assertEquals("arg3", parsedOpt3.getValue());
 }

 @Test
 public void testParseOptionWithInvalidArgument() throws ParseException {
 Options options = new Options();
 SingleValueOption opt = new SingleValueOption("opt1", "Description for opt1", "argName1");
 options.addOption(opt);
 String[] args = new String[]{"-opt1", "arg1,arg2"};
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 }
 catch (ParseException e) {
 assertEquals("Invalid number of arguments for option: opt1", e.getMessage());
 }
 }

 @Test
 public void testParseOptionWithMissingArgument() throws ParseException {
 Options options = new Options();
 SingleValueOption opt = new SingleValueOption("opt1", "Description for opt1", "argName1");
 options.addOption(opt);
 String[] args = new String[]{"-opt1"};
 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Expected ParseException");
 }
 catch (ParseException e) {
 assertEquals("Required option 'argName1' is missing", e.getMessage());
 }
 }
}