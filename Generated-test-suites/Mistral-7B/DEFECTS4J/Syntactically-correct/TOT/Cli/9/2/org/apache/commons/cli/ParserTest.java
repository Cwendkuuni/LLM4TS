package org.apache.commons.cli.test;

import org.apache.commons.cli.CmdLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseSimple() throws Exception {
        String[] args = new String[]{"app", "--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "o", "op");

        CmdLineParser parser = new DefaultParser();
        parser.parse(options, args, true);

        CommandLine cmd = parser.getCommandLine();
        Assert.assertTrue(cmd.hasOption("option"));
        Assert.assertEquals("value", cmd.getOptionValue("option"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("app", cmd.getArgs()[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws Exception {
        String[] args = new String[]{"app", "--unknown"};
        Options options = new Options();
        options.addOption("option", true, "o", "op");

        CmdLineParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        String[] args = new String[]{"app", "--option"};
        Options options = new Options();
        options.addOption("option", true, "o", "op")
                .requiredOption()
                .longOpt("long-option");

        CmdLineParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] args = new String[]{"app", "--unknown"};
        Options options = new Options();
        options.addOption("option", true, "o", "op");

        CmdLineParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testParseWithMultipleOptions() throws Exception {
        String[] args = new String[]{"app", "--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1", "op1");
        options.addOption("option2", true, "o2", "op2");

        CmdLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"app", "--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addOptions(Arrays.asList(
                new Option("property", true, null, "p")
                        .withLongOpt("long-property")
                        .withValuesVariableName("pv")
        ));

        CmdLineParser parser = new DefaultParser();
        parser.parse(options, args, properties, true);

        CommandLine cmd = parser.getCommandLine();
        Assert.assertTrue(cmd.hasOption("property"));
        List<Object> propertyValues = cmd.getOptionValuesForName("property");
        Assert.assertEquals(2, propertyValues.size());
        Assert.assertEquals("value1", propertyValues.get(0));
        Assert.assertEquals("value2", propertyValues.get(1));
    }

    @Test
    public void testParseWithOptionalOption() throws Exception {
        String[] args = new String[]{"app", "--option"};
        Options options = new Options()
                .addOption("option", true, "o", "op")
                .optionalOption();

        CmdLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertFalse(cmd.hasOption("option"));
        Assert.assertNull(cmd.getOptionValue("option"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("app", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithOptionalGroup() throws Exception {
        String[] args = new String[]{"app", "--group", "value"};
        Options options = new Options()
                .addOptionGroup(new OptionGroup("group")
                        .required(false)
                        .addOption("option", true, "o", "op"))
                .addOption("another", true, "a", "aop");

        CmdLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertFalse(cmd.hasOption("option"));
        Assert.assertNull(cmd.getOptionValue("option"));
        Assert.assertTrue(cmd.hasOption("another"));
        Assert.assertEquals("value", cmd.getOptionValue("group.option"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("app", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithValidAndInvalidOptions() throws Exception {
        String[] args = new String[]{"app", "--option1", "--option2", "--unknown"};
        Options options = new Options()
                .addOption("option1", true, "o1", "op1")
                .addOption("option2", true, "o2", "op2")
                .addOption("unknown", true, "u", "up");

        CmdLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertEquals("", cmd.getOptionValue("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals("", cmd.getOptionValue("option2"));
        Assert.assertFalse(cmd.hasOption("unknown"));
        Assert.assertNull(cmd.getOptionValue("unknown"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("app", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithInvalidOptionOrder() throws Exception {
        String[] args = new String[]{"app", "--option2", "--option1"};
        Options options = new Options()
                .addOption("option1", true, "o1", "op1")
                .addOption("option2", true, "o2", "op2");

        CmdLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertEquals("", cmd.getOptionValue("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals("", cmd.getOptionValue("option2"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("app", cmd.getArgs()[0]);
    }

    @Test
    public void testHelpFormat() throws Exception {
        String[] args = new String[]{"--help"};
        Options options = new Options()
                .addOption("option1", true, "o1", "op1")
                .addOption("option2", true, "o2", "op2")
                .addOptionGroup(new OptionGroup("group")
                        .required(false)
                        .addOption("option", true, "o", "op")
                );

        CmdLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        parser.parse(options, args, true, formatter, "App", true);

        String help = formatter.printHelp("App", Arrays.asList(args), options, true);
        Assert.assertNotNull(help);
        Assert.assertTrue(help.contains("option1"));
        Assert.assertTrue(help.contains("--option1"));
        Assert.assertTrue(help.contains("[value]"));
        Assert.assertTrue(help.contains("option2"));
        Assert.assertTrue(help.contains("--option2"));
        Assert.assertTrue(help.contains("[value]"));
        Assert.assertTrue(help.contains("group"));
        Assert.assertTrue(help.contains("--group"));
        Assert.assertTrue(help.contains("option"));
        Assert.assertTrue(help.contains("--option"));
        Assert.assertTrue(help.contains("[value]"));
    }
}