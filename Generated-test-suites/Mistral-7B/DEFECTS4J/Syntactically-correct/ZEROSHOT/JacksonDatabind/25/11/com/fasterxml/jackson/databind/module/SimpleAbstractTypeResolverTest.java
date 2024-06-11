import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

private ObjectMapper objectMapper;
private SimpleAbstractTypeResolver resolver;

@Before
public void setup() {
objectMapper = new ObjectMapper();
resolver = new SimpleAbstractTypeResolver();
resolver.addMapping(Collection.class, LinkedList.class);
resolver.addMapping(List.class, LinkedList.class);
objectMapper.setAnnotationIntrospector(new SimpleBeanPropertyIntrospector());
objectMapper.setTypeResolver(resolver);
}

@After
public void teardown() {
objectMapper = null;
resolver = null;
}

@Test
public void testFindTypeMapping() {
JavaType javaType = new TypeReference<List<String>>() {}.getType();
JavaType expectedJavaType = new TypeReference<LinkedList<String>>() {}.getType();
JavaType actualJavaType = objectMapper.getTypeFactory().findTypeMapping(new DeserializationConfig.Builder().build(), javaType);
assertEquals(expectedJavaType, actualJavaType);
}

@Test
public void testFindTypeMapping_Null() {
JavaType javaType = null;
JavaType actualJavaType = objectMapper.getTypeFactory().findTypeMapping(new DeserializationConfig.Builder().build(), javaType);
assertNull(actualJavaType);
}

@Test
public void testResolveAbstractType() {
JavaType javaType = new TypeReference<Collection<String>>() {}.getType();
JavaType expectedJavaType = null;
JavaType actualJavaType = objectMapper.getTypeFactory().resolveAbstractType(new DeserializationConfig.Builder().build(), javaType);
assertEquals(expectedJavaType, actualJavaType);
}

@Test(expected = IllegalArgumentException.class)
public void testAddMapping_SameClass() {
resolver.addMapping(String.class, String.class);
}

@Test(expected = IllegalArgumentException.class)
public void testAddMapping_InvalidSubType() {
resolver.addMapping(Collection.class, String.class);
}

@Test(expected = IllegalArgumentException.class)
public void testAddMapping_NonAbstractSuperType() {
resolver.addMapping(List.class, LinkedList.class);
resolver.addMapping(ArrayList.class, LinkedList.class); // non-abstract
}
}