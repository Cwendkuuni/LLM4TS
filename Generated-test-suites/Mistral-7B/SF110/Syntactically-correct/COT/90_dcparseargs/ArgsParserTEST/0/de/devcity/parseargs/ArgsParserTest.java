package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser parser;

 @Test
 public void testArgsParserConstructor() {
 parser = new ArgsParser(new String[] {});
 assertEquals(0, parser.getArgsCount());
 }

 @Test
 public void testArgsParserSetter() {
 parser = new ArgsParser();
 String[] args = new String[] {"arg1", "--key", "arg3", "--key value", "-key value2", "arg6"};
 parser.setArgs(args);
 assertEquals(args.length, parser.getArgsCount());
 }

 @Test
 public void testGetArgsLeftCountBasic() {
 String[] args = new String[] {"arg1", "--key", "arg3", "--key value", "-key value2", "arg6"};
 parser.setArgs(args);
 assertEquals(5, parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgumentNullKey() {
 parser.setArgs(new String[] {"--key", "arg1"});
 SwitchArgument result = parser.parseSwitchArgument(null);
 assertEquals(-1, result.getIndex());
 }

 @Test
 public void testParseSwitchArgumentShortKey() {
 parser.setArgs(new String[] {"-k", "arg1", "--key", "arg3", "--key value", "-key value2", "arg6"});
 SwitchArgument result = parser.parseSwitchArgument("k");
 assertEquals(2, result.getIndex());
 }

 @Test
 public void testParseSwitchArgumentShortKeyMultiple() {
 parser.setArgs(new String[] {"-k", "arg1", "--key1", "arg3", "--key value", "-k", "--key2", "arg6"});
 List<SwitchArgument> results = new ArrayList<>();
 results.add(parser.parseSwitchArgument("k"));
 results.add(parser.parseSwitchArgument("k"));
 assertEquals(2, results.size());
 }

 @Test
 public void testParseSwitchArgumentLongKey() {
 parser.setArgs(new String[] {"--arg", "arg1", "--arg key", "arg3", "--key value", "-key value2", "arg6"});
 SwitchArgument result = parser.parseSwitchArgument("arg key");
 assertEquals(2, result.getIndex());
 }

 @Test
 public void testParseSwitchArgumentLongKeyMultiple() {
 parser.setArgs(new String[] {"--arg", "arg1", "--arg key1", "arg3", "--key value", "--arg key2", "arg6"});
 List<SwitchArgument> results = new ArrayList<>();
 results.add(parser.parseSwitchArgument("arg key1"));
 results.add(parser.parseSwitchArgument("arg key2"));
 assertEquals(2, results.size());
 }

 @Test
 public void testParseSwitchArgumentMissing() {
 parser.setArgs(new String[] {"--arg", "arg1", "--arg key", "arg3", "--key value", "-key value2", "arg6"});
 SwitchArgument result = parser.parseSwitchArgument("missingKey");
 assertEquals(-1, result.getIndex());
 }

 @Test
 public void testParseParameterArgumentNullKey() {
 parser.setArgs(new String[] {"--key value", "arg1"});
 ParameterArgument result = parser.parseParameterArgument(null);
 assertEquals(-1, result.getIndex());
 }

 @Test
 public void testParseParameterArgumentShortKey() {
 parser.setArgs(new String[] {"--key value", "-k arg", "arg1", "--key value2", "-k value2 arg", "arg6"});
 ParameterArgument result = parser.parseParameterArgument("k");
 assertEquals(2, result.getIndex());
 assertEquals("arg", result.getValue());
 }

 @Test
 public void testParseParameterArgumentShortKeyMultiple() {
 parser.setArgs(new String[] {"--key value", "-k arg1", "--key value1 arg1", "-k arg2", "--key value2 arg2", "arg6"});
 List<ParameterArgument> results = new ArrayList<>();
 results.add(parser.parseParameterArgument("k"));
 results.add(parser.parseParameterArgument("k"));
 assertEquals(2, results.size());
 assertEquals("arg1", results.get(0).getValue());
 assertEquals("arg2", results.get(1).getValue());
 }

 @Test
 public void testParseParameterArgumentLongKey() {
 parser.setArgs(new String[] {"--key value", "--arg key value", "arg1", "--key value2", "--arg key2 value2", "arg6"});
 ParameterArgument result = parser.parseParameterArgument("arg key value");
 assertEquals(2, result.getIndex());
 assertEquals("value", result.getValue());
 }

 @Test
 public void testParseParameterArgumentLongKeyMultiple() {
 parser.setArgs(new String[] {"--key value", "--arg key1 value1", "arg1", "--key value2", "--arg key2 value2", "arg6"});
 List<ParameterArgument> results = new ArrayList<>();
 results.add(parser.parseParameterArgument("arg key1 value1"));
 results.add(parser.parseParameterArgument("arg key2 value2"));
 assertEquals(2, results.size());
 assertEquals("value1", results.get(0).getValue());
 assertEquals("value2", results.get(1).getValue());
 }

 @Test
 public void testParseParameterArgumentMissing() {
 parser.setArgs(new String[] {"--key value", "--arg key1 value1", "arg1", "--key value2", "--arg key2 value2", "arg6"});
 ParameterArgument result = parser.parseParameterArgument("missingKey");
 assertEquals(-1, result.getIndex());
 }

 @Test (expected = NullPointerException.class)
 public void testArgsParserNullArgs() {
 parser = new ArgsParser(null);
 }

 @Test
 public void testParseStringArgumentEmpty() {
 parser.setArgs(new String[] {});
 List<StringArgument> result = parser.parseStringArgument();
 assertEquals(0, result.size());
 }

 @Test
 public void testParseStringArgumentOne() {
 parser.setArgs(new String[] {"arg1"});
 List<StringArgument> result = parser.parseStringArgument();
 assertEquals(1, result.size());
 assertEquals("arg1", result.get(0).getValue());
 }

 @Test
 public void testParseStringArgumentMultiple() {
 parser.setArgs(new String[] {"arg1", "arg2", "arg3"});
 List<StringArgument> result = parser.parseStringArgument();
 assertEquals(3, result.size());
 assertEquals("arg1", result.get(0).getValue());
 assertEquals("arg2", result.get(1).getValue());
 assertEquals("arg3", result.get(2).getValue());
 }

}