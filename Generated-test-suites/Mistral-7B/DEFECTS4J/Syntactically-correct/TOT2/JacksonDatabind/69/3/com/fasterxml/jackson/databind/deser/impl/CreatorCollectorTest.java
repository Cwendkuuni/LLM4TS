import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.impl.CreatorProperty;
import com.fasterxml.jackson.databind.deser.impl.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CreatorCollectorTest {

 private ObjectMapper mapper;

 @Before
 public void init() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(ArrayList.class);
 CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue(null);
 assertEquals("Should return an ArrayList instance", ArrayList.class, result.getClass());
 }

 @Test
 public void testStringCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(String.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.String.value", "testStringCreator");
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(String.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams stringCreator = mapper.findCreator(String.class, config);
 collector.addStringCreator(stringCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue("testString");
 assertEquals("Should return the testString created using the String creator", "testString", result);
 }

 @Test
 public void testIntCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Integer.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.Int.value", 10);
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Integer.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams intCreator = mapper.findCreator(Integer.class, config);
 collector.addIntCreator(intCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue(null);
 assertEquals("Should return an Integer instance with value 10", 10, result);
 }

 @Test
 public void testLongCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Long.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.Long.value", 20L);
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Long.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams longCreator = mapper.findCreator(Long.class, config);
 collector.addLongCreator(longCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue(null);
 assertEquals("Should return a Long instance with value 20", 20L, result);
 }

 @Test
 public void testDoubleCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Double.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.Double.value", 3.14);
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Double.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams doubleCreator = mapper.findCreator(Double.class, config);
 collector.addDoubleCreator(doubleCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue(null);
 assertEquals("Should return a Double instance with value 3.14", 3.14, result);
 }

 @Test
 public void testBooleanCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Boolean.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.Boolean.value", true);
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Boolean.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams booleanCreator = mapper.findCreator(Boolean.class, config);
 collector.addBooleanCreator(booleanCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue(null);
 assertTrue("Should return a Boolean instance with value true", result instanceof Boolean);
 assertTrue("Should return a Boolean instance with value true", (Boolean) result);
 }

 @Test
 public void testPropertyBasedCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.PropertyBased.Foo.property1.value", "TestProperty1Value");
 config.put("creator.PropertyBased.Foo.property2.value", 10);
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Foo.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams creator = mapper.findCreator(Foo.class, config);
 SettableBeanProperty[] properties = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("property1", String.class),
 new SettableBeanProperty.Std("property2", Integer.class)
 };
 collector.addPropertyCreator(creator, true, properties);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue(null);
 Foo foo = (Foo) result;
 assertEquals("Should return a Foo instance with property1='TestProperty1Value' and property2=10", "TestProperty1Value", foo.getProperty1());
 assertEquals("Should return a Foo instance with property1='TestProperty1Value' and property2=10", 10, foo.getProperty2());
 }

 @Test
 public void testDelegatingCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Bar.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.Delegating.Bar.delegate.value", "TestDelegate");
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Bar.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams creator = mapper.findCreator(Bar.class, config);
 SettableBeanProperty[] injectables = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("delegate", String.class)
 };
 collector.addDelegatingCreator(creator, true, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Object result = inst.createValue(null);
 Bar bar = (Bar) result;
 assertEquals("Should return a Bar instance with delegate='TestDelegate'", "TestDelegate", bar.getDelegate());
 }

 @Test(expected = IllegalStateException.class)
 public void testDuplicateCreators() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Foo.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.PropertyBased.Foo.property1.value", "TestProperty1Value");
 config.put("creator.PropertyBased.Foo.property1.value", "TestProperty1Value"); // duplicate
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Foo.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams creator = mapper.findCreator(Foo.class, config);
 SettableBeanProperty[] properties = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("property1", String.class),
 new SettableBeanProperty.Std("property2", Integer.class)
 };
 collector.addPropertyCreator(creator, true, properties); // will fail on duplicate
 }

 @Test
 public void testMissingCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Baz.class);
 CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 try {
 inst.createValue(null);
 fail("Should throw an exception as no creator was provided");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCreator() throws IOException {
 BeanDescription beanDesc = mapper.getTypeFactory().constructType(Baz.class);
 ObjectNode config = mapper.getFactory().createObjectNode();
 config.put("creator.Invalid.Baz.arg.value", "InvalidCreator");
 BeanDescription newBeanDesc = mapper.getTypeFactory().constructType(Baz.class);
 CreatorCollector collector = new CreatorCollector(newBeanDesc, mapper.getConfig());
 AnnotatedWithParams creator = mapper.findCreator(Baz.class, config);
 try {
 collector.addPropertyCreator(creator, true, new SettableBeanProperty[0]);
 fail("Should throw an exception as no properties were provided");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}

class Foo {
 private String property1;
 private int property2;

 public String getProperty1() { return property1; }
 public void setProperty1(String prop) { property1 = prop; }
 public int getProperty2() { return property2; }
 public void setProperty2(int prop) { property2 = prop; }
}

class Bar {
 private String delegate;

 public String getDelegate() { return delegate; }
 public void setDelegate(String delegate) { this.delegate = delegate; }
}

class Baz {}