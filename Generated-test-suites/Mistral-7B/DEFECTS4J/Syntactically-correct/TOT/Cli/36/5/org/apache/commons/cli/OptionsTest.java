package org.apache.commons.cli.test;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testBasicUsage() {
        Options options = new Options();
        options.addOption("a", "alert", false, "Display an alert message");
        options.addOption("b", "backup", true, "Back up files");

        BasicParser parser = new BasicParser();
        try {
            parser.parse(new String[]{"-a"}, options);
            assertTrue(options.isSetOption("a"));
            assertFalse(options.isSetOption("b"));
        } catch (ParseException e) {
            fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test
    public void testLongOptionUsage() {
        Options options = new Options();
        options.addOption("alert", "a", false, "Display an alert message");
        options.addOption("backup", "b", true, "Back up files");

        BasicParser parser = new BasicParser();
        try {
            parser.parse(new String[]{"--alert"}, options);
            assertTrue(options.isSetOption("alert"));
            assertFalse(options.isSetOption("backup"));
        } catch (ParseException e) {
            fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test
    public void testShortOptionWithArgUsage() {
        Options options = new Options();
        options.addOption("a", "alert,m MESSAGE", false, "Display an alert message MESSAGE");

        BasicParser parser = new BasicParser();
        try {
            parser.parse(new String[]{"-a", "Test"}, options);
            assertEquals("Test", options.getOptionValue("m"));
            assertTrue(options.isSetOption("a"));
        } catch (ParseException e) {
            fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "Display an alert message");

        BasicParser parser = new BasicParser();
        parser.parse(new String[]{}, options);
        fail("Expected a ParseException but no error was thrown.");
    }

    @Test
    public void testMixedUsage() {
        Options options = new Options();
        options.addOption("a", "alert,m MESSAGE", false, "Display an alert message MESSAGE");
        options.addOption("b", "backup,f FILE", true, "Back up files FILE");

        BasicParser parser = new BasicParser();
        try {
            parser.parse(new String[]{"-a", "Test", "--backup", "/path/to/file"}, options);
            assertEquals("Test", options.getOptionValue("m"));
            assertTrue(options.isSetOption("a"));
            assertTrue(options.isSetOption("backup"));
            assertEquals("/path/to/file", options.getOptionValue("f"));
        } catch (ParseException e) {
            fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test
    public void testOptionGroupUsage() {
        OptionGroup group = new OptionGroup("network", "Network options:");
        group.addOption("h", "host", true, "connect to host");
        group.addOption("p", "port", true, "connect to port");

        Options options = new Options();
        options.addOption("h", "help", false, "print this help message");
        options.addOption("i", "inputfile", true, "input file name");
        options.addOptionGroup(group);

        BasicParser parser = new BasicParser();
        try {
            parser.parse(new String[]{"--help", "--h", "--network", "--host", "localhost", "--port", "80"}, options);
            assertFalse(options.isSetOption("h"));
            assertTrue(options.isSetOption("help"));
            assertTrue(options.isSetOption("network"));
            assertFalse(options.isSetOption("group"));
            assertTrue(options.isSetOption("host"));
            assertEquals("localhost", options.getOptionValue("host"));
            assertTrue(options.isSetOption("port"));
            assertEquals("80", options.getOptionValue("port"));
            assertTrue(options.isSetOption("inputfile"));
            assertNull(options.getOptionValue("i"));
        } catch (ParseException e) {
            fail("Parsing failed: " + e.getMessage());
        }
    }
}