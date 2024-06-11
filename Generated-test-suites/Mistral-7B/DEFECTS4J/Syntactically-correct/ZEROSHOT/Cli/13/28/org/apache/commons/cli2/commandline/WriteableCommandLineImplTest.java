import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Test
 public void testWriteableCommandLineImpl() {
 // Given
 commandLine = new WriteableCommandLineImpl(new Option.Builder("option").build(), Collections.emptyList());

 // When
 // Nothing to add

 // Then
 assertEquals("", commandLine.toString());
 assertEquals(Collections.emptyList(), commandLine.getOptions());
 assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
 assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
 assertEquals(Collections.emptyMap(), commandLine.getProperties());
 assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());

 // When
 // Add option
 option = new Option.Builder("option").build();
 commandLine.addOption(option);

 // Then
 assertEquals("option", commandLine.toString());
 assertEquals(Collections.singletonList(option), commandLine.getOptions());
 assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
 assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
 assertEquals(Collections.emptyMap(), commandLine.getProperties());
 assertEquals(Collections.singleton("option"), commandLine.getOptionTriggers());

 // When
 // Add value
 commandLine = new WriteableCommandLineImpl(new Option.Builder("option").build(), Collections.singletonList("value"));
 commandLine.addOption(option);
 commandLine.addValue(option, "value2");

 // Then
 assertEquals("option=value2", commandLine.toString());
 assertEquals(Collections.singletonList(option), commandLine.getOptions());
 assertEquals(Collections.singletonList(Arrays.asList("value2")), commandLine.getValues(option, null));
 assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
 assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
 assertEquals(Collections.emptyMap(), commandLine.getProperties());
 assertEquals(Collections.singleton("option"), commandLine.getOptionTriggers());

 // When
 // Add switch
 commandLine = new WriteableCommandLineImpl(new Option.Builder("option").buildOptional(), Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);

 // Then
 assertEquals("option", commandLine.toString());
 assertEquals(Collections.singletonList(option), commandLine.getOptions());
 assertEquals(Collections.emptyList(), commandLine.getValues(option, null));
 assertEquals(Boolean.TRUE, commandLine.getSwitch(option, Boolean.FALSE));
 assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
 assertEquals(Collections.singletonMap(option, Boolean.TRUE), commandLine.getDefaultSwitches());
 assertEquals(Collections.emptyMap(), commandLine.getProperties());
 assertEquals(Collections.singleton("option"), commandLine.getOptionTriggers());

 // When
 // Add property
 commandLine.addProperty("property", "value");

 // Then
 assertEquals("option", commandLine.toString());
 assertEquals(Collections.singletonList(option), commandLine.getOptions());
 assertEquals(Collections.emptyList(), commandLine.getValues(option, null));
 assertEquals(Boolean.TRUE, commandLine.getSwitch(option, Boolean.FALSE));
 assertEquals(Collections.singletonMap("property", "value"), commandLine.getProperties());
 assertEquals(Collections.singleton("option"), commandLine.getOptionTriggers());
 }
}