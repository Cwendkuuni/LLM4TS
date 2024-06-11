import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = {"--option1", "--option2"};

        Options options = new Options();
        options.addRequiredOption("option1", true, "o1");
        options.addRequiredOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
    }

    @Test
    public void testParseWithOptionalOptions() throws ParseException {
        String[] args = {"--option1", "value1", "--option2", "value2"};

        Options options = new Options();
        Option option1 = options.addOption("option1", true, "o1");
        option1.setOptionalArg(true);
        Option option2 = options.addOption("option2", true, "o2");
        option2.setOptionalArg(true);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(4, cmd.getNumArgs());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        String[] args = {"--option", "value"};

        Options options = new Options();
        Option option = options.addOption("option", true, "o");
        option.setValueSeparator(':');

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithMultiValueOption() throws ParseException {
        String[] args = {"--option", "value1", "--option", "value2"};

        Options options = new Options();
        Option option = options.addOption("option", true, "o");
        option.setValueSeparator(':');

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(4, cmd.getNumArgs());
        assertTrue(cmd.hasOption("option"));
        String[] values = cmd.getOptionValues("option");
        assertEquals(2, values.length);
        Arrays.asList(values).contains("value1");
        Arrays.asList(values).contains("value2");
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"--invalid"};

        Options options = new Options();

        Parser parser = new DefaultParser();

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"--option"};

        Options options = new Options();
        options.addRequiredOption("option", true, "o");

        Parser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = {"--option"};

        Options options = new Options();
        Option option = options.addOption("option", true, "o");
        option.setRequiredArg(true);

        Parser parser = new DefaultParser();

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException {
        String[] args = {"--option", "value", "extra"};

        Options options = new Options();
        Option option = options.addOption("option", true, "o");
        option.setRequiredArg(false);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(3, cmd.getNumArgs());
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
        assertEquals("extra", cmd.getArg(2));
    }
}