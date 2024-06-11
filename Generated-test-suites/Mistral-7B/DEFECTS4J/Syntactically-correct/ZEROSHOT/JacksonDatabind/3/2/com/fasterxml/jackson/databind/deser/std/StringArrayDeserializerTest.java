package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Tests for {@link StringArrayDeserializer}.
 */
public class StringArrayDeserializerTest extends TestBase {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String JSON_STRING = "[\"abc\", \"def\", null, \"ghi\", \"jkl\"]";

	@Before
	public void setUp() throws Exception {
		MAPPER.registerModule(new SimpleModule().addDeserializer(String[].class, StringArrayDeserializer.instance));
	}

	@Test
	public void testDeserializeWithDefaultDeserializer() throws IOException {
		String[] result = MAPPER.readValue(JSON_STRING, String[].class);
		assertArrayEquals(new String[] { "abc", "def", null, "ghi", "jkl" }, result);
	}

	@Test
	public void testDeserializeWithCustomDeserializer() throws IOException {
		String[] result = MAPPER.readValue(JSON_STRING, new CustomStringDeserializer(String.class).getType());
		assertArrayEquals(new String[] { "abc", "def", null, "ghi", "jkl" }, result);
	}

	@Test
	public void testDeserializeWithEmptyArray() throws IOException {
		String[] result = MAPPER.readValue("[]", String[].class);
		assertArrayEquals(new String[0], result);
	}

	@Test
	public void testDeserializeWithNullInput() throws IOException {
		String[] result = MAPPER.readValue(null, String[].class);
		assertArrayEquals(new String[0], result);
	}

	@Test(expected = JsonMappingException.class)
	public void testDeserializeWithInvalidInput() throws IOException {
		MAPPER.readValue("123", String[].class);
	}

	@Test
	public void testDeserializeWithType() throws IOException {
		TypeDescriptor typeDescriptor = new TypeDescriptor().forType(String[].class);
		TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructTypeDeserializer(typeDescriptor);
		String[] result = typeDeserializer.deserializeTypedFromArray(jsonParser(), objectMapper());
		assertArrayEquals(new String[] { "abc", "def", null, "ghi", "jkl" }, result);
	}

	@Test
	public void testCreateContextual() throws Exception {
		JsonDeserializer<String> customDeserializer = new CustomStringDeserializer(String.class);
		StringArrayDeserializer deserializer = new StringArrayDeserializer();
		ContextualDeserializer contextualDeserializer = deserializer.createContextual(objectMapper().getDeserializationContext(), null);
		assertEquals(StringArrayDeserializer.class, contextualDeserializer.getClass());
		JsonDeserializer<?> deser = contextualDeserializer.createContextual(objectMapper().getDeserializationContext(), null);
		assertEquals(customDeserializer, deser);
	}

	private static JsonParser jsonParser() throws IOException {
		return objectMapper().getFactory().createParser(JSON_STRING);
	}

	private static class CustomStringDeserializer extends StdSerializer<String> {
		private final Class<String> _class;

		public CustomStringDeserializer(Class<String> clazz) {
			this(_classIdentifierFrom(clazz));
			_class = clazz;
		}

		public CustomStringDeserializer(ClassIdentifier identifier) {
			this(String.class, identifier);
		}

		public CustomStringDeserializer(Class<String> clazz, ClassIdentifier identifier) {
			super(clazz, identifier);
		}

		@Override
		public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			return p.getValueAsString();
		}
	}
}