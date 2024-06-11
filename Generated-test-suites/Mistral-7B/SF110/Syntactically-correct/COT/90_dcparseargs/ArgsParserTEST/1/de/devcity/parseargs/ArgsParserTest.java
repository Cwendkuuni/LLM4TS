package de.devcity.parseargs.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;
import de.devcity.parseargs.arguments.ParameterArgument;

public class ArgsParserTest {

 private String[] args = { "-a", "--long-a", "1", "--switch", "--long-switch" };
 private ArgsParser parser;

 @Before
 public void setup() {
 parser = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertTrue(parser.getArgsCount() == 5);
 }

 @Test
 public void testGetArgs() {
 String[] result = parser.getArgs();
 String[] expected = args;
 assertArrayEquals(expected, result);
 }

 @Test
 public void testSetArgs() {
 String[] newArgs = { "-b", "--long-b", "2" };
 parser.setArgs(newArgs);
 assertFalse(Arrays.equals(args, parser.getArgs()));
 assertArrayEquals(newArgs, parser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 parser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertTrue(parser.getArgsLeftCount() == 3);
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgument = parser.parseSwitchArgument("-switch");
 assertTrue(switchArgument.getKey().equals("-switch"));
 assertTrue(switchArgument.isLong());
 assertTrue(switchArgument.getIndex() == 3);

 SwitchArgument nullSwitchArgument = parser.parseSwitchArgument("nonExistent");
 assertNull(nullSwitchArgument);
 }

 @Test(expected = NullPointerException.class)
 public void testParseSwitchArgumentNullKey() {
 parser.parseSwitchArgument(null);
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgument = parser.parseParameterArgument("long-a");
 assertTrue(parameterArgument.getKey().equals("long-a"));
 assertTrue(parameterArgument.getValue().equals("1"));
 assertTrue(parameterArgument.getIndex() == 1);

 ParameterArgument nullParameterArgument = parser.parseParameterArgument("nonExistent");
 assertNull(nullParameterArgument);
 }

 @Test(expected = NullPointerException.class)
 public void testParseParameterArgumentNullKey() {
 parser.parseParameterArgument(null);
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArguments = parser.parseStringArgument();
 List<StringArgument> expected = Arrays.asList(new StringArgument(1, "-a"), new StringArgument(4, "--long-a"));
 assertEquals(expected, stringArguments);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseStringArgumentEmptyParser() {
 parser = new ArgsParser(new String[0]);
 parser.parseStringArgument();
 }

 @Test
 public void testParseStringArgumentEmptyArgs() {
 parser.setArgs(new String[0]);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertTrue(stringArguments.isEmpty());
 }

 @Test
 public void testParseStringArgumentMixedArgs() {
 String[] argsMixed = { "-a", "--long-a", "1", "--switch", "B:valueB", "C:valueC" };
 parser = new ArgsParser(argsMixed);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 List<StringArgument> expected = Arrays.asList(new StringArgument(1, "-a"), new StringArgument(4, "--long-a"), new StringArgument(5, "B"), new StringArgument(6, "valueB"), new StringArgument(7, "C"), new StringArgument(8, "valueC"));
 assertEquals(expected, stringArguments);
 }

 private void assertArrayEquals(Object expected, Object actual) {
 if (!expected.getClass().isAssignableFrom(actual.getClass())) {
 throw new AssertionError("Expected: " + expected + ", but: " + actual);
 }
 if (!expected.equals(actual)) {
 throw new AssertionError("Expected:\n" + Arrays.toString((Object[])expected) + "\nbut was:\n" + Arrays.toString((Object[])actual) + "\n");
 }
 }

 private void assertEquals(Object expected, Object actual) {
 if (!expected.equals(actual)) {
 throw new AssertionError("Expected:\n" + expected + "\nbut was:\n" + actual);
 }
 }
}