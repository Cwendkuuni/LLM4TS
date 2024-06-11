import com.fasterxml.jackson.databind.MapperFactoryManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

public class StdSubtypeResolverTest {

 private StdSubtypeResolver resolver;
 private MapperFactoryManager manager;

 @BeforeEach
 public void setup() {
 resolver = new StdSubtypeResolver();
 manager = new MapperFactoryManager(new AnnotationIntrospector());
 ObjectMapper mapper = manager.getMapper();
 mapper.setSubtypeResolver(resolver);
 }

 /**
 * Test for registerSubtypes method with Classes
 */
 @Test
 public void testRegisterSubtypesWithClasses() {
 Class<?> baseType = String.class;
 Class<?> subtype1 = "package com.test;".equals(baseType.getPackage().getName()) ?
 String.class : Integer.class;
 Class<?> subtype2 = "package com.test;".equals(baseType.getPackage().getName()) ?
 Double.class : String.class;

 resolver.registerSubtypes(subtype1, subtype2);

 // Test collectAndResolveSubtypesByClass method
 JavaType baseTypeJavaType = mapper.getTypeFactory().constructType(baseType);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(manager.getConfig(),
 null, baseTypeJavaType);

 // Verify that subtypes are correctly resolved
 List<String> expectedNames = new ArrayList<>();
 expectedNames.add("Integer");
 expectedNames.add("Double");
 expectedNames.add("String");

 List<String> actualNames = new ArrayList<>();
 for (NamedType namedType : subtypes) {
 actualNames.add(namedType.getName());
 }

 assertEquals(expectedNames, actualNames);
 }

 /**
 * Test for registerSubtypes method with NamedTypes
 */
 @Test
 public void testRegisterSubtypesWithNamedTypes() {
 Class<?> baseType = String.class;
 NamedType subtype1 = new NamedType(Integer.class, "Int");
 NamedType subtype2 = new NamedType(Double.class, "Double");

 resolver.registerSubtypes(subtype1, subtype2);

 // Test collectAndResolveSubtypesByClass method
 JavaType baseTypeJavaType = mapper.getTypeFactory().constructType(baseType);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(manager.getConfig(),
 null, baseTypeJavaType);

 // Verify that subtypes are correctly resolved
 List<String> expectedNames = new ArrayList<>();
 expectedNames.add("Int");
 expectedNames.add("Double");
 expectedNames.add("String");

 List<String> actualNames = new ArrayList<>();
 for (NamedType namedType : subtypes) {
 actualNames.add(namedType.getName());
 }

 assertEquals(expectedNames, actualNames);
 }

 /**
 * Test for collectAndResolveSubtypesByClass method with empty list of subtypes
 */
 @Test
 public void testCollectAndResolveSubtypesByClassWithEmptyList() {
 Class<?> baseType = String.class;

 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(manager.getConfig(),
 null, mapper.getTypeFactory().constructType(baseType));

 // Verify that empty list is returned
 assertTrue(subtypes.isEmpty());
 }

 /**
 * Test for collectAndResolveSubtypesByTypeId method with empty list of subtypes
 */
 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithEmptyList() {
 Class<?> baseType = String.class;

 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(manager.getConfig(),
 null, mapper.getTypeFactory().constructType(baseType));

 // Verify that empty list is returned
 assertTrue(subtypes.isEmpty());
 }

 /**
 * Test for collectAndResolveSubtypesByTypeId method with registered subtypes
 */
 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
 Class<?> baseType = String.class;
 Class<?> subtype1 = "package com.test;".equals(baseType.getPackage().getName()) ?
 String.class : Integer.class;
 Class<?> subtype2 = "package com.test;".equals(baseType.getPackage().getName()) ?
 Double.class : String.class;

 resolver.registerSubtypes(subtype1, subtype2);

 // Test collectAndResolveSubtypesByTypeId method
 JavaType baseTypeJavaType = mapper.getTypeFactory().constructType(baseType);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(manager.getConfig(),
 null, baseTypeJavaType);

 // Verify that subtypes are correctly resolved
 List<String> expectedNames = new ArrayList<>();
 expectedNames.add("Integer");
 expectedNames.add("Double");
 expectedNames.add("String");

 List<String> actualNames = new ArrayList<>();
 for (NamedType namedType : subtypes) {
 actualNames.add(namedType.getName());
 }

 assertEquals(expectedNames, actualNames);
 }

 /**
 * Test for collectAndResolveSubtypesByTypeId method with no registered subtypes
 */
 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithNoRegisteredSubtypes() {
 Class<?> baseType = String.class;

 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(manager.getConfig(),
 null, mapper.getTypeFactory().constructType(baseType));

 // Verify that empty list is returned
 assertTrue(subtypes.isEmpty());
 }

 /**
 * Test for collectAndResolveSubtypesByTypeId method with null baseType
 */
 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() {
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(manager.getConfig(),
 null, null);

 // Verify that empty list is returned
 assertTrue(subtypes.isEmpty());
 }
}