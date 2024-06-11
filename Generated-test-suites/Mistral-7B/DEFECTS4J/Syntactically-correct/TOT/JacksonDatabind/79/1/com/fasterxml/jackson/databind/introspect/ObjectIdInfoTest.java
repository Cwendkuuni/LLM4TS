import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstructorWithPropertyNameAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsonschema.IdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithPropertyNameStringAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class);
 ObjectIdInfo objectIdInfoWithNameString = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsonschema.IdGenerator, alwaysAsId=false", objectIdInfo.toString());
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsonschema.IdGenerator, alwaysAsId=false", objectIdInfoWithNameString.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithPropertyNameStringAndScopeAndAlwaysAsIdFalse() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false);
 ObjectIdInfo objectIdInfoWithNameString = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsonschema.IdGenerator, alwaysAsId=false", objectIdInfo.toString());
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsonschema.IdGenerator, alwaysAsId=false", objectIdInfoWithNameString.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructorWithNullPropertyName() {
 try {
 new ObjectIdInfo(null, Object.class, IdGenerator.class);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructorWithNullScope() {
 try {
 new ObjectIdInfo(new PropertyName("id"), null, IdGenerator.class);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructorWithNullObjectIdGenerator() {
 try {
 new ObjectIdInfo(new PropertyName("id"), Object.class, null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, true);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsonschema.IdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdAndDifferentObjectIdGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, true);
 ObjectIdInfo objectIdInfoWithDifferentObjectIdGenerator = objectIdInfo.withGeneratorType(OtherIdGenerator.class);
 assertNotEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsonschema.IdGenerator, alwaysAsId=true", objectIdInfoWithDifferentObjectIdGenerator.toString());
 }

 @Test
 public void testObjectIdInfoWithNullResolverType() {
 try {
 new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, true, null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}