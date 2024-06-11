import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option("a", "option-a", false, "This is an option A description.");
 assertEquals("a", option.getOpt());
 assertEquals("option-a", option.getLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("This is an option A description.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertEquals(-1, option.getArgs());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionCreationWithArg() {
 Option option = new Option("b", "option-b", true, "This is an option B description.");
 assertEquals("b", option.getOpt());
 assertEquals("option-b", option.getLongOpt());
 assertTrue(option.hasArg());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("This is an option B description.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertEquals(1, option.getArgs());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionCreationWithArgAndLongOpt() {
 Option option = new Option("c", "option-c", true, "Option C description.", "value");
 assertEquals("c", option.getOpt());
 assertEquals("option-c", option.getLongOpt());
 assertTrue(option.hasArg());
 assertEquals(String.class, option.getType());
 assertFalse(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertEquals("Option C description.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertEquals(1, option.getArgs());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionId() {
 Option option = new Option("d", "option-d", false, "Option D description.");
 assertEquals('d', option.getId());
 }

 @Test
 public void testOptionWithValues() {
 Option option = new Option("e", "option-e", false, "Option E description.", "value1,value2");
 assertEquals("e", option.getOpt());
 assertEquals("option-e", option.getLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("Option E description.", option.getDescription());
 assertFalse(option.isRequired());
 assertNull(option.getArgName());
 assertFalse(option.hasArgs());
 assertTrue(option.hasValueSeparator());
 assertEquals(',', option.getValueSeparator());
 assertEquals(2, option.getArgs());
 assertFalse(option.hasNoValues());
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 }

 @Test
 public void testOptionParsingFailure() {
 Option option = null;
 try {
 option = new Option("f", null, false, null);
 fail("Option creation should have failed.");
 }
 catch (IllegalArgumentException iae) {
 // expected
 }
 try {
 option = new Option("g", null, false, "Option G description.", null);
 fail("Option creation should have failed.");
 }
 catch (IllegalArgumentException iae) {
 // expected
 }
 try {
 option = new Option("h", "option-h", -1, "Option H description.");
 fail("Option creation should have failed.");
 }
 catch (IllegalArgumentException iae) {
 // expected
 }
 try {
 option = new Option("i", "option-i", true, null, null);
 fail("Option creation should have failed.");
 }
 catch (IllegalArgumentException iae) {
 // expected
 }
 try {
 option = new Option("j", "option-j", false, "Option J description.", null, 5);
 fail("Option creation should have failed.");
 }
 catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testOptionParsingSuccess() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("k", "option-k", false, "Option K description.");
 parser.parse(new String[]{"-k", "value1", "--option-k=value2"});
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 }

 @Test(expected = ParseException.class)
 public void testOptionParsingFailure() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("l", "option-l", false, "Option L description.");
 parser.parse(new String[]{"--option-l", "value1"});
 fail("Option parsing should have failed.");
 }

 @Test
 public void testOptionParsingWithValueSeparator() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("m", "option-m", false, "Option M description.", ",arg1,arg2");
 parser.parse(new String[]{"-m", "arg1,arg2"});
 assertEquals("arg1", option.getValue(0));
 assertEquals("arg2", option.getValue(1));
 }

 @Test(expected = ParseException.class)
 public void testOptionParsingWithInvalidValueSeparator() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("n", "option-n", false, "Option N description.", ";arg1,arg2");
 parser.parse(new String[]{"-n", "arg1;arg2"});
 fail("Option parsing should have failed.");
 }

 @Test
 public void testOptionParsingWithOptionalArg() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("o", "option-o", true, "Option O description.");
 parser.parse(new String[]{"-o"});
 assertNull(option.getValue());
 parser.parse(new String[]{"-o", "value1"});
 assertEquals("value1", option.getValue());
 }

 @Test(expected = ParseException.class)
 public void testOptionParsingWithInvalidOptionalArg() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("p", "option-p", true, "Option P description.", "value");
 parser.parse(new String[]{"-p"});
 fail("Option parsing should have failed.");
 }

 @Test
 public void testOptionParsingWithRequiredArg() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("q", "option-q", true, "Option Q description.");
 parser.parse(new String[]{"-q", "value1"});
 assertEquals("value1", option.getValue());
 parser.parse(new String[]{"-q"});
 throw new ParseException("Option parsing should have failed.");
 }

 @Test(expected = ParseException.class)
 public void testOptionParsingWithInvalidArgNumber() throws ParseException {
 OptionParser parser = new DefaultParser();
 Option option = new Option("r", "option-r", true, "Option R description.", "value1,value2,value3");
 parser.parse(new String[]{"-r", "value1,value2"});
 fail("Option parsing should have failed.");
 }
}