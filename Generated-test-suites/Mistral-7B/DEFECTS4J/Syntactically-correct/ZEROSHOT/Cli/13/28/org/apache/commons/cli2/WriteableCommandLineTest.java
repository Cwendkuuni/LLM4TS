import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.ParseContext;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 commandLine = new DefaultWriteableCommandLine();
 }

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(1, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 List<Object> defaultValues = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertEquals(1, commandLine.getOptions().size());
 assertTrue(option.getValues().isEmpty());
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertEquals(1, commandLine.getOptions().size());
 assertTrue(option.getValues().isEmpty());
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("value"));
 }

 @Test
 public void testParse() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.parse(new String[]{"-o", "value1", "--option", "value2"});
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test
 public void testParseWithDefaultValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 List<Object> defaultValues = Arrays.asList("value1", "value2");
 commandLine.setDefaultValues(option, defaultValues);
 commandLine.parse(new String[]{"--option"});
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test
 public void testParseWithEmptyValues() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.parse(new String[]{"--option"});
 assertEquals(1, commandLine.getOptions().size());
 assertTrue(option.getValues().isEmpty());
 }

 @Test
 public void testParseSwitches() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.parse(new String[]{"-o", "--option"});
 assertEquals(1, commandLine.getOptions().size());
 assertTrue(option.isSet());
 }

 @Test
 public void testParseMixed() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.parse(new String[]{"-o", "value1", "--option", "value2"});
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test
 public void testParseWithUnknownOption() {
 Option option = Option.builder("o").longOpt("option").build();
 commandLine.addOption(option);
 commandLine.parse(new String[]{"--unknown"});
 assertEquals(1, commandLine.getOptions().size());
 assertFalse(option.isSet());
 assertNull(option.getValues().get(0));
 }

 @Test
 public void testParseWithEmptyArgs() {
 commandLine.parse(new String[]{"--"});
 assertNotNull(commandLine.getParsedOptions());
 assertEquals(Collections.emptyMap(), commandLine.getParsedOptions());
 }
}