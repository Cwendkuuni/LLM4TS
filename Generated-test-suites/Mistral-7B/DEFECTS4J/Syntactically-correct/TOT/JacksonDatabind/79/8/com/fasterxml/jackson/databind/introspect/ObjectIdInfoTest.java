import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SimpleObjectIdResolver;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 PropertyName propertyName = new PropertyName("id");
 Class<ObjectIdGenerator<Object>> generatorClass = ObjectIdGenerator.IdIntGenerator.class;
 Class<ObjectIdResolver> resolverClass = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, generatorClass, resolverClass);

 assertEquals("id", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.IdIntGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdTrue() {
 PropertyName propertyName = new PropertyName("id");
 Class<ObjectIdGenerator<Object>> generatorClass = ObjectIdGenerator.IdIntGenerator.class;
 Class<ObjectIdResolver> resolverClass = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, generatorClass, resolverClass, true);

 ObjectIdInfo withAlwaysAsIdFalse = new ObjectIdInfo(objectIdInfo.getPropertyName(), objectIdInfo.getScope(), objectIdInfo.getGeneratorType(), false, objectIdInfo.getResolverType());
 assertNotSame(objectIdInfo, withAlwaysAsIdFalse);
 assertFalse(withAlwaysAsIdFalse.getAlwaysAsId());

 ObjectIdInfo withAlwaysAsIdTrue = objectIdInfo.withAlwaysAsId(true);
 assertTrue(withAlwaysAsIdTrue.getAlwaysAsId());
 assertEquals(objectIdInfo, withAlwaysAsIdTrue);
 }

 @Test
 public void testObjectIdInfoWithNullPropertyName() {
 assertNull(new ObjectIdInfo(null, Object.class, ObjectIdGenerator.IdIntGenerator.class, SimpleObjectIdResolver.class));
 }

 @Test
 public void testObjectIdInfoWithNullScope() {
 assertNull(new ObjectIdInfo(new PropertyName("id"), null, ObjectIdGenerator.IdIntGenerator.class, SimpleObjectIdResolver.class));
 }

 @Test
 public void testObjectIdInfoWithNullGeneratorType() {
 assertNull(new ObjectIdInfo(new PropertyName("id"), Object.class, null, SimpleObjectIdResolver.class));
 }

 @Test
 public void testObjectIdInfoWithNullResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.IdIntGenerator.class, null);
 assertNotNull(objectIdInfo.getResolverType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());

 ObjectIdInfo withNullResolverType = new ObjectIdInfo(objectIdInfo.getPropertyName(), objectIdInfo.getScope(), objectIdInfo.getGeneratorType(), false, null);
 assertNull(withNullResolverType.getResolverType());
 }
}