package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getDefaultWidth());
        assertEquals(1, formatter.getDefaultLeftPad());
        assertEquals(3, formatter.getDefaultDescPad());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getDefaultOptPrefix());
        assertEquals("--", formatter.getDefaultLongOptPrefix());
        assertEquals("arg", formatter.getDefaultArgName());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSetters() {
        HelpFormatter formatter = new HelpFormatter();

        formatter.setWidth(50);
        assertEquals(50, formatter.getDefaultWidth());

        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getDefaultLeftPad());

        formatter.setDescPadding(10);
        assertEquals(10, formatter.getDefaultDescPad());

        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());

        formatter.setNewLine("---------------------------------------------------\n");
        assertEquals("---------------------------------------------------\n", formatter.getNewLine());

        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getDefaultOptPrefix());

        formatter.setLongOptPrefix("#");
        assertEquals("#", formatter.getDefaultLongOptPrefix());

        formatter.setArgName("param");
        assertEquals("param", formatter.getDefaultArgName());

        formatter.setOptionComparator(null);
        assertNull(formatter.getOptionComparator());

        formatter.setOptionComparator(new HelpFormatter.OptionComparator());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpBasic() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp -a arg1 -b arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpNullCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = null;
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp -a arg1 -b arg2";
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String header = "Options:";
        String footer = "Help for MyApp";
        formatter.printHelp(cmdLineSyntax, header, options, footer);
    }

    @Test
    public void testPrintUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyApp";
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        optionA.setArgName("arg");
        optionA.setDescription("Option A description");
        options.addOption(optionA);
        Option optionB = new Option("b", true, "Option B");
        optionB.setArgName("arg");
        optionB.setDescription("Option B description");
        options.addOption(optionB);
        formatter.printUsage(System.out, 80, cmdLineSyntax, options);
    }

    @Test
    public void testPrintWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed auctor, magna at commodo auctor, quam ante bibendum arcu, a aliquam leo lorem eu odio. Vivamus malesuada, mi non ullamcorper iaculis, mi turpis volutpat mauris, ut commodo nibh nibh at ipsum.";
        int width = 50;
        int nextLineTabStop = 10;
        String wrappedText = formatter.renderWrappedText(new StringBuffer(), width, nextLineTabStop, text).toString();
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n" +
                "  Sed auctor, magna at commodo auctor,\n" +
                "  quam ante bibendum arcu,\n" +
                "  a aliquam leo lorem eu odio.\n" +
                "Vivamus malesuada, mi non ullamcorper iaculis,\n" +
                "  mi turpis volutpat mauris,\n" +
                "  ut commodo nibh nibh at ipsum.", wrappedText);
    }
}