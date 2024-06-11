import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

 private MapperConfig<?> config;
 private AnnotationIntrospector ai;

 @Before
 public void setUp() {
 config = new MapperConfig();
 ai = config.getAnnotationIntrospector();
 }

 @Test
 public void testRegisterSubtypesByClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> parentClass = Parent.class;
 Class<?> subtype1 = Subtype1.class;
 Class<?> subtype2 = Subtype2.class;

 resolver.registerSubtypes(subtype1, subtype2);
 JavaType baseType = config.typeFactory().constructType(parentClass);
 List<NamedType> registeredSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);

 assertEquals(2, registeredSubtypes.size());
 assertTrue(registeredSubtypes.contains(new NamedType(subtype1, null)));
 assertTrue(registeredSubtypes.contains(new NamedType(subtype2, null)));
 }

 @Test
 public void testRegisterSubtypesByClassWithAnnotation() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> parentClass = Parent.class;
 Class<?> subtype1 = Subtype1Annotated.class;

 resolver.registerSubtypes(parentClass);
 List<NamedType> registeredSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);

 assertEquals(1, registeredSubtypes.size());
 assertTrue(registeredSubtypes.contains(new NamedType(subtype1, null)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterSubtypesNull() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 resolver.registerSubtypes(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterSubtypesEmptyArray() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 resolver.registerSubtypes(new Class<?>[0]);
 }

 @Test
 public void testRegisterSubtypesByName() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> parentClass = Parent.class;
 String subtype1Name = "Subtype1";
 String subtype2Name = "Subtype2";

 resolver.registerSubtypes(parentClass);
 List<NamedType> registeredSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);

 assertEquals(2, registeredSubtypes.size());
 assertTrue(registeredSubtypes.contains(new NamedType(Parent.class, subtype1Name)));
 assertTrue(registeredSubtypes.contains(new NamedType(Parent.class, subtype2Name)));
 }

 @Test
 public void testRegisterSubtypesByNameWithClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> parentClass = Parent.class;
 Class<?> subtype1 = Subtype1.class;
 String subtype1Name = "Subtype1";

 resolver.registerSubtypes(parentClass, subtype1.getName());
 List<NamedType> registeredSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);

 assertEquals(1, registeredSubtypes.size());
 assertTrue(registeredSubtypes.contains(new NamedType(subtype1, subtype1Name)));
 }

 @Test
 public void testCollectAndResolveSubtypesByClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 Parent parent = new Parent();
 List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, parent.getClass());

 assertEquals(2, collectedSubtypes.size());
 assertTrue(collectedSubtypes.contains(new NamedType(Subtype1.class, null)));
 assertTrue(collectedSubtypes.contains(new NamedType(Subtype2.class, null)));
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithAnnotation() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, parent.getClass());

 assertEquals(1, collectedSubtypes.size());
 assertTrue(collectedSubtypes.contains(new NamedType(Subtype1Annotated.class, null)));
 }

 @Test
 public void testCollectAndResolveSubtypesByClassNull() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);
 assertTrue(collectedSubtypes.isEmpty());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 Parent parent = new Parent();
 List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, parent.getClass());

 assertEquals(2, collectedSubtypes.size());
 assertTrue(collectedSubtypes.contains(new NamedType(Subtype1.class, null)));
 assertTrue(collectedSubtypes.contains(new NamedType(Subtype2.class, null)));
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithAnnotation() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, parent.getClass());

 assertEquals(1, collectedSubtypes.size());
 assertTrue(collectedSubtypes.contains(new NamedType(Subtype1Annotated.class, null)));
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdNull() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
 assertTrue(collectedSubtypes.isEmpty());
 }
}