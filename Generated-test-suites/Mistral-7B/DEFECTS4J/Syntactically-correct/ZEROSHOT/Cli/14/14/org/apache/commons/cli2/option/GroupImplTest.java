import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.GroupImpl;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setup() {
        options = new ArrayList<>();

        Option option1 = new OptionImpl("option1", true, "d", "desc1") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> arguments)
                    throws OptionException {
            }
        };
        Option option2 = new OptionImpl("option2", true, "e", "desc2") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> arguments)
                    throws OptionException {
            }
        };
        Option option3 = new OptionImpl("option3", true, "f", "desc3") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> arguments)
                    throws OptionException {
            }
        };

        Argument arg1 = new OptionImpl(null, false, null, "arg1") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> arguments)
                    throws OptionException {
            }
        };
        Argument arg2 = new OptionImpl(null, false, null, "arg2") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> arguments)
                    throws OptionException {
            }
        };

        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(arg1);
        options.add(arg2);

        group = new GroupImpl(options, null, "group", 1, 3);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("option1");
        commandLine.addOption("option2");
        commandLine.addOption("option3");
        commandLine.addArgument("arg1");
        commandLine.addArgument("arg2");

        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option3"));
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "arg2"));

        assertFalse(group.canProcess(commandLine, "invalid"));
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("option1");
        commandLine.addOption("option2");
        commandLine.addOption("option3");
        commandLine.addArgument("arg1");
        commandLine.addArgument("arg2");

        group.process(commandLine, commandLine.listIterator());

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasOption("option3"));
        assertTrue(commandLine.hasArgument("arg1"));
        assertTrue(commandLine.hasArgument("arg2"));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("option1");
        commandLine.addOption("option2");

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("missing option: option3", e.getMessage());
        }

        commandLine.addOption("option3");

        group.validate(commandLine);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(null, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);

        String usage = buffer.toString();
        assertEquals("group:\n" +
                "  option1 --desc1 [arg1]\n" +
                "  option2 --desc2 [arg2]\n" +
                "  option3 --desc3 [arg1]\n" +
                "  arg1\n" +
                "  arg2\n", usage);
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        int count = 0;

        for (HelpLine helpLine : helpLines) {
            if (helpLine.getOption().isRequired()) {
                assertEquals("option1", helpLine.getOption().getLongOpt());
                assertEquals("d", helpLine.getOption().getOpt());
                assertEquals("desc1", helpLine.getOption().getDescription());
                assertEquals(null, helpLine.getOption().getValueDescription());
                assertEquals(false, helpLine.hasArgument());
                assertEquals(1, helpLine.getIndex());
                assertEquals(1, helpLine.getLineNumber());
                assertEquals(null, helpLine.getShortDescription());
                count++;
            }

            if (helpLine.getOption() instanceof Argument) {
                assertEquals("arg1", helpLine.getOption().getLongOpt());
                assertEquals(null, helpLine.getOption().getOpt());
                assertEquals(null, helpLine.getOption().getDescription());
                assertEquals(null, helpLine.getOption().getValueDescription());
                assertEquals(true, helpLine.hasArgument());
                assertEquals(2, helpLine.getIndex());
                assertEquals(1, helpLine.getLineNumber());
                assertEquals("an optional argument", helpLine.getShortDescription());
                count++;
            }

            if (count < 3) {
                fail();
            }
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> groupOptions = group.getOptions();

        assertEquals(5, groupOptions.size());

        Option option1 = groupOptions.get(0);
        Option option2 = groupOptions.get(1);
        Option option3 = groupOptions.get(2);
        Argument arg1 = (Argument) groupOptions.get(3);
        Argument arg2 = (Argument) groupOptions.get(4);

        assertEquals("option1", option1.getLongOpt());
        assertEquals("d", option1.getOpt());
        assertNull(option1.getValueDescription());

        assertEquals("option2", option2.getLongOpt());
        assertEquals("e", option2.getOpt());
        assertNull(option2.getValueDescription());

        assertEquals("option3", option3.getLongOpt());
        assertEquals("f", option3.getOpt());
        assertNull(option3.getValueDescription());

        assertNull(arg1.getValueDescription());

        assertEquals("arg1", arg2.getValueDescription());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> groupAnonymous = group.getAnonymous();

        assertEquals(2, groupAnonymous.size());

        Argument arg1 = (Argument) groupAnonymous.get(0);
        Argument arg2 = (Argument) groupAnonymous.get(1);

        assertEquals("arg1", arg1.getValueDescription());
        assertEquals("arg2", arg2.getValueDescription());
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("option1");
        Option option3 = group.findOption("option3");

        assertEquals("option1", option1.getLongOpt());
        assertEquals("option3", option3.getLongOpt());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }
}