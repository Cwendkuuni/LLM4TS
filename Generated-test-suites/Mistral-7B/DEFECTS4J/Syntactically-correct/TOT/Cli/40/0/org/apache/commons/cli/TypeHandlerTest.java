package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testStringValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("stringOption", true, "String Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--stringOption=test"});

        assertEquals("test", commandLine.getOptionValue("stringOption"));
    }

    @Test
    public void testObjectValue() throws ParseException, ClassNotFoundException, InstantiationException
    {
        Options options = new Options();
        options.addOption("objectOption", true, "Object Option");
        options.getOption("objectOption").setType(String.class);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--objectOption=java.lang.String@1"});

        Class<?> clazz = Class.forName("java.lang.String");
        Object expected = "Java String";
        Object actual = commandLine.getOptionValue("objectOption");

        assertEquals(expected, actual);
        assertEquals(clazz, actual.getClass());
    }

    @Test
    public void testNumberValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("numberOption", true, "Number Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--numberOption=12345"});

        Number expected = Long.valueOf(12345);
        Number actual = commandLine.getNumber("numberOption");

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testNumberValueInvalid() throws ParseException
    {
        Options options = new Options();
        options.addOption("numberOption", true, "Number Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--numberOption=invalid"});

        commandLine.getNumber("numberOption");
    }

    @Test
    public void testDateValue() throws ParseException, UnsupportedOperationException
    {
        Options options = new Options();
        options.addOption("dateOption", true, "Date Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--dateOption=2022-12-31"});

        Date expected = new Date(2022 - 1900, 11, 31);
        Date actual = commandLine.getDate("dateOption");

        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDateValueUnsupported() throws ParseException
    {
        Options options = new Options();
        options.addOption("dateOption", true, "Date Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{});

        commandLine.getDate("dateOption");
    }

    @Test
    public void testClassValue() throws ParseException, ClassNotFoundException, InstantiationException
    {
        Options options = new Options();
        options.addOption("classOption", true, "Class Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--classOption=java.lang.String"});

        Class<?> expected = Class.forName("java.lang.String");
        Class<?> actual = Class.forName((String) commandLine.getOptionValue("classOption"));

        assertEquals(expected, actual);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassValueInvalid() throws ParseException, ClassNotFoundException
    {
        Options options = new Options();
        options.addOption("classOption", true, "Class Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--classOption=InvalidClass"});

        commandLine.getOptionValue("classOption");
    }

    @Test
    public void testFileValue() throws ParseException, IOException
    {
        Options options = new Options();
        options.addOption("fileOption", true, "File Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--fileOption=path/to/file.txt"});

        File expected = new File("path/to/file.txt");
        File actual = new File((String) commandLine.getOptionValue("fileOption"));

        assertEquals(expected, actual);
        assertEquals(expected.exists(), actual.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileValueNotFound() throws ParseException, FileNotFoundException
    {
        Options options = new Options();
        options.addOption("fileOption", true, "File Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--fileOption=nonExistentFile.txt"});

        commandLine.getOptionValue("fileOption");
    }

    @Test
    public void testExistingFileValue() throws ParseException, IOException
    {
        Options options = new Options();
        options.addOption("existingFileOption", true, "Existing File Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--existingFileOption=path/to/existingfile.txt"});

        File expected = new File("path/to/existingfile.txt");
        File actual = new File((String) commandLine.getOptionValue("existingFileOption"));

        assertEquals(expected, actual);
        assertTrue(actual.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testExistingFileValueNotFound() throws ParseException, FileNotFoundException
    {
        Options options = new Options();
        options.addOption("existingFileOption", true, "Existing File Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--existingFileOption=nonExistingFile.txt"});

        commandLine.getOptionValue("existingFileOption");
    }

    @Test
    public void testFilesValue() throws ParseException, IOException
    {
        Options options = new Options();
        options.addOption("filesOption", true, "Files Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--filesOption=path/to/file1.txt,path/to/file2.txt"});

        String[] expected = new String[]{"path/to/file1.txt", "path/to/file2.txt"};
        File[] actual = (File[]) commandLine.getValues("filesOption");

        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilesValueInvalid() throws ParseException
    {
        Options options = new Options();
        options.addOption("filesOption", true, "Files Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--filesOption=path/to/file.txt,invalid"});

        commandLine.getValues("filesOption");
    }

    @Test
    public void testUrlValue() throws ParseException, MalformedURLException
    {
        Options options = new Options();
        options.addOption("urlOption", true, "URL Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--urlOption=http://example.com"});

        URL expected = new URL("http://example.com");
        URL actual = new URL((String) commandLine.getOptionValue("urlOption"));

        assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testUrlValueInvalid() throws ParseException, MalformedURLException
    {
        Options options = new Options();
        options.addOption("urlOption", true, "URL Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--urlOption=invalid"});

        commandLine.getOptionValue("urlOption");
    }
}