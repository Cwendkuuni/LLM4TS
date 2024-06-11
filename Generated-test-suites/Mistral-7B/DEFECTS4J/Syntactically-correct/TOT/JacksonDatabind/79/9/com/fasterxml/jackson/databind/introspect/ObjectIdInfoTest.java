import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectIdInfoTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 ObjectMapper.Setup setup = objectMapper -> {
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 };
 objectMapper = new ObjectMapper().setUp(setup);
 }

 @Test
 public void testObjectIdInfoConstruction() {
 PropertyName propertyName = new PropertyName("id");
 Class<Integer> scope = Integer.class;
 Class<JsonIdentityInfo.NameIdGenerator> generator = JsonIdentityInfo.NameIdGenerator.class;
 Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Integer, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$NameIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 PropertyName propertyName = new PropertyName("id");
 Class<Integer> scope = Integer.class;
 Class<JsonIdentityInfo.NameIdGenerator> generator = JsonIdentityInfo.NameIdGenerator.class;
 Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Integer, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$NameIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testGetters() throws InstantiationException {
 PropertyName propertyName = new PropertyName("id");
 Class<Integer> scope = Integer.class;
 Class<JsonIdentityInfo.NameIdGenerator> generator = JsonIdentityInfo.NameIdGenerator.class;
 Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

 assertEquals(propertyName, objectIdInfo.getPropertyName());
 assertEquals(scope, objectIdInfo.getScope());
 assertEquals(generator, objectIdInfo.getGeneratorType());
 assertEquals(resolver, objectIdInfo.getResolverType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }

 @Test(expected = NullPointerException.class)
 public void testNullPropertyName() {
 PropertyName propertyName = null;
 Class<Integer> scope = Integer.class;
 Class<JsonIdentityInfo.NameIdGenerator> generator = JsonIdentityInfo.NameIdGenerator.class;
 Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 }

 @Test(expected = NullPointerException.class)
 public void testNullScope() {
 PropertyName propertyName = new PropertyName("id");
 Class<?> scope = null;
 Class<JsonIdentityInfo.NameIdGenerator> generator = JsonIdentityInfo.NameIdGenerator.class;
 Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 }

 @Test(expected = NullPointerException.class)
 public void testNullGenerator() {
 PropertyName propertyName = new PropertyName("id");
 Class<Integer> scope = Integer.class;
 Class<? extends ObjectIdGenerator<?>> generator = null;
 Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 }

 @Test(expected = NullPointerException.class)
 public void testNullResolver() {
 PropertyName propertyName = new PropertyName("id");
 Class<Integer> scope = Integer.class;
 Class<? extends ObjectIdGenerator<?>> generator = JsonIdentityInfo.NameIdGenerator.class;
 Class<? extends ObjectIdResolver> resolver = null;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
 }
}