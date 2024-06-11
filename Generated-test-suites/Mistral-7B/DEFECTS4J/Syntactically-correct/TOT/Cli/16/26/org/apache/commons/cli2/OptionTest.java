import org.apache.commons.cli2.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = Mockito.mock(WriteableCommandLine.class);
 option = Mockito.spy(new MyOption());
 }

 @Test
 public void testProcess() {
 List args = Mockito.mock(List.class);
 Mockito.when(args.hasNext()).thenReturn(true).thenReturn(false);
 Mockito.when(args.next()).thenReturn("--myOption").thenReturn("value");

 option.process(commandLine, args.listIterator());

 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(commandLine).setValue(argumentCaptor.capture(), Mockito.eq("value"));
 assertEquals("value", commandLine.getValue("myOption"));
 }

 @Test(expected = OptionException.class)
 public void testProcessWithInvalidArgument() {
 List args = Mockito.mock(List.class);
 Mockito.when(args.hasNext()).thenReturn(true).thenReturn(false);
 Mockito.when(args.next()).thenReturn("invalidArgument");

 option.process(commandLine, args.listIterator());
 }

 @Test
 public void testCanProcess() {
 assertEquals(true, option.canProcess(commandLine, "value"));
 assertEquals(true, option.canProcess(commandLine, "--myOption"));
 assertEquals(false, option.canProcess(commandLine, "invalidArgument"));
 }

 @Test
 public void testGetTriggers() {
 Set triggers = option.getTriggers();
 assertEquals(getClass(), triggers.getClass());
 assertEquals(1, triggers.size());
 assertEquals("--myOption", triggers.iterator().next());
 }

 @Test
 public void testGetPrefixes() {
 Set prefixes = option.getPrefixes();
 assertEquals(getClass(), prefixes.getClass());
 assertEquals(1, prefixes.size());
 assertEquals("--", prefixes.iterator().next());
 }

 @Test
 public void testValidate() {
 Set helpSettings = Mockito.mock(Set.class);
 try {
 option.validate(commandLine);
 } catch (OptionException e) {
 // expected
 }
 Mockito.verify(commandLine).validate();
 }

 @Test(expected = OptionException.class)
 public void testValidateWithInvalidCommandLine() {
 Set helpSettings = Mockito.mock(Set.class);
 Mockito.when(commandLine.getValues("myOption")).thenReturn(new String[]{"invalidValue"});
 option.validate(commandLine);
 }

 @Test
 public void testHelpLines() {
 Set helpSettings = Mockito.mock(Set.class);
 List helpLines = option.helpLines(1, helpSettings, null);
 assertEquals(1, helpLines.size());
 assertEquals("Help for option myOption\n", helpLines.get(0).getShortDescription());
 }

 @Test
 public void testAppendUsage() {
 StringBuffer buffer = new StringBuffer();
 Set helpSettings = Mockito.mock(Set.class);
 option.appendUsage(buffer, helpSettings, null);
 assertEquals("myOption\n  Optional: true\n  Syntax: --myOption [value]\n", buffer.toString());
 }

 @Test
 public void testGetPreferredName() {
 assertEquals("myOption", option.getPreferredName());
 }

 @Test
 public void testGetDescription() {
 assertEquals("This is a test option\n", option.getDescription());
 }

 @Test
 public void testGetId() {
 assertEquals(1, option.getId());
 }

 @Test
 public void testFindOption() {
 Option anotherOption = Mockito.spy(new AnotherOption());
 commandLine.setOptions(new Options().addOptions(option).addOptions(anotherOption));
 Option foundOption = option.findOption("--myOption");
 assertEquals(option, foundOption);
 }

 @Test
 public void testIsRequired() {
 assertEquals(false, option.isRequired());
 }

 @Test
 public void testHasParent() {
 Option parentOption = Mockito.spy(new ParentOption());
 option.setParent(parentOption);
 assertTrue(option.hasParent());
 assertEquals(parentOption, option.getParent());
 }

}

class MyOption extends AbstractOption {
 // Empty implementation
}

class AnotherOption extends AbstractOption {
 // Empty implementation
}

class ParentOption extends AbstractOption {
 // Empty implementation
}