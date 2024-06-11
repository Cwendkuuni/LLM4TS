package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionComparator;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWidthNegative() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(-5);
    }

    @Test
    public void testPrintHelp() throws IOException {
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null, false);
        assertEquals("usage: java MyApp\n", System.out.getBuffer().toString());
        System.out.getBuffer().setLength(0);

        cmdLineSyntax = "java MyApp [--option1 arg1] [--option2 arg2]";
        options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("u", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        formatter.printHelp(System.out, 80, cmdLineSyntax, options, null, false);
        assertEquals("usage: java MyApp [--option1 arg1] [--option2 arg2]\n" +
                "options:\n" +
                "  -o, --option1  Option 1\n" +
                "  -u, --option2\n", System.out.getBuffer().toString());
        System.out.getBuffer().setLength(0);
    }

    @Test
    public void testPrintUsage() throws IOException {
        String cmdLineSyntax = "java MyApp [--option1 arg1] [--option2 arg2]";
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("u", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(System.out, 80, cmdLineSyntax, options);
        String expectedUsage = "java MyApp\n" +
                "options:\n" +
                "  -o, --option1 <arg1>\n" +
                "  -u, --option2\n";
        assertEquals(expectedUsage, System.out.getBuffer().toString());
        System.out.getBuffer().setLength(0);
    }
}