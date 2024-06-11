import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_basic() {
        Option option = new Option("o", "output", true, "Output directory");
        List arguments = Arrays.asList("test", "input.txt", "-o", "/tmp");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("/tmp", commandLine.getProperty("output"));
        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals("/tmp", commandLine.getOption("output").getValue().get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_edge_case_option_value() {
        Option option = new Option("o", "output", true, "Output directory");
        List arguments = Arrays.asList("test", "--output", "/tmp/test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("/tmp/test", commandLine.getProperty("output"));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_edge_case_switch_already_set() {
        Option option = new Option("o", "output", false, "Output directory");
        List arguments = Arrays.asList("test", "-o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_edge_case_default_value() {
        Option option = new Option("o", "output", true, "Output directory");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultValues(option, Arrays.asList("/default/output"));

        Assert.assertEquals("/default/output", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_edge_case_default_switch() {
        Option option = new Option("o", "output", false, "Output directory");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("o", "output", true, "Output directory");
        List arguments = Arrays.asList("test", "-o", "/tmp");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("test -o /tmp", commandLine.toString());
    }
}