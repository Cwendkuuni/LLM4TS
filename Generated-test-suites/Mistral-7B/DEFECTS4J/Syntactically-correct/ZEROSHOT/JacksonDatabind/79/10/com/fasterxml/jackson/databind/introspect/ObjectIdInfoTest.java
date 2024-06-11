import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ObjectIdInfo.class)
public class ObjectIdInfoTest {

 private PropertyName propertyName = new PropertyName("id");
 private Class<String> scope = String.class;
 private Class<ObjectIdGenerator<Object>> generator = SimpleObjectIdGenerator.class;
 private Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithCustomResolver() {
 Class<CustomObjectIdResolver> customResolver = CustomObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, customResolver);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdGenerator, alwaysAsId=false, resolverType=class com.example.CustomObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
 ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdGenerator, alwaysAsId=true", withAlwaysAsId.toString());
 }

 @Test
 public void testGetters() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
 assertEquals(propertyName, objectIdInfo.getPropertyName());
 assertEquals(scope, objectIdInfo.getScope());
 assertEquals(generator, objectIdInfo.getGeneratorType());
 assertEquals(resolver, objectIdInfo.getResolverType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
 String expectedString = "ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdGenerator, alwaysAsId=false";
 assertEquals(expectedString, objectIdInfo.toString());
 }

 @Test
 public void testJsonIdentityInfoWithCustomGeneratorAndResolver() {
 JsonIdentityInfo jsonIdentityInfo = new JsonIdentityInfo.Name("id").generator(generator).resolver(resolver);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(jsonIdentityInfo.getPropertyName(), jsonIdentityInfo.getScope(), jsonIdentityInfo.getGenerator(), jsonIdentityInfo.isAlwaysAsId(), jsonIdentityInfo.getResolver());
 assertEquals("ObjectIdInfo: propName=id, scope=class com.fasterxml.jackson.annotation.JsonIdentityInfo, generatorType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.annotation.SimpleObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testJsonIdentityInfoWithDefaultGeneratorAndCustomResolver() {
 JsonIdentityInfo jsonIdentityInfo = new JsonIdentityInfo.Name("id").resolver(resolver);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(jsonIdentityInfo.getPropertyName(), jsonIdentityInfo.getScope(), null, jsonIdentityInfo.isAlwaysAsId(), jsonIdentityInfo.getResolver());
 assertEquals("ObjectIdInfo: propName=id, scope=class com.fasterxml.jackson.annotation.JsonIdentityInfo, generatorType=null, alwaysAsId=false, resolverType=class com.fasterxml.jackson.annotation.SimpleObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testJsonIdentityInfoWithDefaultGeneratorAndDefaultResolver() {
 JsonIdentityInfo jsonIdentityInfo = new JsonIdentityInfo.Name("id");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(jsonIdentityInfo.getPropertyName(), jsonIdentityInfo.getScope(), null, jsonIdentityInfo.isAlwaysAsId(), null);
 assertEquals("ObjectIdInfo: propName=id, scope=class com.fasterxml.jackson.annotation.JsonIdentityInfo, generatorType=null, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testObjectIdInfoWithCustomGeneratedId() throws Exception {
 ObjectIdGenerator<Object> customIdGenerator = PowerMockito.mock(ObjectIdGenerator.class);
 ObjectIdResolver customIdResolver = PowerMockito.mock(ObjectIdResolver.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
 ObjectIdInfo customObjectIdInfo = new ObjectIdInfo(propertyName, scope, customIdGenerator, false, customIdResolver).withGenerator(customIdGenerator);
 PowerMockito.whenNew(SimpleObjectIdGenerator.class).withArguments(Mockito.any(PropertyName.class), Mockito.any(JavaType.class), Mockito.any(ObjectIdGenerator.class)).thenReturn(customIdGenerator);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class [Ljava.lang.Object;, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", customObjectIdInfo.toString());
 }

}

class SimpleObjectIdGenerator implements ObjectIdGenerator<Object> {
 // empty implementation for testing
}

class CustomObjectIdResolver implements ObjectIdResolver {
 // empty implementation for testing
}