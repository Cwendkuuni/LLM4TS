import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction1() {
 PropertyName propName = new PropertyName("id");
 Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
 Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, Object.class, generator, false, resolver);

 assertEquals("id", objectIdInfo.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstruction2() {
 PropertyName propName = new PropertyName("id");
 Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, Object.class, generator);

 assertEquals("id", objectIdInfo.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstruction3() {
 PropertyName propName = new PropertyName("id");
 Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("id", Object.class, generator);

 assertEquals("id", objectIdInfo.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 PropertyName propName = new PropertyName("id");
 Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerators.IntArrayIdGenerator.class;
 ObjectIdResolver resolver = new SimpleObjectIdResolver();
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, Object.class, generator, true, resolver);
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);

 assertNotSame(objectIdInfo, newObjectIdInfo);
 assertFalse(newObjectIdInfo.getAlwaysAsId());
 }
}