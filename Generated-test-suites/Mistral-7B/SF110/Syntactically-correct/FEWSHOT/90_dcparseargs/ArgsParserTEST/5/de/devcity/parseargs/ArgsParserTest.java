package de.devcity.parseargs;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParser() {
        ArgsParser parser = new ArgsParser(new String[0]);
        assertEquals(0, parser.getArgsCount());
        assertEquals(new String[0], parser.getArgs());

        String[] args = {"arg1", "arg2", "--switch", "-k:value", "--longSwitch", "--", "arg3"};
        ArgsParser parser2 = new ArgsParser(args);
        assertEquals(6, parser2.getArgsCount());
        String[] expectedArgs = {"arg1", "arg2", "--switch", "-k:value", "--longSwitch", "--", "arg3"};
        assertArrayEquals(expectedArgs, parser2.getArgs());

        assertEquals(4, parser2.getArgsLeftCount());
        assertNull(parser2.parseSwitchArgument(null));
        assertNull(parser2.parseSwitchArgument(""));
        assertNull(parser2.parseSwitchArgument("--"));
        assertNull(parser2.parseSwitchArgument("--notFound"));

        SwitchArgument switchArg = parser2.parseSwitchArgument("--switch");
        assertEquals(4, parser2.getArgsLeftCount());
        assertEquals(0, switchArg.getIndex());
        assertEquals("switch", switchArg.getKey());
        assertTrue(switchArg.getValue());

        assertNull(parser2.parseSwitchArgument("-s"));

        SwitchArgument longSwitchArg = parser2.parseSwitchArgument("--longSwitch");
        assertEquals(3, parser2.getArgsLeftCount());
        assertEquals(5, switchArg.getIndex());
        assertEquals("longSwitch", longSwitchArg.getKey());
        assertTrue(longSwitchArg.getValue());

        assertNull(parser2.parseParameterArgument("p"));

        ParameterArgument paramArg = parser2.parseParameterArgument("k:value");
        assertNull(parser2.parseParameterArgument("k"));
        assertNull(parser2.parseParameterArgument("k:"));
        assertNull(parser2.parseParameterArgument("k:value:"));
        assertNull(parser2.parseParameterArgument("k:value:"));
        assertEquals(2, parser2.getArgsLeftCount());
        assertEquals(5, paramArg.getIndex());
        assertEquals("k", paramArg.getKey());
        assertEquals("value", paramArg.getValue());

        assertNull(parser2.parseParameterArgument("k"));
        assertNull(parser2.parseParameterArgument("k:"));
        assertNull(parser2.parseParameterArgument("k:"));
        assertNull(parser2.parseParameterArgument("k:value:"));
        assertNull(parser2.parseParameterArgument("k:value:"));
        assertEquals(0, parser2.getArgsLeftCount());

        List<StringArgument> stringArgs = parser2.parseStringArgument();
        List<StringArgument> expectedStringArgs = Arrays.asList(new StringArgument(1, "arg3"));
        assertEquals(1, stringArgs.size());
        assertSame(expectedStringArgs.get(0), stringArgs.get(0));
    }

    @Test
    public void testArgsParserNullArgs() {
        ArgsParser parser = new ArgsParser(null);
        assertNull(parser.getArgs());
        assertEquals(0, parser.getArgsCount());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserNullArgs2() {
        ArgsParser parser = new ArgsParser(null);
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (null == expected || null == actual || expected.length != actual.length) {
            assertFalse("Arrays not equal", false);
        } else {
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], actual[i]);
            }
            assertTrue("Arrays equal", true);
        }
    }
}