package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private String[] args = null;
 private ArgsParser parser = null;

 @Test
 public void testArgsParserWithEmptyArgs() {
 args = new String[0];
 parser = new ArgsParser(args);
 assertEquals(0, parser.getArgsCount());
 assertNull(parser.getArgs());
 }

 @Test
 public void testArgsParserWithValidArgs() {
 args = new String[]{"arg1", "-s", "arg3", "--arg4=value4", "-a", "arg6"};
 parser = new ArgsParser(args);
 assertEquals(6, parser.getArgsCount());
 assertEquals(args, parser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testArgsParserWithNullArgs() {
 args = null;
 parser = new ArgsParser(args);
 fail("ArgsParser constructor should not accept null args.");
 }

 @Test
 public void testGetArgsLeftCount() {
 args = new String[]{"arg1", "-s", "arg3", "--arg4=value4", "-a", "arg6"};
 parser = new ArgsParser(args);
 assertEquals(4, parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 args = new String[]{"arg1", "-s", "arg3", "--arg4=value4", "-a", "arg6"};
 parser = new ArgsParser(args);

 SwitchArgument swArg1 = parser.parseSwitchArgument("s");
 assertEquals(1, swArg1.getIndex());
 assertEquals("s", swArg1.getKey());
 assertTrue(swArg1.isLong());

 SwitchArgument swArg2 = parser.parseSwitchArgument("a");
 assertEquals(-1, swArg2.getIndex());
 assertFalse(swArg2.isLong());
 }

 @Test
 public void testParseStringArgument() {
 args = new String[]{"arg1", "-s", "arg3", "--arg4=value4", "-a", "arg6", "--arg5=value5"};
 parser = new ArgsParser(args);
 List<StringArgument> strArgs = parser.parseStringArgument();
 List<StringArgument> expected = new ArrayList<StringArgument>();
 expected.add(new StringArgument(4, "value5"));
 expected.add(new StringArgument(6, "arg6"));
 assertEquals(expected, strArgs);
 }

 @Test
 public void testParseParameterArgument() {
 args = new String[]{"arg1", "-s", "arg3", "--arg4=value4", "-a", "--arg5=value5", "arg6=value6", "arg7"};
 parser = new ArgsParser(args);

 ParameterArgument paramArg1 = parser.parseParameterArgument("arg5");
 assertEquals(5, paramArg1.getIndex());
 assertEquals("arg5", paramArg1.getKey());
 assertEquals("value5", paramArg1.getValue());

 ParameterArgument paramArg2 = parser.parseParameterArgument("arg3");
 assertEquals(2, paramArg2.getIndex());
 assertEquals("arg3", paramArg2.getKey());
 assertEquals(null, paramArg2.getValue());
 }
}