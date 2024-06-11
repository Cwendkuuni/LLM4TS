import org.apache.commons.cli2.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class OptionTest {

    @Test
    public void testOptionProcess() {
        WriteableCommandLine commandLine = new BasicParseResults();
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                Assert.assertEquals("option", args.next().get(0));
                commandLine.setValue("value", args.next().get(0));
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue("defaultValue", "defaultValue");
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return argument.equals("option");
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return arguments.hasNext() && arguments.next().get(0).equals("option");
            }

            @Override
            public Set getTriggers() {
                return new LinkedHashSet(Arrays.asList("--option"));
            }

            @Override
            public Set getPrefixes() {
                return new LinkedHashSet(Arrays.asList("--"));
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // no validation needed
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                // no help lines needed
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // no usage information needed
            }

            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
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

            @Override
            public void setParent(Option parent) {
                // not applicable
            }
        };

        List<String> args = Arrays.asList("--option", "value").subList(0, 2).listIterator();
        option.process(commandLine, args);

        Assert.assertEquals("value", commandLine.getValue("value"));
    }

    @Test
    public void testOptionValidation() {
        WriteableCommandLine commandLine = new BasicParseResults();
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                // no operation
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // no default value
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                throw new OptionException("Validation error");
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                // no help lines needed
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // no usage information needed
            }

            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
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

            @Override
            public void setParent(Option parent) {
                // not applicable
            }
        };

        List<String> args = Arrays.asList("--option").listIterator();
        try {
            option.process(commandLine, args);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("Validation error", e.getMessage());
        }
    }

    @Test
    public void testOptionFindOption() {
        Option parentOption = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                // no operation
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // no default value
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // no validation needed
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                // no help lines needed
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // no usage information needed
            }

            @Override
            public String getPreferredName() {
                return "parent";
            }

            @Override
            public String getDescription() {
                return "This is a parent option";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public Option findOption(String trigger) {
                Option childOption = new Option() {
                    @Override
                    public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                        // no operation
                    }

                    @Override
                    public void defaults(WriteableCommandLine commandLine) {
                        // no default value
                    }

                    @Override
                    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                        return true;
                    }

                    @Override
                    public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                        return true;
                    }

                    @Override
                    public Set getTriggers() {
                        return null;
                    }

                    @Override
                    public Set getPrefixes() {
                        return null;
                    }

                    @Override
                    public void validate(WriteableCommandLine commandLine) throws OptionException {
                        // no validation needed
                    }

                    @Override
                    public List helpLines(int depth, Set helpSettings, Comparator comp) {
                        // no help lines needed
                        return null;
                    }

                    @Override
                    public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                        // no usage information needed
                    }

                    @Override
                    public String getPreferredName() {
                        return "child";
                    }

                    @Override
                    public String getDescription() {
                        return "This is a child option";
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
                        return parentOption;
                    }

                    @Override
                    public void setParent(Option parent) {
                        // not applicable
                    }
                };
                parentOption.setParent(null);
                parentOption.setParent(childOption);
                return childOption;
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
                // not applicable
            }
        };

        Option childOption = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                // no operation
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // no default value
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // no validation needed
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                // no help lines needed
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // no usage information needed
            }

            @Override
            public String getPreferredName() {
                return "child";
            }

            @Override
            public String getDescription() {
                return "This is a child option";
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
                return parentOption;
            }

            @Override
            public void setParent(Option parent) {
                // not applicable
            }
        };

        Option foundOption = parentOption.findOption("child");
        Assert.assertNotNull(foundOption);
        Assert.assertEquals(childOption, foundOption);
    }

    @Test
    public void testOptionIsRequired() {
        WriteableCommandLine commandLine = new BasicParseResults();
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                // no operation
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // no default value
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }

            @Override
            public Set getTriggers() {
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // no validation needed
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                // no help lines needed
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // no usage information needed
            }

            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
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
                return true;
            }

            @Override
            public Option getParent() {
                return null;
            }

            @Override
            public void setParent(Option parent) {
                // not applicable
            }
        };

        List<String> args = Arrays.asList().listIterator();
        try {
            option.process(commandLine, args);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertTrue(e.getMessage().contains("Option 'option' is required"));
        }
    }
}