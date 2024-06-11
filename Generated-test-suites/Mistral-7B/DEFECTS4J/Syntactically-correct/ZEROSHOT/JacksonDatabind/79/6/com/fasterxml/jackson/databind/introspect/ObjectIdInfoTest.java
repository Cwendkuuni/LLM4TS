import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstructor() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class,
 JsonIdentityInfo.None.class);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.JsonIdentityInfo$None, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class,
 JsonIdentityInfo.None.class, true);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.JsonIdentityInfo$None, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class,
 JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.JsonIdentityInfo$None, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testGetters() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class,
 JsonIdentityInfo.None.class, false);
 assertEquals("testPropertyName", objectIdInfo.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(JsonIdentityInfo.None.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class,
 JsonIdentityInfo.None.class, false);
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
 assertNotEquals(objectIdInfo, newObjectIdInfo);
 assertEquals(true, newObjectIdInfo.getAlwaysAsId());
 }
}