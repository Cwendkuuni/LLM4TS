package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import java.text.Format;
import java.text.ParseException;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTest {

	@Test
	public void testSimpleFormat() throws ParseException {
		ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
		Format[] formats = new Format[2];
		formats[0] = new SimpleDateFormat("dd-MM-yyyy");
		formats[1] = new MessageFormat("{} is {0,number,integer} years old.");
		Map<String, FormatFactory> registry = new ConcurrentHashMap<>();
		registry.put("date", () -> new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH));
		registry.put("message", () -> formats);
		ExtendedMessageFormat extendedFormat = new ExtendedMessageFormat(format.toPattern(), Locale.ENGLISH, registry);

		Object[] args = new Object[] {"John Doe", new Date()};
		String formattedMessage = extendedFormat.format(args);
		String expectedMessage = String.format("Hello John Doe, your age is %d.", new Date().getYear() + 1900);
		assertEquals(expectedMessage, formattedMessage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidFormatArgumentIndex() {
		ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
		ExtendedMessageFormat extendedFormat = new ExtendedMessageFormat(format.toPattern(), Locale.ENGLISH, null);
		Object[] args = new Object[] {"John Doe"};
		extendedFormat.format(args, 3);
	}

	@Test
	public void testNullFormatFactory() {
		ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
		Map<String, FormatFactory> registry = new ConcurrentHashMap<>();
		registry.put("date", null);
		ExtendedMessageFormat extendedFormat = new ExtendedMessageFormat(format.toPattern(), Locale.ENGLISH, registry);
		Object[] args = new Object[] {"John Doe", new Date()};
		try {
			extendedFormat.format(args);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(StringUtils.contains(e.getMessage(), "format factory for key"));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidFormatDescription() {
		ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
		Map<String, FormatFactory> registry = new ConcurrentHashMap<>();
		registry.put("date", () -> new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH));
		registry.put("message", () -> new Object[] {new MessageFormat("{} is {0,number,integer} years old.")});
		ExtendedMessageFormat extendedFormat = new ExtendedMessageFormat(format.toPattern(), Locale.ENGLISH, registry);
		Object[] args = new Object[] {"John Doe", new Date()};
		extendedFormat.applyPattern("Hello {0}, your age is {1} {2}.");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMissingFormatDescription() {
		ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
		Map<String, FormatFactory> registry = new ConcurrentHashMap<>();
		registry.put("date", () -> new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH));
		ExtendedMessageFormat extendedFormat = new ExtendedMessageFormat(format.toPattern(), Locale.ENGLISH, registry);
		Object[] args = new Object[] {"John Doe", new Date()};
		extendedFormat.applyPattern("Hello {2}, your age is {1}.");
	}
}