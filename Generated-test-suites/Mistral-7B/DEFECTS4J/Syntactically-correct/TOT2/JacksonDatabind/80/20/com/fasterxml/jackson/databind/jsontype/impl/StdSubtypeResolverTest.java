import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

 private MapperConfig<?> config;
 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 config = new MapperFactory().getMapperConfig();
 objectMapper = new ObjectMapper(config);
 }

 @Test
 public void testRegisterSubtypesByClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = String.class;

 resolver.registerSubtypes(subclass1, subclass2);
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.size() == 2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterSubtypesByClassWithNull() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 JavaType baseType = null;
 resolver.registerSubtypes(baseType);
 }

 @Test
 public void testRegisterSubtypesByClassWithEmptyArray() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 JavaType baseType = objectMapper.getTypeFactory().constructType(Object.class);
 resolver.registerSubtypes(new Class<?>[]{});
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.isEmpty());
 }

 @Test
 public void testRegisterSubtypesByClassWithSingleClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass = Integer.class;

 resolver.registerSubtypes(subclass);
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.size() == 1);
 }

 @Test
 public void testRegisterSubtypesByClassWithMultipleClasses() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = String.class;

 resolver.registerSubtypes(subclass1, subclass2);
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.size() == 2);
 }

 @Test
 public void testRegisterSubtypesByAnnotation() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 JavaType baseType = objectMapper.getTypeFactory().constructType(Object.class);
 MyAnnotation annotation = new MyAnnotation();

 resolver.registerSubtypes(MyAnnotation.class);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.size() > 0);
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithEmptySet() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 JavaType baseType = objectMapper.getTypeFactory().constructType(Object.class);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.isEmpty());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithSingleClassSubtype() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass = Integer.class;

 StdSubtypeResolver.registerDefaultSubtypes(config);
 resolver.registerSubtypes(subclass);
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.size() == 1);
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithMultipleClassSubtypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = String.class;

 StdSubtypeResolver.registerDefaultSubtypes(config);
 resolver.registerSubtypes(subclass1, subclass2);
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertTrue(subtypes.size() == 2);
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithEmptyMap() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 JavaType baseType = objectMapper.getTypeFactory().constructType(Object.class);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertTrue(subtypes.isEmpty());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithSingleClassSubtype() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass = Integer.class;

 StdSubtypeResolver.registerDefaultSubtypes(config);
 resolver.registerSubtypes(subclass);
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertTrue(subtypes.size() == 1);
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithMultipleClassSubtypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = String.class;

 StdSubtypeResolver.registerDefaultSubtypes(config);
 resolver.registerSubtypes(subclass1, subclass2);
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertTrue(subtypes.size() == 2);
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithSingleClassSubtypeWithAnnotation() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass = Integer.class;
 MyAnnotation annotation = new MyAnnotation();

 StdSubtypeResolver.registerDefaultSubtypes(config);
 resolver.registerSubtypes(subclass, annotation.getClass());
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertTrue(subtypes.size() > 0);
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithMultipleClassSubtypesWithAnnotation() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = String.class;
 MyAnnotation annotation = new MyAnnotation();

 StdSubtypeResolver.registerDefaultSubtypes(config);
 resolver.registerSubtypes(subclass1, subclass2, annotation.getClass());
 JavaType baseType = objectMapper.getTypeFactory().constructType(superclass);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertTrue(subtypes.size() > 1);
 }
}