// 
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = getWriteableCommandLine();
        Option option = new Option("--option", true, "Default value");
        cmd.addOption(option);
        assert(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = getWriteableCommandLine();
        Option option = new Option("--option", true, "Default value");
        cmd.addOption(option);
        cmd.addValue(option, "Test Value");
        List<Object> values = cmd.getValuesForOption(option);
        assert(values.contains("Test Value"));
    }

    // Test other methods in a similar manner

    // 
    private WriteableCommandLine getWriteableCommandLine() {
        return new Cli2DefaultParser().parseRequiringDefaultValues(null, new String[0], WriteableCommandLine.class);
    }
}