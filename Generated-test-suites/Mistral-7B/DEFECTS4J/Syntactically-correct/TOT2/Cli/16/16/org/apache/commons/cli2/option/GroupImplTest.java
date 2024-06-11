import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.exceptions.OptionException;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithBasicOptions() throws OptionException {
        final List options = Arrays.asList(
                Option.builder("a").longOpt("optionA").required(false).build(),
                Option.builder("b").longOpt("optionB").required(false).build(),
                Option.builder("c").longOpt("optionC").required(true).build()
        );

        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 1, 1);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--optionC", "--optionA", "valueA"));

        Assert.assertEquals("valueA", group.getValue("optionA"));
        Assert.assertNull(group.getValue("optionB"));
        Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
        Assert.assertEquals("valueA", group.getValue("optionA", null));
        Assert.assertNull(group.getValue("optionB", "defaultValue"));
        Assert.assertEquals("valueA", cmd.getOptionValue("optionA", "defaultValue"));
        Assert.assertEquals("valueA", group.getValue("optionA", null, "defaultValue"));

        Assert.assertEquals("This is my group", group.getDescription());

        final HelpFormatter formatter = new HelpFormatter();
        group.printHelp("MyGroup", Arrays.asList("--optionA", "--optionB", "--optionC"), true);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithRequiredOptionMissing() throws OptionException {
        final List options = Arrays.asList(
                Option.builder("a").longOpt("optionA").required(true).build(),
                Option.builder("b").longOpt("optionB").required(false).build()
        );

        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 1, 1);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--optionB", "valueB"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithTooManyOptions() throws OptionException {
        final List options = Arrays.asList(
                Option.builder("a").longOpt("optionA").required(false).build(),
                Option.builder("b").longOpt("optionB").required(false).build(),
                Option.builder("c").longOpt("optionC").required(true).build()
        );

        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 1, 2);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--optionA", "--optionA", "--optionC"));
    }

    @Test
    public void testGroupWithAnonymousOptions() throws OptionException {
        final List options = Arrays.asList(
                Option.builder("g").longOpt("group").required(false).build(),
                Option.builder("a").longOpt("optionA").required(false).build(),
                Option.builder("b").longOpt("optionB").required(false).build(),
                Argument.builder("arg").longOpt("argument").required(false).build()
        );

        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 0, Integer.MAX_VALUE);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--group", "--optionA", "valueA", "--optionB", "valueB", "--arg", "valueArg"));

        Assert.assertEquals("valueA", group.getValue("optionA"));
        Assert.assertEquals("valueB", group.getValue("optionB"));
        Assert.assertEquals("valueArg", group.getValue("argument"));
        Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
        Assert.assertEquals("valueB", cmd.getOptionValue("optionB"));
        Assert.assertEquals("valueArg", cmd.getOptionValue("argument"));
    }

    @Test
    public void testGroupWithNestedOptions() throws OptionException {
        final List options = Arrays.asList(
                Option.builder("g").longOpt("group").required(false).build(),
                Option.builder("a").longOpt("optionA").required(false).build(),
                Option.builder("b").longOpt("optionB").required(false).build(),
                Option.builder("c").longOpt("optionC").required(false).build(),
                Option.builder("d").longOpt("optionD").required(false).build(),
                Option.builder("e").longOpt("optionE").required(false).build()
        );

        final Option optionC = Option.builder("c").longOpt("optionC").required(false).build();
        optionC.setValueDescription("Value for option c");
        optionC.setArgumentDescription("Argument for option c");

        final Option optionD = Option.builder("d").longOpt("optionD").required(false).build();
        optionD.setValueDescription("Value for option d");
        optionD.setArgumentDescription("Argument for option d");

        final Option optionE = Option.builder("e").longOpt("optionE").required(false).build();
        optionE.setValueDescription("Value for option e");
        optionE.setArgumentDescription("Argument for option e");

        final List nestedOptions = Arrays.asList(optionC, optionD, optionE);

        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 0, Integer.MAX_VALUE);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--group", "--optionC", "valueC", "--optionD", "valueD", "argD", "--optionE", "valueE", "argE"));

        final Option optionCValue = group.findOption("optionC");
        Assert.assertNotNull(optionCValue);
        Assert.assertEquals("valueC", optionCValue.getValue());
        Assert.assertEquals("Value for option c", optionCValue.getDescription());

        final Option optionDValue = group.findOption("optionD");
        Assert.assertNotNull(optionDValue);
        Assert.assertEquals("valueD", optionDValue.getValue());
        Assert.assertEquals("valueD", cmd.getOptionValue("optionD"));
        Assert.assertEquals("Argument for option d", optionDValue.getArgumentValue());
        Assert.assertEquals("argD", cmd.getOptionValue("argD"));

        final Option optionEValue = group.findOption("optionE");
        Assert.assertNotNull(optionEValue);
        Assert.assertEquals("valueE", optionEValue.getValue());
        Assert.assertEquals("valueE", cmd.getOptionValue("optionE"));
        Assert.assertEquals("Argument for option e", optionEValue.getArgumentValue());
        Assert.assertEquals("argE", cmd.getOptionValue("argE"));

        final HelpFormatter formatter = new HelpFormatter();
        group.printHelp("MyGroup", Arrays.asList("--optionA", "--optionB", "--group"), true);
    }

    @Test
    public void testGroupWithAnonymousArgument() throws OptionException {
        final List options = Arrays.asList(
                Option.builder("g").longOpt("group").required(false).build(),
                Argument.builder("arg").longOpt("argument").required(false).build()
        );

        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 0, Integer.MAX_VALUE);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--group", "--arg", "value"));

        Assert.assertEquals("value", group.getValue("argument"));
        Assert.assertEquals("value", cmd.getOptionValue("argument"));
        Assert.assertEquals("value", group.getValue("argument", null));
        Assert.assertEquals("value", cmd.getOptionValue("argument", "defaultValue"));
        Assert.assertEquals("value", group.getValue("argument", null, "defaultValue"));
    }

    @Test
    public void testGroupWithMixedOptionsAndArguments() throws OptionException {
        final List options = Arrays.asList(
                Option.builder("g").longOpt("group").required(false).build(),
                Option.builder("a").longOpt("optionA").required(false).build(),
                Argument.builder("argA").longOpt("argA").required(false).build(),
                Argument.builder("argB").longOpt("argB").required(false).build()
        );

        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 0, Integer.MAX_VALUE);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--group", "--optionA", "valueA", "--argA", "valueArgA", "--argB", "valueArgB"));

        Assert.assertEquals("valueA", group.getValue("optionA"));
        Assert.assertEquals("valueArgA", group.getValue("argA"));
        Assert.assertEquals("valueArgB", group.getValue("argB"));
        Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
        Assert.assertEquals("valueArgA", cmd.getOptionValue("argA"));
        Assert.assertEquals("valueArgB", cmd.getOptionValue("argB"));
    }

    @Test
    public void testGroupWithEmptyOptionsList() {
        final List options = Collections.emptyList();
        final GroupImpl group = new GroupImpl(options, "MyGroup", "This is my group", 0, Integer.MAX_VALUE);

        final CommandLine cmd = new CommandLine();
        cmd.parse(group, Arrays.asList("--group"));

        Assert.assertEquals("This is my group", group.getDescription());
        Assert.assertEquals("", group.getPreferredName());
        Assert.assertEquals(Collections.emptyList(), group.getOptions());
        Assert.assertEquals(Collections.emptyList(), group.getAnonymous());
        Assert.assertEquals(Collections.emptySet(), group.getTriggers());
        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());
        Assert.assertFalse(group.isRequired());
    }
}