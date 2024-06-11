import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<Object> values = commandLine.getValuesForOption(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    // Test other methods similarly
}