package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        Options options = new Options();
        options.addOption("a", "arg1", true, "description1");
        options.addOption("b", "arg2", false, "description2");
        options.addOptionGroup(new OptionGroup("group1", "group1"){{
            this.setRequired(false);
            this.addOption("c", "arg3", true, "description3");
            this.addOption("d", "arg4", false, "description4");
        }});
        options.addOption("e", "arg5", false, "description5");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);

        // Test standard output
        String expectedOutput = "usage: Test <arg1> ...\n" +
                "\n"+
                "options:\n" +
                "  -a,--arg1 ARG1           description1\n" +
                "  -b,--arg2               description2\n" +
                "  --group1                 group1\n" +
                "  --group1 -c,--arg3 ARG3  description3 (required)\n" +
                "  --group1 -d,--arg4       description4\n" +
                "  -e,--arg5               description5\n";

        Assert.assertEquals(expectedOutput, System.out.getBuffer().toString());
        System.out.getBuffer().setLength(0);

        // Test printHelp with header and footer
        String header = "My Test App";
        String footer = "Visit www.myapp.com for more info.";
        formatter.printHelp("Test", options, header, footer);

        // Test printHelp with width
        int width = 50;
        formatter.printHelp("Test", options, width);

        // Test printHelp with autoUsage
        formatter.printHelp("Test", options, true);

        // Test printHelp with PrintWriter
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        formatter.printHelp(pw, width, "Test", options);
        String actualOutput = sw.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, width, "Test", options);
    }
}