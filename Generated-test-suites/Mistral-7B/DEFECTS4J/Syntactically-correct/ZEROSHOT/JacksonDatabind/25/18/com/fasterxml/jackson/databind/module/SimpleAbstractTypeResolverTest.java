import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 objectMapper.activateDefaultTyping(objectMapper.getDefaultTyping(), this.resolver);
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 Class<?> src = Collection.class;
 JavaType type = typeFactory.constructType(src);
 Class<?> dst = resolver.findTypeMapping(null, type).getRawClass();
 assertEquals(LinkedList.class, dst);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidMapping() {
 SimpleAbstractTypeResolver resolverWithError = new SimpleAbstractTypeResolver();
 resolverWithError.addMapping(LinkedList.class, Collection.class);
 JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
 resolverWithError.findTypeMapping(null, type);
 }

 @Test
 public void testFindTypeMappingWithMultipleMappings() {
 SimpleAbstractTypeResolver resolverWithMultipleMappings = new SimpleAbstractTypeResolver();
 resolverWithMultipleMappings.addMapping(Collection.class, LinkedList.class);
 resolverWithMultipleMappings.addMapping(Collection.class, ArrayList.class);
 JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
 Class<?> dst = resolverWithMultipleMappings.findTypeMapping(null, type).getRawClass();
 assertEquals(ArrayList.class, dst);
 }

 @Test
 public void testResolveAbstractType() {
 assertNull(resolver.resolveAbstractType(null, null));
 }
}