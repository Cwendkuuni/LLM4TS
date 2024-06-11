import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription beanDesc = Introspector.findBeanDescription(Map.class, mapper.getConfiguration());
 collector = new CreatorCollector(beanDesc, mapper.getConfiguration());
 }

 @Test
 public void testDefaultCreator() {
 // Create a default creator
 AnnotatedWithParams defaultCreator = AnnotatedWithParams.noArgs(Map.class);
 collector.setDefaultCreator(defaultCreator);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfiguration());
 // Test that the default creator is used
 Object map = inst.createValue(mapper.getConfigurationContext(), null);
 assertTrue(map instanceof Map);
 }

 @Test
 public void testStringCreator() throws Exception {
 // Create a String creator
 AnnotatedWithParams stringCreator = AnnotatedWithParams.of(Map.class, String.class, "stringCreator");
 collector.addStringCreator(stringCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfiguration());
 // Test that the String creator is used with a String argument
 Object map = inst.createValue(mapper.getConfigurationContext(), "testMap");
 assertTrue(map instanceof Map);
 }

 @Test
 public void testIntCreator() throws Exception {
 // Create an Int creator
 AnnotatedWithParams intCreator = AnnotatedWithParams.of(Map.class, int[].class, "intCreator");
 collector.addIntCreator(intCreator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfiguration());
 // Test that the Int creator is used with an Int array argument
 int[] initialCapacity = new int[10];
 Object map = inst.createValue(mapper.getConfigurationContext(), initialCapacity);
 assertTrue(map instanceof Map);
 }

 @Test
 public void testDelegatingCreator() throws Exception {
 // Create a delegating creator
 AnnotatedWithParams delegatingCreator = AnnotatedWithParams.of(Map.class, List.class, "delegateCreator");
 SettableBeanProperty[] delegateArgs = new SettableBeanProperty[] { new SettableBeanProperty.Std("delegate", List.class) };
 collector.addDelegatingCreator(delegatingCreator, true, delegateArgs);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfiguration());
 // Test that the delegating creator is used with a List argument
 List<Integer> delegate = new ArrayList<Integer>();
 Object map = inst.createValue(mapper.getConfigurationContext(), delegate);
 assertTrue(map instanceof Map);
 assertEquals(delegate, map);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 // Create duplicate creators
 AnnotatedWithParams stringCreator = AnnotatedWithParams.of(Map.class, String.class, "stringCreator");
 collector.addStringCreator(stringCreator, true);
 // Create another String creator with the same name
 AnnotatedWithParams stringCreator2 = AnnotatedWithParams.of(Map.class, String.class, "stringCreator");
 collector.addStringCreator(stringCreator2, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfiguration());
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 // Create a property-based creator
 AnnotatedWithParams propertyCreator = AnnotatedWithParams.of(Map.class, Map.Entry.class, "propertyCreator");
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("key", String.class),
 new SettableBeanProperty.Std("value", Object.class)
 };
 collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfiguration());
 // Test that the property-based creator is used with a Map.Entry argument
 Map.Entry<String,Object> entry = new AbstractMap.SimpleEntry<String,Object>("key", new Object());
 Object map = inst.createValue(mapper.getConfigurationContext(), entry);
 assertTrue(map instanceof Map);
 // Test that the Map created by the property-based creator contains the expected key-value pairs
 assertEquals("key", map.keySet().iterator().next());
 assertEquals("value", map.values().iterator().next());
 }

}