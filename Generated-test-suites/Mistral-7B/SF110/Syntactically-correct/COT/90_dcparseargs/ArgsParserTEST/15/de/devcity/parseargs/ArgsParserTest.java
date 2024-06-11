package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser parser;
 private String[] args;

 @Before
 public void setUp() {
 args = new String[] {"-a", "--long-arg", "value", "arg1", "--long-arg2", "value2"};
 parser = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(args.length, parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(args, parser.getArgs());
 }

 @Test
 public void testSetArgs() {
 assertNull(parser.getArgs());
 parser.setArgs(args);
 assertArrayEquals(args, parser.getArgs());
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(3, parser.getArgsLeftCount());
 }

 @Test(expected = NullPointerException.class)
 public void testParseSwitchArgumentNullArgs() {
 parser = new ArgsParser(null);
 parser.parseSwitchArgument("test");
 fail("NullPointerException expected but not thrown.");
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgument1 = parser.parseSwitchArgument("a");
 assertEquals(0, switchArgument1.getIndex());
 assertEquals("a", switchArgument1.getKey());
 assertTrue(switchArgument1.isLong());

 SwitchArgument switchArgument2 = parser.parseSwitchArgument("long-arg");
 assertEquals(2, switchArgument2.getIndex());
 assertEquals("long-arg", switchArgument2.getKey());
 assertTrue(switchArgument2.isLong());

 SwitchArgument switchArgument3 = parser.parseSwitchArgument("not-present");
 assertEquals(-1, switchArgument3.getIndex());
 assertNull(switchArgument3.getKey());
 assertFalse(switchArgument3.isLong());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseParameterArgumentIndexOutOfBounds() {
 parser = new ArgsParser(new String[] {"arg1", "--long-arg", "value", "arg2"});
 parser.parseParameterArgument("not-present");
 fail("IndexOutOfBoundsException expected but not thrown.");
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgument1 = parser.parseParameterArgument("a");
 assertEquals(0, parameterArgument1.getIndex());
 assertEquals("a", parameterArgument1.getKey());
 assertEquals("value", parameterArgument1.getValue());

 ParameterArgument parameterArgument2 = parser.parseParameterArgument("long-arg2");
 assertEquals(4, parameterArgument2.getIndex());
 assertEquals("long-arg2", parameterArgument2.getKey());
 assertEquals("value2", parameterArgument2.getValue());

 ParameterArgument parameterArgument3 = parser.parseParameterArgument("not-present");
 assertNull(parser.parseParameterArgument("not-present"));
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(3, stringArguments.size());
 StringArgument stringArgument1 = stringArguments.get(0);
 assertEquals(1, stringArgument1.getIndex());
 assertEquals("arg1", stringArgument1.getKey());
 assertEquals("", stringArgument1.getValue());

 StringArgument stringArgument2 = stringArguments.get(1);
 assertEquals(3, stringArgument2.getIndex());
 assertEquals("", stringArgument2.getKey());
 assertEquals("value", stringArgument2.getValue());

 StringArgument stringArgument3 = stringArguments.get(2);
 assertEquals(5, stringArgument3.getIndex());
 assertEquals("arg2", stringArgument3.getKey());
 assertEquals("", stringArgument3.getValue());
 }

 @Test
 public void testParseStringArgumentWithValue() {
 String[] argsWithValue = {"-a", "--long-arg", "value", "arg1=test", "--long-arg2=test2", "arg2=test3"};
 ArgsParser parserWithValue = new ArgsParser(argsWithValue);
 List<StringArgument> stringArgumentsWithValue = parserWithValue.parseStringArgument();
 assertEquals(4, stringArgumentsWithValue.size());
 StringArgument stringArgument1WithValue = stringArgumentsWithValue.get(0);
 assertEquals(1, stringArgument1WithValue.getIndex());
 assertEquals("arg1", stringArgument1WithValue.getKey());
 assertEquals("test", stringArgument1WithValue.getValue());

 StringArgument stringArgument2WithValue = stringArgumentsWithValue.get(1);
 assertEquals(3, stringArgument2WithValue.getIndex());
 assertEquals("", stringArgument2WithValue.getKey());
 assertEquals("value", stringArgument2WithValue.getValue());

 StringArgument stringArgument3WithValue = stringArgumentsWithValue.get(2);
 assertEquals(4, stringArgument3WithValue.getIndex());
 assertEquals("--long-arg2", stringArgument3WithValue.getKey());
 assertEquals("test2", stringArgument3WithValue.getValue());

 StringArgument stringArgument4WithValue = stringArgumentsWithValue.get(3);
 assertEquals(6, stringArgument4WithValue.getIndex());
 assertEquals("arg2", stringArgument4WithValue.getKey());
 assertEquals("test3", stringArgument4WithValue.getValue());
 }

 @Test
 public void testParseStringArgumentWithMixedArguments() {
 String[] argsMixed = {"-a", "--long-arg", "value", "arg1=test1", "--long-arg2=test2", "arg2=test3", "new-arg=test4"};
 ArgsParser parserMixed = new ArgsParser(argsMixed);
 List<StringArgument> stringArgumentsMixed = parserMixed.parseStringArgument();
 assertEquals(5, stringArgumentsMixed.size());
 StringArgument stringArgument1Mixed = stringArgumentsMixed.get(0);
 assertEquals(1, stringArgument1Mixed.getIndex());
 assertEquals("arg1", stringArgument1Mixed.getKey());
 assertEquals("test1", stringArgument1Mixed.getValue());

 StringArgument stringArgument2Mixed = stringArgumentsMixed.get(1);
 assertEquals(3, stringArgument2Mixed.getIndex());
 assertEquals("--long-arg", stringArgument2Mixed.getKey());
 assertEquals("", stringArgument2Mixed.getValue());

 StringArgument stringArgument3Mixed = stringArgumentsMixed.get(2);
 assertEquals(4, stringArgument3Mixed.getIndex());
 assertEquals("--long-arg2", stringArgument3Mixed.getKey());
 assertEquals("test2", stringArgument3Mixed.getValue());

 StringArgument stringArgument4Mixed = stringArgumentsMixed.get(3);
 assertEquals(6, stringArgument4Mixed.getIndex());
 assertEquals("arg2", stringArgument4Mixed.getKey());
 assertEquals("test3", stringArgument4Mixed.getValue());

 StringArgument stringArgument5Mixed = stringArgumentsMixed.get(4);
 assertEquals(7, stringArgument5Mixed.getIndex());
 assertEquals("new-arg", stringArgument5Mixed.getKey());
 assertEquals("test4", stringArgument5Mixed.getValue());
 }

}