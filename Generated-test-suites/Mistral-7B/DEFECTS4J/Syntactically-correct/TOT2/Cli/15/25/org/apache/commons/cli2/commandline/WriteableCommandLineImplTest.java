import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testGetOption() {
        Option option = new Option("option", true, "o");
        Option rootOption = new Option("rootOption", true, "r");
        List arguments = new ArrayList();
        arguments.add("r");
        arguments.add("o");
        arguments.add("option");
        arguments.add("value");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addOption(option);

        Option result = commandLine.getOption("option");

        Assert.assertEquals(option, result);
    }

    // Edge cases and exception handling scenarios
    // ...
}