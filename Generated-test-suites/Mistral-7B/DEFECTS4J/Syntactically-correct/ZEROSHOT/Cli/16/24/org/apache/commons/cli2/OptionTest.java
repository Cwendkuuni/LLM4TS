import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.BasicOption;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    /**
     * Test for the process method.
     */
    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--option1", "value1", "--option2", "value2").listIterator();

        Option option1 = new BasicOption("option1", true, "o1")
                .setValueSeparator('=')
                .setArgName("arg1")
                .setRequired(false);

        Option option2 = new BasicOption("option2", false, "o2")
                .setValueSeparator('=')
                .setArgName("arg2")
                .setRequired(false);

        option1.process(commandLine, args);
        assertEquals("value1", commandLine.getOptionValues("option1"));

        option2.process(commandLine, args);
        assertNull(commandLine.getOptionValues("option2"));

        args.next(); // consume next argument

        option2.process(commandLine, args);
        assertEquals("value2", commandLine.getOptionValues("option2")[0]);

        // test invalid option
        assertThrows(OptionException.class, () -> option1.process(commandLine, args));
    }

    /**
     * Test for the defaults method.
     */
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new BasicCommandLine();
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        option.defaults(commandLine);
        assertNull(commandLine.getOptionValues("option"));

        option.setValueDefaults(new String[]{"defaultValue"});
        assertEquals(Arrays.asList("defaultValue"), commandLine.getOptionValues("option"));
    }

    /**
     * Test for the canProcess method.
     */
    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--option1", "value1", "--option2", "value2").listIterator();

        Option option1 = new BasicOption("option1", true, "o1")
                .setValueSeparator('=')
                .setArgName("arg1")
                .setRequired(false);

        Option option2 = new BasicOption("option2", false, "o2")
                .setValueSeparator('=')
                .setArgName("arg2")
                .setRequired(false);

        Set<String> triggers1 = new HashSet<>(Arrays.asList("--option1"));
        Set<String> triggers2 = new HashSet<>(Arrays.asList("--option2"));

        assertTrue(option1.canProcess(commandLine, args));
        assertEquals(triggers1, option1.getTriggers());

        assertFalse(option1.canProcess(commandLine, args));

        args.next(); // consume next argument

        assertTrue(option2.canProcess(commandLine, args));
        assertEquals(triggers2, option2.getTriggers());

        assertFalse(option1.canProcess(commandLine, args));
    }

    /**
     * Test for the getTriggers method.
     */
    @Test
    public void testGetTriggers() {
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        Set<String> expected = new HashSet<>(Arrays.asList("--option"));
        Set<String> result = option.getTriggers();

        assertEquals(expected, result);
    }

    /**
     * Test for the getPrefixes method.
     */
    @Test
    public void testGetPrefixes() {
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        Set<String> expected = new HashSet<>(Arrays.asList("-", "--"));
        Set<String> result = option.getPrefixes();

        assertEquals(expected, result);
    }

    /**
     * Test for the validate method.
     */
    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new BasicCommandLine();

        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        assertNull(option.validate(commandLine));

        commandLine.putBooleanOption("noOption", false);
        assertThrows(OptionException.class, () -> option.validate(commandLine));
    }

    /**
     * Test for the helpLines method.
     */
    @Test
    public void testHelpLines() {
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        List<Option> options = new ArrayList<>();
        options.add(option);

        int depth = 1;
        Comparator<Option> comparator = (o1, o2) -> o1.getId() - o2.getId();

        List<HelpLine> helpLines = option.helpLines(depth, new HashSet<>(), comparator);

        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);

        assertEquals("--option ARG\n", helpLine.getShortOptionString());
        assertEquals("option\n", helpLine.getLongOptionString());
        assertEquals("Option description\n", helpLine.getDescription());
    }

    /**
     * Test for the appendUsage method.
     */
    @Test
    public void testAppendUsage() {
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comparator = (o1, o2) -> o1.getId() - o2.getId();

        option.appendUsage(buffer, helpSettings, comparator);

        assertEquals("--option <arg>\n", buffer.toString());
    }

    /**
     * Test for the getPreferredName method.
     */
    @Test
    public void testGetPreferredName() {
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        assertEquals("option", option.getPreferredName());
    }

    /**
     * Test for the getDescription method.
     */
    @Test
    public void testGetDescription() {
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        assertEquals("Option description", option.getDescription());
    }

    /**
     * Test for the getId method.
     */
    @Test
    public void testGetId() {
        Option option = new BasicOption("option", false, "o")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        assertEquals(1, option.getId());
    }

    /**
     * Test for the findOption method.
     */
    @Test
    public void testFindOption() {
        Option option1 = new BasicOption("option1", false, "o1")
                .setValueSeparator('=')
                .setArgName("arg1")
                .setRequired(false);

        Option option2 = new BasicOption("option2", false, "o2")
                .setValueSeparator('=')
                .setArgName("arg2")
                .setRequired(false);

        Option parentOption = new BasicOption("parentOption", false, "p")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false)
                .addOption(option1)
                .addOption(option2);

        assertNull(parentOption.findOption("unknownOption"));
        assertEquals(option1, parentOption.findOption("option1"));
        assertEquals(option2, parentOption.findOption("option2"));
    }

    /**
     * Test for the isRequired method.
     */
    @Test
    public void testIsRequired() {
        Option option1 = new BasicOption("option1", false, "o1")
                .setValueSeparator('=')
                .setArgName("arg1")
                .setRequired(false);

        Option option2 = new BasicOption("option2", false, "o2")
                .setValueSeparator('=')
                .setArgName("arg2")
                .setRequired(true);

        assertFalse(option1.isRequired());
        assertTrue(option2.isRequired());
    }

    /**
     * Test for the getParent method.
     */
    @Test
    public void testGetParent() {
        Option option1 = new BasicOption("option1", false, "o1")
                .setValueSeparator('=')
                .setArgName("arg1")
                .setRequired(false);

        Option option2 = new BasicOption("option2", false, "o2")
                .setValueSeparator('=')
                .setArgName("arg2")
                .setRequired(false);

        Option parentOption = new BasicOption("parentOption", false, "p")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false)
                .addOption(option1)
                .addOption(option2);

        assertNull(option1.getParent());
        assertEquals(parentOption, option2.getParent());
    }

    /**
     * Test for the setParent method.
     */
    @Test
    public void testSetParent() {
        Option option1 = new BasicOption("option1", false, "o1")
                .setValueSeparator('=')
                .setArgName("arg1")
                .setRequired(false);

        Option option2 = new BasicOption("option2", false, "o2")
                .setValueSeparator('=')
                .setArgName("arg2")
                .setRequired(false);

        Option parentOption = new BasicOption("parentOption", false, "p")
                .setValueSeparator('=')
                .setArgName("arg")
                .setRequired(false);

        option1.setParent(parentOption);
        option2.setParent(parentOption);

        assertEquals(parentOption, option1.getParent());
        assertEquals(parentOption, option2.getParent());
    }
}