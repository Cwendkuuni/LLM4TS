import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionImplTest {

 @Test
 public void testOptionImplConstructor() {
 OptionImpl option = new OptionImpl(1, true);
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 OptionImpl option = new OptionImpl(1, false);
 WriteableCommandLine commandLine = new CommandLine();
 assertFalse(option.canProcess(commandLine, null));

 String[] args = {"--option1", "value"};
 WriteableCommandLine commandLineWithArgs = new CommandLine(args);
 ListIterator<String> arguments = commandLineWithArgs.getParsedArgs().listIterator();
 assertTrue(option.canProcess(commandLineWithArgs, arguments));
 }

 @Test
 public void testToString() {
 OptionImpl option = new OptionImpl(1, false);
 String expected = "OptionImpl[id=1, required=false]";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, false);
 OptionImpl option2 = new OptionImpl(1, false);
 OptionImpl option3 = new OptionImpl(2, false);

 assertEquals(option1, option1);
 assertEquals(option1, option2);
 assertNotSame(option1, option3);
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, false);
 OptionImpl option2 = new OptionImpl(1, false);
 OptionImpl option3 = new OptionImpl(2, false);
 OptionImpl option4 = new OptionImpl(1, false) {
 @Override
 public String getPreferredName() {
 return "newPreferredName";
 }
 };

 assertEquals(option1.hashCode(), option1.hashCode());
 assertEquals(option1.hashCode(), option2.hashCode());
 assertNotEquals(option1.hashCode(), option3.hashCode());
 assertNotEquals(option1.hashCode(), option4.hashCode());
 }

 @Test
 public void testFindOption() {
 OptionImpl option1 = new OptionImpl(1, false) {
 @Override
 public Set<String> getTriggers() {
 return OptionBuilder.withLongOpt("option1")
 .withAliases("o1")
 .withDescription("Option 1")
 .create();
 }
 };
 OptionImpl option2 = new OptionImpl(2, false) {
 @Override
 public Set<String> getTriggers() {
 return OptionBuilder.withLongOpt("option2")
 .withAliases("o2")
 .withDescription("Option 2")
 .create();
 }
 };
 OptionImpl option3 = new OptionImpl(1, false) {
 @Override
 public Set<String> getTriggers() {
 return OptionBuilder.withLongOpt("option1")
 .withAliases("o11")
 .withDescription("Option 1.1")
 .create();
 }
 };

 assertEquals(option1, option1.findOption("--option1"));
 assertSame(option1, option1.findOption("--o1"));
 assertNull(option1.findOption("--option2"));
 assertNull(option1.findOption("--o11"));
 assertNull(option1.findOption("--unknown"));
 }

 @Test
 public void testIsRequired() {
 OptionImpl option1 = new OptionImpl(1, false);
 assertFalse(option1.isRequired());

 OptionImpl option2 = new OptionImpl(2, true);
 assertTrue(option2.isRequired());
 }

 @Test
 public void testCheckPrefixes() {
 OptionImpl option = new OptionImpl(1, false) {
 @Override
 public Set<String> getPrefixes() {
 return OptionBuilder.withLongOpt("option")
 .create()
 .getPrefixes();
 }
 };
 Set<String> prefixes = OptionBuilder.withLongOpt("opt")
 .create()
 .getPrefixes();
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidPrefix() {
 OptionImpl option = new OptionImpl(1, false) {
 @Override
 public Set<String> getPrefixes() {
 return OptionBuilder.withLongOpt("option")
 .create()
 .getPrefixes();
 }
 };
 Set<String> prefixes = OptionBuilder.withLongOpt("options")
 .create()
 .getPrefixes();
 option.checkPrefixes(prefixes);
 }
}