import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.impl.CreatorProperty;
import com.fasterxml.jackson.databind.deser.impl.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private DeserializationConfig config;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 config = mapper.getConfig();
 collector = new CreatorCollector(new AnnotatedClass(HashMap.class), config);
 }

 @Test
 public void testDefaultCreator() throws IOException {
 collector.setDefaultCreator(new AnnotatedWithParams(new HashMapConstructor(), null));
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 HashMap map = instantiator.createValueInstance(null);
 assertNotNull(map);
 assertEquals(HashMap.class, map.getClass());
 }

 @Test
 public void testStringCreator() throws IOException {
 String key = "test";
 collector.addStringCreator(new AnnotatedWithParams(new HashMapStringConstructor(key), false), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 assertEquals(key, map.containsKey(key));
 }

 @Test
 public void testIntCreator() throws IOException {
 int initialCapacity = 3;
 collector.addIntCreator(new AnnotatedWithParams(new HashMapIntConstructor(initialCapacity), false), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 assertEquals(initialCapacity, map.size());
 }

 @Test
 public void testLongCreator() throws IOException {
 long initialCapacity = 5L;
 collector.addLongCreator(new AnnotatedWithParams(new HashMapLongConstructor(initialCapacity), false), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 assertEquals(initialCapacity, map.size());
 }

 @Test
 public void testDoubleCreator() throws IOException {
 double initialCapacity = 7.5;
 collector.addDoubleCreator(new AnnotatedWithParams(new HashMapDoubleConstructor(initialCapacity), false), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 assertEquals(initialCapacity, map.size());
 }

 @Test
 public void testBooleanCreator() throws IOException {
 boolean initialCapacity = true;
 collector.addBooleanCreator(new AnnotatedWithParams(new HashMapBooleanConstructor(initialCapacity), false), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 assertEquals(initialCapacity, map.isEmpty());
 }

 @Test
 public void testDelegateCreator() throws IOException {
 String delegateClassName = "java.util.LinkedList";
 collector.addDelegatingCreator(new AnnotatedWithParams(new HashMapLinkedListConstructor(), false, null), false, null);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 assertTrue(map instanceof HashMap);
 // test that delegate class is used for instantiation
 LinkedList list = (LinkedList) Class.forName(delegateClassName).newInstance();
 map.put(new Integer(1), list);
 assertEquals(list, map.get(new Integer(1)));
 }

 @Test
 public void testArrayDelegateCreator() throws IOException {
 String delegateClassName = "java.util.ArrayList";
 Object[] args = new Object[] {new Integer(1), new Integer(2)};
 collector.addDelegatingCreator(new AnnotatedWithParams(new HashMapArrayListConstructor(), false, args), false, null);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 assertTrue(map instanceof HashMap);
 // test that delegate class is used for instantiation
 ArrayList list = (ArrayList) Arrays.asList(args).get(0);
 map.put(new Integer(1), list);
 assertEquals(list, map.get(new Integer(1)));
 }

 @Test
 public void testPropertyBasedCreator() throws IOException {
 String key = "test";
 String value = "testValue";
 collector.addPropertyCreator(new AnnotatedWithParams(new HashMapPropertyConstructor(), false, new CreatorProperty[] {
 new CreatorProperty(new AnnotatedMember(new HashMapKeyValueProperty("test"), new HashMapKeyValueAnnotation()), null),
 new CreatorProperty(new AnnotatedMember(new HashMapKeyValueProperty("value"), new HashMapKeyValueAnnotation()), null)
 }), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 HashMap map = (HashMap) instantiator.createValueInstance(null);
 HashMap testMap = new HashMap();
 testMap.put(key, value);
 assertEquals(testMap, map);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws IOException {
 String key = "test";
 collector.addStringCreator(new AnnotatedWithParams(new HashMapStringConstructor(key), false), false);
 collector.addStringCreator(new AnnotatedWithParams(new HashMapStringConstructor(key), false), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullCreator() throws IOException {
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreatorWithInvalidType() throws IOException {
 collector.addStringCreator(new AnnotatedWithParams(new HashMapIntConstructor(1), false), false);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIncompleteParameter() throws IOException {
 AnnotatedParameter parameter = new AnnotatedParameter(new HashMapKeyValueProperty("test"), new HashMapKeyValueAnnotation());
 collector.addIncompleteParameter(parameter);
 collector.setDefaultCreator(null);
 ValueInstantiator instantiator = collector.constructValueInstance(config);
 }
}