package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser parser;

 @Before
 public void setup() {
 this.parser = new ArgsParser(new String[]{"arg1", "--key=value", "--longKey=anotherValue", "-key value2", "arg3"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(5, this.parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(new String[]{"arg1", "--key=value", "--longKey=anotherValue", "-key", "value2", "arg3"}, this.parser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 this.parser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(3, this.parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgumentShortKey() {
 SwitchArgument sw = this.parser.parseSwitchArgument("k");
 assertEquals(0, sw.getIndex());
 assertFalse(sw.isLong());
 }

 @Test
 public void testParseSwitchArgumentLongKey() {
 SwitchArgument sw = this.parser.parseSwitchArgument("--longKey");
 assertEquals(1, sw.getIndex());
 assertTrue(sw.isLong());
 }

 @Test
 public void testParseSwitchArgumentInvalidKey() {
 SwitchArgument sw = this.parser.parseSwitchArgument("invalidKey");
 assertEquals(-1, sw.getIndex());
 assertFalse(sw.isLong());
 }

 @Test
 public void testParseParameterArgumentShortKey() {
 ParameterArgument pa = this.parser.parseParameterArgument("k");
 assertEquals(-1, pa.getIndex());
 assertNull(pa.getValue());
 }

 @Test
 public void testParseParameterArgumentLongKey() {
 ParameterArgument pa = this.parser.parseParameterArgument("--key");
 assertEquals(2, pa.getIndex());
 assertEquals("value2", pa.getValue());
 }

 @Test
 public void testParseParameterArgumentInvalidKey() {
 ParameterArgument pa = this.parser.parseParameterArgument("invalidKey");
 assertEquals(-1, pa.getIndex());
 assertNull(pa.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentMissingValue() {
 this.parser.setArgs(new String[]{"arg1", "--key"});
 ParameterArgument pa = this.parser.parseParameterArgument("--key");
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> list = this.parser.parseStringArgument();
 assertEquals(3, list.size());
 StringArgument sw1 = list.get(0);
 assertEquals(0, sw1.getIndex());
 assertEquals("arg1", sw1.getValue());
 StringArgument sw2 = list.get(1);
 assertEquals(3, sw2.getIndex());
 assertEquals("arg3", sw2.getValue());
 }

 @Test
 public void testParseStringArgumentWithEmptyArg() {
 this.parser.setArgs(new String[]{"arg1", "", "--key=value", "--longKey=anotherValue", "arg3"});
 List<StringArgument> list = this.parser.parseStringArgument();
 assertEquals(3, list.size());
 StringArgument sw1 = list.get(0);
 assertEquals(0, sw1.getIndex());
 assertEquals("arg1", sw1.getValue());
 StringArgument sw2 = list.get(1);
 assertEquals(3, sw2.getIndex());
 assertEquals("arg3", sw2.getValue());
 StringArgument sw3 = list.get(2);
 assertNull(sw3);
 }

 @Test
 public void testParseStringArgumentWithPrefix() {
 this.parser.setArgs(new String[]{"--argPrefix", "arg1", "--key=value", "--longKey=anotherValue", "arg3"});
 List<StringArgument> list = this.parser.parseStringArgument();
 assertEquals(3, list.size());
 StringArgument sw1 = list.get(0);
 assertEquals(1, sw1.getIndex());
 assertEquals("arg1", sw1.getValue());
 StringArgument sw2 = list.get(1);
 assertEquals(4, sw2.getIndex());
 assertEquals("arg3", sw2.getValue());
 StringArgument sw3 = list.get(2);
 assertNull(sw3);
 }

 @Test
 public void testParseStringArgumentWithSuffix() {
 this.parser.setArgs(new String[]{"arg1", "--key=value", "--longKey=anotherValue", "arg3--suffix"});
 List<StringArgument> list = this.parser.parseStringArgument();
 assertEquals(3, list.size());
 StringArgument sw1 = list.get(0);
 assertEquals(0, sw1.getIndex());
 assertEquals("arg1", sw1.getValue());
 StringArgument sw2 = list.get(1);
 assertEquals(3, sw2.getIndex());
 assertEquals("arg3", sw2.getValue());
 StringArgument sw3 = list.get(2);
 assertEquals(4, sw3.getIndex());
 assertEquals("suffix", sw3.getValue());
 }

}