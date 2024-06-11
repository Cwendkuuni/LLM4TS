import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option optionA = new Option("a", "option-a", false, "Option A");
        Option optionB = new Option("b", "option-b", true, "Option B");
        Option optionC = new Argument(0, "argument-c");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(optionA, Collections.singletonList(optionC));

        // When
        commandLine.addOption(optionB);
        commandLine.addValue(optionA, "value-a");
        commandLine.addValue(optionA, "value-b");
        commandLine.addSwitch(optionB, true);

        // Then
        assertTrue(commandLine.hasOption(optionA));
        assertTrue(commandLine.hasOption(optionB));

        assertEquals(2, commandLine.getValues(optionA, null).size());
        assertEquals("value-a", commandLine.getValues(optionA, null).get(0));
        assertEquals("value-b", commandLine.getValues(optionA, null).get(1));

        assertTrue(commandLine.getSwitch(optionB, false));

        // Properties test
        commandLine.addProperty(optionA, "property-a", "value-a");
        commandLine.addProperty(optionB, "property-b", "value-b");

        assertEquals("value-a", commandLine.getProperty(optionA, "property-a", null));
        assertEquals("value-b", commandLine.getProperty(optionB, "property-b", null));

        // Normalised test
        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("argument-c", normalised.get(0));
        assertEquals("-a", normalised.get(1));
        assertEquals("value-a");
        assertEquals("value-b", normalised.get(2));
        assertEquals("-b", normalised.get(3));

        // Options test
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(optionA, options.get(0));
        assertEquals(optionB, options.get(1));

        // Option triggers test
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("a"));
        assertTrue(optionTriggers.contains("b"));
    }
}