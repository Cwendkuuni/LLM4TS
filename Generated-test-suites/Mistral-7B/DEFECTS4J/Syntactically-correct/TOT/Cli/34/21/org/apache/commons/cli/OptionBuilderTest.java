package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testOptionWithSingleRequiredArgument()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("inputFile")
                .isRequired()
                .hasArg()
                .withArgName("file");
        Option option = optionBuilder.create('i');
        assertNotNull(option);
        assertEquals("inputFile", option.getLongOpt());
        assertEquals("Specify input file.", option.getDescription());
        assertFalse(option.isValueSeparatorSet());
        assertFalse(option.isUnboundedValued());
        assertEquals(1, option.getMaxArgValues());
        assertFalse(option.isAllowedMultipleSelect());
        assertFalse(option.isAllowedMultipleValues());
        assertFalse(option.isRequiredArg());
        assertFalse(option.isValueOptional());
        assertNull(option.getValue());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(new String[]{"-i", "test.txt"});
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        assertEquals("test.txt", option.getValue());

        try
        {
            cmd = parser.parse(new String[]{"-i"});
            fail("Expected ParseException to be thrown.");
        }
        catch (ParseException e)
        {
            // Expected
        }

        try
        {
            cmd = parser.parse(new String[]{"-i", "test.txt", "test2.txt"});
            fail("Expected ParseException to be thrown.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testOptionWithOptionalSingleArgument()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("outputFile")
                .hasArg()
                .withArgName("file")
                .isOptionalArg();
        Option option = optionBuilder.create('o');
        assertNotNull(option);
        assertEquals("outputFile", option.getLongOpt());
        assertEquals("Specify output file.", option.getDescription());
        assertFalse(option.isValueSeparatorSet());
        assertFalse(option.isUnboundedValued());
        assertEquals(1, option.getMaxArgValues());
        assertFalse(option.isAllowedMultipleSelect());
        assertTrue(option.isValueOptional());
        assertNull(option.getValue());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(new String[]{"-o", "test.txt"});
            assertEquals("test.txt", option.getValue());
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-o"});
            assertNull(option.getValue());
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-o", "test.txt", "test2.txt"});
            fail("Expected ParseException to be thrown.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testOptionWithMultipleOptionalArguments()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("outputFiles")
                .isOptionalArg()
                .hasArgs();
        Option option = optionBuilder.create('o');
        assertNotNull(option);
        assertEquals("outputFiles", option.getLongOpt());
        assertEquals("Specify output files.", option.getDescription());
        assertFalse(option.isValueSeparatorSet());
        assertTrue(option.isUnboundedValued());
        assertEquals(-1, option.getMaxArgValues());
        assertFalse(option.isAllowedMultipleSelect());
        assertTrue(option.isValueOptional());
        assertNull(option.getValue());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(new String[]{"-o", "test1.txt", "test2.txt"});
            String[] outputFiles = option.getValues();
            assertEquals(2, outputFiles.length);
            assertEquals("test1.txt", outputFiles[0]);
            assertEquals("test2.txt", outputFiles[1]);
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-o", "test1.txt"});
            String[] outputFiles = option.getValues();
            assertEquals(1, outputFiles.length);
            assertEquals("test1.txt", outputFiles[0]);
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-o"});
            assertNotNull(option.getValues());
            assertEquals(0, option.getValues().length);
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }
    }

    @Test
    public void testOptionWithCustomValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("separatorOption")
                .hasArg()
                .withArgName("separator")
                .isOptionalArg()
                .withValueSeparator(',');
        Option option = optionBuilder.create('s');
        assertNotNull(option);
        assertEquals("separatorOption", option.getLongOpt());
        assertEquals("Specify separator.", option.getDescription());
        assertTrue(option.isValueOptional());
        assertTrue(option.isValueSeparatorSet());
        assertEquals(',', option.getValueSeparator());
        assertNull(option.getValue());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(new String[]{"-s", "comma"});
            assertEquals("comma", option.getValue());
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-s"});
            assertNull(option.getValue());
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-s", "comma,separator"});
            fail("Expected ParseException to be thrown.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testOptionWithCustomArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("customArgNameOption")
                .hasArg()
                .withArgName("customArgName")
                .isOptionalArg();
        Option option = optionBuilder.create('c');
        assertNotNull(option);
        assertEquals("customArgNameOption", option.getLongOpt());
        assertEquals("Specify custom argument name.", option.getDescription());
        assertFalse(option.isValueSeparatorSet());
        assertFalse(option.isUnboundedValued());
        assertEquals(1, option.getMaxArgValues());
        assertFalse(option.isAllowedMultipleSelect());
        assertTrue(option.isValueOptional());
        assertNull(option.getValue());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(new String[]{"-c", "testCustomArgName"});
            assertEquals("testCustomArgName", option.getValue());
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-c"});
            assertNull(option.getValue());
        }
        catch (ParseException e)
        {
            fail("Failed to parse command line.");
        }

        try
        {
            cmd = parser.parse(new String[]{"-c", "testCustomArgName1,testCustomArgName2"});
            fail("Expected ParseException to be thrown.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }
}