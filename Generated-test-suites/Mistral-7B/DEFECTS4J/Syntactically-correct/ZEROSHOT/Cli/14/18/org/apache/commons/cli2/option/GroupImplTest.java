import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // given
        final List options = new ArrayList();
        final Option option1 = new Option("option1", "option1", true, "Option 1");
        final Option option2 = new Option("option2", "option2", false, "Option 2");
        final Argument argument1 = new Argument("arg1", "arg1", true, "Argument 1");
        final Argument argument2 = new Argument("arg2", "arg2", true, "Argument 2");
        options.add(option1);
        options.add(option2);
        options.add(argument1);
        options.add(argument2);

        final Group group = new GroupImpl(options, "Group", "Group", 1, 2);
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // when processing a valid command line
        group.process(commandLine, commandLine.getArguments().listIterator());

        // then the command line should have the expected options and arguments
        assertEquals("Option 1", commandLine.getOptionValues("option1").get(0));
        assertEquals("Argument 1", commandLine.getArgumentValues().get(0));
        assertNull(commandLine.getArgumentValues().get(1));
        assertEquals("Option 2", commandLine.getOptionValues("option2").get(0));
        assertNull(commandLine.getOptionValues("option3")); // not present
        assertNull(commandLine.getArgumentValues().get(2)); // not present

        // when validating a command line
        group.validate(commandLine);

        // then no exception should be thrown

        // when testing getters
        assertEquals("Group", group.getPreferredName());
        assertSame(group, group.findOption("Group"));
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertTrue(group.isRequired());
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, "option1"));
        assertFalse(group.canProcess(commandLine, "option2"));
        assertFalse(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "arg2"));
        assertSame(group.getPrefixes(), group.getTriggers());
        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getAnonymous().size());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableList(options), group.getAnonymous());
    }

    @Test
    public void testGroupImplInvalid() {
        // given
        final List options = new ArrayList();
        final Option option1 = new Option("option1", "option1", true, "Option 1");
        final Option option2 = new Option("option2", "option2", false, "Option 2");
        final Argument argument1 = new Argument("arg1", "arg1", true, "Argument 1");
        final Argument argument2 = new Argument("arg2", "arg2", true, "Argument 2");
        options.add(option1);
        options.add(option2);
        options.add(argument1);
        options.add(argument2);

        final Group group = new GroupImpl(options, "Group", "Group", 1, 3);
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // when processing a command line
        try {
            group.process(commandLine, commandLine.getArguments().listIterator());
            assertFalse("OptionException should have been thrown", true);
        } catch (OptionException e) {
            // expected
        }

        // when validating a command line
        try {
            group.validate(commandLine);
            assertFalse("OptionException should have been thrown", true);
        } catch (OptionException e) {
            // expected
        }

        // when testing getters
        assertEquals("Group", group.getPreferredName());
        assertSame(group, group.findOption("Group"));
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertTrue(group.isRequired());
        assertFalse(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, "option1"));
        assertFalse(group.canProcess(commandLine, "option2"));
        assertFalse(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "arg2"));
        assertSame(group.getPrefixes(), group.getTriggers());
        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getAnonymous().size());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableList(options), group.getAnonymous());
    }
}