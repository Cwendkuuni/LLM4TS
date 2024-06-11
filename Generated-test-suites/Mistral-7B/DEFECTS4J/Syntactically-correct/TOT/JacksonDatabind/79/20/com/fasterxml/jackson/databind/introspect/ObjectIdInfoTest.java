import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class MyScope, generatorType=class MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithDeprecatedPropertyNameAndScopeAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", MyScope.class, MyObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class MyScope, generatorType=class MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithDeprecatedStringNameAndScopeAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", MyScope.class, MyObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class MyScope, generatorType=class MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class, true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class MyScope, generatorType=class MyObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithDeprecatedPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", MyScope.class, MyObjectIdGenerator.class, true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class MyScope, generatorType=class MyObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructorWithDeprecatedStringNameAndScopeAndGeneratorAndAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", MyScope.class, MyObjectIdGenerator.class, true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class MyScope, generatorType=class MyObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructorWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, MyScope.class, MyObjectIdGenerator.class);
 fail("Expected IllegalArgumentException, but got none");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructorWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, MyObjectIdGenerator.class);
 fail("Expected IllegalArgumentException, but got none");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoConstructorWithNullGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, null);
 fail("Expected IllegalArgumentException, but got none");
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class);
 ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class MyScope, generatorType=class MyObjectIdGenerator, alwaysAsId=true", withAlwaysAsId.toString());
 }

 @Test
 public void testGetPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class);
 assertEquals("testProperty", objectIdInfo.getPropertyName().getId());
 }

 @Test
 public void testGetScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class);
 assertEquals(MyScope.class, objectIdInfo.getScope());
 }

 @Test
 public void testGetGeneratorType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class);
 assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 }

 @Test
 public void testGetResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class, SimpleObjectIdResolver.class);
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 }

 @Test
 public void testGetAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class, true);
 assertTrue(objectIdInfo.getAlwaysAsId());
 }
}