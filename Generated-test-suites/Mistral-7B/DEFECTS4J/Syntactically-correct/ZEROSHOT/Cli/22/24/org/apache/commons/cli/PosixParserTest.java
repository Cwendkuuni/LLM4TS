package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class PosixParserTest
{
    @Test
    public void testFlattenWithEmptyArguments()
    {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertNull(result);
    }

    @Test
    public void testFlattenWithSingleArgument()
    {
        Options options = new Options();
        String[] arguments = {"arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenWithSingleOption()
    {
        Options options = new Options().addOption("o", true, "Option");
        String[] arguments = {"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"o"}, result);
    }

    @Test
    public void testFlattenWithSingleValueOption()
    {
        Options options = new Options().addOption("o", true, "Option");
        String[] arguments = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"o", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOption()
    {
        Options options = new Options().addOption("long-option", true, "Long Option");
        String[] arguments = {"--long-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"long-option"}, result);
    }

    @Test
    public void testFlattenWithLongValueOption()
    {
        Options options = new Options().addOption("long-option", true, "Long Option");
        String[] arguments = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"long-option", "value"}, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen()
    {
        Options options = new Options();
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenWithMultipleArguments()
    {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    @Test
    public void testFlattenWithSingleOptionAndValueAndArguments()
    {
        Options options = new Options().addOption("o", true, "Option");
        String[] arguments = {"-o", "value", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"o", "value", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions()
    {
        Options options = new Options()
                .addOption("o1", true, "Option 1")
                .addOption("o2", true, "Option 2")
                .addOption("o3", true, "Option 3");
        String[] arguments = {"-o1", "-o2", "-o3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"o1", "o2", "o3"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptionsAndValues()
    {
        Options options = new Options()
                .addOption("o1", true, "Option 1")
                .addOption("o2", true, "Option 2")
                .addOption("o3", true, "Option 3");
        String[] arguments = {"-o1", "value1", "-o2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"o1", "value1", "o2", "value2"}, result);
    }

    @Test
    public void testFlattenWithMultiArgOption()
    {
        Options options = new Options()
                .addOption("a", true, "Option A")
                .addOption("a", true, "Option A", 3);
        String[] arguments = {"-a", "arg1", "-a", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"a", "arg1", "a", Arrays.asList(new String[]{"arg2", "arg3"}).toArray(new String[0])}, result);
    }

    @Test
    public void testFlattenWithMultiArgOptionAndValue()
    {
        Options options = new Options()
                .addOption("a", true, "Option A")
                .addOption("a", true, "Option A", 3);
        String[] arguments = {"-a", "arg1", "-a", "arg2,arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"a", "arg1", "a", Arrays.asList(new String[]{"arg2,arg3"}).toArray(new String[0])}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption()
    {
        Options options = new Options();
        String[] arguments = {"arg1", "--nonOption", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"arg1", "--nonOption", "arg3"}, result);
    }
}