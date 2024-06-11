import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionsTest {

    @Test
    public void testOptionsWithShortAndLongOptions() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "Option A");
        options.addOption("b", "aliasB", true, "Option B with argument");
        options.addOption("c", "aliasC", false, "Option C");

        CommandLineParser parser = new DefaultParser();
        List<Option> optionsList = options.helpOptions();
        Assertions.assertEquals(3, optionsList.size());

        Option optionA = optionsList.get(0);
        Assertions.assertEquals("a", optionA.getKey());
        Assertions.assertFalse(optionA.isRequired());
        Assertions.assertEquals("aliasA", optionA.getLongOpt());
        Assertions.assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(1);
        Assertions.assertEquals("b", optionB.getKey());
        Assertions.assertFalse(optionB.isRequired());
        Assertions.assertEquals("aliasB", optionB.getLongOpt());
        Assertions.assertTrue(optionB.hasArg());

        Option optionC = optionsList.get(2);
        Assertions.assertEquals("c", optionC.getKey());
        Assertions.assertFalse(optionC.isRequired());
        Assertions.assertNull(optionC.getLongOpt());
        Assertions.assertFalse(optionC.hasArg());
    }

    @Test
    public void testOptionsWithOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Option group");
        group.setRequired(true);
        options.addOptionGroup(group);

        group.addOption("a", "aliasA", false, "Option A in group");
        group.addOption("b", "aliasB", true, "Option B with argument in group");

        CommandLineParser parser = new DefaultParser();
        List<Option> optionsList = options.helpOptions();
        Assertions.assertEquals(4, optionsList.size());

        Option optionA = optionsList.get(2);
        Assertions.assertEquals("a", optionA.getKey());
        Assertions.assertEquals("group", optionA.getOptionGroup().getName());
        Assertions.assertFalse(optionA.isRequired());
        Assertions.assertEquals("aliasA", optionA.getLongOpt());
        Assertions.assertFalse(optionA.hasArg());

        Option optionB = optionsList.get(3);
        Assertions.assertEquals("b", optionB.getKey());
        Assertions.assertEquals("group", optionB.getOptionGroup().getName());
        Assertions.assertEquals(true, optionB.isRequired());
        Assertions.assertEquals("aliasB", optionB.getLongOpt());
        Assertions.assertTrue(optionB.hasArg());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "Option A");

        Assertions.assertTrue(options.hasOption("a"));
        Assertions.assertTrue(options.hasOption("a")); // test case insensitive
        Assertions.assertTrue(options.hasOption("--aliasA"));
        Assertions.assertTrue(options.hasOption("--alias-a"));

        Assertions.assertFalse(options.hasOption("b"));
        Assertions.assertFalse(options.hasOption("c"));
    }
}