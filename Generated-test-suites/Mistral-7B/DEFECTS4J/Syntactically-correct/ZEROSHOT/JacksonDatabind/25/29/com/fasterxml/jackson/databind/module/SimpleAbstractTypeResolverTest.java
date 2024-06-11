import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

 private ObjectMapper objectMapper;
 private SimpleAbstractTypeResolver simpleAbstractTypeResolver;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
 simpleAbstractTypeResolver.addMapping(Collection.class, LinkedList.class);
 simpleAbstractTypeResolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testSimpleAbstractTypeResolver_findTypeMapping() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType expectedJavaType = TypeFactory.defaultInstance().constructType(LinkedList.class);
 JavaType actualJavaType = simpleAbstractTypeResolver.findTypeMapping(objectMapper.getDeserializationConfig(), javaType);
 assertEquals(expectedJavaType, actualJavaType);
 }

 @Test
 public void testSimpleAbstractTypeResolver_findTypeMapping_null() {
 JavaType javaType = null;
 JavaType actualJavaType = simpleAbstractTypeResolver.findTypeMapping(objectMapper.getDeserializationConfig(), javaType);
 assertNull(actualJavaType);
 }

 @Test
 public void testSimpleAbstractTypeResolver_findTypeMapping_wrongType() throws JsonMappingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType actualJavaType = simpleAbstractTypeResolver.findTypeMapping(objectMapper.getDeserializationConfig(), javaType);
 assertNull(actualJavaType);
 }

 @Test
 public void testSimpleAbstractTypeResolver_resolveAbstractType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType expectedJavaType = null;
 JavaType actualJavaType = simpleAbstractTypeResolver.resolveAbstractType(objectMapper.getDeserializationConfig(), javaType);
 assertEquals(expectedJavaType, actualJavaType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSimpleAbstractTypeResolver_resolveAbstractType_exception() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 simpleAbstractTypeResolver.resolveAbstractType(objectMapper.getDeserializationConfig(), javaType);
 }

 @Test
 public void testSimpleAbstractTypeResolver_isEmpty() {
 Module module = new SimpleModule();
 module.addHandler(SimpleAbstractTypeResolver.class);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = objectMapper.getDeserializationConfig().getDefaultTyping();
 ObjectMapper.DefaultTypingResolver originalDefaultTypingResolver = new ObjectMapper().getDefaultTyping();
 ObjectMapper objectMapperWithEmptySimpleAbstractTypeResolver = new ObjectMapper();
 objectMapperWithEmptySimpleAbstractTypeResolver.getDeserializationConfig().setDefaultTyping(new SimpleAbstractTypeResolver());
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
 JavaType actualJavaType = defaultTypingResolver.resolvePropertyType(objectMapper.getDeserializationConfig(), javaType);
 JavaType expectedJavaType = originalDefaultTypingResolver.resolvePropertyType(objectMapper.getDeserializationConfig(), javaType);
 MapType actualMapType = (MapType) actualJavaType;
 MapType expectedMapType = (MapType) expectedJavaType;
 assertEquals(expectedMapType.getTypeFactory().constructCollectionType(LinkedHashMap.class, String.class), actualMapType.getTypeFactory().constructCollectionType(LinkedHashMap.class, String.class));
 JavaType actualJavaTypeWithEmptySimpleAbstractTypeResolver = objectMapperWithEmptySimpleAbstractTypeResolver.getDeserializationConfig().getDefaultTyping().resolvePropertyType(objectMapperWithEmptySimpleAbstractTypeResolver, javaType);
 assertEquals(expectedJavaType, actualJavaTypeWithEmptySimpleAbstractTypeResolver);
 }

 @Test
 public void testSimpleAbstractTypeResolver_notEmpty() {
 Module module = new SimpleModule();
 module.addHandler(SimpleAbstractTypeResolver.class);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = objectMapper.getDeserializationConfig().getDefaultTyping();
 ObjectMapper.DefaultTypingResolver originalDefaultTypingResolver = new ObjectMapper().getDefaultTyping();
 SimpleAbstractTypeResolver simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
 simpleAbstractTypeResolver.addMapping(Collection.class, LinkedList.class);
 simpleAbstractTypeResolver.addMapping(List.class, LinkedList.class);
 ObjectMapper objectMapperWithSimpleAbstractTypeResolver = new ObjectMapper();
 objectMapperWithSimpleAbstractTypeResolver.getDeserializationConfig().setDefaultTyping(simpleAbstractTypeResolver);
 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType actualJavaType = defaultTypingResolver.resolvePropertyType(objectMapper.getDeserializationConfig(), javaType);
 JavaType expectedJavaType = originalDefaultTypingResolver.resolvePropertyType(objectMapper.getDeserializationConfig(), javaType);
 JavaType actualJavaTypeWithSimpleAbstractTypeResolver = objectMapperWithSimpleAbstractTypeResolver.getDeserializationConfig().getDefaultTyping().resolvePropertyType(objectMapperWithSimpleAbstractTypeResolver, javaType);
 assertEquals(expectedJavaType.narrowBy(LinkedList.class), actualJavaTypeWithSimpleAbstractTypeResolver);
 }
}