import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testGroupImpl_ValidOption() throws OptionException {
        final List options = new ArrayList();
        final Option option1 = new Option("option1", "option1", true, "Description 1");
        options.add(option1);

        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption(option1.getLongOpt());

        group.process(commandLine, commandLine.iterator());

        assertTrue(group.canProcess(commandLine, option1.getLongOpt()));
    }

    @Test
    public void testGroupImpl_ValidArgument() throws OptionException {
        final List options = new ArrayList();
        final Option option1 = new Option("option1", null, false, "Description 1");
        options.add(option1);
        final Argument argument = new Argument("argument1", "Argument 1");
        options.add(argument);

        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addArgument(argument.getLongOpt());

        group.process(commandLine, commandLine.iterator());

        assertTrue(group.canProcess(commandLine, argument.getLongOpt()));
    }

    @Test
    public void testGroupImpl_ValidOptions() throws OptionException {
        final List options = new ArrayList();
        final Option option1 = new Option("option1", "option1", true, "Description 1");
        options.add(option1);
        final Option option2 = new Option("option2", "option2", false, "Description 2");
        options.add(option2);

        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption(option1.getLongOpt());
        commandLine.addOption(option2.getLongOpt());

        group.process(commandLine, commandLine.iterator());

        assertTrue(group.canProcess(commandLine, option1.getLongOpt()));
        assertTrue(group.canProcess(commandLine, option2.getLongOpt()));
    }

    @Test
    public void testGroupImpl_MinimumOptions() throws OptionException {
        final List options = new ArrayList();
        final Option option1 = new Option("option1", "option1", true, "Description 1");
        options.add(option1);

        final Group group = new GroupImpl(options, null, null, 2, 2, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption(option1.getLongOpt());
        commandLine.addOption(option1.getLongOpt());

        group.process(commandLine, commandLine.iterator());

        assertTrue(group.canProcess(commandLine, option1.getLongOpt()));
        assertTrue(group.canProcess(commandLine, option1.getLongOpt()));
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_MaximumOptions() throws OptionException {
        final List options = new ArrayList();
        final Option option1 = new Option("option1", "option1", true, "Description 1");
        options.add(option1);

        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption(option1.getLongOpt());
        commandLine.addOption(option1.getLongOpt());

        group.process(commandLine, commandLine.iterator());

        assertFalse(group.canProcess(commandLine, option1.getLongOpt()));
    }
}