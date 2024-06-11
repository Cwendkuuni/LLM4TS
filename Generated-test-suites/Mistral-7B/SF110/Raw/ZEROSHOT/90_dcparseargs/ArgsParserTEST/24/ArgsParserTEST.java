package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.Arguments;
import de.devcity.parseargs.args.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest {

 private String[] args = new String[]{"-a", "--test", "-b", "1", "--long-argument-key", "value", "--another-long-argument-key", "anotherValue"};
 private ArgsParser argsParser;

 @Before
 public void setUp() {
 argsParser = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals("Wrong number of arguments", args.length, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals("Wrong argument array", args, argsParser.getArgs());
 }

 @Test
 public void testParseSwitchArgument() {
 Argument arg;

 // test switch argument with short key
 arg = argsParser.parseSwitchArgument("a");
 assertEquals("Wrong switch argument index", 0, arg.getIndex());
 assertEquals("Wrong switch argument key", "a", arg.getKey());
 assertTrue("Wrong switch argument isSet", arg.isSet());

 // test switch argument with long key
 arg = argsParser.parseSwitchArgument("longArgumentKey");
 assertEquals("Wrong switch argument index", 6, arg.getIndex());
 assertEquals("Wrong switch argument key", "longArgumentKey", arg.getKey());
 assertTrue("Wrong switch argument isSet", arg.isSet());
 }

 @Test
 public void testParseParameterArgument() {
 Argument arg;

 // test parameter argument with short key and value
 arg = argsParser.parseParameterArgument("b");
 assertEquals("Wrong parameter argument index", 2, arg.getIndex());
 assertEquals("Wrong parameter argument key", "b", arg.getKey());
 assertNotNull("Wrong parameter argument value", arg.getValue());
 assertEquals("Wrong parameter argument value", "1", arg.getValue());

 // test parameter argument with long key and value
 arg = argsParser.parseParameterArgument("longArgumentKey");
 assertEquals("Wrong parameter argument index", 6, arg.getIndex());
 assertEquals("Wrong parameter argument key", "longArgumentKey", arg.getKey());
 assertNotNull("Wrong parameter argument value", arg.getValue());
 assertEquals("Wrong parameter argument value", "value", arg.getValue());

 // test parameter argument with long key and no value
 arg = argsParser.parseParameterArgument("anotherLongArgumentKey");
 assertEquals("Wrong parameter argument index", 8, arg.getIndex());
 assertEquals("Wrong parameter argument key", "anotherLongArgumentKey", arg.getKey());
 assertNull("Wrong parameter argument value", arg.getValue());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 List<StringArgument> expectedStringArguments = new ArrayList<StringArgument>();
 expectedStringArguments.add(new StringArgument(3, "1"));
 expectedStringArguments.add(new StringArgument(7, "value"));
 expectedStringArguments.add(new StringArgument(9, "anotherValue"));
 assertEquals("Wrong number of string arguments", expectedStringArguments.size(), stringArguments.size());
 for (int i = 0; i < stringArguments.size(); i++) {
 StringArgument stringArgument = stringArguments.get(i);
 StringArgument expectedStringArgument = expectedStringArguments.get(i);
 assertEquals("Wrong string argument index", expectedStringArgument.getIndex(), stringArgument.getIndex());
 assertEquals("Wrong string argument key", expectedStringArgument.getKey(), stringArgument.getKey());
 assertEquals("Wrong string argument value", expectedStringArgument.getValue(), stringArgument.getValue());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 ArgsParser nullArgsParser = new ArgsParser(null);
 }

}