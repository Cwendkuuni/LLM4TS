package de.devcity.parseargs;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParserInstance() {
        ArgsParser argsParser = new ArgsParser();
        ArgsParser argsParserWithArgs = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(new String[0], argsParser.getArgs());
        assertNull(argsParser.getArgsLeftCount());
        assertEquals(2, argsParserWithArgs.getArgsCount());
        assertEquals(2, argsParserWithArgs.getArgsLeftCount());
    }

    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = new String[0];
        ArgsParser argsParserWithArgs = new ArgsParser(args);
        assertEquals(0, argsParser.getArgsCount());
        assertEquals(2, argsParserWithArgs.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = new String[0];
        ArgsParser argsParserWithArgs = new ArgsParser(args);
        String[] expectedArgs = new String[0];
        String[] actualArgs = argsParser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
        expectedArgs = new String[]{"arg1", "arg2"};
        argsParserWithArgs.setArgs(new String[]{"arg1", "arg2"});
        actualArgs = argsParserWithArgs.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2"};
        argsParser.setArgs(args);
        String[] expectedArgs = new String[]{"arg1", "arg2"};
        String[] actualArgs = argsParser.getArgs();
        assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser argsParser = new ArgsParser();
        ArgsParser argsParserWithArgs = new ArgsParser(new String[0]);
        int expectedArgsLeftCount = 0;
        int actualArgsLeftCount = argsParser.getArgsLeftCount();
        assertEquals(expectedArgsLeftCount, actualArgsLeftCount);
        expectedArgsLeftCount = 2;
        argsParserWithArgs.setArgs(new String[]{"arg1", "arg2"});
        actualArgsLeftCount = argsParserWithArgs.getArgsLeftCount();
        assertEquals(expectedArgsLeftCount, actualArgsLeftCount);
    }

    @Test
    public void testParseSwitchArgument_ShortKey() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-a"});
        SwitchArgument expectedSwitchArgument = new SwitchArgument(0, "a", true);
        SwitchArgument actualSwitchArgument = argsParser.parseSwitchArgument("a");
        assertEquals(expectedSwitchArgument, actualSwitchArgument);
    }

    @Test
    public void testParseSwitchArgument_ShortKey_NoMatch() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "-b"});
        SwitchArgument expectedSwitchArgument = new SwitchArgument(-1, "a", false);
        SwitchArgument actualSwitchArgument = argsParser.parseSwitchArgument("a");
        assertEquals(expectedSwitchArgument, actualSwitchArgument);
    }

    @Test
    public void testParseSwitchArgument_LongKey() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--long-key"});
        SwitchArgument expectedSwitchArgument = new SwitchArgument(0, "long-key", true);
        SwitchArgument actualSwitchArgument = argsParser.parseSwitchArgument("long-key");
        assertEquals(expectedSwitchArgument, actualSwitchArgument);
    }

    @Test
    public void testParseSwitchArgument_LongKey_NoMatch() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "--long-key2"});
        SwitchArgument expectedSwitchArgument = new SwitchArgument(-1, "long-key", false);
        SwitchArgument actualSwitchArgument = argsParser.parseSwitchArgument("long-key");
        assertEquals(expectedSwitchArgument, actualSwitchArgument);
    }

    @Test
    public void testParseParameterArgument_ShortKey() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-a=value"});
        String expectedValue = "value";
        ParameterArgument expectedParameterArgument = new ParameterArgument(0, "a", expectedValue);
        ParameterArgument actualParameterArgument = argsParser.parseParameterArgument("a");
        assertEquals(expectedParameterArgument, actualParameterArgument);
        assertEquals(expectedValue, actualParameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_ShortKey_NoMatch() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "-a"});
        ParameterArgument expectedParameterArgument = new ParameterArgument(-1, "a", null);
        ParameterArgument actualParameterArgument = argsParser.parseParameterArgument("a");
        assertEquals(expectedParameterArgument, actualParameterArgument);
        assertNull(actualParameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKey() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"--long-key=value"});
        String expectedValue = "value";
        ParameterArgument expectedParameterArgument = new ParameterArgument(0, "long-key", expectedValue);
        ParameterArgument actualParameterArgument = argsParser.parseParameterArgument("long-key");
        assertEquals(expectedParameterArgument, actualParameterArgument);
        assertEquals(expectedValue, actualParameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_LongKey_NoMatch() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "--long-key2"});
        ParameterArgument expectedParameterArgument = new ParameterArgument(-1, "long-key", null);
        ParameterArgument actualParameterArgument = argsParser.parseParameterArgument("long-key");
        assertEquals(expectedParameterArgument, actualParameterArgument);
        assertNull(actualParameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgument_MixedShortLong() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"-a=value", "--long-key=value2"});
        List<ParameterArgument> expectedParameterArguments = new ArrayList<>();
        expectedParameterArguments.add(new ParameterArgument(0, "a", "value"));
        expectedParameterArguments.add(new ParameterArgument(1, "long-key", "value2"));
        List<ParameterArgument> actualParameterArguments = argsParser.parseParameterArguments();
        assertEquals(expectedParameterArguments, actualParameterArguments);
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "-a=value1", "--long-key=value2", "-b=value3", "--long-key2=value4", "arg3"});
        List<StringArgument> expectedStringArguments = Arrays.asList(new StringArgument(2, "arg3"), new StringArgument(5, "--long-key2=value4"));
        List<StringArgument> actualStringArguments = argsParser.parseStringArguments();
        assertEquals(expectedStringArguments, actualStringArguments);
    }

    @Test
    public void testParseStringArguments_Empty() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[0]);
        List<StringArgument> expectedStringArguments = Collections.emptyList();
        List<StringArgument> actualStringArguments = argsParser.parseStringArguments();
        assertEquals(expectedStringArguments, actualStringArguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgument_InvalidShortKey() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "--"});
        argsParser.parseSwitchArgument("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgument_InvalidLongKey() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1"});
        argsParser.parseSwitchArgument("--");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testParseParameterArgument_InvalidIndex() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[0]);
        argsParser.parseParameterArgument(-1);
    }

    @Test
    public void testParseParameterArguments() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[]{"arg1", "-a=value1", "--long-key=value2", "-b=value3", "--long-key2=value4", "arg3"});
        List<ParameterArgument> expectedParameterArguments = Arrays.asList(new ParameterArgument(0, "a", "value1"), new ParameterArgument(3, "long-key", "value2"), new ParameterArgument(5, "long-key2", "value4"));
        List<ParameterArgument> actualParameterArguments = argsParser.parseParameterArguments();
        assertEquals(expectedParameterArguments, actualParameterArguments);
    }

    @Test
    public void testParseParameterArguments_Empty() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(new String[0]);
        List<ParameterArgument> expectedParameterArguments = Collections.emptyList();
        List<ParameterArgument> actualParameterArguments = argsParser.parseParameterArguments();
        assertEquals(expectedParameterArguments, actualParameterArguments);
    }

}