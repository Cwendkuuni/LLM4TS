import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 objectMapper = new ObjectMapper();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 objectMapper.registerModule(new SimpleModule().setAbstractTypeResolver(resolver));
 }

 @Test
 public void testFindTypeMapping() {
 Class<List<Integer>> listType = List.class.getTypeParameters()[0].getType();
 JavaType javaType = objectMapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaType mappedType = resolver.findTypeMapping(objectMapper.getConfig(), javaType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithAbstractType() {
 JavaType abstractType = TypeFactory.defaultInstance().constructType(AbstractList.class);
 JavaType mappedType = resolver.findTypeMapping(objectMapper.getConfig(), abstractType);
 assertEquals(ArrayList.class, mappedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithNoMapping() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType mappedType = resolver.findTypeMapping(objectMapper.getConfig(), javaType);
 assertNull(mappedType);
 }

 @Test
 public void testResolveAbstractType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(AbstractList.class);
 JavaType resolvedType = resolver.resolveAbstractType(objectMapper.getConfig(), javaType);
 assertNull(resolvedType);
 }

 @Test
 public void testResolveAbstractTypeWithMapping() {
 Class<List<String>> listType = List.class.getTypeParameters()[0].getType();
 JavaType abstractType = TypeFactory.defaultInstance().constructType(AbstractList.class, String.class);
 JavaType resolvedType = resolver.resolveAbstractType(objectMapper.getConfig(), abstractType);
 assertEquals(ArrayList.class, resolvedType.getRawClass());
 }
}