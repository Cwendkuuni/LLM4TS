import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoDeprecatedConstructionWithString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testPropertyName", Object.class, ObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoDeprecatedConstructionWithStringAndResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo("testPropertyName", Object.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithAllParameters() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, false, ObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.ObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
 }

 @Test
 public void testGetters() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, false);
 assertNotNull(objectIdInfo._propertyName);
 assertEquals("testPropertyName", objectIdInfo._propertyName.getName());
 assertNotNull(objectIdInfo._scope);
 assertEquals("class java.lang.Object", objectIdInfo._scope.getName());
 assertNotNull(objectIdInfo._generator);
 assertEquals("class com.fasterxml.jackson.annotation.ObjectIdGenerator", objectIdInfo._generator.getName());
 assertNotNull(objectIdInfo._resolver);
 assertEquals("class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo._resolver.getName());
 assertFalse(objectIdInfo._alwaysAsId);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), null, ObjectIdGenerator.class, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, false, null);
 }
}