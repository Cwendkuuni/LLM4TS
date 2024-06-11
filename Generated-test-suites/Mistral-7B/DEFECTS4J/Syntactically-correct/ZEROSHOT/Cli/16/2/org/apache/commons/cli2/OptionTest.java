import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        option = new TestOption();
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testProcess() throws OptionException {
        List<String> args = Arrays.asList("--test", "arg1", "arg2").listIterator();
        option.process(commandLine, args);
        Assert.assertEquals("--test", commandLine.getString("test"));
        Assert.assertEquals("arg1", commandLine.getString("test.arg1"));
        Assert.assertEquals("arg2", commandLine.getString("test.arg2"));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        Assert.assertNull(commandLine.getString("test"));
        Assert.assertNull(commandLine.getString("test.arg1"));
        Assert.assertNull(commandLine.getString("test.arg2"));
    }

    @Test
    public void testCanProcess() throws OptionException {
        List<String> args = Arrays.asList("--test", "arg1").listIterator();
        Assert.assertTrue(option.canProcess(commandLine, args));

        args = Arrays.asList("--arg", "test").listIterator();
        Assert.assertFalse(option.canProcess(commandLine, args));

        args = Arrays.asList("--test", "arg1", "--arg", "test").listIterator();
        Assert.assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        Assert.assertNotNull(triggers);
        Assert.assertEquals("--test", triggers.iterator().next());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        Assert.assertNotNull(prefixes);
        Assert.assertEquals("--", prefixes.iterator().next());
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        option.validate(commandLine);
        List<String> args = Arrays.asList("--test", "arg1", "--arg", "test").listIterator();
        option.process(commandLine, args);
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List<Option> options = new ArrayList<>();
        options.add(option);
        Set<DisplaySetting> helpSettings = new HashSet<>();

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, null);
        Assert.assertNotNull(helpLines);
        Assert.assertEquals(3, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals("--test\n", helpLine.getUsage());
        Assert.assertEquals("test option\n", helpLine.getShortDescription());
        Assert.assertEquals("test option\n", helpLine.getLongDescription());

        helpLine = helpLines.get(1);
        Assert.assertEquals("--test arg1 <arg2>\n", helpLine.getUsage());
        Assert.assertEquals("an argument for test option\n", helpLine.getShortDescription());
        Assert.assertEquals("an argument for test option\n", helpLine.getLongDescription());

        helpLine = helpLines.get(2);
        Assert.assertEquals("", helpLine.getUsage());
        Assert.assertEquals("an optional argument for test option\n", helpLine.getShortDescription());
        Assert.assertEquals("an optional argument for test option\n", helpLine.getLongDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, helpSettings, null);
        Assert.assertEquals("--test\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("test", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("test option\n", option.getDescription());
    }

    @Test
    public void testgetId() {
        Assert.assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option otherOption = new OtherOption();
        commandLine.putValue(TestOption.TEST_ARG1, "testArg1");
        commandLine.putValue(OtherOption.OTHER_ARG, "otherArg");
        List<String> args = Arrays.asList("--test", "arg1", "--other", "arg").listIterator();

        Option foundOption = option.findOption(args.next());
        Assert.assertEquals(option, foundOption);

        foundOption = otherOption.findOption(args.next());
        Assert.assertEquals(otherOption, foundOption);

        foundOption = option.findOption("--test2");
        Assert.assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = new ParentOption();
        option.setParent(parentOption);
        Assert.assertEquals(parentOption, option.getParent());
    }

    private static class TestOption extends AbstractOption {
        static final String TEST_ARG1 = "arg1";
        static final String TEST_ARG2 = "arg2";

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
            super.process(commandLine, args);
            commandLine.putValue(TEST_ARG1, args.next());
            commandLine.putValue(TEST_ARG2, args.next());
        }

        @Override
        public Set<String> getTriggers() {
            return new HashSet<>(Arrays.asList("--test"));
        }

        @Override
        public Set<String> getPrefixes() {
            return new HashSet<>(Arrays.asList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty
        }

        @Override
        public List<HelpLine> helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator comparator) {
            List<HelpLine> helpLines = new ArrayList<>();
            helpLines.add(new HelpLineImpl("-test\n", "test option\n", "test option\n"));
            helpLines.add(new HelpLineImpl("-test <arg1> <arg2>\n", "an argument for test option\n", "an argument for test option\n"));
            helpLines.add(new HelpLineImpl("", "an optional argument for test option\n", "an optional argument for test option\n"));
            return helpLines;
        }

        @Override
        public String getPreferredName() {
            return "test";
        }

        @Override
        public String getDescription() {
            return "test option\n";
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }
    }

    private static class OtherOption extends AbstractOption {
        static final String OTHER_ARG = "arg";

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
            super.process(commandLine, args);
            commandLine.putValue(OTHER_ARG, args.next());
        }

        @Override
        public Set<String> getTriggers() {
            return new HashSet<>(Arrays.asList("--other"));
        }

        @Override
        public Set<String> getPrefixes() {
            return new HashSet<>(Arrays.asList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty
        }

        @Override
        public List<HelpLine> helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator comparator) {
            List<HelpLine> helpLines = new ArrayList<>();
            helpLines.add(new HelpLineImpl("-other\n", "other option\n", "other option\n"));
            helpLines.add(new HelpLineImpl("-other <arg>\n", "an argument for other option\n", "an argument for other option\n"));
            return helpLines;
        }

        @Override
        public String getPreferredName() {
            return "other";
        }

        @Override
        public String getDescription() {
            return "other option\n";
        }

        @Override
        public int getId() {
            return 2;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }
    }

    private static class ParentOption extends AbstractOption {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
            // empty
        }

        @Override
        public Set<String> getTriggers() {
            return new HashSet<>();
        }

        @Override
        public Set<String> getPrefixes() {
            return new HashSet<>();
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty
        }

        @Override
        public List<HelpLine> helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator comparator) {
            return new ArrayList<>();
        }

        @Override
        public String getPreferredName() {
            return "parent";
        }

        @Override
        public String getDescription() {
            return "parent option\n";
        }

        @Override
        public int getId() {
            return 3;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }
    }
}

class HelpLineImpl implements HelpLine {
    private final String usage;
    private final String shortDescription;
    private final String longDescription;

    public HelpLineImpl(String usage, String shortDescription, String longDescription) {
        this.usage = usage;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public String getLongDescription() {
        return longDescription;
    }
}