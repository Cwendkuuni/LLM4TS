import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = mock(WriteableCommandLine.class);
 }

 @Test
 public void testAddOption() {
 Option option = mock(Option.class);
 commandLine.addOption(option);
 verify(commandLine).addOption(option);
 }

 @Test
 public void testAddValue() {
 Option option = mock(Option.class);
 Object value = new Object();
 commandLine.addValue(option, value);
 verify(option).setValue(value);
 }

 @Test
 public void testGetValues() {
 Option option = mock(Option.class);
 when(option.getValue()).thenReturn(new ArrayList<>());
 when(commandLine.getValues(option)).thenCallRealMethod();
 assertEquals(new ArrayList<>(), commandLine.getValues(option));

 Option optionWithValue = mock(Option.class);
 when(optionWithValue.getValue()).thenReturn(new ArrayList<>());
 when(optionWithValue.getValue(commandLine)).thenReturn("testValue");
 when(commandLine.getValues(optionWithValue)).thenCallRealMethod();
 assertEquals(new ArrayList<>(), commandLine.getValues(optionWithValue));
 assertEquals("testValue", commandLine.getValues(optionWithValue).get(0));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = mock(Option.class);
 List defaults = new ArrayList<>();
 defaults.add("defaultValue1");
 defaults.add("defaultValue2");
 commandLine.setDefaultValues(option, defaults);
 verify(option).setValue(defaults);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = mock(Option.class);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = mock(Option.class);
 commandLine.setDefaultSwitch(option, Boolean.TRUE);
 verify(option).setLongOpt("" + option.getLongOpt());
 verify(option).setValue(Boolean.TRUE);
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property1", "value1");
 assertEquals("value1", commandLine.getOptionValue("property1"));
 }

 @Test
 public void testLooksLikeOption() {
 when(commandLine.getOptions()).thenReturn(new Option[0]);
 assertFalse(commandLine.looksLikeOption("option1"));

 Option option = mock(Option.class);
 Option[] options = new Option[]{option};
 when(commandLine.getOptions()).thenReturn(options);
 assertTrue(commandLine.looksLikeOption("option1"));
 }
}