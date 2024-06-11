package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ExternalTypeHandlerTest {

	private static class DummyTypeDeserializer extends TypeDeserializer {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		public DummyTypeDeserializer() {
			super(TypeFactory.defaultInstance().constructType(String.class));
		}

		@Override
		public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
			return p.getText();
		}

		@Override
		public PropertyName getPropertyName() {
			return new PropertyName("typeId");
		}

		@Override
		public boolean isCachable() {
			return true;
		}

		@Override
		public void resolveType(DeserializationContext ctxt, BeanPropertyModel propertyModel) throws JsonMappingException {
			// do nothing
		}
	}

	@SuppressWarnings("unchecked")
	private static class DummyClass {
		private String typeId;
		private String prop1;
		private String prop2;

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

		public String getProp1() {
			return prop1;
		}

		public void setProp1(String prop1) {
			this.prop1 = prop1;
		}

		public String getProp2() {
			return prop2;
		}

		public void setProp2(String prop2) {
			this.prop2 = prop2;
		}
	}

	@Test
	public void testHandleTypePropertyValue() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"prop1\":1,\"typeId\":\"DummyType\"}");
		Object result = handler.start().handleTypePropertyValue(p, context, "prop1", null);
		assertEquals(1, result);
		assertEquals("DummyType", handler.getTypeById(0));

		p.nextToken(); // consume "}";

		result = handler.complete(p, context, null);
		DummyClass d = (DummyClass) result;
		assertEquals(1, d.getProp1());
		assertEquals("DummyType", d.getTypeId());
	}

	@Test
	public void testHandlePropertyValue() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"typeId\":\"DummyType\",\"prop1\":1}");
		Object result = handler.start().handlePropertyValue(p, context, "typeId", null);
		assertEquals("DummyType", result);
		assertEquals("DummyType", handler.getTypeById(0));

		result = handler.complete(p, context, null);
		DummyClass d = (DummyClass) result;
		assertEquals(1, d.getProp1());
		assertEquals("DummyType", d.getTypeId());
	}

	@Test
	public void testHandleMissingTypePropertyValue() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"prop1\":1}");
		try {
			handler.start().handleTypePropertyValue(p, context, "typeId", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("No property 'typeId' found in 'ExternalTypeHandler' for handling", e.getMessage());
		}

		try {
			handler.complete(p, context, null);
			fail();
		} catch (JsonMappingException e) {
			assertEquals("No property 'typeId' found in 'ExternalTypeHandler'", e.getMessage());
		}
	}

	@Test
	public void testHandleMissingPropertyValue() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"prop1\":1}");
		try {
			handler.start().handlePropertyValue(p, context, "prop2", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("No property 'prop2' found in 'ExternalTypeHandler' for handling", e.getMessage());
		}

		try {
			handler.complete(p, context, null);
			fail();
		} catch (JsonMappingException e) {
			assertEquals("No property 'prop2' found in 'ExternalTypeHandler'", e.getMessage());
		}
	}

	@Test
	public void testCompleteNullTypeId() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"prop1\":1}");
		handler.start();
		handler.handlePropertyValue(p, context, "typeId", null);
		try {
			handler.complete(p, context, null);
			fail();
		} catch (JsonMappingException e) {
			assertEquals("Missing external type id property 'typeId'", e.getMessage());
		}
	}

	@Test
	public void testCompleteNullProperty() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"typeId\":\"DummyType\"}");
		handler.start();
		String typeId = handler.getTypeById(0);
		assertEquals("DummyType", typeId);
		handler.handleTypePropertyValue(p, context, "prop1", null);
		try {
			handler.complete(p, context, null);
			fail();
		} catch (JsonMappingException e) {
			assertEquals("Missing property 'prop1' for external type id 'typeId'", e.getMessage());
		}
	}

	@Test
	public void testCompleteMissingTypeId() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"prop1\":1}");
		handler.start();
		try {
			handler.complete(p, context, null);
			fail();
		} catch (JsonMappingException e) {
			assertEquals("Missing external type id property 'typeId'", e.getMessage());
		}
	}

	@Test
	public void testCompleteMissingProperty() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"typeId\":\"DummyType\"}");
		handler.start();
		String typeId = handler.getTypeById(0);
		assertEquals("DummyType", typeId);
		try {
			handler.complete(p, context, null);
			fail();
		} catch (JsonMappingException e) {
			assertEquals("Missing property 'prop1' for external type id 'typeId'", e.getMessage());
		}
	}

	@Test
	public void testCompleteWithCreator() throws IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		DeserializationContext context = mapper.getDeserializationContext();

		Builder builder = new Builder();

		SettableBeanProperty p1 = new SettableBeanProperty("prop1", DummyClass.class, new DummyTypeDeserializer());
		SettableBeanProperty p2 = new SettableBeanProperty("typeId", String.class, new DummyTypeDeserializer());
		SettableBeanProperty p3 = new SettableBeanProperty("creator", DummyClass[].class, new DummyCreator());
		builder.addExternal(p1, new DummyTypeDeserializer());
		builder.addExternal(p2, new DummyTypeDeserializer());
		builder.addExternal(p3, new DummyCreator());

		ExternalTypeHandler handler = builder.build();

		JsonParser p = factory.createParser("{\"typeId\":\"DummyType\",\"prop1\":1,\"creator\":[{\"typeId\":\"Child1\",\"prop1\":2},{\"typeId\":\"Child2\",\"prop1\":3}]}");
		Object result = handler.start().handlePropertyValue(p, context, "typeId", null);
		assertEquals("DummyType", result);
		assertEquals("DummyType", handler.getTypeById(0));

		result = handler.complete(p, context, null);
		DummyClass[] creator = (DummyClass[]) result;
		assertEquals(2, creator.length);
		assertEquals("Child1", creator[0].getTypeId());
		assertEquals(2, creator[0].getProp1());
		assertEquals("Child2", creator[1].getTypeId());
		assertEquals(3, creator[1].getProp1());
	}
}