import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OptionTest {

    @Test
    public void testOptionProcess() {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--test", "--option", "value").listIterator();

        option.process(commandLine, args);

        Assert.assertEquals("--test", commandLine.getOptionNames().get(0));
        Assert.assertEquals("value", commandLine.getOptionValues("option").get(0));
    }

    @Test
    public void testOptionProcess_InvalidArgument() {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--test", "--option", "invalid-value", "invalid-argument").listIterator();

        try {
            option.process(commandLine, args);
            Assert.fail();
        } catch (OptionException e) {
            // expected
        }

        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertEquals("invalid-argument", commandLine.getArgs().get(0));
    }

    @Test
    public void testOptionProcess_MultipleArguments() {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--test", "--option", "value1", "value2").listIterator();

        option.process(commandLine, args);

        Assert.assertEquals(2, commandLine.getOptionValues("option").size());
        Assert.assertEquals("value1", commandLine.getOptionValues("option").get(0));
        Assert.assertEquals("value2", commandLine.getOptionValues("option").get(1));
    }

    @Test
    public void testOptionDefaults() {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.defaults(commandLine);

        Assert.assertFalse(commandLine.hasOption("option"));
        Assert.assertFalse(commandLine.hasOption("option-with-arg"));
    }

    @Test
    public void testOptionCanProcess() {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Assert.assertTrue(option.canProcess(commandLine, "test"));
        Assert.assertTrue(option.canProcess(commandLine, "option"));
        Assert.assertTrue(option.canProcess(commandLine, "--option"));
        Assert.assertFalse(option.canProcess(commandLine, "invalid-option"));

        ListIterator<String> args = Arrays.asList("--test", "--option", "value").listIterator();

        Assert.assertTrue(option.canProcess(commandLine, args));
        Assert.assertFalse(option.canProcess(commandLine, args, true)); // consume the argument
    }

    @Test
    public void testOptionGetTriggers() {
        Option option = new MyOption();
        Set<String> triggers = option.getTriggers();

        Assert.assertNotNull(triggers);
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("--option", triggers.iterator().next());
    }

    @Test
    public void testOptionGetPrefixes() {
        Option option = new MyOption();
        Set<String> prefixes = option.getPrefixes();

        Assert.assertNotNull(prefixes);
        Assert.assertEquals(1, prefixes.size());
        Assert.assertEquals("--", prefixes.iterator().next());
    }

    @Test
    public void testOptionValidate() {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.putBooleanOption("invalid-option", false);

        try {
            option.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            // expected
        }

        Assert.assertFalse(commandLine.hasOption(MyOption.OPTION_NAME));
        Assert.assertFalse(commandLine.hasOption("invalid-option"));
    }

    @Test
    public void testOptionHelpLines() {
        Option option = new MyOption();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comp = null;

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, comp);

        Assert.assertNotNull(helpLines);
        Assert.assertEquals(1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        Assert.assertNotNull(helpLine);
        Assert.assertEquals("--option", helpLine.getOptionSpec().getLongOpt());
        Assert.assertEquals("option", helpLine.getOptionSpec().getOpt());
        Assert.assertEquals("An option with an argument.", helpLine.getDescription());
    }

    @Test
    public void testOptionAppendUsage() {
        Option option = new MyOption();

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, Collections.emptySet(), null);

        Assert.assertEquals("--option <argument>", buffer.toString());
    }

    @Test
    public void testOptionGetPreferredName() {
        Option option = new MyOption();

        String preferredName = option.getPreferredName();

        Assert.assertEquals("option", preferredName);
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new MyOption();

        String description = option.getDescription();

        Assert.assertEquals("An option with an argument.", description);
    }

    @Test
    public void testOptiongetId() {
        Option option = new MyOption();

        int id = option.getId();

        Assert.assertEquals(1, id);
    }

    @Test
    public void testOptionFindOption() {
        Option parentOption = new ParentOption();
        Option option1 = new Option1();
        Option option2 = new Option2();

        parentOption.addOption(option1);
        parentOption.addOption(option2);

        Option foundOption = parentOption.findOption("option1");

        Assert.assertEquals(option1, foundOption);
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new MyOption();

        boolean isRequired = option.isRequired();

        Assert.assertFalse(isRequired);
    }

    @Test
    public void testOptionHasParent() {
        Option parentOption = new ParentOption();
        Option option = new MyOption();

        parentOption.addOption(option);

        Option parent = option.getParent();

        Assert.assertEquals(parentOption, parent);
    }

    private static class MyOption implements Option {
        static final String OPTION_NAME = "option";

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            // implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            // implementation
        }

        @Override
        public Set<String> getTriggers() {
            Set<String> triggers = new HashSet<>();
            triggers.add("--option");
            return triggers;
        }

        @Override
        public Set<String> getPrefixes() {
            Set<String> prefixes = new HashSet<>();
            prefixes.add("--");
            return prefixes;
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // implementation
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public String getPreferredName() {
            return "option";
        }

        @Override
        public String getDescription() {
            return "An option with an argument.";
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }
    }

    private static class ParentOption implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            // implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            // implementation
        }

        @Override
        public Set<String> getTriggers() {
            Set<String> triggers = new HashSet<>();
            triggers.add("--parent");
            return triggers;
        }

        @Override
        public Set<String> getPrefixes() {
            Set<String> prefixes = new HashSet<>();
            prefixes.add("--");
            return prefixes;
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // implementation
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public String getPreferredName() {
            return "parent";
        }

        @Override
        public String getDescription() {
            return "A parent option.";
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void addOption(Option option) {
            // implementation
        }
    }

    private static class Option1 implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            // implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            // implementation
        }

        @Override
        public Set<String> getTriggers() {
            Set<String> triggers = new HashSet<>();
            triggers.add("--option1");
            return triggers;
        }

        @Override
        public Set<String> getPrefixes() {
            Set<String> prefixes = new HashSet<>();
            prefixes.add("--");
            return prefixes;
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // implementation
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public String getPreferredName() {
            return "option1";
        }

        @Override
        public String getDescription() {
            return "An option1 with an argument.";
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }
    }

    private static class Option2 implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            // implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            // implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            // implementation
        }

        @Override
        public Set<String> getTriggers() {
            Set<String> triggers = new HashSet<>();
            triggers.add("--option2");
            return triggers;
        }

        @Override
        public Set<String> getPrefixes() {
            Set<String> prefixes = new HashSet<>();
            prefixes.add("--");
            return prefixes;
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // implementation
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // implementation
        }

        @Override
        public String getPreferredName() {
            return "option2";
        }

        @Override
        public String getDescription() {
            return "An option2 with an argument.";
        }

        @Override
        public int getId() {
            return 2;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }
    }
}