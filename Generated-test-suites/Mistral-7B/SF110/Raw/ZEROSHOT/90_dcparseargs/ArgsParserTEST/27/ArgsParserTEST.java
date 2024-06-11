package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;

public class ArgsParserTest {

 private String[] args = new String[]{"-a", "--long-key", "1", "--long-key=value", "-b", "--key=value", "arg", "--", "--key=value2"};
 private ArgsParser ap;

 @Before
 public void setUp() {
 ap = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(args.length, ap.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(args, ap.getArgs());
 }

 @Test
 public void testSetArgs() {
 String[] newArgs = new String[]{"newArg1", "newArg2"};
 ap.setArgs(newArgs);
 assertNotNull(ap.getArgs());
 assertArrayEquals(newArgs, ap.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 ap.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 int count = 0;
 for (String arg : ap.getArgs()) {
 if (arg != null) {
 count++;
 }
 }
 assertEquals(count, ap.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArg = ap.parseSwitchArgument("-a");
 assertTrue(switchArg.isPresent());
 assertEquals("-a", switchArg.getKey());

 switchArg = ap.parseSwitchArgument("--long-key");
 assertTrue(switchArg.isPresent());
 assertEquals("--long-key", switchArg.getKey());

 switchArg = ap.parseSwitchArgument("--non-existent-key");
 assertFalse(switchArg.isPresent());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument paramArg = ap.parseParameterArgument("--key");
 assertNull(paramArg);

 paramArg = ap.parseParameterArgument("--long-key=value");
 assertNotNull(paramArg);
 assertEquals("--long-key", paramArg.getKey());
 assertEquals("value", paramArg.getValue());
 }

 @Test
 public void testParseParameterArgumentWithShortKey() {
 ParameterArgument paramArg = ap.parseParameterArgument("-b");
 assertNotNull(paramArg);
 assertEquals("-b", paramArg.getKey());
 }

 @Test
 public void testParseParameterArgumentWithValueMissing() {
 ParameterArgument paramArg = ap.parseParameterArgument("--key");
 assertNull(paramArg);
 }

 @Test
 public void testParseParameterArgumentWithValueMissing2() {
 ParameterArgument paramArg = ap.parseParameterArgument("--key=value");
 assertNotNull(paramArg);
 assertEquals("--key", paramArg.getKey());
 assertNull(paramArg.getValue());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArgs = ap.parseStringArgument();
 assertEquals(5, stringArgs.size());
 for (int i = 0; i < stringArgs.size(); i++) {
 assertEquals(args[i], stringArgs.get(i).getValue());
 }
 }

 @Test
 public void testParseStringArgumentWithEmptyArgs() {
 ArgsParser emptyArgsParser = new ArgsParser(new String[0]);
 List<StringArgument> stringArgs = emptyArgsParser.parseStringArgument();
 assertEquals(0, stringArgs.size());
 }
}