import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private Option option;
 private CommandLineParser parser;

 @Before
 public void setUp() {
 option = new OptionBuilder("o", "option")
 .required(false)
 .withLongOpt("myOption")
 .withDescription("An example option")
 .create();
 parser = new DefaultParser();
 }

 @Test
 public void testCanProcessTrue() {
 WriteableCommandLine commandLine = new GenericCommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();
 arguments.add("myOption");
 arguments.add("value");

 assertTrue(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testCanProcessFalse() {
 WriteableCommandLine commandLine = new GenericCommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

 assertFalse(option.canProcess(commandLine, arguments));
 arguments.next();
 arguments.next();
 arguments.next(); // consume "myOption"
 arguments.next(); // consume "value"

 assertFalse(option.canProcess(commandLine, arguments));
 }

 @Test
 public void testGetId() {
 assertEquals( option.getId(), option.getOptionParser().getOptions().indexOf(option) );
 }

 @Test
 public void testEquals() {
 Option option2 = new OptionBuilder("o", "option2")
 .required(false)
 .withLongOpt("myOption2")
 .withDescription("Another example option")
 .create();

 assertFalse(option.equals(option2));

 Option option3 = new OptionBuilder("o", "option")
 .required(false)
 .withLongOpt("myOption")
 .withDescription("An example option")
 .create();

 assertTrue(option.equals(option3));
 }

 @Test
 public void testHashCode() {
 Option option2 = new OptionBuilder("o", "option2")
 .required(false)
 .withLongOpt("myOption2")
 .withDescription("Another example option")
 .create();

 int optionHashCode = option.hashCode();
 int option2HashCode = option2.hashCode();

 assertNotEquals(optionHashCode, option2HashCode);

 Option option3 = new OptionBuilder("o", "option")
 .required(false)
 .withLongOpt("myOption")
 .withDescription("An example option")
 .create();

 assertEquals(optionHashCode, option3.hashCode());
 }

 @Test
 public void testFindOption() {
 Option option4 = new OptionBuilder("o", "option4")
 .required(false)
 .withLongOpt("myOption4")
 .withDescription("Another example option")
 .create();
 Option option5 = new OptionBuilder("o", "option5")
 .required(false)
 .withLongOpt("myOption5")
 .withDescription("Another example option")
 .create();

 Option foundOption = option.findOption("myOption");
 Option foundOption2 = option.findOption("myOption2");
 Option foundOption4 = option.findOption("myOption4");
 Option foundOption5 = option.findOption("myOption5");

 assertEquals(option, foundOption);
 assertNull(foundOption2);
 assertNull(foundOption4);
 assertNull(foundOption5);
 }

 @Test
 public void testIsRequired() {
 Option requiredOption = new OptionBuilder("r", "requiredOption")
 .required(true)
 .withLongOpt("myRequiredOption")
 .withDescription("A required option")
 .create();

 assertTrue(requiredOption.isRequired());
 }

 @Test
 public void testCheckPrefixes() {
 Set prefixes = java.util.Collections.singleton("--");
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithMissingPrefix() {
 Set prefixes = java.util.Collections.singleton("--o");
 option.checkPrefixes(prefixes);
 }
}