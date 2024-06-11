import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectIdInfoTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, IdResolver.class);
 Assert.assertEquals("id", objectIdInfo.getPropertyName().getName());
 Assert.assertEquals(Object.class, objectIdInfo.getScope());
Assert.assertEquals(IdGenerator.class, objectIdInfo.getGeneratorType());
Assert.assertEquals(false, objectIdInfo.getAlwaysAsId());
Assert.assertEquals(IdResolver.class, objectIdInfo.getResolverType());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, true, IdResolver.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsIdFalse = new ObjectIdInfo(objectIdInfo._propertyName, objectIdInfo._scope, objectIdInfo._generator, false, objectIdInfo._resolver);
 Assert.assertNotEquals(objectIdInfo, objectIdInfoWithAlwaysAsIdFalse);
 Assert.assertFalse(objectIdInfoWithAlwaysAsIdFalse.getAlwaysAsId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new ObjectIdInfo(null, Object.class, IdGenerator.class, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullScope() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new ObjectIdInfo(new PropertyName("id"), null, IdGenerator.class, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullGeneratorType() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new ObjectIdInfo(new PropertyName("id"), Object.class, null, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullResolverType() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null));
 }
}