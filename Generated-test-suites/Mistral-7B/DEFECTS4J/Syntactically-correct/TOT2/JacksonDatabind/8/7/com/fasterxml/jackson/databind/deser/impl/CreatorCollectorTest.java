import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private BeanDescription beanDesc;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 beanDesc = new AnnotatedClass(CreatorCollector.class).getDefaultInstance();
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object created = inst.createValue(mapper, null);
 assertTrue(created instanceof CreatorCollector);
 }

 @Test
 public void testStringCreator() throws Exception {
 String creatorName = "CreatorWithStringArg";
 AnnotatedWithParams stringCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, creatorName, String.class), null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addStringCreator(stringCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object created = inst.createValue(mapper, "test");
 assertEquals("test", created);
 }

 @Test
 public void testIntCreator() throws Exception {
 String creatorName = "CreatorWithIntArg";
 AnnotatedWithParams intCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, creatorName, int.class), null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addIntCreator(intCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object created = inst.createValue(mapper, 42);
 assertEquals(42, created);
 }

 @Test
 public void testLongCreator() throws Exception {
 String creatorName = "CreatorWithLongArg";
 AnnotatedWithParams longCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, creatorName, long.class), null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addLongCreator(longCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object created = inst.createValue(mapper, 42L);
 assertEquals(42L, created);
 }

 @Test
 public void testDoubleCreator() throws Exception {
 String creatorName = "CreatorWithDoubleArg";
 AnnotatedWithParams doubleCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, creatorName, double.class), null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addDoubleCreator(doubleCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object created = inst.createValue(mapper, 42.0);
 assertEquals(42.0, created);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 String creatorName = "CreatorWithBooleanArg";
 AnnotatedWithParams booleanCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, creatorName, boolean.class), null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addBooleanCreator(booleanCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object created = inst.createValue(mapper, true);
 assertTrue(created instanceof Boolean);
 assertEquals(true, created);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 String creatorName = "CreatorWithStringArg";
 AnnotatedWithParams stringCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, creatorName, String.class), null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addStringCreator(stringCreator, true);
 collector.addStringCreator(stringCreator, true);
 try {
 collector.constructValueInstantiator(mapper.getConfig());
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDelegateCreator() throws Exception {
 String delegateClassName = "TestDelegate";
 AnnotatedWithParams delegateCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, "creatorWithDelegate", CreatorCollector.class), null);
 CreatorProperty[] injectables = new CreatorProperty[] { new CreatorProperty("injectable1", String.class), new CreatorProperty("injectable2", int.class) };
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addDelegatingCreator(delegateCreator, true, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object delegate = mapper.readValue(mapper.writeValueAsString(new Object()), Delegate.class);
 Object created = inst.createValue(mapper, delegate);
 assertTrue(created instanceof CreatorCollector);
 assertEquals(delegate, created.getClass().getDeclaredField("delegate").get(created));
 assertEquals("injectable1Value", created.getClass().getDeclaredField("injectable1").get(created));
 assertEquals(42, created.getClass().getDeclaredField("injectable2").get(created));
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 String propertyBasedClassName = "TestPropertyBased";
 AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class, "creatorWithProperties", PropertyBased.class), null);
 CreatorProperty[] properties = new CreatorProperty[] { new CreatorProperty("property1", String.class), new CreatorProperty("property2", int.class) };
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addPropertyCreator(propertyBasedCreator, true, properties);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object created = inst.createValue(mapper, new Object() {{
 setProperty1("property1Value");
 setProperty2(42);
 }});
 assertTrue(created instanceof PropertyBased);
 assertEquals("property1Value", created.getClass().getDeclaredField("property1").get(created));
 assertEquals(42, created.getClass().getDeclaredField("property2").get(created));
 }

 @Test
 public void testIncompleteParameter() throws Exception {
 AnnotatedParameter incompleteParameter = new AnnotatedParameter(new AnnotatedMethod(CreatorCollector.class, "creatorWithIncompleteParameter", Object[].class).getParameters()[0]);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addIncompleteParameter(incompleteParameter);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object[] args = new Object[] { new Object(), new Object() };
 Object created = inst.createValue(mapper, args);
 assertNotNull(created);
 }
}

class Delegate {
 private Object delegate;

 public Object getDelegate() {
 return delegate;
 }

 public void setDelegate(Object delegate) {
 this.delegate = delegate;
 }

 public String injectable1;
 public int injectable2;
}

class PropertyBased {
 private String property1;
 private int property2;

 public String getProperty1() {
 return property1;
 }

 public void setProperty1(String property1) {
 this.property1 = property1;
 }

 public int getProperty2() {
 return property2;
 }

 public void setProperty2(int property2) {
 this.property2 = property2;
 }
}