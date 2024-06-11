import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {
    private Options options;
    private CommandLine commandLine;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List<Option> optionList = new ArrayList<>();
        optionList.add(new Option("a", "option A", true, "This is option A"));
        optionList.add(new Option("b", "option B"));
        optionList.add(new Option("c", "option C", false, "This is option C"));
        optionList.add(new Option("d", "option D", true, "This is option D"));

        this.options = new Options();
        this.options.addGroup(new Group("My Group")
            .addOptions(optionList)
            .required(false));

        this.commandLine = new CommandLine("");
        this.formatter = new HelpFormatter();
    }

    @Test
    public void testValidUsage() {
        this.commandLine.parse(this.options.parse(-1, new String[]{"-a", "value-a", "-b", "value-b", "-c"}));
        assertTrue("Valid usage of group", this.commandLine.isComplete());
    }

    @Test(expected = OptionException.class)
    public void testInvalidUsage() {
        this.commandLine.parse(this.options.parse(-1, new String[]{"-a", "value-a", "-a"}));
        fail("Invalid usage of group should throw OptionException");
    }

    @Test
    public void testHelp() {
        this.formatter.printHelp("test", this.options, true, true);
    }
}