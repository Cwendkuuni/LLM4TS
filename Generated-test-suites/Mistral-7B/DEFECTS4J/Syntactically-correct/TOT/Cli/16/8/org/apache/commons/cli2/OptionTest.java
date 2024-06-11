import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionProcess() throws Exception {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new BasicParseResults();

        List<String> args = new ArrayList<>();
        args.add("--myOption");
        args.listIterator().next(); // move iterator to next element
        option.process(commandLine, args.listIterator());

        Assert.assertEquals("Expected value for option", "value", commandLine.getValue("myOption"));
    }

    @Test
    public void testOptionDefaults() throws Exception {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new BasicParseResults();
        option.defaults(commandLine);

        Assert.assertNull("Expected null value for option", commandLine.getValue("myOption"));
    }

    @Test
    public void testOptionCanProcessArgument() throws Exception {
        Option option = new MyOption();

        WriteableCommandLine commandLine = new BasicParseResults();
        List<String> args = new ArrayList<>();
        args.add("--myOptionValue");

        Assert.assertTrue("Expected the option to be able to process the argument", option.canProcess(commandLine, args.listIterator()));
    }

    @Test(expected = OptionException.class)
    public void testOptionCanProcessInvalidArgument() throws Exception {
        Option option = new MyOption();

        WriteableCommandLine commandLine = new BasicParseResults();
        List<String> args = new ArrayList<>();
        args.add("invalidArgument");

        option.canProcess(commandLine, args.listIterator());
    }

    @Test
    public void testOptionGetTriggers() {
        Option option = new MyOption();
        Set<String> triggers = option.getTriggers();

        Assert.assertNotNull("Expected the set of triggers to not be null", triggers);
        Assert.assertEquals("Expected the size of triggers to be 1", 1, triggers.size());
        Assert.assertEquals("Expected the first trigger to be '--myOption'", "--myOption", triggers.iterator().next());
    }

    @Test
    public void testOptionGetPrefixes() {
        Option option = new MyOption();
        Set<String> prefixes = option.getPrefixes();

        Assert.assertNotNull("Expected the set of prefixes to not be null", prefixes);
        Assert.assertEquals("Expected the size of prefixes to be 1", 1, prefixes.size());
        Assert.assertEquals("Expected the first prefix to be '-'", "-");
    }

    @Test
    public void testOptionValidate() throws Exception {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new BasicParseResults();

        List<String> args = new ArrayList<>();
        args.add("--myOption");
        args.add("invalidValue");

        option.validate(commandLine);

        Assert.assertEquals("Expected no errors after validation", 0, commandLine.getNumArgs());
    }

    @Test(expected = OptionException.class)
    public void testOptionValidateInvalidCommandLine() throws Exception {
        Option option = new MyOption();
        WriteableCommandLine commandLine = new BasicParseResults();

        List<String> args = new ArrayList<>();
        args.add("invalidCommandLine");

        option.validate(commandLine);
    }

    @Test
    public void testOptionHelpLines() {
        Option option = new MyOption();
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> helpLines = option.helpLines(0, null, null);

        Assert.assertNotNull("Expected the helpLines to not be null", helpLines);
        Assert.assertEquals("Expected the number of helpLines to be 1", 1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals("Expected the helpLine to have the correct name", "--myOption", helpLine.getOpt());
        Assert.assertEquals("Expected the helpLine to have the correct description", "My option description", helpLine.getLongOpt());
        Assert.assertEquals("Expected the helpLine to have the correct values", "[value]", helpLine.getOptArg());
    }

    @Test
    public void testOptionAppendUsage() {
        Option option = new MyOption();
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);

        String usage = buffer.toString();
        Assert.assertTrue("Expected the usage string to contain '--myOption'", usage.contains("--myOption"));
        Assert.assertTrue("Expected the usage string to contain 'My option description' ", usage.contains("My option description"));
    }

    @Test
    public void testOptionGetPreferredName() {
        Option option = new MyOption();
        String preferredName = option.getPreferredName();

        Assert.assertEquals("Expected the preferred name to be '--myOption'", "--myOption", preferredName);
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new MyOption();
        String description = option.getDescription();

        Assert.assertEquals("Expected the description to be 'My option description'", "My option description", description);
    }

    @Test
    public void testOptiongetId() {
        Option option = new MyOption();
        int id = option.getId();

        Assert.assertEquals("Expected the id to be 0", 0, id);
    }

    @Test
    public void testOptionFindOption() {
        Option option1 = new MyOption();
        Option option2 = new MyOption();

        Option foundOption = option1.findOption("--myOption2");
        Assert.assertNull("Expected null when searching for an option that doesn't exist", foundOption);

        Option parentOption = new ParentOption();
        parentOption.addOption(option1);
        parentOption.addOption(option2);

        Option foundOption2 = parentOption.findOption("--myOption1");
        Assert.assertEquals("Expected the found option to be option1", option1, foundOption2);
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new MyOption();
        boolean isRequired = option.isRequired();

        Assert.assertFalse("Expected option to not be required", isRequired);

        Option requiredOption = new RequiredOption();
        isRequired = requiredOption.isRequired();

        Assert.assertTrue("Expected option to be required", isRequired);
    }

    @Test
    public void testOptionGetParent() {
        Option option = new MyOption();
        Option parentOption = new ParentOption();
        parentOption.addOption(option);

        Option parent = option.getParent();
        Assert.assertEquals("Expected the parent to be the parentOption", parentOption, parent);
    }

    @Test
    public void testOptionSetParent() {
        Option option = new MyOption();
        Option parentOption = new ParentOption();

        option.setParent(parentOption);
        Option parent = option.getParent();

        Assert.assertEquals("Expected the parent to be the parentOption", parentOption, parent);
    }
}

class ParentOption extends Option {
    @Override
    public void addOption(Option option) {
        super.addOption(option);
    }
}

class MyOption extends Option {
    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        commandLine.setValue("myOption", args.next());
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        // Empty implementation
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return true;
    }

    @Override
    public Set getTriggers() {
        return new HashSet() {{
            add("--myOption");
        }};
    }

    @Override
    public Set getPrefixes() {
        return new HashSet() {{
            add("-");
        }};
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // Empty implementation
    }

    @Override
    public List helpLines(int depth, Set helpSettings, Comparator comp) {
        return new ArrayList();
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        // Empty implementation
    }

    @Override
    public String getPreferredName() {
        return "--myOption";
    }

    @Override
    public String getDescription() {
        return "My option description";
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
    public void setParent(Option parent) {
        // Empty implementation
    }
}

class RequiredOption extends Option {
    @Override
    public boolean isRequired() {
        return true;
    }
}