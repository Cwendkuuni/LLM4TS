import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class OptionImplTest {

    private Option option;
    private CommandLineParser parser;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option = new OptionBuilder()
                .withLongOpt("longOption")
                .withShortOpt('s')
                .withDescription("A test option")
                .create();

        parser = Mockito.spy(Mockito.mock(CommandLineParser.class));
        commandLine = Mockito.spy(Mockito.mock(WriteableCommandLine.class));
    }

    @After
    public void tearDown() {
        parser = null;
        commandLine = null;
        option = null;
    }

    @Test
    public void testCanProcess() {
        // Test with valid argument
        Mockito.when(commandLine.getArguments()).thenReturn(new String[]{"arg"});
        assertTrue(option.canProcess(commandLine, commandLine.listIterator()));

        // Test with no arguments
        Mockito.when(commandLine.getArguments()).thenReturn(new String[0]);
        assertFalse(option.canProcess(commandLine, commandLine.listIterator()));

        // Test with an argument that does not match
        Mockito.when(commandLine.getArguments()).thenReturn(new String[]{"notArg"});
        assertFalse(option.canProcess(commandLine, commandLine.listIterator()));
    }

    @Test
    public void testFindOption() {
        Option option2 = new OptionBuilder()
                .withLongOpt("anotherOption")
                .withShortOpt('a')
                .create();

        Set<Option> options = Mockito.mock(Set.class);
        Mockito.when(options.add(option)).thenReturn(true);
        Mockito.when(options.add(option2)).thenReturn(true);
        option.setOptions(options);

        Option foundOption = option.findOption("s");
        assertEquals(option, foundOption);

        Option foundOption2 = option.findOption("a");
        assertEquals(option2, foundOption2);

        Option notFoundOption = option.findOption("x");
        assertNull(notFoundOption);
    }

    @Test
    public void testEquals() {
        Option option2 = new OptionBuilder()
                .withLongOpt("anotherOption")
                .withShortOpt('a')
                .create();

        assertTrue(option.equals(option));
        assertTrue(option.equals(option.clone()));
        assertFalse(option.equals(null));
        assertFalse(option.equals(new Object()));
        assertFalse(option.equals(option2));

        Option clonedOption = option.clone();
        assertTrue(clonedOption.equals(option));
        assertTrue(clonedOption.equals(option.clone()));
        assertFalse(clonedOption.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option2 = new OptionBuilder()
                .withLongOpt("anotherOption")
                .withShortOpt('a')
                .create();

        int hashCode1 = option.hashCode();
        Option clonedOption = option.clone();
        int hashCode2 = clonedOption.hashCode();
        int hashCode3 = option2.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        String expected = "Option [name=longOption, aliases=[s], description=A test option]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testId() {
        int id = option.getId();
        assertEquals(0, id);

        Option option2 = new OptionBuilder()
                .withLongOpt("anotherOption")
                .withShortOpt('a')
                .withId(1)
                .create();

        int id2 = option2.getId();
        assertEquals(1, id2);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());

        Option requiredOption = new OptionBuilder()
                .withLongOpt("requiredOption")
                .withShortOpt('r')
                .isRequired()
                .create();

        assertTrue(requiredOption.isRequired());
    }
}