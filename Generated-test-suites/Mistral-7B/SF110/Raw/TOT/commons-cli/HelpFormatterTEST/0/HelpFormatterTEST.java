package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class HelpFormatterTest {

    @Test
    public void testDefaultHelp() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter -h";
        Options options = new Options();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelp = "usage: java org.apache.commons.cli.HelpFormatter [-h] [options] ...\n" +
                "\noptions:\n" +
                "  -h,--help             print this help message and exit\n" +
                "  --width <int>          specify the output width of the help message (default: 74)\n" +
                "  --left-padding <int>   specify the left padding of the help message (default: 1)\n" +
                "  --desc-padding <int>   specify the padding between the option description and the next option (default: 3)\n" +
                "  --syntax-prefix <str>  specify the prefix for the syntax information\n" +
                "  --opt-prefix <str>     specify the prefix for the option letters\n" +
                "  --long-opt-prefix <str> specify the prefix for the long option names\n" +
                "  --arg-name <str>       specify the argument name for options that require an argument\n" +
                "\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testWidthOption() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter -w 50 -h";
        Options options = new Options();
        options.addOption("w", "width", true, "specify the output width of the help message");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelp = "usage: java org.apache.commons.cli.HelpFormatter [-w <int>] [-h] [options] ...\n" +
                "\noptions:\n" +
                "  -h,--help             print this help message and exit\n" +
                "  -w,--width <int>       specify the output width of the help message (default: 50)\n" +
                "\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintUsage() throws ParseException {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter myApp";
        Options options = new Options();
        options.addOption(null, "input", true, "input file");
        options.addOption(null, "output", true, "output file");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);

        String expectedHelp = "usage: java org.apache.commons.cli.HelpFormatter myApp [options] ...\n" +
                "\noptions:\n" +
                "  -input <file>           input file (required)\n" +
                "  -output <file>          output file (required)\n" +
                "\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithHeader() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter myApp -h";
        String header = "My App - Command Line User Interface";
        Options options = new Options();

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(74);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setLeftPadding(1);
        formatter.setDescPadding(3);
        formatter.printHelp(cmdLineSyntax, header, options);

        String expectedHelp = "My App - Command Line User Interface\n" +
                "=============================================\n" +
                "\nusage: java org.apache.commons.cli.HelpFormatter myApp [-h] [options] ...\n" +
                "\noptions:\n" +
                "  -h,--help             print this help message and exit\n" +
                "\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithFooter() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter myApp -h";
        String header = "My App - Command Line User Interface";
        String footer = "Visit our website for more information.";
        Options options = new Options();

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(74);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setLeftPadding(1);
        formatter.setDescPadding(3);
        formatter.printHelp(cmdLineSyntax, header, options, footer);

        String expectedHelp = "My App - Command Line User Interface\n" +
                "=============================================\n" +
                "\nusage: java org.apache.commons.cli.HelpFormatter myApp [-h] [options] ...\n" +
                "\noptions:\n" +
                "  -h,--help             print this help message and exit\n" +
                "\nVisit our website for more information.\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter myApp";
        Options options = new Options();
        options.addOption(null, "input", true, "input file");
        options.addOption(null, "output", true, "output file");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options, true);

        String expectedHelp = "usage: java org.apache.commons.cli.HelpFormatter myApp [options] ...\n" +
                "\noptions:\n" +
                "  -input <file>           input file (required)\n" +
                "  -output <file>          output file (required)\n" +
                "\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithCustomOptionComparator() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter myApp";
        Options options = new Options();
        options.addOption("z", "zoption", false, "z option");
        options.addOption("x", "xoption", false, "x option");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new HelpFormatter.OptionComparator() {
            @Override
            public int compare(Object o1, Object o2) {
                final Option opt1 = (Option)o1;
                final Option opt2 = (Option)o2;
                return opt2.getKey().compareToIgnoreCase(opt1.getKey());
            }
        });
        formatter.printHelp(cmdLineSyntax, options);

        String expectedHelp = "usage: java org.apache.commons.cli.HelpFormatter myApp [options] ...\n" +
                "\noptions:\n" +
                "  -x,--xoption             x option\n" +
                "  -z,--zoption             z option\n" +
                "\n";

        Assert.assertEquals(expectedHelp, System.out.getBuffer().toString());
    }
}