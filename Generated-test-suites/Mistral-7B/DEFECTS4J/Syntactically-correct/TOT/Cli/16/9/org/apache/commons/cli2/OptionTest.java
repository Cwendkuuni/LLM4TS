import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        option = OptionBuilder.create("option")
                .description("Test option")
                .build();
        commandLine = new CommandLine();
    }

    @Test
    public void testProcess() {
        option.process(commandLine, commandLine.listIterator(0));
        assertEquals("Test option", commandLine.getOptionValue("option"));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValue("option"));
    }

    @Test
    public void testCanProcess_String() {
        assertTrue(option.canProcess(commandLine, "test"));
    }

    @Test
    public void testCanProcess_ListIterator() {
        List<String> args = Arrays.asList("test", "arg1", "arg2").subList(0, 2).listIterator();
        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args)); // consume next argument
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        assertNotNull(triggers);
        assertTrue(triggers.contains("option"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        assertNotNull(prefixes);
        assertTrue(prefixes.contains("-"));
        assertTrue(prefixes.contains("--"));
    }

    @Test
    public void testValidate() {
        commandLine.parse(Arrays.asList("--option", "--option", "arg"));
        option.validate(commandLine);
        // no exception thrown, test valid
    }

    @Test(expected = OptionException.class)
    public void testValidate_Invalid() {
        commandLine.parse(Arrays.asList("--option", "arg"));
        option.validate(commandLine);
        // OptionException expected
    }

    @Test
    public void testHelpLines() {
        List<Object> helpLines = option.helpLines(0, new LinkedHashSet<>(), null);
        assertFalse(helpLines.isEmpty());
        String helpLine = (String) helpLines.get(0);
        assertTrue(helpLine.contains("Test option"));
        assertTrue(helpLine.contains("--option"));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new LinkedHashSet<>(), null);
        String usage = buffer.toString();
        assertTrue(usage.contains("Test option"));
        assertTrue(usage.contains("--option"));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test option", option.getDescription());
    }

    @Test
    public void testGetId() {
        assertEquals(0, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option2 = OptionBuilder.create("option2")
                .description("Test option 2")
                .build();
        commandLine.parse(Arrays.asList("--option", "--option2"));
        Option found = option.findOption("option2");
        assertSame(option2, found);
    }

    @Test
    public void testIsRequired_true() {
        Option option = OptionBuilder.create("option")
                .required(true)
                .build();
        CommandLine cmdLine = new CommandLine();
        List<String> args = Arrays.asList("--option");
        cmdLine.parse(args.subList(0, 1).iterator());
        assertThrows(OptionException.class, () -> option.validate(cmdLine));
    }

    @Test
    public void testIsRequired_false() {
        Option option = OptionBuilder.create("option")
                .required(false)
                .build();
        CommandLine cmdLine = new CommandLine();
        List<String> args = Arrays.asList("--option");
        cmdLine.parse(args.subList(0, 1).iterator());
        try {
            option.validate(cmdLine);
        } catch (OptionException e) {
            fail("OptionException not expected");
        }
    }
}