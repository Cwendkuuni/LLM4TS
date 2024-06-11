import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ListOption;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private static Option createOption() {
 return new OptionImpl(1, "option1", false, "o") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return true;
 }

 @Override
 public String getPreferredName() {
 return "option1";
 }

 @Override
 public String getDescription() {
 return "This is an option";
 }

 @Override
 public String[] getPrefixes() {
 return new String[]{"-p", "--prefix"};
 }

 @Override
 public String[] getTriggers() {
 return new String[]{"o", "option"};
 }
 };
 }

 @Test
 public void testOptionImpl_constructor() {
 Option option = createOption();
 assertEquals(1, option.getId());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionImpl_canProcess() {
 Option option = createOption();
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getArguments();
CommandLine parsedCommandLine = new CommandLine();
 parsedCommandLine.parse(new String[]{"-p", "option", "arg"});
 arguments = parsedCommandLine.getArguments();

 assertTrue(option.canProcess(commandLine, arguments));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionImpl_canProcess_noPrefix() {
 Option option = createOption();
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getArguments();
CommandLine parsedCommandLine = new CommandLine();
 parsedCommandLine.parse(new String[]{"-n", "option", "arg"});
 arguments = parsedCommandLine.getArguments();

 option.checkPrefixes(parsedCommandLine.getOptions().keySet());
 }

 @Test
 public void testOptionImpl_toString() {
 Option option = createOption();
 assertEquals(option.toString(), "option1\n" +
 "This is an option\n" +
 "[-p, --prefix], [o, option]\n");
 }

 @Test
 public void testOptionImpl_equals() {
 Option option1 = createOption();
 Option option2 = createOption();
 Option option3 = createOption();
 Option option4 = new OptionImpl(1, "option1", false, "o") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return false;
 }
 };

 assertEquals(option1, option1);
 assertEquals(option1, option2);
 assertNotEquals(option1, option3);
 assertNotEquals(option1, option4);
 }

 @Test
 public void testOptionImpl_hashCode() {
 Option option1 = createOption();
 Option option2 = createOption();
 Option option3 = new OptionImpl(1, "option1", false, "o") {
 @Override
 public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
 return false;
 }
 };

 assertEquals(option1.hashCode(), option1.hashCode());
 assertEquals(option1.hashCode(), option2.hashCode());
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionImpl_findOption() {
 Option option = createOption();
 Option foundOption = option.findOption("o");
 assertEquals(option, foundOption);
 }

 @Test
 public void testOptionImpl_findOption_null() {
 Option option = createOption();
 Option foundOption = option.findOption(null);
 assertNull(foundOption);
 }

 @Test
 public void testOptionImpl_isRequired() {
 Option option = createOption();
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionImpl_defaults() {
 Option option = createOption();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 option.defaults(commandLine);
 assertEmpty(commandLine.getOptions());
 }
}