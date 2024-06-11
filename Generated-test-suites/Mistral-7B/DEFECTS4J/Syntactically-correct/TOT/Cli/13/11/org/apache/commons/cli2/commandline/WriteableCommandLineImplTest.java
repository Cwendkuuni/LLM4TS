import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.ParseResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.cli2.commandline.WriteableCommandLineImpl.WriteableCommandLineImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        option = new Option("o", "output", false, "Output file name");

        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("input.txt"));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option returnedOption = commandLine.getOption("o");
        assertNotNull(returnedOption);
        assertEquals(option, returnedOption);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("o"));
        assertFalse(commandLine.looksLikeOption("i"));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals( Arrays.asList("output.txt"), values);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testToString() {
        assertEquals("input.txt", commandLine.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("test", "value");
        Set properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertEquals("value", commandLine.getProperty("test", ""));
    }

    @Test
    public void testGetValuesAndDefaults() {
        commandLine.addValue(option, "output1.txt");
        commandLine.addValue(option, "output2.txt");
        commandLine.setDefaultValues(option, Arrays.asList("output3.txt"));

        List values = commandLine.getValues(option, null);
        List defaultValues = commandLine.getValues(option, Arrays.asList("output4.txt"));

        assertEquals(Arrays.asList("output1.txt", "output2.txt"), values);
        assertEquals(Arrays.asList("output3.txt", "output4.txt"), defaultValues);
    }

    @Test
    public void testGetSwitchAndDefaults() {
        commandLine.addSwitch(option, true);
        commandLine.setDefaultSwitch(option, false);

        boolean switchValue = commandLine.getSwitch(option, null);
        boolean defaultSwitchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
        assertFalse(defaultSwitchValue);
    }
}