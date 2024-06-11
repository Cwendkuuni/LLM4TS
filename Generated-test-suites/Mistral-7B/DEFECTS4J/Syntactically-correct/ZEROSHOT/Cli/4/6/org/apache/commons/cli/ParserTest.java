package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;

public class ParserTest {

    @org.junit.Test
    public void testParser() throws Exception {
        String[] args = new String[]{"-a", "1", "--b=2", "--c", "3", "-d"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                    boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLineParser clp = parser;

        Options options = new Options();
        options.addOption("a", true, "aOption");
        options.addOption("b", true, "bOption");
        options.addOption("c", false, "cOption");
        options.addOption("d", false, "dOption");

        CommandLine cmd = clp.parse(options, args, null);

        assertEquals(2, cmd.getNumArgs());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("nonExistentOption"));
        assertEquals(false, cmd.hasOption("c"));
        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals(false, cmd.hasOption("nonExistentOption"));

        // Check that required options are present
        options.addRequiredOption("rOption");
        assertThrows(MissingOptionException.class, () -> {
            clp.parse(options, args, null);
        });

        // Check that an unknown option throws UnrecognizedOptionException
        options.addOption("unknownOption", false, "unknownOption");
        assertThrows(UnrecognizedOptionException.class, () -> {
            clp.parse(options, args, null);
        });

        // Check that an option with no value is parsed correctly
        options.getOption("cOption").setRequired(false);

        args = new String[]{"--c"};
        cmd = clp.parse(options, args, null);

        assertEquals(0, cmd.getNumArgs());
        assertEquals(true, cmd.hasOption("c"));

        // Check that an option with multiple values is parsed correctly
        options.getOption("bOption").setArgs(1);

        args = new String[]{"--b", "value1", "--b", "value2"};
        cmd = clp.parse(options, args, null);

        assertEquals(1, cmd.getNumArgs());
        List<String> values = cmd.getOptionValues("bOption");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
}