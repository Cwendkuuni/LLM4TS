import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class WriteableCommandLineTest {

 private WriteableCommandLine writeableCommandLine;
 private Option option;

 @Before
 public void setUp() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 option = mock(Option.class);
 }

 @After
 public void tearDown() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testAddOption() {
 writeableCommandLine.addOption(option);
 verify(writeableCommandLine).addOption(option);
 }

 @Test
 public void testAddValue() {
 writeableCommandLine.addValue(option, "value1");
 verify(option).setValue(new ArrayList<Object>() {{ add("value1"); }});
 }

 @Test
 public void testGetValues() {
 when(option.getValue()).thenReturn(new ArrayList<Object>() {{ add("value1"); add("value2"); }});
 List<Object> values = writeableCommandLine.getValuesForOption(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 List<Object> defaultValues = new ArrayList<Object>() {{ add("default1"); add("default2"); }};
 writeableCommandLine.setDefaultValues(option, defaultValues);
 verify(option).setDefaultValues(defaultValues);
 }

 @Test
 public void testAddSwitch() {
 writeableCommandLine.addSwitch(option, true);
 verify(option).setValued(true);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 writeableCommandLine.addSwitch(option, true);
 try {
 writeableCommandLine.addSwitch(option, true);
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSetDefaultSwitch() {
 writeableCommandLine.setDefaultSwitch(option, true);
 verify(option).setDefaultValue(true);
 }

 @Test
 public void testAddProperty() {
 writeableCommandLine.addProperty("property1", "value1");
 verify(writeableCommandLine).addProperty("property1", "value1");
 }

 @Test
 public void testLooksLikeOption() {
 writeableCommandLine.looksLikeOption("--option");
 verify(writeableCommandLine).looksLikeOption("--option");
 }

 @Test
 public void testLooksLikeOptionWithNoDash() {
 writeableCommandLine.looksLikeOption("option");
 assertFalse(writeableCommandLine.looksLikeOption("option"));
 }
}