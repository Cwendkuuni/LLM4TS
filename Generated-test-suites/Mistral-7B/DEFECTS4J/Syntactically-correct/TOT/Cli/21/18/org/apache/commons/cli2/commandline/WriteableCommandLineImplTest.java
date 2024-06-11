import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option optionA = new Option("a", "answer", true, "Answer to the question")
                .setDefaultValue("yes");
        Option optionB = new Option("b", "bool", false, "Boolean value");
        Option optionC = new Option("c", "count", "1,2,3", "Number of times the command is executed");

        commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "Root option"),
                Arrays.asList("--a", "--bool", "--c=2"));

        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        commandLine.addOption(optionC);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(optionA));
        assertTrue(commandLine.hasOption(optionB));
        assertTrue(commandLine.hasOption(optionC));
        assertFalse(commandLine.hasOption(new Option("d", "dummy", false, "Dummy option")));
    }

    @Test
    public void testGetOption() {
        Option parsedOptionA = commandLine.getOption(optionA.getPreferredName());
        Option parsedOptionB = commandLine.getOption(optionB.getPreferredName());
        Option parsedOptionC = commandLine.getOption(optionC.getPreferredName());

        assertEquals(optionA, parsedOptionA);
        assertEquals(optionB, parsedOptionB);
        assertEquals(optionC, parsedOptionC);
        assertNull(commandLine.getOption("dummy"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueNull() {
        commandLine.getValues(optionA, null);
    }

    @Test
    public void testGetValues() {
        List valuesA = commandLine.getValues(optionA, null);
        List valuesB = commandLine.getValues(optionB, null);
        List valuesC = commandLine.getValues(optionC, null);

        assertEquals("yes", valuesA.get(0));
        assertTrue(valuesB.isEmpty());
        assertEquals("2", valuesC.get(0));
        assertEquals("3", valuesC.get(1));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(optionA, "color", "red");

        String propertyValue = commandLine.getProperty("answer.color");
        assertEquals("red", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(optionA, "color", "red");

        Set properties = commandLine.getProperties(optionA);

        assertEquals(1, properties.size());
        assertTrue(properties.contains("color"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetSwitchNull() {
        commandLine.getSwitch(optionB, null);
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(optionB, true);

        Boolean switchValue = commandLine.getSwitch(optionB, false);

        assertTrue(switchValue);
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "Root option"),
                Arrays.asList("--a", "--bool=foo", "--c=2"));
    }
}