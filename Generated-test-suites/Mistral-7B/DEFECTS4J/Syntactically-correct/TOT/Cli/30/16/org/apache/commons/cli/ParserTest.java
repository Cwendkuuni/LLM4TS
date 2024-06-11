package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testSimpleParsing() throws ParseException
    {
        // given
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args);

        // then
        Assertions.assertFalse(cmd.hasOption("c"));
        Assertions.assertTrue(cmd.hasOption("a"));
        Assertions.assertTrue(cmd.hasOption("b"));
        Assertions.assertEquals(1, cmd.getOptionValues("a").length);
        Assertions.assertEquals("1", cmd.getOptionValues("a")[0]);
        Assertions.assertEquals(2, cmd.getOptionValues("b").length);
        Assertions.assertEquals("2", cmd.getOptionValues("b")[0]);
        Assertions.assertNull(cmd.getOptionValues("c"));
        Assertions.assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParsingWithOptionsGroup() throws ParseException
    {
        // given
        String[] args = new String[]{"--mygroup", "--optionA", "1", "--optionB", "2"};
        Options options = new Options();
        OptionGroup mygroup = new OptionGroup("mygroup", "My group");
        mygroup.setRequired(true);

        Option optionA = new Option("a", true, "option A");
        optionA.setLongOpt("longOptionA");
        mygroup.addOption(optionA);

        Option optionB = new Option("b", true, "option B");
        optionB.setLongOpt("longOptionB");
        mygroup.addOption(optionB);

        options.addOptionGroup(mygroup);

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args);

        // then
        Assertions.assertEquals(mygroup, cmd.getOptionGroup());
        Assertions.assertTrue(cmd.hasOption("a"));
        Assertions.assertTrue(cmd.hasOption("b"));
        Assertions.assertEquals(1, cmd.getOptionValues("a").length);
        Assertions.assertEquals("1", cmd.getOptionValues("a")[0]);
        Assertions.assertEquals(2, cmd.getOptionValues("b").length);
        Assertions.assertEquals("2", cmd.getOptionValues("b")[0]);
        Assertions.assertNull(cmd.getOptionValues("c"));
        Assertions.assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParsingWithProperties() throws ParseException
    {
        // given
        String[] args = new String[]{"--propertyfile", "/path/to/myfile.properties"};
        Options options = new Options();
        options.addOption("pf", true, "property file");
        options.addGlobalOption(new Option("pf", "propertyfile", false, "load this file for options"));

        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, properties);

        // then
        Assertions.assertTrue(cmd.hasOption("optionA"));
        Assertions.assertTrue(cmd.hasOption("optionB"));
        Assertions.assertEquals(2, cmd.getOptions().size());
        Assertions.assertEquals("valueA", cmd.getOptionValues("optionA")[0]);
        Assertions.assertEquals("valueB", cmd.getOptionValues("optionB")[0]);
        Assertions.assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParsingWithArbitraryArguments() throws ParseException
    {
        // given
        String[] args = new String[]{"--optionA", "1", "--optionB", "2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args);

        // then
        Assertions.assertTrue(cmd.hasOption("a"));
        Assertions.assertTrue(cmd.hasOption("b"));
        Assertions.assertEquals(1, cmd.getOptionValues("a").length);
        Assertions.assertEquals("1", cmd.getOptionValues("a")[0]);
        Assertions.assertEquals(2, cmd.getOptionValues("b").length);
        Assertions.assertEquals("2", cmd.getOptionValues("b")[0]);
        Assertions.assertEquals(2, cmd.getArgs().length);
        Assertions.assertEquals("arg1", cmd.getArgs()[0]);
        Assertions.assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParsingWithStopAtNonOption() throws ParseException
    {
        // given
        String[] args = new String[]{"--optionA", "1", "--optionB", "2", "arg1", "arg2", "--"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, null, true);

        // then
        Assertions.assertTrue(cmd.hasOption("a"));
        Assertions.assertTrue(cmd.hasOption("b"));
        Assertions.assertEquals(1, cmd.getOptionValues("a").length);
        Assertions.assertEquals("1", cmd.getOptionValues("a")[0]);
        Assertions.assertEquals(2, cmd.getOptionValues("b").length);
        Assertions.assertEquals(4, cmd.getArgs().length);
        Assertions.assertEquals("arg1", cmd.getArgs()[0]);
        Assertions.assertEquals("arg2", cmd.getArgs()[1]);
        Assertions.assertEquals("arg2", cmd.getArgs()[2]);
        Assertions.assertEquals("arg2", cmd.getArgs()[3]);
    }

    @Test
    public void testParsingWithUnrecognizedOption() throws ParseException
    {
        // given
        String[] args = new String[]{"--unrecognizedOption", "--optionA", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        Parser parser = new DefaultParser();

        // then
        Assertions.assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParsingWithMissingRequiredOption() throws ParseException
    {
        // given
        String[] args = new String[]{"--optionA", "1"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        Option optionB = new Option("b", true, "option B");
        optionB.setRequired(true);
        options.addOption(optionB);

        Parser parser = new DefaultParser();

        // then
        Assertions.assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParsingWithMissingRequiredOptionGroup() throws ParseException
    {
        // given
        String[] args = new String[]{"--optionA", "1"};
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("mygroup", "My group");
        optionGroup.setRequired(true);
        Option optionA = new Option("a", true, "option A");
        optionA.setLongOpt("longOptionA");
        optionGroup.addOption(optionA);
        options.addOptionGroup(optionGroup);

        Parser parser = new DefaultParser();

        // then
        Assertions.assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParsingWithMissingProperty() throws ParseException
    {
        // given
        String[] args = new String[]{"--propertyfile", "/path/to/myfile.properties"};
        Options options = new Options();
        options.addOption("pf", true, "property file");
        options.addGlobalOption(new Option("pf", "propertyfile", false, "load this file for options"));

        Properties properties = new Properties();

        Parser parser = new DefaultParser();

        // then
        Assertions.assertThrows(MissingPropertyException.class, () -> parser.parse(options, args, properties));
    }
}