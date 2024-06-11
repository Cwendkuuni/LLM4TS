import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WriteableCommandLineTest {
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("optionName").longOpt("long-option-name").build();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("optionName"));
    }
}