package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser class
 */
public class ParserTest {

    @org.junit.Test
    public void testParser() {
        // Given
        String[] args = {"-a", "--b", "1", "--c=3", "--d=value", "--e", "arg"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");
        options.addOption("d", true, "Option D with argument");
        options.addOption("e", false, "Option E");

        CommandLineParser parser = new DefaultParser();

        // When parsing with no properties
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then
        assert cmd != null;
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }

        // And options
        assertEquals(5, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));

        // And options values
        assertEquals(true, cmd.getOptionValue("a"));
        assertEquals(true, cmd.getOptionValue("b"));
        assertEquals("1", cmd.getOptionValue("c"));
        assertEquals("value", cmd.getOptionValue("d"));
        assertNull(cmd.getOptionValue("e"));

        // Given
        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b", "valueB");

        // When parsing with properties
        try {
            cmd = parser.parse(options, args, properties, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }

        // And options
        assertEquals(5, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));

        // And options values
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("1", cmd.getOptionValue("c"));
        assertEquals("value", cmd.getOptionValue("d"));
        assertNull(cmd.getOptionValue("e"));

        // Given
        options.addRequiredOption("f", false, null, "valueF");

        // When parsing with required option missing
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            // expected
        }

        // Then
        assertThrows(MissingOptionException.class, () -> cmd.getOptionValue("f"));

        // Given
        args = Arrays.copyOfRange(args, 0, args.length - 1);

        // When parsing with required option present
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }

        // And options
        assertEquals(6, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("f"));

        // And options values
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("1", cmd.getOptionValue("c"));
        assertEquals("value", cmd.getOptionValue("d"));
        assertNull(cmd.getOptionValue("e"));
        assertEquals("valueF", cmd.getOptionValue("f"));

        // Given
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .required(false)
                .addOption("g1", false, null, "valueG1")
                .addOption("g2", false, null, "valueG2"));

        // When parsing with option group selected
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }

        // And options
        assertEquals(6, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("f"));

        // And option group
        OptionGroup group = cmd.getOptionGroup("group1");
        assertNull(group);

        // When parsing with option group not selected
        options.getOptionGroup("group1").setSelected(null);

        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }

        // And options
        assertEquals(5, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));

        // And no option group
        assertFalse(cmd.hasOption("g1"));
        assertFalse(cmd.hasOption("g2"));

        // Given
        args = Arrays.copyOfRange(args, 0, args.length - 1);
        args = Arrays.asList(args).subList(0, 3).toArray(new String[0]);

        // When parsing with non-option argument
        try {
            parser.parse(options, args, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then
        assertEquals(3, cmd.getArgs().size());
        assertEquals(args[0], cmd.getArgs().get(0));
        assertEquals(args[1], cmd.getArgs().get(1));
        assertEquals(args[2], cmd.getArgs().get(2));

        // And no options
        assertEquals(0, cmd.getOptions().size());
    }
}