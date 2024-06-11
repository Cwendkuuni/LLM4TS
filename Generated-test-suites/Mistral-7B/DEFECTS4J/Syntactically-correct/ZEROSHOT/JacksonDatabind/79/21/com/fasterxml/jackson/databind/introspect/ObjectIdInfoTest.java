import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class,
 ObjectIdGenerators.IntSequenceGenerator.class, false, SimpleObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IntSequenceGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoDefaultConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=null, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoDeprecatedConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", Object.class, ObjectIdGenerators.IntSequenceGenerator.class);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IntSequenceGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IntSequenceGenerator, alwaysAsId=true", newObjectIdInfo.toString());
 }

 @Test
 public void testGetters() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class, false, SimpleObjectIdResolver.class);

 assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerators.IntSequenceGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }
}