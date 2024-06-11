import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, ObjectIdGenerator.Identity.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.BeanProperty.Name$1, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, ObjectIdGenerator.Identity.class, true, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.BeanProperty.Name$1, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 try {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, String.class, ObjectIdGenerator.Identity.class, false, SimpleObjectIdResolver.class);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("PropertyName cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullScope() {
 try {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, ObjectIdGenerator.Identity.class, false, SimpleObjectIdResolver.class);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Scope class cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullGeneratorType() {
 try {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, null, false, SimpleObjectIdResolver.class);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Generator type class cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullResolverType() {
 try {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, ObjectIdGenerator.Identity.class, false, null);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Resolver type class cannot be null", e.getMessage());
 }
 }
}