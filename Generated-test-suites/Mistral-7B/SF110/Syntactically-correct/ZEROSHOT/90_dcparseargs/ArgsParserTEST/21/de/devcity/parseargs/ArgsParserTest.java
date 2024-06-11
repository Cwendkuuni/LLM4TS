package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;
 private String[] args;

 @Before
 public void setUp() {
 args = new String[]{"-a", "--long-arg", "-b=valueB", "--long-arg-with-value=valueC", "--switch"};
 argsParser = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(5, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(new String[]{"-a", "--long-arg", "-b=valueB", "--long-arg-with-value=valueC", "--switch"}, argsParser.getArgs());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgumentA = argsParser.parseSwitchArgument("-a");
 assertEquals(-1, switchArgumentA.getIndex());
 assertFalse(switchArgumentA.isPresent());

 SwitchArgument switchArgumentLongArg = argsParser.parseSwitchArgument("--long-arg");
 assertEquals(1, switchArgumentLongArg.getIndex());
 assertTrue(switchArgumentLongArg.isPresent());

 SwitchArgument switchArgumentSwitch = argsParser.parseSwitchArgument("--switch");
 assertEquals(4, switchArgumentSwitch.getIndex());
 assertTrue(switchArgumentSwitch.isPresent());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgumentB = argsParser.parseParameterArgument("-b");
 assertEquals(-1, parameterArgumentB.getIndex());
 assertNull(parameterArgumentB.getValue());
 assertFalse(parameterArgumentB.isPresent());

 ParameterArgument parameterArgumentLongArgWithValue = argsParser.parseParameterArgument("--long-arg-with-value");
 assertEquals(2, parameterArgumentLongArgWithValue.getIndex());
 assertEquals("valueC", parameterArgumentLongArgWithValue.getValue());
 assertTrue(parameterArgumentLongArgWithValue.isPresent());

 ParameterArgument parameterArgumentBWithValue = argsParser.parseParameterArgument("-b");
 assertEquals(1, parameterArgumentBWithValue.getIndex());
 assertEquals("valueB", parameterArgumentBWithValue.getValue());
 assertTrue(parameterArgumentBWithValue.isPresent());
 }

 @Test
 public void testParseStringArguments() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 List<StringArgument> expectedStringArguments = new ArrayList<StringArgument>();
 expectedStringArguments.add(new StringArgument(3, "valueB"));
 expectedStringArguments.add(new StringArgument(5, "valueC"));
 assertEquals(expectedStringArguments, stringArguments);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(1, argsParser.getArgsLeftCount());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsWithNullArgArray() {
 argsParser = new ArgsParser(null);
 }
}