package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private static final String[] ARGS_EMPTY = new String[0];
 private static final String[] ARGS_WITH_SWITCH_ARG = {"-s", "-a"};
 private static final String[] ARGS_WITH_LONG_SWITCH_ARG = {"--long-switch-arg", "arg1", "arg2"};
 private static final String[] ARGS_WITH_PARAMETER_ARG = {"-p", "value1", "value2"};
 private static final String[] ARGS_WITH_STRING_ARG = {"-s", "value"};
 private static final String[] ARGS_WITH_MIXED_ARGS = {"-s", "--long-switch-arg", "-p", "value1", "value2", "--long-switch-arg2", "value3"};

 @Test
 public void test_constructor_empty() {
 ArgsParser parser = new ArgsParser();
 assertEquals(0, parser.getArgsCount());
 assertEquals(0, parser.getArgsLeftCount());
 }

 @Test
 public void test_constructor_with_args() {
 ArgsParser parser = new ArgsParser(ARGS_EMPTY);
 assertEquals(0, parser.getArgsCount());
 assertEquals(0, parser.getArgsLeftCount());

 ArgsParser parserWithArgs = new ArgsParser(ARGS_WITH_SWITCH_ARG);
 assertEquals(ARGS_WITH_SWITCH_ARG.length, parserWithArgs.getArgsCount());
 assertEquals(1, parserWithArgs.getArgsLeftCount());
 }

 @Test
 public void test_set_args() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_EMPTY);
 assertEquals(0, parser.getArgsCount());
 assertEquals(0, parser.getArgsLeftCount());

 ArgsParser parserWithArgs = new ArgsParser();
 parserWithArgs.setArgs(ARGS_WITH_SWITCH_ARG);
 assertEquals(ARGS_WITH_SWITCH_ARG.length, parserWithArgs.getArgsCount());
 assertEquals(1, parserWithArgs.getArgsLeftCount());
 }

 @Test
 public void test_get_args_left_count_when_no_args_left() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_SWITCH_ARG);
 assertEquals(1, parser.getArgsLeftCount());

 ArgsParser parserWithNoArgsLeft = new ArgsParser();
 parserWithNoArgsLeft.setArgs(new String[0]);
 assertEquals(0, parserWithNoArgsLeft.getArgsLeftCount());
 }

 @Test
 public void test_get_args_left_count_when_args_left() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_SWITCH_ARG);
 assertEquals(1, parser.getArgsLeftCount());

 ArgsParser parserWithMultipleArgsLeft = new ArgsParser();
 parserWithMultipleArgsLeft.setArgs(ARGS_WITH_MIXED_ARGS);
 assertEquals(2, parserWithMultipleArgsLeft.getArgsLeftCount());
 }

 @Test
 public void test_parse_switch_argument_when_arg_found() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_SWITCH_ARG);
 SwitchArgument switchArgument = parser.parseSwitchArgument("-s");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isFound());

 parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_SWITCH_ARG);
 switchArgument = parser.parseSwitchArgument("s");
 assertEquals(0, switchArgument.getIndex());
 assertTrue(switchArgument.isFound());

 parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_SWITCH_ARG);
 switchArgument = parser.parseSwitchArgument("a");
 assertEquals(1, switchArgument.getIndex());
 assertTrue(switchArgument.isFound());

 parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_LONG_SWITCH_ARG);
 switchArgument = parser.parseSwitchArgument("--long-switch-arg");
 assertEquals(0, switchArgument.getIndex());
 assertTrue(switchArgument.isFound());
 }

 @Test
 public void test_parse_switch_argument_when_arg_not_found() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_EMPTY);
 SwitchArgument switchArgument = parser.parseSwitchArgument("arg");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isFound());

 parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_SWITCH_ARG);
 switchArgument = parser.parseSwitchArgument("notFound");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isFound());
 }

 @Test
 public void test_parse_parameter_argument_when_arg_found() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_PARAMETER_ARG);
 ParameterArgument parameterArgument = parser.parseParameterArgument("p");
 assertEquals(-1, parameterArgument.getIndex());
 assertFalse(parameterArgument.isFound());

 parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_PARAMETER_ARG);
 parameterArgument = parser.parseParameterArgument("p");
 assertEquals(1, parameterArgument.getIndex());
 assertTrue(parameterArgument.isFound());
 assertEquals("value1", parameterArgument.getValue());

 parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_PARAMETER_ARG);
 parameterArgument = parser.parseParameterArgument("p");
 assertEquals(1, parameterArgument.getIndex());
 assertTrue(parameterArgument.isFound());
 assertEquals("value2", parser.parseParameterArgument("p").getValue());
 }

 @Test
 public void test_parse_parameter_argument_when_arg_not_found() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_EMPTY);
 ParameterArgument parameterArgument = parser.parseParameterArgument("p");
 assertEquals(-1, parameterArgument.getIndex());
 assertFalse(parameterArgument.isFound());

 parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_PARAMETER_ARG);
 parameterArgument = parser.parseParameterArgument("notFound");
 assertEquals(-1, parameterArgument.getIndex());
 assertFalse(parameterArgument.isFound());
 }

 @Test
 public void test_parse_string_argument() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_STRING_ARG);
 StringArgument stringArgument = parser.parseStringArgument(0);
 assertEquals(0, stringArgument.getIndex());
 assertTrue(stringArgument.isFound());
 assertEquals("value", stringArgument.getValue());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void test_parse_string_argument_when_index_out_of_bounds() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_EMPTY);
 parser.parseStringArgument(0);
 }

 @Test
 public void test_parse_string_arguments() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(ARGS_WITH_MIXED_ARGS);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(3, stringArguments.size());

 StringArgument firstStringArgument = stringArguments.get(0);
 assertEquals(0, firstStringArgument.getIndex());
 assertTrue(firstStringArgument.isFound());
 assertEquals("value", firstStringArgument.getValue());

 StringArgument secondStringArgument = stringArguments.get(1);
 assertEquals(3, secondStringArgument.getIndex());
 assertTrue(secondStringArgument.isFound());
 assertEquals("value3", secondStringArgument.getValue());
 }
}