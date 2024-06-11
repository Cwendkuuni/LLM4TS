import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OptionImplTest {

    @Test
    public void testOptionImpl() {
        final Option option = new TestOption(1, true);
        final CommandLineParser parser = new DefaultParser();

        // valid usage
        final WriteableCommandLine commandLine = new WriteableCommandLine("Test");
        boolean processed = option.canProcess(commandLine, commandLine.iterator());
        Assert.assertTrue(processed);
        Assert.assertEquals("Test", commandLine.getOptionValue(option.getId()));

        // invalid usage (missing option)
        processed = option.canProcess(commandLine, commandLine.iterator());
        Assert.assertFalse(processed);

        // invalid usage (invalid option name)
        commandLine.addOption("--invalid");
        processed = option.canProcess(commandLine, commandLine.iterator());
        Assert.assertFalse(processed);

        // usage with value
        commandLine.addOption("--test", "value");
        processed = option.canProcess(commandLine, commandLine.iterator());
        Assert.assertTrue(processed);
        Assert.assertEquals("value", commandLine.getOptionValue(option.getId()));

        // test equals method
        Option option2 = new TestOption(2, true);
        Assert.assertFalse(option.equals(option2));
        Option option3 = new TestOption(1, true);
        Assert.assertTrue(option.equals(option3));

        // test hashCode method
        Assert.assertEquals(option.hashCode(), option.hashCode());
        Assert.assertNotEquals(option.hashCode(), option2.hashCode());

        // test findOption method
        Option foundOption = option.findOption("--test");
        Assert.assertEquals(option, foundOption);

        // test isRequired method
        Assert.assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPrefixes() {
        final Option option = new TestOption(1, true);
        final List prefixes = Arrays.asList("test", "invalid");

        option.checkPrefixes(prefixes);
    }

    private static class TestOption extends OptionImpl {
        TestOption(int id, boolean required) {
            super(id, required);
        }

        @Override
        public String getPreferredName() {
            return "test";
        }

        @Override
        public List getTriggers() {
            return Arrays.asList("--test");
        }
    }
}