import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineTest {

    @Test
    void testHasOption() throws ParseException {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("test", true, "Test option");

        cmd.parse(options.parse(new String[]{"-test"}));
        Assertions.assertTrue(cmd.hasOption("test"));
        Assertions.assertEquals(true, cmd.hasOption('t'));

        cmd = new CommandLine();
        Assertions.assertFalse(cmd.hasOption("nonexistent"));
        Assertions.assertFalse(cmd.hasOption('z'));
    }

    @Test
    void testGetOptionValue() throws ParseException {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("test", true, "Test option");

        cmd.parse(options.parse(new String[]{"-test", "arg"}));
        Assertions.assertEquals("arg", cmd.getOptionValue("test"));
        Assertions.assertEquals("arg", cmd.getOptionValue('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", true, "Test option");
        cmd.parse(options.parse(new String[]{"--test", "arg"}));
        Assertions.assertEquals("arg", cmd.getOptionValue("test"));
        Assertions.assertEquals("arg", cmd.getOptionValue('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", null, "Test option");
        cmd.parse(options.parse(new String[]{"-test", "arg"}));
        Assertions.assertEquals("arg", cmd.getOptionValue("test"));
        Assertions.assertNull(cmd.getOptionValue('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", null, "Test option");
        cmd.parse(options.parse(new String[]{"--test", "arg"}));
        Assertions.assertEquals("arg", cmd.getOptionValue("test"));
        Assertions.assertNull(cmd.getOptionValue('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", null, "Test option");
        cmd.parse(options.parse(new String[]{}));
        Assertions.assertNull(cmd.getOptionValue("test"));
        Assertions.assertNull(cmd.getOptionValue('t'));
    }

    @Test
    void testGetOptionValues() throws ParseException {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("test", true, "Test option");

        cmd.parse(options.parse(new String[]{"-test", "arg1", "arg2"}));
        String[] expected = {"arg1", "arg2"};
        Assertions.assertArrayEquals(expected, cmd.getOptionValues("test"));
        Assertions.assertArrayEquals(expected, cmd.getOptionValues('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", true, "Test option");
        cmd.parse(options.parse(new String[]{"--test", "arg1", "arg2"}));
        Assertions.assertArrayEquals(expected, cmd.getOptionValues("test"));
        Assertions.assertArrayEquals(expected, cmd.getOptionValues('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", null, "Test option");
        cmd.parse(options.parse(new String[]{"-test", "arg1", "arg2"}));
        String[] actual = cmd.getOptionValues("test");
        Assertions.assertArrayEquals(expected, actual);
        Assertions.assertNull(cmd.getOptionValues('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", null, "Test option");
        cmd.parse(options.parse(new String[]{"--test", "arg1", "arg2"}));
        actual = cmd.getOptionValues("test");
        Assertions.assertArrayEquals(expected, actual);
        Assertions.assertNull(cmd.getOptionValues('t'));

        cmd = new CommandLine();
        options = new Options();
        options.addOption("test", null, "Test option");
        cmd.parse(options.parse(new String[]{}));
        Assertions.assertNull(cmd.getOptionValues("test"));
        Assertions.assertNull(cmd.getOptionValues('t'));
    }
}