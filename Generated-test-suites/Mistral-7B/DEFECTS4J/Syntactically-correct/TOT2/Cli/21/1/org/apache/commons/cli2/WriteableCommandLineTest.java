import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine() {};
    }

    @Test
    public void testAddOption() {
        Option option = new Option("input", true, "i");
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }
}