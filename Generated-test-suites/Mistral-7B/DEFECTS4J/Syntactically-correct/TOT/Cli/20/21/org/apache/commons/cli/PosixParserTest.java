package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    private static final String[] ARGS_1 = {"-a", "--optionB=valueB", "--optionC", "--optionD=valueD"};
    private static final String[] ARGS_2 = {"--optionA", "-b", "valueC", "--optionE"};
    private static final String[] ARGS_3 = {"--foo", "--bar=qux", "--baz=quux"};
    private static final String[] ARGS_4 = {"--invalidOption"};
    private static final String[] ARGS_5 = {"--", "invalidArg"};

    @Test
    public void testFlatten_Arguments1() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B with value");
        options.addOption("c", false, "Option C");
        options.addOption("d", true, "Option D with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, ARGS_1, false);

        assertArrayEquals(new String[]{"-a", "-b", "valueB", "-c", "-d", "valueD"}, result);
    }

    @Test
    public void testFlatten_Arguments2() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, ARGS_2, false);

        assertArrayEquals(new String[]{"--optionA", "-b", "valueC"}, result);
    }

    @Test
    public void testFlatten_Arguments3() {
        Options options = new Options();
        options.addOption("foo", false, "Option Foo");
        options.addOption("bar", true, "Option Bar with value");
        options.addOption("baz", true, "Option Baz with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, ARGS_3, false);

        assertArrayEquals(new String[]{"--foo", "--bar=qux", "--baz=quux"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_Arguments4() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, ARGS_4, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_Arguments5() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, ARGS_5, false);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertFalse(parser.isEatTheRest());
        assertTrue(parser.isEmptyTokens());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.process("--optionA");
        assertEquals(1, parser.getTokens().size());
        assertEquals("--optionA", parser.getTokens().get(0));

        parser.process("--optionB=valueB");
        assertEquals(2, parser.getTokens().size());
        assertEquals("--optionA", parser.getTokens().get(0));
        assertEquals("--optionB=valueB", parser.getTokens().get(1));

        parser.process("--");
        assertEquals(3, parser.getTokens().size());
        assertEquals("--optionA", parser.getTokens().get(0));
        assertEquals("--optionB=valueB", parser.getTokens().get(1));
        assertEquals("--", parser.getTokens().get(2));

        parser.process("invalidArg");
        assertEquals(4, parser.getTokens().size());
        assertEquals("--optionA", parser.getTokens().get(0));
        assertEquals("--optionB=valueB", parser.getTokens().get(1));
        assertEquals("--", parser.getTokens().get(2));
        assertEquals("invalidArg", parser.getTokens().get(3));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.processOptionToken("a", false);
        assertEquals(1, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));

        parser.processOptionToken("b", false);
        assertEquals(2, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-b", parser.getTokens().get(1));

        parser.processOptionToken("optionC", false);
        assertEquals(3, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-b", parser.getTokens().get(1));
        assertEquals("-optionC", parser.getTokens().get(2));

        parser.processOptionToken("optionD=valueD", false);
        assertEquals(4, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-b", parser.getTokens().get(1));
        assertEquals("-optionC", parser.getTokens().get(2));
        assertEquals("-optionD=valueD", parser.getTokens().get(3));

        PosixParser parser2 = new PosixParser();
        parser2.init();
        parser2.processOptionToken("optionX", true);
        assertNull(parser2.getCurrentOption());
        assertEquals(1, parser2.getTokens().size());
        assertEquals("-optionX", parser2.getTokens().get(0));

        parser2.processOptionToken("optionY=valueY", true);
        assertNotNull(parser2.getCurrentOption());
        assertEquals(2, parser2.getTokens().size());
        assertEquals("-optionX", parser2.getTokens().get(0));
        assertEquals("-optionY=valueY", parser2.getTokens().get(1));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();

        parser.burstToken("-a--optionB=valueB--optionC", false);
        assertEquals(4, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-optionB=valueB", parser.getTokens().get(1));
        assertEquals("-optionC", parser.getTokens().get(2));
        assertEquals("--", parser.getTokens().get(3));

        parser.burstToken("-a--optionB valueB--optionC", false);
        assertEquals(4, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-optionB", parser.getTokens().get(1));
        assertEquals("valueB", parser.getTokens().get(2));
        assertEquals("-optionC", parser.getTokens().get(3));

        parser.burstToken("-a--optionB valueB--optionC--", false);
        assertEquals(5, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-optionB", parser.getTokens().get(1));
        assertEquals("valueB", parser.getTokens().get(2));
        assertEquals("-optionC", parser.getTokens().get(3));
        assertEquals("--", parser.getTokens().get(4));

        parser.burstToken("-a--optionB valueB--optionC--optionD=valueD", false);
        assertEquals(6, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("-optionB", parser.getTokens().get(1));
        assertEquals("valueB", parser.getTokens().get(2));
        assertEquals("-optionC", parser.getTokens().get(3));
        assertEquals("-optionD=valueD", parser.getTokens().get(4));
        assertEquals("--", parser.getTokens().get(5));

        PosixParser parser2 = new PosixParser();
        parser2.init();
        parser2.burstToken("--optionX", true);
        assertEquals(1, parser2.getTokens().size());
        assertEquals("--optionX", parser2.getTokens().get(0));

        parser2.burstToken("--optionX=valueX--optionY=valueY", true);
        assertEquals(3, parser2.getTokens().size());
        assertEquals("--optionX", parser2.getTokens().get(0));
        assertEquals("-optionX=valueX", parser2.getTokens().get(1));
        assertEquals("--optionY=valueY", parser2.getTokens().get(2));

        parser2.burstToken("--optionX=valueX--", true);
        assertEquals(2, parser2.getTokens().size());
        assertEquals("--optionX=valueX", parser2.getTokens().get(0));
        assertEquals("--", parser2.getTokens().get(1));
    }
}