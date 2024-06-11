package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class HelpFormatterTest {

    @Test
    public void testDefaultHelp() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter -h";
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        // Add some assertions here to check the output of printHelp()
        // for example, check the length of the output, or specific substrings

        String expectedHelp = "usage: java org.apache.commons.cli.HelpFormatter [-h] [options]\n\noptions:\n";
        Assertions.assertEquals(expectedHelp, System.out.getCounter().toString());
    }

    @Test
    public void testCustomHelp() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter --help";
        String header = "Custom Help Message";
        String footer = "For more information, visit http://www.example.com";
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        formatter.setLeftPadding(10);
        formatter.setDescPadding(10);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("arg");
        formatter.setOptionComparator(null); // disable sorting of options
        formatter.printHelp(cmdLineSyntax, header, options, footer);

        // Add some assertions here to check the output of printHelp()
        // with custom settings
    }

    @Test
    public void testPrintUsage() {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter --help";
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOptionGroup(new OptionGroup("input-output", "Input/Output Options") {{
            this.setDescription("Specify input and output files.");
            this.addOption("i", "input", true, "Input file.");
            this.addOption("o", "output", true, "Output file.");
            this.addRequiredOption("i", "input", true, "An input file is required.");
            this.addRequiredOption("o", "output", true, "An output file is required.");
        }});
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(System.out, 120, cmdLineSyntax, options);

        // Add some assertions here to check the output of printUsage()
        // for example, check the order of options and option groups
    }

    @Test
    public void testPrintUsageWithArgument() throws ParseException {
        String cmdLineSyntax = "java org.apache.commons.cli.HelpFormatter input output";
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
        options.addOptionGroup(new OptionGroup("input-output", "Input/Output Options") {{
            this.setDescription("Specify input and output files.");
            this.addOption("i", "input", true, "Input file.");
            this.addOption("o", "output", true, "Output file.");
            this.addRequiredOption("i", "input", true, "An input file is required.");
            this.addRequiredOption("o", "output", true, "An output file is required.");
        }});
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"input.txt", "output.txt"});
        formatter.printUsage(System.out, 120, cmdLineSyntax, options, cmd.getArgs());

        // Add some assertions here to check the output of printUsage()
        // with provided arguments
    }
}