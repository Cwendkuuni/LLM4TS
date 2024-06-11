package de.devcity.parseargs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

	@Test
	public void testArgsParserCreationWithNullArgs() {
		ArgsParser argsParser = new ArgsParser();
		assertNull(argsParser.getArgs());
		assertEquals(0, argsParser.getArgsCount());
	}

	@Test
	public void testArgsParserCreationWithArgs() {
		String[] args = {"arg1", "arg2", "arg3"};
		ArgsParser argsParser = new ArgsParser(args);
		assertEquals(args, argsParser.getArgs());
		assertEquals(args.length, argsParser.getArgsCount());
	}

	@Test
	public void testGetArgsLeftCount() {
		ArgsParser argsParser = new ArgsParser(new String[]{"--key1", "value1", "--key2", "value2", "arg3"});
		assertEquals(2, argsParser.getArgsLeftCount());
	}

	@Test
	public void testParseSwitchArgument() {
		ArgsParser argsParser = new ArgsParser(new String[]{"--key", "arg1", "--key", "arg2"});
		SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("key");
		assertEquals(0, switchArgument1.getIndex());
		assertTrue(switchArgument1.isLong());

		SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("key");
		assertEquals(1, switchArgument2.getIndex());
		assertTrue(switchArgument2.isLong());

		SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("nonExistentKey");
		assertEquals(-1, switchArgument3.getIndex());
		assertFalse(switchArgument3.isLong());
	}

	@Test
	public void testParseParameterArgument() {
		ArgsParser argsParser = new ArgsParser(new String[]{"--key=value1", "--key2=value2", "arg3"});
		ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("key");
		assertEquals(0, parameterArgument1.getIndex());
		assertEquals("value1", parameterArgument1.getValue());

		ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("key2");
		assertEquals(1, parameterArgument2.getIndex());
		assertEquals("value2", parameterArgument2.getValue());

		ParameterArgument parameterArgument3 = argsParser.parseParameterArgument("nonExistentKey");
		assertNull(parameterArgument3);
	}

	@Test
	public void testParseStringArgument() {
		ArgsParser argsParser = new ArgsParser(new String[]{"--key1=value1", "--key2=value2", "--key3=value3"});
		List<StringArgument> stringArguments = argsParser.parseStringArgument();
		assertEquals(3, stringArguments.size());
		StringArgument stringArgument1 = stringArguments.get(0);
		assertEquals(0, stringArgument1.getIndex());
		assertEquals("value1", stringArgument1.getValue());

		StringArgument stringArgument2 = stringArguments.get(1);
		assertEquals(1, stringArgument2.getIndex());
		assertEquals("value2", stringArgument2.getValue());

		StringArgument stringArgument3 = stringArguments.get(2);
		assertEquals(2, stringArgument3.getIndex());
		assertEquals("value3", stringArgument3.getValue());
	}

	@Test
	public void testParseStringArgumentWithLongKey() {
		ArgsParser argsParser = new ArgsParser(new String[]{"--longKey=value1", "--longKey2=value2", "--longKey3=value3"});
		List<StringArgument> stringArguments = argsParser.parseStringArgument();
		assertEquals(3, stringArguments.size());
		StringArgument stringArgument1 = stringArguments.get(0);
		assertEquals(0, stringArgument1.getIndex());
		assertEquals("value1", stringArgument1.getValue());

		StringArgument stringArgument2 = stringArguments.get(1);
		assertEquals(1, stringArgument2.getIndex());
		assertEquals("value2", stringArgument2.getValue());

		StringArgument stringArgument3 = stringArguments.get(2);
		assertEquals(2, stringArgument3.getIndex());
		assertEquals("value3", stringArgument3.getValue());
	}
}