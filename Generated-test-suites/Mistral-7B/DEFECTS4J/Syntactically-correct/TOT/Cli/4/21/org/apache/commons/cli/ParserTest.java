import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void testSimpleUsage() throws ParseException {
        String[] args = new String[]{"-a", "1", "--b", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assertions.assertEquals(cmd.getOptionValue("a"), "1");
        Assertions.assertEquals(cmd.getOptionValue("b"), "2");
        Assertions.assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testMissingArgument() {
        String[] args = new String[]{"--a"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Assertions.assertThrows(MissingArgumentException.class, () -> {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
        });
    }

    @Test
    public void testMissingOption() {
        String[] args = new String[]{"-c", "1"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Assertions.assertThrows(UnrecognizedOptionException.class, () -> {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
        });
    }

    @Test
    public void testRequiredOption() {
        String[] args = new String[]{"-a"};

        Options options = new Options();
        Option option = new Option("a", true, "Option A");
        option.setRequired(true);
        options.addOption(option);

        Assertions.assertThrows(MissingOptionException.class, () -> {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
        });
    }

    @Test
    public void testOptionGroup() throws ParseException {
        String[] args = new String[]{"--group", "g1"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        Option option1 = new Option("g1", true, "Group option 1");
        group.addOption(option1);
        Option option2 = new Option("g2", true, "Group option 2");
        group.addOption(option2);
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assertions.assertEquals(cmd.getOptionValue("g1"), "g1");
        Assertions.assertEquals(cmd.getOptionValue("g2"), "g2");
    }

    @Test
    public void testProperties() throws ParseException {
        String[] args = new String[]{"--prop", "prop=value"};

        Properties properties = new Properties();
        properties.setProperty("prop", "value");

        Options options = new Options();
        Option option = new Option("prop", true, "Property");
        option.setLongOpt("prop");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assertions.assertEquals(cmd.getOptionValue("prop"), "value");
    }
}