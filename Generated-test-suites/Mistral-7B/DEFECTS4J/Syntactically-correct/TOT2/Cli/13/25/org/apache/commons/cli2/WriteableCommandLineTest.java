import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.writable.WritableCommandLine;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WritableCommandLine commandLine = new WritableCommandLine(new CommandLine());
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        // Check if option was added
        assert(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WritableCommandLine commandLine = new WritableCommandLine(new CommandLine());
        Option option = Option.builder("option").build();
        commandLine.addValue(option, "value");
        List<Object> values = commandLine.getValuesForOption(option);
        // Check if value was added
        assert(values.contains("value"));
    }

    // Test other methods similarly
}