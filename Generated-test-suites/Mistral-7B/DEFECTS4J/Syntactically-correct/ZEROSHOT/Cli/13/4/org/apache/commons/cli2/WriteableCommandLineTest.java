import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@PowerMockIgnore({"org.apache.commons.cli.ParseException"})
@RunWith(PowerMockRunner.class)
public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;
 private ParseContext parseContext;
 private Option option;

 @Before
 public void setUp() {
 commandLine = PowerMockito.mock(WriteableCommandLine.class);
 parseContext = new ParseContext();
 option = new Option("o", "option", false, "this is an option");
 }

 @Test
 public void testAddOption() {
 PowerMockito.doNothing().when(commandLine).addOption(option);
 commandLine.addOption(option);
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test
 public void testAddValue() {
 PowerMockito.doNothing().when(commandLine).addValue(option, "value");
 commandLine.addValue(option, "value");
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test
 public void testGetValuesForOption() {
 PowerMockito.when(commandLine.getValuesForOption(option)).thenReturn(Arrays.asList("value1", "value2"));
 List<String> values = commandLine.getValuesForOption(option);
 assertEquals(Arrays.asList("value1", "value2"), values);
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test
 public void testSetDefaultValues() {
 List<String> defaultValues = Arrays.asList("value1", "value2");
 PowerMockito.doNothing().when(commandLine).setDefaultValues(option, defaultValues);
 commandLine.setDefaultValues(option, defaultValues);
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchInvalidState() {
 PowerMockito.doThrow(new IllegalStateException()).when(commandLine).addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testAddSwitch() {
 PowerMockito.doNothing().when(commandLine).addSwitch(option, true);
 commandLine.addSwitch(option, true);
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test
 public void testSetDefaultSwitch() {
 PowerMockito.doNothing().when(commandLine).setDefaultSwitch(option, true);
 commandLine.setDefaultSwitch(option, true);
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test
 public void testAddProperty() {
 PowerMockito.doNothing().when(commandLine).addProperty("property", "value");
 commandLine.addProperty("property", "value");
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test
 public void testLooksLikeOption() {
 PowerMockito.when(commandLine.looksLikeOption("option")).thenReturn(true);
 assertTrue(commandLine.looksLikeOption("option"));
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }

 @Test
 public void testLooksLikeOptionFalse() {
 PowerMockito.when(commandLine.looksLikeOption("argument")).thenReturn(false);
 assertFalse(commandLine.looksLikeOption("argument"));
 PowerMockito.verifyStatic().parse(parseContext);
 PowerMockito.verifyZeroInteractions(commandLine);
 }
}