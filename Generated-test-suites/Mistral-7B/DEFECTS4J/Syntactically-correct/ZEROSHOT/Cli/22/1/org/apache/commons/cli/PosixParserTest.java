package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for PosixParser
 */
public class PosixParserTest
{
    @Test
    public void testFlattenBasic()
    {
        String[] args = new String[]{"--foo", "--bar=123", "-a", "-b", "-c=def", "-d", "--", "-e", "-f"};

        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("c", true, "c");
        options.addOption("d", true, "d");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(10, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertNull(result[2]); // no value for option 'b'
        assertEquals("-a", result[3]);
        assertEquals("-b", result[4]);
        assertEquals("-c", result[5]);
        assertEquals("def", result[6]);
        assertEquals("--", result[7]);
        assertEquals("-e", result[8]);
        assertEquals("-f", result[9]);
    }

    @Test
    public void testFlattenStopAtNonOption()
    {
        String[] args = new String[]{"--foo", "--bar=123", "-a", "-b", "-c=def", "--", "-e", "-f", "notanoption"};

        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("c", true, "c");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(9, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertNull(result[2]); // no value for option 'b'
        assertEquals("-a", result[3]);
        assertEquals("-b", result[4]);
        assertEquals("-c", result[5]);
        assertEquals("--", result[6]);
        assertEquals("-e", result[7]);
        assertEquals("-f", result[8]);
    }

    @Test
    public void testBurstToken()
    {
        String[] args = new String[]{"--foo=-a--bar=123", "--baz=xyz--qux"};

        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("baz", true, "z");
        options.addOption("qux", true, "x");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(8, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("--bar", result[2]);
        assertEquals("123", result[3]);
        assertEquals("--baz", result[4]);
        assertEquals("xyz", result[5]);
        assertEquals("--qux", result[6]);
        assertNull(result[7]);
    }
}