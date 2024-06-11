import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.MyScope, generatorType=class com.example.MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class, true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.MyScope, generatorType=class com.example.MyObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithNullPropertyName() {
 try {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, MyScope.class, MyObjectIdGenerator.class, false, MyResolver.class);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testObjectIdInfoWithNullScope() {
 try {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, MyObjectIdGenerator.class, false, MyResolver.class);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testObjectIdInfoWithNullGeneratorType() {
 try {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, null, false, MyResolver.class);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testObjectIdInfoWithNullResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class, false);
 ObjectIdInfo objectIdInfoWithResolver = objectIdInfo.withResolver(MyResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.MyScope, generatorType=class com.example.MyObjectIdGenerator, alwaysAsId=false, resolverType=class com.example.MyResolver", objectIdInfoWithResolver.toString());
 }

 @Test
 public void testObjectIdInfoWithDefaultResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), MyScope.class, MyObjectIdGenerator.class, false);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.MyScope, generatorType=class com.example.MyObjectIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.jsontype.SimpleObjectIdResolver", objectIdInfo.toString());
 }
}

class MyScope {
 // an empty class representing a scope
}

class MyObjectIdGenerator implements ObjectIdGenerator<String> {
 // an empty implementation of ObjectIdGenerator
}

class MyResolver implements ObjectIdResolver {
 // an empty implementation of ObjectIdResolver
}