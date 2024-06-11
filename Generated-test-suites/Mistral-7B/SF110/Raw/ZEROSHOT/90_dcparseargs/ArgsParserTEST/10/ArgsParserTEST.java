package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private static final String[] STANDARD_ARGS = new String[]{"arg1", "arg2", "arg3"};
 private static final String[] SWITCH_ARGS = new String[]{"-s", "--long-switch"};
 private static final String[] PARAMETER_ARGS = new String[]{"--key=value1", "--key2=value2"};
 private static final String[] PARAMETER_AND_SWITCH_ARGS = new String[]{"--key=value1", "-s", "--key2=value2"};
 private static final String[] STRING_ARGS = new String[]{"--key=value1", "--key2=value2"};

 private ArgsParser parser;

 @Before
 public void setUp() {
 this.parser = new ArgsParser(STANDARD_ARGS);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(3, this.parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(STANDARD_ARGS, this.parser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 this.parser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(0, this.parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 final ArgsParser switchParser = new ArgsParser(SWITCH_ARGS);
 final SwitchArgument switchArg = switchParser.parseSwitchArgument("s");
 assertTrue(switchArg.isPresent());
 assertEquals("s", switchArg.getKey());
 final ArgsParser longSwitchParser = new ArgsParser(SWITCH_ARGS);
 final SwitchArgument longSwitchArg = longSwitchParser.parseSwitchArgument("longSwitch");
 assertTrue(longSwitchArg.isPresent());
 assertEquals("longSwitch", longSwitchArg.getKey());
 final ArgsParser invalidSwitchParser = new ArgsParser(STANDARD_ARGS);
 final SwitchArgument invalidSwitchArg = invalidSwitchParser.parseSwitchArgument("invalid");
 assertFalse(invalidSwitchArg.isPresent());
 }

 @Test
 public void testParseParameterArgument() {
 final ArgsParser parameterParser = new ArgsParser(PARAMETER_ARGS);
 final ParameterArgument paramArg1 = parameterParser.parseParameterArgument("key");
 assertTrue(paramArg1.isPresent());
 assertEquals("value1", paramArg1.getValue());
 final ArgsParser invalidParameterParser = new ArgsParser(STANDARD_ARGS);
 final ParameterArgument invalidParamArg = invalidParser.parseParameterArgument("invalid");
 assertFalse(invalidParamArg.isPresent());
 }

 @Test
 public void testParseParameterAndSwitchArguments() {
 final ArgsParser parameterAndSwitchParser = new ArgsParser(PARAMETER_AND_SWITCH_ARGS);
 final SwitchArgument switchArg = parameterAndSwitchParser.parseSwitchArgument("s");
 assertTrue(switchArg.isPresent());
 assertEquals("s", switchArg.getKey());
 final ParameterArgument paramArg1 = parameterAndSwitchParser.parseParameterArgument("key");
 assertTrue(paramArg1.isPresent());
 assertEquals("value1", paramArg1.getValue());
 final ParameterArgument paramArg2 = parameterAndSwitchParser.parseParameterArgument("key2");
 assertTrue(paramArg2.isPresent());
 assertEquals("value2", paramArg2.getValue());
 final ArgsParser invalidParameterAndSwitchParser = new ArgsParser(STANDARD_ARGS);
 final SwitchArgument invalidSwitchArg = invalidParameterAndSwitchParser.parseSwitchArgument("invalid");
 assertFalse(invalidSwitchArg.isPresent());
 final ParameterArgument invalidParamArg = invalidParameterAndSwitchParser.parseParameterArgument("invalid");
 assertFalse(invalidParamArg.isPresent());
 }

 @Test
 public void testParseStringArguments() {
 final ArgsParser stringParser = new ArgsParser(STRING_ARGS);
 final List<StringArgument> stringArgs = stringParser.parseStringArgument();
 assertEquals(2, stringArgs.size());
 assertTrue(stringArgs.get(0).isPresent());
 assertEquals("value1", stringArgs.get(0).getValue());
 assertTrue(stringArgs.get(1).isPresent());
 assertEquals("value2", stringArgs.get(1).getValue());
 }
}