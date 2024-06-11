import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfo() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.Identity.class);

 assertEquals("id", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.Identity.class, objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.Identity.class, true);

 assertEquals("id", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.Identity.class, objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.Identity.class, false, CustomObjectIdResolver.class);

 assertEquals("id", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.Identity.class, objectIdInfo.getGeneratorType());
 assertSame(CustomObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdAndResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.Identity.class, true, CustomObjectIdResolver.class);

 assertEquals("id", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.Identity.class, objectIdInfo.getGeneratorType());
 assertSame(CustomObjectIdResolver.class, objectIdInfo.getResolverType());
 assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.Identity.class);

 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IdTypeIdentity", objectIdInfo.toString());
 }
}