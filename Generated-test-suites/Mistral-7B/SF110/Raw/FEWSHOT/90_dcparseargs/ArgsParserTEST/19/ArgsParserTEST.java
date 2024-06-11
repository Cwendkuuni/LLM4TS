package de.devcity.parseargs;

import de.devcity.parseargs.arguments.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser ap = new ArgsParser(new String[]{"arg1", "arg2"});
        Assert.assertEquals(2, ap.getArgsCount());

        ArgsParser apEmpty = new ArgsParser(new String[]{});
        Assert.assertEquals(0, apEmpty.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser ap = new ArgsParser(new String[]{"arg1", "arg2"});
        String[] args = ap.getArgs();
        Assert.assertEquals("arg1", args[0]);
        Assert.assertEquals("arg2", args[1]);

        ArgsParser apEmpty = new ArgsParser(new String[]{});
        String[] argsEmpty = apEmpty.getArgs();
        Assert.assertArrayEquals(new String[]{}, argsEmpty);
    }

    @Test
    public void testSetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser ap = new ArgsParser();
        ap.setArgs(args);

        String[] expectedArgs = new String[]{"arg1", "arg2", "arg3"};
        Assert.assertArrayEquals(expectedArgs, ap.getArgs());

        String[] innerArgs = ap.getInnerArgs();
        Assert.assertArrayEquals(expectedArgs, innerArgs);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser ap = new ArgsParser();
        ArgsParser apArgs = new ArgsParser(new String[]{"--arg1", "--arg2"});
        Assert.assertEquals(2, apArgs.getArgsLeftCount());

        ArgsParser apNoArgs = new ArgsParser();
        Assert.assertEquals(0, apNoArgs.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentSingleCharacter() {
        ArgsParser ap = new ArgsParser(new String[]{"-a", "--some-arg"});
        SwitchArgument switchArgument = ap.parseSwitchArgument("-a");
        Assert.assertEquals(0, switchArgument.getIndex());
        Assert.assertEquals("a", switchArgument.getKey());
        Assert.assertTrue(switchArgument.getValue());

        SwitchArgument switchArgumentFalse = ap.parseSwitchArgument("--some-arg");
        Assert.assertEquals(1, switchArgumentFalse.getIndex());
        Assert.assertEquals("some-arg", switchArgumentFalse.getKey());
        Assert.assertFalse(switchArgumentFalse.getValue());

        ArgsParser apEmpty = new ArgsParser();
        SwitchArgument switchArgumentEmpty = apEmpty.parseSwitchArgument("--some-arg");
        Assert.assertEquals(-1, switchArgumentEmpty.getIndex());
        Assert.assertEquals("some-arg", switchArgumentEmpty.getKey());
        Assert.assertFalse(switchArgumentEmpty.getValue());
    }

    @Test
    public void testParseSwitchArgumentMultipleCharacter() {
        ArgsParser ap = new ArgsParser(new String[]{"--arg1", "--arg2", "--arg3"});
        SwitchArgument switchArgument = ap.parseSwitchArgument("--arg1");
        Assert.assertEquals(0, switchArgument.getIndex());
        Assert.assertEquals("arg1", switchArgument.getKey());
        Assert.assertTrue(switchArgument.getValue());

        SwitchArgument switchArgument2 = ap.parseSwitchArgument("--arg2");
        Assert.assertEquals(1, switchArgument2.getIndex());
        Assert.assertEquals("arg2", switchArgument2.getKey());
        Assert.assertTrue(switchArgument2.getValue());

        SwitchArgument switchArgument3 = ap.parseSwitchArgument("--arg3");
        Assert.assertEquals(2, switchArgument3.getIndex());
        Assert.assertEquals("arg3", switchArgument3.getKey());
        Assert.assertTrue(switchArgument3.getValue());

        ArgsParser apEmpty = new ArgsParser();
        SwitchArgument switchArgumentEmpty = apEmpty.parseSwitchArgument("--arg3");
        Assert.assertEquals(-1, switchArgumentEmpty.getIndex());
        Assert.assertEquals("arg3", switchArgumentEmpty.getKey());
        Assert.assertFalse(switchArgumentEmpty.getValue());
    }

    @Test
    public void testParseParameterArgumentSingleCharacter() {
        ArgsParser ap = new ArgsParser(new String[]{"-a=value", "--some-arg=value"});
        ParameterArgument parameterArgument = ap.parseParameterArgument("--some-arg");
        Assert.assertEquals(1, parameterArgument.getIndex());
        Assert.assertEquals("some-arg", parameterArgument.getKey());
        Assert.assertEquals("value", parameterArgument.getValue());

        ArgsParser apEmpty = new ArgsParser();
        ParameterArgument parameterArgumentEmpty = apEmpty.parseParameterArgument("--some-arg");
        Assert.assertEquals(-1, parameterArgumentEmpty.getIndex());
        Assert.assertEquals("some-arg", parameterArgumentEmpty.getKey());
        Assert.assertNull(parameterArgumentEmpty.getValue());
    }

    @Test
    public void testParseParameterArgumentMultipleCharacter() {
        ArgsParser ap = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        ParameterArgument parameterArgument = ap.parseParameterArgument("--arg2");
        Assert.assertEquals(1, parameterArgument.getIndex());
        Assert.assertEquals("arg2", parameterArgument.getKey());
        Assert.assertEquals("value2", parameterArgument.getValue());

        ArgsParser apEmpty = new ArgsParser();
        ParameterArgument parameterArgumentEmpty = apEmpty.parseParameterArgument("--arg2");
        Assert.assertEquals(-1, parameterArgumentEmpty.getIndex());
        Assert.assertEquals("arg2", parameterArgumentEmpty.getKey());
        Assert.assertNull(parameterArgumentEmpty.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser ap = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        List<StringArgument> stringArguments = ap.parseStringArgument();
        List<StringArgument> expectedStringArguments = new ArrayList<>();
        expectedStringArguments.add(new StringArgument(0, "--arg1"));
        expectedStringArguments.add(new StringArgument(1, "--arg2"));
        Assert.assertEquals(expectedStringArguments, stringArguments);

        ArgsParser apEmpty = new ArgsParser();
        List<StringArgument> stringArgumentsEmpty = apEmpty.parseStringArgument();
        Assert.assertEquals(new ArrayList<>(), stringArgumentsEmpty);
    }
}