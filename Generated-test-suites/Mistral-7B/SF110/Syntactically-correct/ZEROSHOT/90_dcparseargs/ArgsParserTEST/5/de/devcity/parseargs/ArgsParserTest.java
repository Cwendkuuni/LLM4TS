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

	@Test
	public void testArgsParserWithEmptyArgs() {
		ArgsParser argsParser = new ArgsParser();
		String[] args = {};
		ArgsParser argParser = new ArgsParser(args);
		assertEquals(0, argParser.getArgsCount());
		assertEquals(0, argParser.getArgsLeftCount());
	}

	@Test
	public void testArgsParserWithValidArgs() {
		ArgsParser argsParser = new ArgsParser();
		String[] args = { "-a", "--b=123", "-c=456", "--d", "e" };
		ArgsParser argParser = new ArgsParser(args);
		assertEquals(5, argParser.getArgsCount());
		assertEquals(5, argParser.getArgsLeftCount());

		SwitchArgument switchArgumentA = argParser.parseSwitchArgument("-a");
		assertEquals(-1, switchArgumentA.getIndex());
		assertFalse(switchArgumentA.isWithValue());

		ParameterArgument parameterArgumentB = argParser.parseParameterArgument("--b");
		assertEquals(-1, parameterArgumentB.getIndex());
		assertNotNull(parameterArgumentB.getValue());
		assertEquals("123", parameterArgumentB.getValue());

		SwitchArgument switchArgumentC = argParser.parseSwitchArgument("-c");
		assertEquals(-1, switchArgumentC.getIndex());
		assertFalse(switchArgumentC.isWithValue());

		ParameterArgument parameterArgumentD = argParser.parseParameterArgument("--d");
		assertEquals(-1, parameterArgumentD.getIndex());
		assertNull(parameterArgumentD.getValue());

		StringArgument stringArgumentE = argParser.parseStringArgument().get(0);
		assertEquals(-1, stringArgumentE.getIndex());
		assertNotNull(stringArgumentE.getValue());
		assertEquals("e", stringArgumentE.getValue());
	}

	@Test(expected = NullPointerException.class)
	public void testArgsParserWithNullArgs() {
		ArgsParser argsParser = new ArgsParser();
		ArgsParser argParser = argsParser.new ArgsParser(null);
	}

	@Test
	public void testArgsParserWithShortAndLongKeys() {
		ArgsParser argsParser = new ArgsParser();
		String[] args = { "-a", "--b=123", "-c=456", "--d=789", "e" };
		ArgsParser argParser = new ArgsParser(args);
		assertEquals(6, argParser.getArgsCount());
		assertEquals(6, argParser.getArgsLeftCount());

		SwitchArgument switchArgumentA = argParser.parseSwitchArgument("-a");
		assertEquals(0, switchArgumentA.getIndex());
		assertFalse(switchArgumentA.isWithValue());

		ParameterArgument parameterArgumentB = argParser.parseParameterArgument("--b");
		assertEquals(1, parameterArgumentB.getIndex());
		assertNotNull(parameterArgumentB.getValue());
		assertEquals("123", parameterArgumentB.getValue());

		SwitchArgument switchArgumentC = argParser.parseSwitchArgument("-c");
		assertEquals(2, switchArgumentC.getIndex());
		assertFalse(switchArgumentC.isWithValue());

		ParameterArgument parameterArgumentD = argParser.parseParameterArgument("--d");
		assertEquals(4, parameterArgumentD.getIndex());
		assertNotNull(parameterArgumentD.getValue());
		assertEquals("789", parameterArgumentD.getValue());

		List<StringArgument> stringArguments = argParser.parseStringArgument();
		assertEquals(1, stringArguments.size());
		StringArgument stringArgumentE = stringArguments.get(0);
		assertEquals(5, stringArgumentE.getIndex());
		assertNotNull(stringArgumentE.getValue());
		assertEquals("e", stringArgumentE.getValue());
	}

	@Test
	public void testArgsParserWithOnlySwitchArguments() {
		ArgsParser argsParser = new ArgsParser();
		String[] args = { "-a", "-b", "-c", "-d" };
		ArgsParser argParser = new ArgsParser(args);
		assertEquals(4, argParser.getArgsCount());
		assertEquals(4, argParser.getArgsLeftCount());

		List<SwitchArgument> switchArguments = new ArrayList<>();
		switchArguments.add(argParser.parseSwitchArgument("-a"));
		switchArguments.add(argParser.parseSwitchArgument("-b"));
		switchArguments.add(argParser.parseSwitchArgument("-c"));
		switchArguments.add(argParser.parseSwitchArgument("-d"));

		assertEquals(4, switchArguments.size());
		for (SwitchArgument switchArgument : switchArguments) {
			assertTrue(switchArgument.isWithValue());
		}
	}

	@Test
	public void testArgsParserWithOnlyStringArguments() {
		ArgsParser argsParser = new ArgsParser();
		String[] args = { "arg1", "arg2", "arg3" };
		ArgsParser argParser = new ArgsParser(args);
		assertEquals(3, argParser.getArgsCount());
		assertEquals(3, argParser.getArgsLeftCount());

		List<StringArgument> stringArguments = argParser.parseStringArgument();
		assertEquals(3, stringArguments.size());
		for (StringArgument stringArgument : stringArguments) {
			assertEquals(args[stringArgument.getIndex()], stringArgument.getValue());
		}
	}

	@Test
	public void testArgsParserWithMixedArguments() {
		ArgsParser argsParser = new ArgsParser();
		String[] args = { "-a", "--b=123", "-c=456", "--d=789", "e", "arg1", "arg2" };
		ArgsParser argParser = new ArgsParser(args);
		assertEquals(8, argParser.getArgsCount());
		assertEquals(8, argParser.getArgsLeftCount());

		SwitchArgument switchArgumentA = argParser.parseSwitchArgument("-a");
		assertEquals(0, switchArgumentA.getIndex());
		assertFalse(switchArgumentA.isWithValue());

		ParameterArgument parameterArgumentB = argParser.parseParameterArgument("--b");
		assertEquals(1, parameterArgumentB.getIndex());
		assertNotNull(parameterArgumentB.getValue());
		assertEquals("123", parameterArgumentB.getValue());

		SwitchArgument switchArgumentC = argParser.parseSwitchArgument("-c");
		assertEquals(3, switchArgumentC.getIndex());
		assertFalse(switchArgumentC.isWithValue());

		ParameterArgument parameterArgumentD = argParser.parseParameterArgument("--d");
		assertEquals(5, parameterArgumentD.getIndex());
		assertNotNull(parameterArgumentD.getValue());
		assertEquals("789", parameterArgumentD.getValue());

		List<StringArgument> stringArguments = argParser.parseStringArgument();
		assertEquals(3, stringArguments.size());
		for (int i = 0; i < stringArguments.size(); ++i) {
			StringArgument stringArgument = stringArguments.get(i);
			assertEquals(i + 6, stringArgument.getIndex());
			assertEquals("arg" + (i + 1), stringArgument.getValue());
		}
	}

	@Test
	public void testArgsParserWithInvalidArguments() {
		ArgsParser argsParser = new ArgsParser();
		String[] args = { "-a", "--b=123", "-c=456", "--d--e=789", "e", "arg1", "arg2" };
		ArgsParser argParser = new ArgsParser(args);
		assertEquals(8, argParser.getArgsCount());
		assertEquals(8, argParser.getArgsLeftCount());

		// Switch arguments with values
		SwitchArgument switchArgumentD = argParser.parseSwitchArgument("--d--");
		assertEquals(-1, switchArgumentD.getIndex());
		assertFalse(switchArgumentD.isWithValue());

		// Unknown switch argument
		SwitchArgument switchArgumentF = argParser.parseSwitchArgument("--f");
		assertEquals(-1, switchArgumentF.getIndex());
		assertFalse(switchArgumentF.isWithValue());

		// Unknown parameter argument
		ParameterArgument parameterArgumentG = argParser.parseParameterArgument("--g");
		assertEquals(-1, parameterArgumentG.getIndex());
		assertNull(parameterArgumentG.getValue());
	}
}