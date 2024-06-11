import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    @Test
    public void testProcess() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        List<String> args = List.of("--testOption", "testValue");
        ListIterator<String> iterator = args.listIterator();

        option.process(commandLine, iterator);
        assertEquals("testValue", commandLine.getOptionValue("testOption"));

        iterator.next(); // move to next argument
        assertFalse(option.canProcess(commandLine, iterator));
    }

    @Test
    public void testDefaults() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValue("testOption"));
    }

    @Test
    public void testCanProcess() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        List<String> args = List.of("--testOption", "testValue");
        ListIterator<String> iterator = args.listIterator();

        assertTrue(option.canProcess(commandLine, iterator));

        iterator.next(); // move to next argument
        assertFalse(option.canProcess(commandLine, iterator));
    }

    @Test
    public void testGetTriggers() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        assertNull(option.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        Set<String> prefixes = option.getPrefixes();
        assertNotNull(prefixes);
        assertEquals(1, prefixes.size());
        assertEquals("--", prefixes.iterator().next());
    }

    @Test
    public void testValidate() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        List<String> args = List.of("--testOption", "testValue");
        ListIterator<String> iterator = args.listIterator();

        option.process(commandLine, iterator);

        assertThrows(OptionException.class, () -> option.validate(commandLine));
    }

    @Test
    public void testHelpLines() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        HelpFormatter formatter = new HelpFormatter();
        String helpText = option.helpLines(1, null, null).stream()
                .map(HelpLine::toString)
                .collect(Collectors.joining("\n"));

        System.out.println(helpText);
    }

    @Test
    public void testAppendUsage() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);

        System.out.println(buffer);
    }

    @Test
    public void testGetPreferredName() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        assertEquals("testOption", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        assertEquals("A test option", option.getDescription());
    }

    @Test
    public void testgetId() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(false)
                .create();

        assertEquals(0, option.getId());
    }

    @Test
    public void testFindOption() {
        Option parentOption = OptionBuilder.withLongOpt("parentOption")
                .withDescription("A parent option")
                .isRequired(false)
                .create();

        Option childOption = OptionBuilder.withLongOpt("childOption")
                .withDescription("A child option")
                .isRequired(false)
                .create();
        parentOption.setParent(childOption);

        Option foundOption = parentOption.findOption("childOption");
        assertNotNull(foundOption);
        assertEquals(childOption, foundOption);

        Option notFoundOption = parentOption.findOption("nonExistentOption");
        assertNull(notFoundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("A test option")
                .isRequired(true)
                .create();

        assertTrue(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = OptionBuilder.withLongOpt("parentOption")
                .withDescription("A parent option")
                .isRequired(false)
                .create();

        Option childOption = OptionBuilder.withLongOpt("childOption")
                .withDescription("A child option")
                .isRequired(false)
                .create();
        parentOption.setParent(childOption);

        assertEquals(childOption, parentOption.getParent());
    }

    @Test
    public void testSetParent() {
        Option childOption = OptionBuilder.withLongOpt("childOption")
                .withDescription("A child option")
                .isRequired(false)
                .create();

        Option parentOption = OptionBuilder.withLongOpt("parentOption")
                .withDescription("A parent option")
                .isRequired(false)
                .create();
        childOption.setParent(parentOption);

        assertEquals(parentOption, childOption.getParent());
    }
}