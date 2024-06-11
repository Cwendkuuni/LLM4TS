import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory _factory;
 private TypeParser _parser;

 @Before
 public void setup() {
 _factory = new TypeFactory();
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParseEmptyType() {
 String canonical = "";
 JavaType type = _parser.parse(canonical);
 assertNull(type);
 }

 @Test
 public void testParsePrimitiveType() {
 String canonical = "int";
 JavaType type = _parser.parse(canonical);
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseClassType() {
 String canonical = "java.lang.String";
 JavaType type = _parser.parse(canonical);
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testParseArrayType() {
 String canonical = "[Ljava.lang.String;";
 JavaType type = _parser.parse(canonical);
 JavaType componentType = type.getComponentType();
 assertEquals(String.class, componentType.getRawClass());
 }

 @Test
 public void testParseGenericTypeWithNoArguments() {
 String canonical = "java.util.List";
 JavaType type = _parser.parse(canonical);
 assertNotNull(type);
 assertNull(type.getTypeBindings());
 }

 @Test
 public void testParseGenericTypeWithSingleArgument() {
 String canonical = "java.util.List<java.lang.String>";
 JavaType type = _parser.parse(canonical);
 List<JavaType> arguments = type.getTypeBindings().forDeserialization();
 assertEquals(1, arguments.size());
 assertEquals(String.class, arguments.get(0).getRawClass());
 }

 @Test
 public void testParseGenericTypeWithMultipleArguments() {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType type = _parser.parse(canonical);
 List<JavaType> arguments = type.getTypeBindings().forDeserialization();
 assertEquals(2, arguments.size());
 assertEquals(String.class, arguments.get(0).getRawClass());
 List<JavaType> listArguments = arguments.get(1).getTypeBindings().forDeserialization();
 assertEquals(1, listArguments.size());
 assertEquals(Integer.TYPE, listArguments.get(0).getRawClass());
 }

 @Test
 public void testParseNestedGenerics() {
 String canonical = "java.util.Map<java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>, java.lang.Integer>";
 JavaType type = _parser.parse(canonical);
 List<JavaType> arguments = type.getTypeBindings().forDeserialization();
 assertEquals(2, arguments.size());
 JavaType mapType = arguments.get(0);
 List<JavaType> innerArguments = mapType.getTypeBindings().forDeserialization();
 assertEquals(2, innerArguments.size());
 assertEquals(String.class, innerArguments.get(0).getRawClass());
 List<JavaType> listArguments = innerArguments.get(1).getTypeBindings().forDeserialization();
 assertEquals(1, listArguments.size());
 assertEquals(Integer.TYPE, listArguments.get(0).getRawClass());
 JavaType outerArgument = arguments.get(1);
 assertEquals(Integer.TYPE, outerArgument.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>foo>";
 _parser.parse(canonical);
 }
}