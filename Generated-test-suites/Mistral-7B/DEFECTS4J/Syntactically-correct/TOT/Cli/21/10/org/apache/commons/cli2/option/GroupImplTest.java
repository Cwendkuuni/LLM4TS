package org.apache.commons.cli2.option;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

public class GroupImplTest extends TestCase {

    public void testCreateGroupWithValidOptionsAndArguments() {
        // create a test group
        final List options = new ArrayList();
        options.add(createOption("option1", "description1", false, Integer.class, 1, 1));
        options.add(createOption("option2", "description2", true, String.class, 0, 1));
        options.add(createArgument("arg1", "description1", false, Integer.class));
        options.add(createArgument("arg2", "description2", true, String.class));

        final Group group = new GroupImpl(options, "Group", "A test group", 1, 1, false);

        // test canProcess method with valid arguments
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("option1", 1);
        commandLine.addArgument("arg1", 1);
        Assert.assertTrue(group.canProcess(commandLine, null));

        // test process method with valid arguments
        final List iterator = new ArrayList().listIterator();
        group.process(commandLine, iterator);
        Assert.assertEquals("option1", iterator.next());
        Assert.assertEquals("arg1", iterator.next());

        // test validate method with valid arguments
        group.validate(commandLine);

        // test appendUsage method with valid settings
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, DisplaySetting.ALL, null);
        Assert.assertEquals("usage for Group\n" +
                "[option1]  description1\n" +
                "[arg1]      description1\n" +
                "[option2]  description2\n" +
                "[arg2]      description2\n", buffer.toString());
    }

    public void testCreateGroupWithInvalidOptionCount() {
        // create a test group
        final List options = new ArrayList();
        options.add(createOption("option1", "description1", false, Integer.class, 1, 1));
        options.add(createOption("option1", "description1", false, Integer.class, 1, 1));
        options.add(createArgument("arg1", "description1", false, Integer.class));

        try {
            new GroupImpl(options, "Group", "A test group", 1, 1, false);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    public void testCreateGroupWithInvalidArgumentCount() {
        // create a test group
        final List options = new ArrayList();
        options.add(createOption("option1", "description1", false, Integer.class, 1, 1));
        options.add(createArgument("arg1", "description1", false, Integer.class));
        options.add(createArgument("arg1", "description1", false, Integer.class));

        try {
            new GroupImpl(options, "Group", "A test group", 1, 1, false);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    public void testCreateGroupWithInvalidArgumentType() {
        // create a test group
        final List options = new ArrayList();
        options.add(createOption("option1", "description1", false, Integer.class, 1, 1));
        options.add(createArgument("arg1", "description1", false, Double.class));

        try {
            new GroupImpl(options, "Group", "A test group", 1, 1, false);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    public void testCreateGroupWithInvalidMinimumOrMaximum() {
        // create a test group
        final List options = new ArrayList();
        options.add(createOption("option1", "description1", false, Integer.class, 2, 1));
        options.add(createArgument("arg1", "description1", false, Integer.class));

        try {
            new GroupImpl(options, "Group", "A test group", 1, 1, false);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    private Option createOption(String name, String description, boolean required,
            Class type, int min, int max) {
        return new OptionBuilder()
                .withName(name)
                .withDescription(description)
                .isRequired(required)
                .withType(type)
                .withMinValues(min)
                .withMaxValues(max)
                .create();
    }

    private Argument createArgument(String name, String description,
            boolean required, Class type) {
        return new ArgumentBuilder()
                .withName(name)
                .withDescription(description)
                .isRequired(required)
                .withType(type)
                .create();
    }
}