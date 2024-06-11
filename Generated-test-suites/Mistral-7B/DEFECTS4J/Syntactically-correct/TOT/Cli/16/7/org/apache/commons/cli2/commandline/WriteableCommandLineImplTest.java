import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("Test option")
                .isRequired(false)
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");

        CommandLine parsed = new ParseContext().parse(option, commandLine.asArgs());
        WriteableCommandLine parsedCommandLine = (WriteableCommandLine) parsed;

        assertEquals("testValue", parsedCommandLine.getValues(option, null).get(0));
        assertTrue(parsedCommandLine.hasOption(option));
    }

    @Test
    public void testMissingOption() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("Test option")
                .isRequired(false)
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        CommandLine parsed = new ParseContext().parse(option, commandLine.asArgs());
        WriteableCommandLine parsedCommandLine = (WriteableCommandLine) parsed;

        assertFalse(parsedCommandLine.hasOption(option));
        assertNull(parsedCommandLine.getValues(option, null));
    }

    @Test
    public void testSwitch() {
        Option option = OptionBuilder.withLongOpt("testSwitch")
                .withDescription("Test switch")
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addSwitch(option, true);

        CommandLine parsed = new ParseContext().parse(option, commandLine.asArgs());
        WriteableCommandLine parsedCommandLine = (WriteableCommandLine) parsed;

        assertTrue(parsedCommandLine.getSwitch(option, false));
    }

    @Test
    public void testMissingSwitch() {
        Option option = OptionBuilder.withLongOpt("testSwitch")
                .withDescription("Test switch")
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        CommandLine parsed = new ParseContext().parse(option, commandLine.asArgs());
        WriteableCommandLine parsedCommandLine = (WriteableCommandLine) parsed;

        assertFalse(parsedCommandLine.getSwitch(option, false));
    }

    @Test
    public void testProperties() {
        Option option = OptionBuilder.withLongOpt("testPropertyOption")
                .withDescription("Test property option")
                .create(true);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addProperty(option, "testProperty", "testValue");

        CommandLine parsed = new ParseContext().parse(option, commandLine.asArgs());
        WriteableCommandLine parsedCommandLine = (WriteableCommandLine) parsed;

        assertEquals("testValue", parsedCommandLine.getProperty(option, "testProperty", "defaultValue"));
        assertEquals(1, parsedCommandLine.getProperties(option).size());
    }

    @Test
    public void testMultipleValues() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("Test option")
                .isRequired(false)
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");

        CommandLine parsed = new ParseContext().parse(option, commandLine.asArgs());
        WriteableCommandLine parsedCommandLine = (WriteableCommandLine) parsed;

        List values = parsedCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testNormalised() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withDescription("Test option")
                .isRequired(false)
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--testOption", "a", "b"));

        CommandLine parsed = new ParseContext().parse(option, commandLine.asArgs());
        WriteableCommandLine parsedCommandLine = (WriteableCommandLine) parsed;

        List normalised = parsedCommandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("a", normalised.get(1));
        assertEquals("b", normalised.get(2));
    }
}