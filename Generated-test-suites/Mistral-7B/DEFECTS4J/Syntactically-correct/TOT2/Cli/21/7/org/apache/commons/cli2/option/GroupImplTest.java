import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private GroupImpl group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");
        Option option3 = new Option("o3", "option three");
        group = new GroupImpl(options, "group", null, 1, 3, false);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess_ValidOption() throws OptionException {
        group.canProcess(commandLine, "o1");
        assertTrue(commandLine.hasOption("o1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_InvalidOption() throws OptionException {
        group.canProcess(commandLine, "i");
    }

    @Test
    public void testCanProcess_BurstingOption() throws OptionException {
        commandLine.parse("--o1 --o1 --o2");
        group.canProcess(commandLine, "o1");
        assertTrue(commandLine.hasOption("o1"));
        assertTrue(commandLine.hasOption("o2"));
    }

    @Test
    public void testValidate_ValidOptions() throws OptionException {
        commandLine.parse("--o1 --o2");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions() throws OptionException {
        commandLine.parse("--o1 --o2 --o3");
        group.validate(commandLine);
    }

    @Test
    public void testProcess_ValidOptions() throws OptionException {
        commandLine.parse("--o1 --o2");
        group.process(commandLine, commandLine.iterator());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("option one", commandLine.getOptionValue("o1"));
        assertEquals("option two", commandLine.getOptionValue("o2"));
    }

    @Test
    public void testProcess_AnonymousArguments() throws OptionException {
        List<Option> anonymousOptions = new ArrayList<>();
        Option anonymousOption = new Option("a", "anonymous option");
        anonymousOption.setArgProperty(Option.DEFAULT_ARG_VALUE, "value");
        anonymousOptions.add(anonymousOption);
        group = new GroupImpl(Collections.emptyList(), null, null, 1, 1, false);
        group.setOptions(anonymousOptions);
        commandLine.parse("--a value");
        group.process(commandLine, commandLine.iterator());
        assertEquals("value", commandLine.getOptionValue("a"));
    }
}