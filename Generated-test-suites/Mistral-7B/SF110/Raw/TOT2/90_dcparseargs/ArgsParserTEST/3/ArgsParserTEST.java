package de.devcity.parseargs.test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
// Test for getArgsCount() method
@Test
public void testGetArgsCount() {
ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
assertEquals(3, parser.getArgsCount());
}

// Test for parseSwitchArgument(String key) method
@Test
public void testParseSwitchArgument() {
// Basic usage test case
ArgsParser parser = new ArgsParser(new String[]{"--switchKey"});
SwitchArgument switchArgument = parser.parseSwitchArgument("switchKey");
assertEquals(0, switchArgument.getIndex());
assertEquals("switchKey", switchArgument.getKey());
assertTrue(switchArgument.isLong());
assertNull(parser.getArgs());

// Test for key is null
ArgsParser parser2 = new ArgsParser(new String[]{"--key", "arg1"});
assertNull(parser2.parseSwitchArgument(null));

// Test for key is an empty string
ArgsParser parser3 = new ArgsParser(new String[]{"--", "arg1"});
assertNull(parser3.parseSwitchArgument(""));

// Test for key is longer than one character but does not start with "--"
ArgsParser parser4 = new ArgsParser(new String[]{"--longKeyArgument", "arg1"});
assertNull(parser4.parseSwitchArgument("longKeyArgument"));

// Test for key is present multiple times in the arguments
ArgsParser parser5 = new ArgsParser(new String[]{"--switchKey1", "--switchKey2", "--switchKey1"});
SwitchArgument switchArgument1 = parser5.parseSwitchArgument("switchKey1");
assertEquals(0, switchArgument1.getIndex());
assertEquals("switchKey1", switchArgument1.getKey());
assertTrue(switchArgument1.isLong());
assertNull(parser5.getArgs());

SwitchArgument switchArgument2 = parser5.parseSwitchArgument("switchKey2");
assertEquals(1, switchArgument2.getIndex());
assertEquals("switchKey2", switchArgument2.getKey());
assertTrue(switchArgument2.isLong());
assertNull(parser5.getArgs());

// Test for key is not present in the arguments
ArgsParser parser6 = new ArgsParser(new String[]{"arg1", "arg2"});
assertNull(parser6.parseSwitchArgument("unknownKey"));
}

// Test for parseParameterArgument(String key) method
@Test
public void testParseParameterArgument() {
// Basic usage test case
ArgsParser parser = new ArgsParser(new String[]{"--key=value"});
ParameterArgument parameterArgument = parser.parseParameterArgument("key");
assertEquals(0, parameterArgument.getIndex());
assertEquals("key", parameterArgument.getKey());
assertEquals("value", parameterArgument.getValue());
assertNull(parser.getArgs());

// Test for key is null
ArgsParser parser2 = new ArgsParser(new String[]{"--key=value", "--"});
assertNull(parser2.parseParameterArgument(null));

// Test for key is an empty string
ArgsParser parser3 = new ArgsParser(new String[]{"--key=value", "--"});
assertNull(parser3.parseParameterArgument(""));

// Test for key is longer than one character but does not start with "--"
ArgsParser parser4 = new ArgsParser(new String[]{"--longKeyArgument=value", "--"});
assertNull(parser4.parseParameterArgument("longKeyArgument"));

// Test for value is null
ArgsParser parser5 = new ArgsParser(new String[]{"--key=value", "--key=", "--key=null"});
ParameterArgument parameterArgument1 = parser5.parseParameterArgument("key");
assertEquals(0, parameterArgument1.getIndex());
assertEquals("key", parameterArgument1.getKey());
assertEquals(null, parameterArgument1.getValue());
assertNull(parser5.getArgs());

ParameterArgument parameterArgument2 = parser5.parseParameterArgument("key");
assertEquals(1, parameterArgument2.getIndex());
assertEquals("key", parameterArgument2.getKey());
assertEquals(null, parameterArgument2.getValue());
assertNull(parser5.getArgs());

// Test for key is present multiple times in the arguments
ArgsParser parser6 = new ArgsParser(new String[]{"--key=value1", "--key=value2", "--key=value3"});
ParameterArgument parameterArgument1 = parser6.parseParameterArgument("key");
assertEquals(0, parameterArgument1.getIndex());
assertEquals("key", parameterArgument1.getKey());
assertEquals("value1", parameterArgument1.getValue());
assertNull(parser6.getArgs());

ParameterArgument parameterArgument2 = parser6.parseParameterArgument("key");
assertEquals(1, parameterArgument2.getIndex());
assertEquals("key", parameterArgument2.getKey());
assertEquals("value2", parameterArgument2.getValue());
assertNull(parser6.getArgs());

ParameterArgument parameterArgument3 = parser6.parseParameterArgument("key");
assertEquals(2, parameterArgument3.getIndex());
assertEquals("key", parameterArgument3.getKey());
assertEquals("value3", parameterArgument3.getValue());
assertNull(parser6.getArgs());

// Test for key is not present in the arguments
ArgsParser parser7 = new ArgsParser(new String[]{"--arg1", "--arg2"});
assertNull(parser7.parseParameterArgument("key"));
}

// Test for parseStringArgument() method
@Test
public void testParseStringArguments() {
// Basic usage test case
ArgsParser parser = new ArgsParser(new String[]{"--stringArg1=value1", "--stringArg2=value2"});
List<StringArgument> stringArguments = parser.parseStringArgument();
assertEquals(2, stringArguments.size());
assertEquals("stringArg1", stringArguments.get(0).getKey());
assertEquals("value1", stringArguments.get(0).getValue());
assertEquals("stringArg2", stringArguments.get(1).getKey());
assertEquals("value2", stringArguments.get(1).getValue());
// innerArgs has been modified

// Test for some or all string arguments are missing
ArgsParser parser2 = new ArgsParser(new String[]{"--arg1"});
List<StringArgument> stringArguments2 = parser2.parseStringArgument();
assertEquals(0, stringArguments2.size());

// Test for string arguments have incorrect format
ArgsParser parser3 = new ArgsParser(new String[]{"--stringArg1=value1", "--stringArg3=value3"});
List<StringArgument> stringArguments3 = parser3.parseStringArgument();
assertEquals(1, stringArguments3.size());
assertEquals("stringArg1", stringArguments3.get(0).getKey());
assertEquals("value1", stringArguments3.get(0).getValue());
}

}