import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreatorCollector Test")
public class CreatorCollectorTest {

 private static ObjectMapper mapper;
 private static DeserializationConfig config;

 @BeforeAll
 static void setup() {
 mapper = new ObjectMapper();
 config = mapper.getConfig();
 }

 @AfterAll
 static void teardown() throws Exception {
 mapper.close();
 }

 @Test
 void testDefaultCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(String.class), true);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 LinkedHashMap map = instantiator.createValueInstance(type);
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 }

 @Test
 void testStringCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(String.class), true);
 String creatorMethod = "new String[]{}";
 AnnotatedWithParams stringCreator = new AnnotatedWithParams(creatorMethod, String[].class);
 collector.addStringCreator(stringCreator, true);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 LinkedHashMap map = instantiator.createValueInstance(type);
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 assertEquals("[ ]", map.toString());
 }

 @Test
 void testIntCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(Integer.class), true);
 String creatorMethod = "new Integer[]{}";
 AnnotatedWithParams intCreator = new AnnotatedWithParams(creatorMethod, Integer[].class);
 collector.addIntCreator(intCreator, true);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 LinkedHashMap map = instantiator.createValueInstance(type);
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 assertEquals("{}", map.toString());
 }

 @Test
 void testLongCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(Long.class), true);
 String creatorMethod = "new Long[]{}";
 AnnotatedWithParams longCreator = new AnnotatedWithParams(creatorMethod, Long[].class);
 collector.addLongCreator(longCreator, true);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 LinkedHashMap map = instantiator.createValueInstance(type);
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 assertEquals("{}", map.toString());
 }

 @Test
 void testDoubleCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(Double.class), true);
 String creatorMethod = "new Double[]{}";
 AnnotatedWithParams doubleCreator = new AnnotatedWithParams(creatorMethod, Double[].class);
 collector.addDoubleCreator(doubleCreator, true);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 LinkedHashMap map = instantiator.createValueInstance(type);
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 assertEquals("{}", map.toString());
 }

 @Test
 void testBooleanCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(Boolean.class), true);
 String creatorMethod = "new Boolean[]{}";
 AnnotatedWithParams booleanCreator = new AnnotatedWithParams(creatorMethod, Boolean[].class);
 collector.addBooleanCreator(booleanCreator, true);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 LinkedHashMap map = instantiator.createValueInstance(type);
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 assertEquals("{}", map.toString());
 }

 @Test
 void testDelegatingCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(LinkedHashMap.class), true);
 String delegateClass = "java.util.Collections";
 String creatorMethod = "singletonMap";
 AnnotatedWithParams delegateCreator = new AnnotatedWithParams(creatorMethod, Map.class, String.class, String.class);
 CreatorProperty[] injectables = new CreatorProperty[1];
 injectables[0] = new CreatorProperty("key", String.class);
 collector.addDelegatingCreator(delegateCreator, true, injectables);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class, String.class, String.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 LinkedHashMap map = (LinkedHashMap) instantiator.createValueInstance(type, "key", "value");
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 assertEquals("{key=value}", map.toString());
 }

 @Test
 void testPropertyBasedCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(LinkedHashMap.class), true);
 String creatorClass = "com.example.MyLinkedHashMapCreator";
 String creatorMethod = "createLinkedHashMap";
 AnnotatedWithParams creator = new AnnotatedWithParams(creatorMethod, LinkedHashMap.class, List.class);
 CreatorProperty[] properties = new CreatorProperty[1];
 properties[0] = new CreatorProperty("keyValues", List.class);
 collector.addPropertyCreator(creator, true, properties);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class, List.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 List<Map.Entry<String, String>> keyValues = Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"), new AbstractMap.SimpleEntry<>("key2", "value2"));
 LinkedHashMap map = (LinkedHashMap) instantiator.createValueInstance(type, new TypeReference<List<Map.Entry<String, String>>>() {});
 assertNotNull(map);
 assertTrue(map instanceof LinkedHashMap);
 assertEquals("[key1=value1, key2=value2]", map.toString());
 }

 @Test
 void testMissingCreator() throws IOException {
 CreatorCollector collector = new CreatorCollector(new BeanDescription(LinkedHashMap.class), true);
 JavaType type = mapper.getTypeFactory().constructType(LinkedHashMap.class);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);

 assertThrows(IllegalArgumentException.class, () -> instantiator.createValueInstance(type));
 }
}