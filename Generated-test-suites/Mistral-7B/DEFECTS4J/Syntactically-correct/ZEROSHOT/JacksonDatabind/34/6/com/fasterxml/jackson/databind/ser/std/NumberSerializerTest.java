package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Unit tests for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

	private ObjectMapper mapper = new ObjectMapper();
	private JsonNodeFactory nodeFactory = mapper.getNodeFactory();

	@Test
	public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = new BigDecimal("123.456");
		serializer.serialize(value, gen, null);
		assertEquals("123.456", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeBigInteger() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = new BigInteger("123456789012345678901234567890");
		serializer.serialize(value, gen, null);
		assertEquals("123456789012345678901234567890", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeInteger() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = 123;
		serializer.serialize(value, gen, null);
		assertEquals("123", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeLong() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = 123456789012345L;
		serializer.serialize(value, gen, null);
		assertEquals("123456789012345", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeDouble() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = 123.456;
		serializer.serialize(value, gen, null);
		assertEquals("123.456", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeFloat() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = 123.456f;
		serializer.serialize(value, gen, null);
		assertEquals("123.456", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeByte() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = (byte) 123;
		serializer.serialize(value, gen, null);
		assertEquals("123", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeShort() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Number value = (short) 123;
		serializer.serialize(value, gen, null);
		assertEquals("123", gen.getOutputContentAsString());
	}

	@Test
	public void testSerializeOther() throws JsonGenerationException, IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
		Object value = new Object();
		serializer.serialize(value, gen, null);
		assertEquals("null", gen.getOutputContentAsString());
	}

	@Test
	public void testDeserializeBigDecimal() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123.456}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals(new BigDecimal("123.456"), number);
	}

	@Test
	public void testDeserializeBigInteger() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123456789012345678901234567890}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals(new BigInteger("123456789012345678901234567890"), number);
	}

	@Test
	public void testDeserializeInteger() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals(123, number.intValue());
	}

	@Test
	public void testDeserializeLong() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123456789012345}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals(123456789012345L, number.longValue());
	}

	@Test
	public void testDeserializeDouble() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123.456}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals(123.456, number.doubleValue(), 0.001);
	}

	@Test
	public void testDeserializeFloat() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123.456}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals(123.456f, number.floatValue(), 0.001f);
	}

	@Test
	public void testDeserializeByte() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals((byte) 123, number.byteValue());
	}

	@Test
	public void testDeserializeShort() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":123}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertEquals((short) 123, number.shortValue());
	}

	@Test
	public void testDeserializeOther() throws IOException {
		JsonNode rootNode = mapper.readTree("{\"n\":null}");
		ObjectReader reader = mapper.getFactory().getValueFactory().asNumberReader(null, null);
		Number number = reader.readValue(rootNode.get("n"), Number.class);
		assertNull(number);
	}

	@Test
	public void testGetSchema() throws IOException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonSchema schema = serializer.getSchema(null, null);
		assertEquals("number", schema.getSchemaName());
		assertTrue(schema.isContainerNode());
		assertEquals("integer", schema.getSchemaDescription().get("type"));
		assertFalse(serializer._isInt);

		serializer = new NumberSerializer(Integer.class);
		schema = serializer.getSchema(null, null);
		assertEquals("integer", schema.getSchemaName());
		assertFalse(serializer._isInt);

		serializer = new NumberSerializer(BigInteger.class);
		schema = serializer.getSchema(null, null);
		assertEquals("integer", schema.getSchemaName());
		assertTrue(serializer._isInt);
	}

	@Test
	public void testAcceptJsonFormatVisitor() throws JsonMappingException {
		NumberSerializer serializer = NumberSerializer.instance;
		JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(mapper.getFactory().getGeneratorFactory(), null, nodeFactory);
		serializer.acceptJsonFormatVisitor(visitor, null);
		assertNull(visitor.getCurrentValue());
		assertTrue(visitor.hasIntegerFormat());
		assertTrue(visitor.hasNumberFormat());
		assertFalse(visitor.hasTextFormat());

		serializer = new NumberSerializer(Integer.class);
		visitor = new JsonFormatVisitorWrapper(mapper.getFactory().getGeneratorFactory(), null, nodeFactory);
		serializer.acceptJsonFormatVisitor(visitor, null);
		assertNull(visitor.getCurrentValue());
		assertTrue(visitor.hasIntegerFormat());
		assertTrue(visitor.hasNumberFormat());
		assertFalse(visitor.hasTextFormat());

		serializer = new NumberSerializer(BigDecimal.class);
		visitor = new JsonFormatVisitorWrapper(mapper.getFactory().getGeneratorFactory(), null, nodeFactory);
		serializer.acceptJsonFormatVisitor(visitor, null);
		assertNull(visitor.getCurrentValue());
		assertTrue(visitor.hasNumberFormat());
		assertTrue(visitor.hasTextFormat());
		assertFalse(visitor.hasIntegerFormat());
	}
}