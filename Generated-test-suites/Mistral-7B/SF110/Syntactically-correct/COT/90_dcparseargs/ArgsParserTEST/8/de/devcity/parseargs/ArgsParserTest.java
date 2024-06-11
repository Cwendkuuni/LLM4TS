package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;
import de.devcity.parseargs.arguments.ParameterArgument;

public class ArgsParserTest {

 private String[] args = new String[]{"arg1", "--key1", "arg3", "--key2=value2", "--longKey"};
 private ArgsParser parser;

 @Before
 public void setup() {
 this.parser = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(args.length, this.parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(args, this.parser.getArgs());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArg1 = this.parser.parseSwitchArgument("key1");
 assertEquals(0, switchArg1.getIndex());
 assertEquals("key1", switchArg1.getKey());
 assertTrue(switchArg1.isLong());

 SwitchArgument switchArg2 = this.parser.parseSwitchArgument("longKey");
 assertEquals(-1, switchArg2.getIndex());
 assertEquals("longKey", switchArg2.getKey());
 assertTrue(switchArg2.isLong());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseSwitchArgument_NoMatch() {
 this.parser.parseSwitchArgument("noKey");
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument paramArg1 = this.parser.parseParameterArgument("key1");
 assertEquals(0, paramArg1.getIndex());
 assertEquals("key1", paramArg1.getKey());
 assertEquals("", paramArg1.getValue());

 ParameterArgument paramArg2 = this.parser.parseParameterArgument("key2");
 assertEquals(2, paramArg2.getIndex());
 assertEquals("key2", paramArg2.getKey());
 assertEquals("value2", paramArg2.getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseParameterArgument_NoMatch() {
 this.parser.parseParameterArgument("noKey");
 }

 @Test
 public void testParseParameterArgument_MisMatch() {
 this.args = new String[]{"arg1", "--key1=value1", "--key2Value2"};
 this.parser = new ArgsParser(args);
 ParameterArgument paramArg1 = this.parser.parseParameterArgument("key1");
 assertEquals(0, paramArg1.getIndex());
 assertEquals("key1", paramArg1.getKey());
 assertEquals("value1", paramArg1.getValue());

 ParameterArgument paramArg2 = this.parser.parseParameterArgument("key2");
 assertEquals(2, paramArg2.getIndex());
 assertEquals("key2", paramArg2.getKey());
 assertEquals("Value2", paramArg2.getValue());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArgs = this.parser.parseStringArgument();
 assertEquals(args.length - 2, stringArgs.size());
 for (int i = 0; i < stringArgs.size(); ++i) {
 StringArgument stringArg = stringArgs.get(i);
 assertEquals(i + 1, stringArg.getIndex());
 assertEquals(args[i + 1], stringArg.getValue());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testParseStringArgument_NoMatch() {
 this.parser = new ArgsParser(new String[0]);
 this.parser.parseStringArgument();
 }
}