import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.JsonParser.Feature.*;
import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.JsonMapping.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.junit.Before;
import org.junit.Test;

public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(FEATURE_AUTO_CLOSE_SOURCE, false);
 objectMapper.configure(FEATURE_FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testDeserializeNullifyingDeserializerNullInput() throws Exception {
 ObjectNode nullInputNode = JsonFactory.instance.createObjectNode();
 JsonParser nullInputParser = objectMapper.treeAsTokens(nullInputNode);
 StdDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
 Object result = nullifyingDeserializer.deserialize(nullInputParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerEmptyArrayInput() throws Exception {
 ObjectNode emptyArrayInputNode = JsonFactory.instance.createObjectNode();
 emptyArrayInputNode.put("content", JsonFactory.instance.createValueNode("[{}]"));
 JsonParser emptyArrayInputParser = objectMapper.treeAsTokens(emptyArrayInputNode);
 StdDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
 Object result = nullifyingDeserializer.deserialize(emptyArrayInputParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerEmptyObjectInput() throws Exception {
 ObjectNode emptyObjectInputNode = JsonFactory.instance.createObjectNode();
 JsonParser emptyObjectInputParser = objectMapper.treeAsTokens(emptyObjectInputNode);
 StdDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
 Object result = nullifyingDeserializer.deserialize(emptyObjectInputParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerNonEmptyArrayInput() throws Exception {
 ObjectNode nonEmptyArrayInputNode = JsonFactory.instance.createObjectNode();
 nonEmptyArrayInputNode.put("content", JsonFactory.instance.createValueNode("[1, 2, 3]"));
 JsonParser nonEmptyArrayInputParser = objectMapper.treeAsTokens(nonEmptyArrayInputNode);
 StdDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
 Object result = nullifyingDeserializer.deserialize(nonEmptyArrayInputParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerNonEmptyObjectInput() throws Exception {
 ObjectNode nonEmptyObjectInputNode = JsonFactory.instance.createObjectNode();
 nonEmptyObjectInputNode.put("name", JsonFactory.instance.createValueNode("John Doe"));
 JsonParser nonEmptyObjectInputParser = objectMapper.treeAsTokens(nonEmptyObjectInputNode);
 StdDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
 Object result = nullifyingDeserializer.deserialize(nonEmptyObjectInputParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerFieldNameInput() throws Exception {
 ObjectNode fieldNameInputNode = JsonFactory.instance.createObjectNode();
 fieldNameInputNode.put("name", JsonFactory.instance.createValueNode("John Doe"));
 fieldNameInputNode.put("age", JsonFactory.instance.createNumberNode(30));
 JsonParser fieldNameInputParser = objectMapper.treeAsTokens(fieldNameInputNode);
 StdDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
 Object result = nullifyingDeserializer.deserialize(fieldNameInputParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithType() throws Exception {
 ObjectNode complexInputNode = JsonFactory.instance.createObjectNode();
 complexInputNode.put("name", JsonFactory.instance.createValueNode("John Doe"));
 complexInputNode.put("age", JsonFactory.instance.createNumberNode(30));
 complexInputNode.put("hobbies", JsonFactory.instance.createValueNode("[swimming, reading]"));
 JsonParser complexInputParser = objectMapper.treeAsTokens(complexInputNode);
 ObjectNode expectedOutputNode = JsonFactory.instance.createObjectNode();
 expectedOutputNode.put("hobbies", JsonFactory.instance.createValueNode("[swimming, reading]"));
 TypeDeserializer objectTypeDeserializer = new ObjectMapper().getTypeFactory().constructType(Object.class);
 Object result = nullifyingDeserializer.deserializeWithType(complexInputParser, new TestContext(), objectTypeDeserializer);
 assertNull(result);
 }

 class TestContext extends DeserializationContext {
 }

}