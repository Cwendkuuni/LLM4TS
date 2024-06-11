package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test flatten method with valid options.
     */
    @Test
    public void testFlattenWithValidOptions()
    {
        String[] args = {"-a", "--optionB=test", "-c", "--optionC", "--optionD=value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", true, "Option D");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "test", "-c", "--optionD", "value"}, result);
    }

    /**
     * Test flatten method with invalid options and stopAtNonOption=true.
     */
    @Test
    public void testFlattenWithInvalidOptionsAndStopAtNonOption()
    {
        String[] args = {"--invalidOption", "-a", "--optionB=test", "-c", "--invalidOption2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--invalidOption", "--invalidOption2"}, result);
    }

    /**
     * Test flatten method with invalid options and stopAtNonOption=false.
     */
    @Test
    public void testFlattenWithInvalidOptionsAndStopAtNonOptionFalse()
    {
        String[] args = {"--invalidOption", "-a", "--optionB=test", "-c", "--invalidOption2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--invalidOption", "-a", "--optionB", "test", "--invalidOption2", "-c"}, result);
    }

    /**
     * Test flatten method with an empty array.
     */
    @Test
    public void testFlattenWithEmptyArray()
    {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test flatten method with an empty Options object.
     */
    @Test
    public void testFlattenWithEmptyOptions()
    {
        String[] args = {"--foo", "-a", "--optionB=test", "-c", "--optionD=value"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test HelpFormatter with valid options.
     */
    @Test
    public void testHelpFormatterWithValidOptions()
    {
        String[] args = {"-a", "--optionB=test", "-c", "--optionD=value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", true, "Option D");

        Parser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", "Test program", options, parsedArgs, true);
    }

    /**
     * Test HelpFormatter with invalid options and stopAtNonOption=true.
     */
    @Test
    public void testHelpFormatterWithInvalidOptionsAndStopAtNonOption()
    {
        String[] args = {"--invalidOption", "-a", "--optionB=test", "-c", "--invalidOption2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", "Test program", options, parsedArgs, true);
    }

    /**
     * Test HelpFormatter with invalid options and stopAtNonOption=false.
     */
    @Test
    public void testHelpFormatterWithInvalidOptionsAndStopAtNonOptionFalse()
    {
        String[] args = {"--invalidOption", "-a", "--optionB=test", "-c", "--invalidOption2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", "Test program", options, parsedArgs, true);
    }
}