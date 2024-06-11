package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.Arguments.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest {

 private static final String[] VALID_SWITCH_ARGUMENT_TEST_CASES = new String[]{"-s", "--long-switch"};
 private static final String[] VALID_PARAMETER_ARGUMENT_TEST_CASES = new String[]{"-p=test", "--long-param=test"};
 private static final String[] VALID_STRING_ARGUMENT_TEST_CASES = new String[]{"-s=test1,test2,test3", "--long-string=test1,test2,test3"};
 private static final String[] INVALID_TEST_CASES = new String[]{"-", "--", "-s=test", "--long-param=test=", "--long-string="};

 @Test
 public void testSetArgs() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_SWITCH_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 assertEquals(args.length, parser.getArgsCount());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 ArgsParser parser = new ArgsParser();
 String[] args = null;
 parser.setArgs(args);
 fail("NullPointerException expected but not thrown.");
 }

 @Test
 public void testGetArgs() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_SWITCH_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 assertArrayEquals(args, parser.getArgs());
 }

 @Test
 public void testGetArgsLeftCount() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_SWITCH_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 assertEquals(args.length, parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_SWITCH_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 for (String key : VALID_SWITCH_ARGUMENT_TEST_CASES) {
 SwitchArgument switchArgument = parser.parseSwitchArgument(key);
 assertTrue(switchArgument.isPresent());
 assertEquals(key, switchArgument.getKey());
 }
 }

 @Test
 public void testParseSwitchArgumentInvalidKey() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_SWITCH_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 for (String key : INVALID_TEST_CASES) {
 SwitchArgument switchArgument = parser.parseSwitchArgument(key);
 assertFalse(switchArgument.isPresent());
 }
 }

 @Test
 public void testParseParameterArgument() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_PARAMETER_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 for (String key : VALID_PARAMETER_ARGUMENT_TEST_CASES) {
 ParameterArgument parameterArgument = parser.parseParameterArgument(key);
 assertNotNull(parameterArgument);
 assertEquals(key, parameterArgument.getKey());
 }
 }

 @Test
 public void testParseParameterArgumentInvalidKey() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_PARAMETER_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 for (String key : INVALID_TEST_CASES) {
 ParameterArgument parameterArgument = parser.parseParameterArgument(key);
 assertNull(parameterArgument);
 }
 }

 @Test
 public void testParseStringArgument() {
 ArgsParser parser = new ArgsParser();
 String[] args = VALID_STRING_ARGUMENT_TEST_CASES;
 parser.setArgs(args);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(args.length, stringArguments.size());
 for (int i = 0; i < stringArguments.size(); i++) {
 StringArgument stringArgument = stringArguments.get(i);
 assertEquals(args[i], stringArgument.getValue());
 }
 }

 @Test
 public void testParseStringArgumentInvalidFormat() {
 ArgsParser parser = new ArgsParser();
 String[] args = new String[]{"-s=test,test2"};
 parser.setArgs(args);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(1, stringArguments.size());
 StringArgument stringArgument = stringArguments.get(0);
 assertEquals("test,test2", stringArgument.getValue());
 }

 @Test
 public void testParseStringArgumentEmpty() {
 ArgsParser parser = new ArgsParser();
 String[] args = new String[0];
 parser.setArgs(args);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertTrue(stringArguments.isEmpty());
 }
}