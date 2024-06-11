import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionProcess() throws Exception {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option
        CommandLine commandLine = new CommandLine();

        List<String> argsList = List.of("--my-option", "value");
        ListIterator<String> argsIterator = argsList.listIterator();

        option.process(commandLine, argsIterator);
        Assert.assertEquals("value", commandLine.getOptionValue("my-option"));
    }

    @Test
    public void testOptionDefaults() throws Exception {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.defaults(commandLine);
        Assert.assertNull(commandLine.getOptionValue("my-option"));
    }

    @Test
    public void testOptionCanProcess_String() throws Exception {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option

        WriteableCommandLine commandLine = new WriteableCommandLine();

        Assert.assertTrue(option.canProcess(commandLine, "value"));
        Assert.assertFalse(option.canProcess(commandLine, "invalid"));
    }

    @Test
    public void testOptionCanProcess_ListIterator() throws Exception {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option

        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = List.of("value", "invalid");
        ListIterator<String> argsIterator = args.listIterator();

        Assert.assertTrue(option.canProcess(commandLine, argsIterator));
        Assert.assertFalse(option.canProcess(commandLine, argsIterator));
    }

    @Test(expected = OptionException.class)
    public void testOptionValidate() throws Exception {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.putString("my-option", "invalid");

        option.validate(commandLine);
    }

    @Test
    public void testOptionHelpLines() throws Exception {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option

        HelpFormatter formatter = new HelpFormatter();
        int depth = 1;
        Set helpSettings = Set.of(DisplaySetting.SHOW_DEFAULTS);
        Comparator comp = null;

        List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);
        StringWriter writer = new StringWriter();
        formatter.printHelp(writer, option.getPreferredName(), "", helpLines, helpSettings);

        System.out.println(writer.toString());
    }

    @Test
    public void testOptionAppendUsage() throws Exception {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Set.of(DisplaySetting.SHOW_DEFAULTS);
        Comparator comp = null;

        option.appendUsage(buffer, helpSettings, comp);
        System.out.println(buffer.toString());
    }

    @Test
    public void testOptionGetPreferredName() {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option
        Assert.assertEquals("my-option", option.getPreferredName());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option
        Assert.assertEquals("This is a test option", option.getDescription());
    }

    @Test
    public void testOptiongetId() {
        Option option = new MyOption(); // Assume MyOption is a subclass of Option
        Assert.assertEquals(1, option.getId());
    }

    @Test
    public void testOptionFindOption() {
        Option parentOption = new Option() {
            @Override
            public Set getTriggers() {
                return Set.of("parent");
            }
        };

        Option childOption = new Option() {
            @Override
            public Set getTriggers() {
                return Set.of("child");
            }

            @Override
            public Option findOption(String trigger) {
                if ("child".equals(trigger)) {
                    return this;
                }
                if (parentOption != null) {
                    return parentOption.findOption(trigger);
                }
                return null;
            }
        };

        Option foundOption = childOption.findOption("child");
        Assert.assertEquals(childOption, foundOption);

        Option notFoundOption = childOption.findOption("parent");
        Assert.assertNull(notFoundOption);
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option() {
            @Override
            public boolean isRequired() {
                return true;
            }
        };
        Assert.assertTrue(option.isRequired());

        Option notRequiredOption = new Option();
        Assert.assertFalse(notRequiredOption.isRequired());
    }

    @Test
    public void testOptionGetParent() {
        Option parentOption = new Option();
        Option childOption = new Option();
        childOption.setParent(parentOption);

        Option parent = childOption.getParent();
        Assert.assertEquals(parentOption, parent);

        Option noParentOption = new Option();
        Assert.assertNull(noParentOption.getParent());
    }
}