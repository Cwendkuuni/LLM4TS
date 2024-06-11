package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;

public class ArgsParserTest {

 private ArgsParser parser;

 @SuppressWarnings("static-access")
 @Before
 public void setUp() {
 String[] args = {"arg1", "-s", "arg3", "--longArg", "arg5", "-l", "arg6", "--longArg2", "arg8"};
 this.parser = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(9, this.parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 String[] expectedArgs = {"arg1", "-s", "arg3", "--longArg", "arg5", "-l", "--longArg2", "arg8"};
 assertArrayEquals(expectedArgs, this.parser.getArgs());
 }

 @Test
 public void testSetArgs() {
 String[] testArgs = {"testArg1", "-t", "testArg3", "--testLongArg", "testArg5", "-l", "testArg6", "--testLongArg2", "testArg8"};
 this.parser.setArgs(testArgs);
 String[] expectedArgs2 = {"testArg1", "-t", "testArg3", "--testLongArg", "testArg5", "-l", "--testLongArg2", "testArg8"};
 assertArrayEquals(expectedArgs2, this.parser.getArgs());
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(4, this.parser.getArgsLeftCount());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 this.parser.setArgs(null);
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument expectedSwitchArgumentShort = new SwitchArgument(1, "s", true);
 SwitchArgument expectedSwitchArgumentLong = new SwitchArgument(6, "longArg", true);
 SwitchArgument switchArgumentShort = this.parser.parseSwitchArgument("s");
 SwitchArgument switchArgumentLong = this.parser.parseSwitchArgument("longArg");
 assertEquals(expectedSwitchArgumentShort, switchArgumentShort);
 assertEquals(expectedSwitchArgumentLong, switchArgumentLong);
 }

 @Test(expected = Exception.class)
 public void testParseSwitchArgumentNotFoundShort() {
 this.parser.parseSwitchArgument("notFoundShort");
 }

 @Test(expected = Exception.class)
 public void testParseSwitchArgumentNotFoundLong() {
 this.parser.parseSwitchArgument("--notFoundLongArg");
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument expectedParameterArgumentShort = new ParameterArgument(2, "l", "value");
 ParameterArgument parameterArgumentShort = this.parser.parseParameterArgument("l");
 assertEquals(expectedParameterArgumentShort, parameterArgumentShort);
 }

 @Test
 public void testParseParameterArgumentLong() {
 ParameterArgument expectedParameterArgumentLong = new ParameterArgument(7, "longArg", "value2");
 ParameterArgument parameterArgumentLong = this.parser.parseParameterArgument("longArg");
 assertEquals(expectedParameterArgumentLong, parameterArgumentLong);
 }

 @Test(expected = Exception.class)
 public void testParseParameterArgumentNotFoundShort() {
 this.parser.parseParameterArgument("notFoundShort");
 }

 @Test(expected = Exception.class)
 public void testParseParameterArgumentNotFoundLong() {
 this.parser.parseParameterArgument("--notFoundLongArg");
 }

 @Test(expected = Exception.class)
 public void testParseParameterArgumentWithNoValueShort() {
 this.parser.parseParameterArgument("l");
 }

 @Test(expected = Exception.class)
 public void testParseParameterArgumentWithNoValueLong() {
 this.parser.parseParameterArgument("--longArg");
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> expectedStringArguments = new ArrayList<>();
 expectedStringArguments.add(new StringArgument(3, "value"));
 expectedStringArguments.add(new StringArgument(8, "value2"));
 List<StringArgument> stringArguments = this.parser.parseStringArgument();
 assertEquals(expectedStringArguments, stringArguments);
 }

 @Test(expected = Exception.class)
 public void testParseStringArgumentEmptyString() {
 this.parser.parseStringArgument();
 }

 @Test(expected = Exception.class)
 public void testParseStringArgumentNull() {
 this.parser.parseStringArgument(null);
 }
}