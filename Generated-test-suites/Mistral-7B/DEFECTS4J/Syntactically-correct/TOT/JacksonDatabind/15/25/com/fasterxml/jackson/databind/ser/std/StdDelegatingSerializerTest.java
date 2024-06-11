import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for StdDelegatingSerializer class")
public class StdDelegatingSerializerTest {

 private StdDelegatingSerializer serializer;
 private ObjectMapper mapper;

 @BeforeEach
 void setUp() throws JsonProcessingException {
 mapper = new ObjectMapper();
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.putPOJO("sourceObject", new SourceObject());
 serializer = mapper.findValueSerializer(SourceObject.class);
 }

 @Nested
 @DisplayName("Test convertValue method")
 class ConvertValueTest {

 @Test
 void testConvertValueWithSimpleObject() throws JsonProcessingException {
 Object actual = serializer.convertValue(new SourceObject());
 assertEquals(new DelegateObject(), actual);
 }

 @Test
 void testConvertValueWithNullObject() {
 Object actual = serializer.convertValue(null);
 assertNull(actual);
 }
 }

 @Nested
 @DisplayName("Test serialize method")
 class SerializeTest {

 @Test
 void testSerializeWithValidObject() throws JsonGenerationException, JsonMappingException {
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 serializer.serialize(new SourceObject(), gen, new SerializerProvider());
 ObjectNode actual = mapper.readTree(gen.getOutputBuffer());
 ObjectNode expected = mapper.createObjectNode();
 expected.setPOJO("delegateObject", new DelegateObject());
 assertEquals(expected, actual);
 }

 @Test
 void testSerializeWithNullObject() throws JsonGenerationException, JsonMappingException {
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 serializer.serialize(null, gen, new SerializerProvider());
 assertTrue(gen.getOutputBuffer().toString().isEmpty());
 }

 @Test
 void testSerializeWithEmptyList() throws JsonGenerationException, JsonMappingException {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.putPOJO("sourceList", new ArrayList<>());
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 serializer.serialize(sourceNode, gen, new SerializerProvider());
 ObjectNode actual = mapper.readTree(gen.getOutputBuffer());
 ObjectNode expected = mapper.createObjectNode();
 expected.put("delegateList", mapper.createValueInstance(ArrayList.class));
 assertEquals(expected, actual);
 }

 @Test
 void testSerializeWithEmptyMap() throws JsonGenerationException, JsonMappingException {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.putPOJO("sourceMap", new HashMap<>());
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 serializer.serialize(sourceNode, gen, new SerializerProvider());
 ObjectNode actual = mapper.readTree(gen.getOutputBuffer());
 ObjectNode expected = mapper.createObjectNode();
 expected.put("delegateMap", mapper.createValueInstance(HashMap.class));
 assertEquals(expected, actual);
 }

 @Test
 void testSerializeWithException() throws JsonGenerationException, JsonMappingException {
 assertThrows(JsonProcessingException.class, () -> {
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 serializer.serialize(new Exception(), gen, new SerializerProvider());
 });
 }
 }

 @Nested
 @DisplayName("Test getDelegateSerializer method")
 class GetDelegateSerializerTest {

 @Test
 void testGetDelegateSerializerWithValidSerializer() {
 JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, delegateSerializer);
 assertEquals(delegateSerializer, serializer.getDelegateSerializer());
 }

 @Test
 void testGetDelegateSerializerWithNullSerializer() {
 StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, null);
 assertNull(serializer.getDelegateSerializer());
 }
 }

 @AfterEach
 void tearDown() throws Exception {
 mapper.close();
 }

}