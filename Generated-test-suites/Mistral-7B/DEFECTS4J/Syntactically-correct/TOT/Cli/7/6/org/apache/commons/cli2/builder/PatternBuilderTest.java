import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.builder.arg.ArgumentBuilder;
import org.apache.commons.cli2.builder.opt.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.opt.OptionBuilder;
import org.apache.commons.cli2.builder.opt.GroupBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

 private PatternBuilder builder;
 private CommandLineParser parser;

 @Before
 public void setUp() {
 builder = new PatternBuilder();
 parser = new DefaultParser();
 }

 @Test
 public void testSimpleOption() {
 builder.withPattern("-s, --simple [ARG]");
 Option option = builder.create();
 assertNotNull(option);
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertEquals("simple", option.getOpt());
 assertEquals("s", option.getShortOpt());
 assertNull(option.getArgument());
 }

 @Test
 public void testRequiredOption() {
 builder.withPattern("-r, --required [-a ARG]");
 Option option = builder.create();
 assertNotNull(option);
 assertTrue(option.isRequired());
 assertNull(option.getLongOpt());
 assertEquals("required", option.getOpt());
 assertEquals("r", option.getShortOpt());
 assertNull(option.getArgument());
 }

 @Test
 public void testOptionalWithArgument() {
 builder.withPattern("-p, --prefix [ARG]");
 Option option = builder.create();
 assertNotNull(option);
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertEquals("prefix", option.getOpt());
 assertEquals("p", option.getShortOpt());
 assertNull(option.getArgument());
 }

 @Test
 public void testOptionalWithArgumentAndValue() {
 builder.withPattern("-n, --number [ARG=NUMBER]");
 Option option = builder.create();
 assertNotNull(option);
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertEquals("number", option.getOpt());
 assertEquals("n", option.getShortOpt());
 assertEquals("NUMBER", option.getArgument().getName());
 assertNull(option.getValue());
 }

 @Test
 public void testOptionalWithArgumentAndDefaultValue() {
 builder.withPattern("-d, --default [ARG=NUMBER:3]");
 Option option = builder.create();
 assertNotNull(option);
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertEquals("default", option.getOpt());
 assertEquals("d", option.getShortOpt());
 assertEquals("NUMBER", option.getArgument().getName());
 assertEquals("3", option.getDefaultValue());
 }

 @Test
 public void testOptionalWithArgumentAndRange() {
 builder.withPattern("-l, --limit [ARG=NUMBER:1,10]");
 Option option = builder.create();
 assertNotNull(option);
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertEquals("limit", option.getOpt());
 assertEquals("l", option.getShortOpt());
 assertEquals("NUMBER", option.getArgument().getName());
 assertEquals(1, option.getMinimum());
 assertEquals(10, option.getMaximum());
 }

 @Test(expected = ParseException.class)
 public void testInvalidPattern() {
 builder.withPattern("--invalid");
 Option option = builder.create();
 fail("Expected ParseException");
 }

 @Test
 public void testParse() {
 builder.withPattern("--group [--option1 ARG] [--option2 ARG]");
 Option group = builder.create();
 assertNotNull(group);
 assertEquals("group", group.getOpt());
 assertNull(group.getShortOpt());
 assertTrue(group.isRequired());

 Option option1 = builder.create();
 assertNotNull(option1);
 assertEquals("option1", option1.getOpt());
 assertNull(option1.getShortOpt());
 assertFalse(option1.isRequired());

 Option option2 = builder.create();
 assertNotNull(option2);
 assertEquals("option2", option2.getOpt());
 assertNull(option2.getShortOpt());
 assertFalse(option2.isRequired());

 Option parserResult = null;
 try {
 parserResult = parser.parse(builder.buildParser(), new String[]{"--group", "1", "--option1", "2"});
 } catch (ParseException e) {
 fail(e.getMessage());
 }

 assertNotNull(parserResult);
 assertEquals(3, parserResult.getArguments().size());
 assertEquals("1", parserResult.getArguments().get(0).getValue());
 assertEquals("2", parserResult.getArguments().get(1).getValue());
 assertEquals("", parserResult.getArguments().get(2).getValue());
 }

 @Test
 public void testHelp() {
 builder.withPattern("--group [--option1 ARG] [--option2 ARG]");
 Option group = builder.create();
 assertNotNull(group);
 assertEquals("group", group.getOpt());
 assertNull(group.getShortOpt());
 assertTrue(group.isRequired());

 Option option1 = builder.create();
 assertNotNull(option1);
 assertEquals("option1", option1.getOpt());
 assertNull(option1.getShortOpt());
 assertFalse(option1.isRequired());

 Option option2 = builder.create();
 assertNotNull(option2);
 assertEquals("option2", option2.getOpt());
 assertNull(option2.getShortOpt());
 assertFalse(option2.isRequired());

 HelpFormatter formatter = new HelpFormatter();
 String help = formatter.printHelp("MyApp", builder.buildParser().getOptions(), true);

 assertTrue(help.contains("--group"));
 assertTrue(help.contains("--option1"));
 assertTrue(help.contains("--option2"));
 }
}