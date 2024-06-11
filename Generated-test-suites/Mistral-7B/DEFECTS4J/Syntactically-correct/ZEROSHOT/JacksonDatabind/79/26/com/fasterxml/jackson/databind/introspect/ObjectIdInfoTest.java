import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstructionWithProperties() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, IdGenerator.class, ObjectIdResolver.class);
 assertNotNull(objectIdInfo);
 assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(IdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithNameAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, IdGenerator.class);
 assertNotNull(objectIdInfo);
 assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(IdGenerator.class, objectIdInfo.getGeneratorType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithNameAndScopeAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", Object.class, IdGenerator.class);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, IdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals(expectedObjectIdInfo, objectIdInfo);
 }

 @Test
 public void testObjectIdInfoConstructionWithNameAndScopeAndGeneratorAndAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", Object.class, IdGenerator.class, true);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, IdGenerator.class, true, SimpleObjectIdResolver.class);
 assertEquals(expectedObjectIdInfo, objectIdInfo);
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, IdGenerator.class, false);
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
 assertEquals(objectIdInfo, objectIdInfo);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(objectIdInfo._propertyName, objectIdInfo._scope, objectIdInfo._generator, true, objectIdInfo._resolver);
 assertEquals(expectedObjectIdInfo, newObjectIdInfo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, IdGenerator.class);
 fail("ObjectIdInfo should not accept null PropertyName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, IdGenerator.class);
 fail("ObjectIdInfo should not accept null scope");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null);
 fail("ObjectIdInfo should not accept null generator");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, IdGenerator.class, false);
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withResolver(null);
 fail("ObjectIdInfo should not accept null resolver");
 }
}