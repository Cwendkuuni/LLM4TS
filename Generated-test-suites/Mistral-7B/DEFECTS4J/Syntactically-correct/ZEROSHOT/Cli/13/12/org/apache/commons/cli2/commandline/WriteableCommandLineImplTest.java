import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl_ShouldCreateInstanceWithEmptyValues() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
 assertEquals(Collections.emptySet(), writeableCommandLine.getOptionTriggers());
 assertNull(writeableCommandLine.getProperty("property"));
 assertFalse(writeableCommandLine.looksLikeOption("option"));
 assertEquals(Collections.emptyList(), writeableCommandLine.getValues(null, null));
 assertNull(writeableCommandLine.getSwitch(null, null));
 assertEquals(Collections.emptyMap(), writeableCommandLine.getDefaultValues());
 assertEquals(Collections.emptyMap(), writeableCommandLine.getDefaultSwitches());
 assertEquals(Collections.emptyList(), writeableCommandLine.getNormalised());
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldAddOption() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 Option option = new Option("option", "option description");
 writeableCommandLine.addOption(option);

 assertEquals(1, writeableCommandLine.getOptions().size());
 assertEquals(option, writeableCommandLine.getOption(Option.builder("option").build().getPreferredName()));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldAddValue() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 Option option = new Option("option", "option description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");

 assertEquals(1, writeableCommandLine.getOptions().size());
 assertEquals(option, writeableCommandLine.getOption("option"));
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldAddSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 Option option = new Option("option", "option description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);

 assertEquals(1, writeableCommandLine.getOptions().size());
 assertEquals(option, writeableCommandLine.getOption("option"));
 assertTrue(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldGetProperty() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 writeableCommandLine.addProperty("property", "value");

 assertEquals("value", writeableCommandLine.getProperty("property"));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldGetProperties() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 writeableCommandLine.addProperty("property1", "value1");
 writeableCommandLine.addProperty("property2", "value2");

 Set properties = writeableCommandLine.getProperties();
 assertEquals(2, properties.size());
 assertTrue(properties.contains("property1"));
 assertTrue(properties.contains("property2"));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldToString() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.singletonList("arg1"));
 assertEquals("arg1", writeableCommandLine.toString());
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldGetOptions() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.singletonList("arg1"));
 Option option1 = new Option("option1", "option1 description");
 Option option2 = new Option("option2", "option2 description");
 writeableCommandLine.addOption(option1);
 writeableCommandLine.addOption(option2);

 assertEquals(2, writeableCommandLine.getOptions().size());
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldGetOptionTriggers() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.singletonList("arg1"));
 Option option1 = new Option("option1", "option1 description");
 Option option2 = new Option("option2", "option2 description");
 writeableCommandLine.addOption(option1);
 writeableCommandLine.addOption(option2);

 assertEquals(2, writeableCommandLine.getOptionTriggers().size());
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldGetValues() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.singletonList("arg1"));
 Option option = new Option("option", "option description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addValue(option, "value1");
 writeableCommandLine.addValue(option, "value2");

 assertEquals(1, writeableCommandLine.getOptions().size());
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldGetSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.singletonList("arg1"));
 Option option = new Option("option", "option description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.addSwitch(option, true);
 writeableCommandLine.addSwitch(option, false);

 assertEquals(1, writeableCommandLine.getOptions().size());
 assertTrue(writeableCommandLine.getSwitch(option, true));
 assertFalse(writeableCommandLine.getSwitch(option, false));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldSetDefaultValues() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.singletonList("arg1"));
 Option option = new Option("option", "option description");
 List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 writeableCommandLine.addOption(option);
 writeableCommandLine.setDefaultValues(option, defaultValues);

 assertEquals(1, writeableCommandLine.getOptions().size());
 List values = writeableCommandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("defaultValue1", values.get(0));
 assertEquals("defaultValue2", values.get(1));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldSetDefaultSwitch() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.singletonList("arg1"));
 Option option = new Option("option", "option description");
 writeableCommandLine.addOption(option);
 writeableCommandLine.setDefaultSwitch(option, true);

 assertEquals(1, writeableCommandLine.getOptions().size());
 assertTrue(writeableCommandLine.getSwitch(option, true));
 }

 @Test
 public void testWriteableCommandLineImpl_ShouldGetNormalised() {
 WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));

 assertEquals(2, writeableCommandLine.getNormalised().size());
 assertEquals("arg1", writeableCommandLine.getNormalised().get(0));
 assertEquals("arg2", writeableCommandLine.getNormalised().get(1));
 }
}