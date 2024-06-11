import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, JsonIdentityInfo.None.class, false, ObjectIdResolver.Std.class);
 assertEquals("ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, JsonIdentityInfo.None.class, true, ObjectIdResolver.Std.class);
 assertEquals("ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullPropertyNameThrowsNPE() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, JsonIdentityInfo.None.class, false, ObjectIdResolver.Std.class);
 }

 @Test(expected = NullPointerException.class)
 public void testNullScopeThrowsNPE() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), null, JsonIdentityInfo.None.class, false, ObjectIdResolver.Std.class);
 }

 @Test(expected = NullPointerException.class)
 public void testNullGeneratorThrowsNPE() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, null, false, ObjectIdResolver.Std.class);
 }

 @Test(expected = NullPointerException.class)
 public void testNullResolverThrowsNPE() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, JsonIdentityInfo.None.class, false, null);
 }
}