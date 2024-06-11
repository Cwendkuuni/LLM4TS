import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 private ObjectIdInfo objectIdInfo;
 private ObjectIdGeneratorTest objGen;

 @Before
 public void setUp() {
 objGen = new ObjectIdGeneratorTest();
 objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 }

 @Test
 public void testObjectIdInfoConstructor() {
 // Test default constructor
 PropertyName prop = new PropertyName("defaultProp");
 Class<?> scope = int[].class;
 Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
 ObjectIdInfo objIdInfoDefault = new ObjectIdInfo(prop, scope, gen, false, SimpleObjectIdResolver.class);

 // Test constructor with no resolver
 PropertyName prop2 = new PropertyName("testProp2");
 Class<?> scope2 = String[].class;
 Class<? extends ObjectIdGenerator<?>> gen2 = ObjectIdGenerator.class;
 ObjectIdInfo objIdInfoNoResolver = new ObjectIdInfo(prop2, scope2, gen2, false);

 // Test constructor with null generator
 PropertyName prop3 = new PropertyName("testProp3");
 Class<?> scope3 = Boolean[].class;
 Class<? extends ObjectIdGenerator<?>> gen3 = null;
 ObjectIdInfo objIdInfoNullGen = new ObjectIdInfo(prop3, scope3, gen3, false);

 // Test constructor with null resolver
 PropertyName prop4 = new PropertyName("testProp4");
 Class<?> scope4 = double[].class;
 Class<? extends ObjectIdGenerator<?>> gen4 = ObjectIdGenerator.class;
 Class<? extends ObjectIdResolver> resolver4 = null;
 ObjectIdInfo objIdInfoNullResolver = new ObjectIdInfo(prop4, scope4, gen4, false, resolver4);

 // Test constructor with alwaysAsId
 PropertyName prop5 = new PropertyName("testProp5");
 Class<?> scope5 = Float[].class;
 Class<? extends ObjectIdGenerator<?>> gen5 = ObjectIdGenerator.class;
 boolean alwaysAsId = true;
 ObjectIdInfo objIdInfoAlwaysAsId = new ObjectIdInfo(prop5, scope5, gen5, alwaysAsId, SimpleObjectIdResolver.class);

 // Test toString method
 assertEquals("ObjectIdInfo: propName=testProp, scope=class [Ljava.lang.Object;, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testGetters() {
 assertNotNull(objectIdInfo.getPropertyName());
 assertNotNull(objectIdInfo.getScope());
 assertNotNull(objectIdInfo.getGeneratorType());
 assertNotNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testWithAlwaysAsId() {
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
 assertTrue(newObjectIdInfo.getAlwaysAsId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithAlwaysAsIdSameState() {
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
 assertEquals(objectIdInfo, newObjectIdInfo);
 }

 @Test
 public void testObjectIdInfoEquals() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals(objectIdInfo, newObjectIdInfo);
 }

 @Test
 public void testObjectIdInfoNotEquals() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp2"), Object.class, ObjectIdGenerator.class, true, SimpleObjectIdResolver.class);
 assertNotEquals(objectIdInfo, newObjectIdInfo);
 }

 @Test
 public void testObjectIdInfoHashCode() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 int hashCode = objectIdInfo.hashCode();
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp2"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(hashCode, newObjectIdInfo2.hashCode());
 }

 @Test
 public void testObjectIdInfoToString() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoToStringIfNulls() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(null, null, null, false, null);
 assertEquals("ObjectIdInfo: propName=null, scope=null, generatorType=null, alwaysAsId=false", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoToStringIfEmptyPropName() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName(""), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoToStringIfNullScope() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), null, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProp, scope=null, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoToStringIfNullGenerator() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, null, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=null, alwaysAsId=false", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoToStringIfNullResolver() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, null);
 assertEquals("ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoToStringIfAlwaysAsIdTrue() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, true, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=true", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoToStringIfAllNull() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(null, null, null, false, null);
 assertEquals("ObjectIdInfo: propName=null, scope=null, generatorType=null, alwaysAsId=false", newObjectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfoWithAlwaysAsId = newObjectIdInfo.withAlwaysAsId(true);
 assertTrue(newObjectIdInfoWithAlwaysAsId.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithSamePropertyName() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithSamePropertyNameButDifferentScope() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), int[].class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithSamePropertyNameButDifferentGenerator() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGeneratorTest.CustomObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithSamePropertyNameButDifferentResolver() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, CustomObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithDifferentPropertyName() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp2"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), null, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithNullGenerator() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, null, false, SimpleObjectIdResolver.class);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithNullResolver() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, null);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithAllNulls() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(null, null, null, false, null);
 ObjectIdInfo newObjectIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertNotEquals(newObjectIdInfo, newObjectIdInfo2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), null, ObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGeneratorClass() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, null, false, SimpleObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullResolverClass() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, null);
 }

 @Test
 public void testObjectIdInfoWithCustomObjectIdResolver() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, CustomObjectIdResolver.class);
 }

 @Test
 public void testObjectIdInfoWithCustomObjectIdGenerator() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, CustomObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
 }

 @Test
 public void testObjectIdInfoWithCustomObjectIdGeneratorAndCustomObjectIdResolver() {
 ObjectIdInfo newObjectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, CustomObjectIdGenerator.class, false, CustomObjectIdResolver.class);
 }
}