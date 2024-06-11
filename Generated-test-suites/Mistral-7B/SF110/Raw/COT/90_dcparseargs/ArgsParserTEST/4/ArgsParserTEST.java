package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;

 @Before
 public void setUp() {
 argsParser = new ArgsParser(new String[]{"arg1", "--key=value", "--arg", "-key2=value2"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(4, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 String[] expectedArgs = new String[]{"arg1", "--key=value", "--arg", "-key2=value2"};
 assertArrayEquals(expectedArgs, argsParser.getArgs());
 }

 @Test
 public void testSetArgs() {
 String[] args = new String[]{"arg1", "--key=value", "--arg", "-key2=value2"};
 ArgsParser parser = new ArgsParser();
 parser.setArgs(args);
 assertEquals(4, parser.getArgsCount());
 assertArrayEquals(args, parser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(2, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument keySwitch = argsParser.parseSwitchArgument("key");
 assertEquals(1, keySwitch.getIndex());
 assertEquals("key", keySwitch.getKey());
 assertTrue(keySwitch.isLong());

 SwitchArgument notPresentKeySwitch = argsParser.parseSwitchArgument("notPresentKey");
 assertEquals(-1, notPresentSwitch.getIndex());
 assertEquals("notPresentKey", notPresentSwitch.getKey());
 assertFalse(notPresentSwitch.isLong());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument keyParam = argsParser.parseParameterArgument("key");
 assertEquals(1, keyParam.getIndex());
 assertEquals("value", keyParam.getValue());
 assertEquals("key", keyParam.getKey());

 ParameterArgument notPresentParam = argsParser.parseParameterArgument("notPresentKey");
 assertNull(notPresentParam);
 }

 @Test
 public void testParseParameterArgumentWithLongKey() {
 ArgsParser parser = new ArgsParser(new String[]{"--key=value", "--arg=value2", "-arg"});
 ParameterArgument param = parser.parseParameterArgument("--key");
 assertEquals(0, param.getIndex());
 assertEquals("value", param.getValue());
 assertEquals("key", param.getKey());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseParameterArgumentIndexOutOfBounds() {
 ArgsParser parser = new ArgsParser(new String[]{"arg1", "--key=value", "--arg", "-key2=value2"});
 parser.parseParameterArgument("notPresentKey");
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> expectedArgList = new ArrayList<>();
 expectedArgList.add(new StringArgument(1, "value"));
 expectedArgList.add(new StringArgument(3, "value2"));
 List<StringArgument> result = argsParser.parseStringArgument();
 assertEquals(expectedArgList, result);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseStringArgumentEmptyList() {
 ArgsParser parser = new ArgsParser(new String[0]);
 parser.parseStringArgument();
 }

}