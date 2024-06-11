import org.apache.commons.cli2.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class OptionTest {

 private WriteableCommandLine commandLine;
 private ListIterator<String> arguments;

 @Before
 public void setup() {
 commandLine = mock(WriteableCommandLine.class);
 arguments = mock(ListIterator.class);
 }

 @Test
 public void testProcess_validOption_processesArgument() throws OptionException {
 Option option = mock(Option.class);
 when(option.getTriggers()).thenReturn(SingletonSet.valueOf("--option"));

 // Given
 option.process(commandLine, arguments);

 // Then
 verify(commandLine).setValue(eq("value"), any());
 }

 @Test(expected = OptionException.class)
 public void testProcess_invalidOption_throwsException() throws OptionException {
 Option option = mock(Option.class);
 when(option.getTriggers()).thenReturn(SingletonSet.valueOf("--invalid"));

 // Given
 arguments.next(); // Move to the next argument

 // When
 option.process(commandLine, arguments);

 // Then
 verify(commandLine, never()).setValue(anyString(), any());
 }

 @Test
 public void testProcess_invalidArgument_ignoresIt() throws OptionException {
 Option option = mock(Option.class);
 when(option.getTriggers()).thenReturn(SingletonSet.valueOf("--option"));

 // Given
 arguments.next(); // Move to the next argument
 arguments.next(); // Move to the next argument

 // When
 option.process(commandLine, arguments);

 // Then
 verify(commandLine, never()).setValue(anyString(), any());
 }

 @Test
 public void testCanProcess_validArgument() {
 Option option = mock(Option.class);
 when(option.canProcess(commandLine, eq("--option"))).thenReturn(true);

 // Given

 // When
 boolean result = option.canProcess(commandLine, arguments);

 // Then
 assertTrue(result);
 }

 @Test
 public void testCanProcess_invalidArgument() {
 Option option = mock(Option.class);
 when(option.canProcess(commandLine, eq("--invalid"))).thenReturn(false);

 // Given

 // When
 boolean result = option.canProcess(commandLine, arguments);

 // Then
 assertFalse(result);
 }

 @Test
 public void testValidate_validOption() throws OptionException {
 Option option = mock(Option.class);

 // Given
 option.validate(commandLine);

 // Then
 verifyNoMoreInteractions(commandLine);
 }

 @Test(expected = OptionException.class)
 public void testValidate_invalidOption() throws OptionException {
 Option option = mock(Option.class);
 when(option.validate(commandLine)).thenThrow(new OptionException());

 // Given

 // When
 option.validate(commandLine);

 // Then
 verify(commandLine).addException(any());
 }

 @Test
 public void testHelpLines_validOption() {
 Option option = mock(Option.class);
 when(option.getTriggers()).thenReturn(SingletonSet.valueOf("--option"));
 when(option.getPrefixes()).thenReturn(SingletonSet.valueOf("--"));

 // Given
 List<HelpLine> helpLines = option.helpLines(0, null, null);

 // Then
 assertEquals("Expected number of HelpLines", 1, helpLines.size());
 }

 @Test
 public void testHelpLines_invalidOption() {
 Option option = mock(Option.class);
 when(option.getTriggers()).thenReturn(SingletonSet.valueOf("--invalid"));
 when(option.getPrefixes()).thenReturn(SingletonSet.valueOf("--"));

 // Given
 List<HelpLine> helpLines = option.helpLines(0, null, null);

 // Then
 assertEquals("Expected number of HelpLines", 0, helpLines.size());
 }

 @Test
 public void testAppendUsage_validOption() {
 Option option = mock(Option.class);
 when(option.getTriggers()).thenReturn(SingletonSet.valueOf("--option"));
 when(option.getDescription()).thenReturn("Description");

 // Given
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, null, null);

 // Then
 assertEquals("Expected usage string for valid option",
 "  --option\n" +
 "Description\n",
 buffer.toString());
 }

 @Test
 public void testAppendUsage_invalidOption() {
 Option option = mock(Option.class);
 when(option.getTriggers()).thenReturn(SingletonSet.valueOf("--invalid"));
 when(option.getDescription()).thenReturn("Description");

 // Given
 StringBuffer buffer = new StringBuffer();
 option.appendUsage(buffer, null, null);

 // Then
 assertTrue("Expected usage string for invalid option to be empty",
 buffer.toString().isEmpty());
 }
}