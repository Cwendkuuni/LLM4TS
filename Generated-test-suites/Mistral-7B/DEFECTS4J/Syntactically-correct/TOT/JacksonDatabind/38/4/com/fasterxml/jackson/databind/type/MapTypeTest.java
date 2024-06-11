package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializer.EmptyImpl;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapTypeTest {

	@Test
	public void testConstruct() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

		MapType actual = MapType.construct(mapType, bindings, superClass, superInterfaces, keyType, valueType);
		assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", actual.toString());
	}

	@Test
	public void testConstructWithTypeHandler() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object valueHandler = new EmptyImpl();

		MapType actual = MapType.construct(mapType, bindings, superClass, superInterfaces, keyType, valueType, valueHandler);
		assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", actual.toString());
	}

	@Test
	public void testConstructDeprecated() {
		Class<Map<String, Integer>> mapType = Map.class;
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

		MapType actual = MapType.construct(mapType, keyType, valueType);
		assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", actual.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructWithInvalidType() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(HashMap.class);

		MapType actual = MapType.construct(mapType, bindings, superClass, superInterfaces, keyType, valueType);
	}

	@Test
	public void testWithTypeHandler() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object valueHandler = new EmptyImpl();

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, valueHandler, null, false);
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void testWithContentTypeHandler() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object valueHandler = new EmptyImpl();
		Object contentTypeHandler = new EmptyImpl();

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, valueHandler, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType.withTypeHandler(contentTypeHandler), valueHandler, null, false);
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void testWithValueHandler() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object valueHandler = new EmptyImpl();

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, valueHandler, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, valueHandler, false);
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void testWithContentValueHandler() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object valueHandler = new EmptyImpl();
		Object contentValueHandler = new EmptyImpl();

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expectedWithHandler = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, contentValueHandler, false);
		assertEquals(expectedWithHandler.toString(), actual.withContentValueHandler(contentValueHandler).toString());
	}

	@Test
	public void testWithStaticTyping() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType.withStaticTyping(), valueType.withStaticTyping(), null, null, true);
		assertEquals(expected.toString(), actual.withStaticTyping().toString());
	}

	@Test
	public void testWithValueHandlerAndStaticTyping() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object valueHandler = new EmptyImpl();

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, valueHandler, null, true);
		assertEquals(expected.toString(), actual.withValueHandler(valueHandler).withStaticTyping().toString());
	}

	@Test
	public void testWithKeyType() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		JavaType newKeyType = TypeFactory.defaultInstance().constructType(Long.class);

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, newKeyType, valueType, null, null, false);
		assertEquals(expected.toString(), actual.withKeyType(newKeyType).toString());
	}

	@Test
	public void testWithContentType() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		JavaType newContentType = TypeFactory.defaultInstance().constructType(String[].class);

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType, newContentType, null, null, false);
		assertEquals(expected.toString(), actual.withContentType(newContentType).toString());
	}

	@Test
	public void testRefine() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		Class<Map<String, Long>> newMapType = Map.class;
		TypeBindings newBindings = TypeBindings.defaultBindings();
		JavaType newSuperClass = Object.class;
		JavaType[] newSuperInterfaces = new JavaType[0];

		MapType expected = new MapType(newMapType, newBindings, newSuperClass, newSuperInterfaces, keyType, valueType, null, null, false);
		MapType refined = actual.refine(newMapType, newBindings, newSuperClass, newSuperInterfaces);
		assertEquals(expected.toString(), refined.toString());
	}

	@Test
	public void testWithKeyTypeHandler() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object keyTypeHandler = new EmptyImpl();

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType.withTypeHandler(keyTypeHandler), valueType, null, null, false);
		assertEquals(expected.toString(), actual.withKeyTypeHandler(keyTypeHandler).toString());
	}

	@Test
	public void testWithKeyValueHandler() {
		Class<Map<String, Integer>> mapType = Map.class;
		TypeBindings bindings = TypeBindings.defaultBindings();
		JavaType superClass = Object.class;
		JavaType[] superInterfaces = new JavaType[0];
		JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
		JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
		Object keyValueHandler = new EmptyImpl();

		MapType actual = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
		MapType expected = new MapType(mapType, bindings, superClass, superInterfaces, keyType, valueType, null, keyValueHandler, false);
		assertEquals(expected.toString(), actual.withKeyValueHandler(keyValueHandler).toString());
	}
}