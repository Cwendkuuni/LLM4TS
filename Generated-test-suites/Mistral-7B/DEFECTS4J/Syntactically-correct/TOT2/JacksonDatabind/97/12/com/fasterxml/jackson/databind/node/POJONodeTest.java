import org.junit.Assert;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class POJONodeTest {

	@Test
	public void testPOJONodeConstruction() {
		Object obj = new Object();
		POJONode pojoNode = new POJONode(obj);
		assertEquals(obj, pojoNode.getPojo());
	}

	@Test
	public void testPOJONodeNullInput() {
		assertThrows(IllegalArgumentException.class, () -> new POJONode(null));
	}

	@Test
	public void testPOJONodeNonSerializableObject() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		assertThrows(IllegalArgumentException.class, () -> mapper.writerForTree(new ObjectWriter.DefaultSerializers.BaseSerializerProvider())
				.writeValueAsString(new POJONode(new NonSerializableObject())));
	}

	@Test
	public void testPOJONodeBinaryData() throws IOException {
		byte[] rawData = new byte[] { 1, 2, 3 };
		POJONode pojoNode = new POJONode(rawData);
		assertEquals(rawData, pojoNode.binaryValue());
	}

	@Test
	public void testPOJONodeAsText() {
		POJONode pojoNode = new POJONode(new Boolean(true));
		assertEquals("true", pojoNode.asText());
		pojoNode = new POJONode(new Integer(123));
		assertEquals("123", pojoNode.asText());
		pojoNode = new POJONode(new Double(3.14));
		assertEquals("3.14", pojoNode.asText());
		pojoNode = new POJONode("Hello");
		assertEquals("Hello", pojoNode.asText());
	}

	@Test
	public void testPOJONodeAsBoolean() {
		POJONode pojoNode = new POJONode(true);
		assertTrue(pojoNode.asBoolean(false));
		pojoNode = new POJONode(false);
		assertFalse(pojoNode.asBoolean(true));
		pojoNode = new POJONode((Object) null);
		assertFalse(pojoNode.asBoolean(true));
	}

	@Test
	public void testPOJONodeAsInt() {
		POJONode pojoNode = new POJONode(123);
		assertEquals(123, pojoNode.asInt(0));
		pojoNode = new POJONode(120.5);
		assertEquals(0, pojoNode.asInt(0));
	}

	@Test
	public void testPOJONodeAsLong() {
		POJONode pojoNode = new POJONode(123L);
		assertEquals(123, pojoNode.asLong(0));
		pojoNode = new POJONode(120.5);
		assertEquals(0, pojoNode.asLong(0));
	}

	@Test
	public void testPOJONodeAsDouble() {
		POJONode pojoNode = new POJONode(1.23);
		assertEquals(1.23, pojoNode.asDouble(0), 0.001);
		pojoNode = new POJONode(123);
		assertEquals(123.0, pojoNode.asDouble(0), 0.001);
	}

	@Test
	public void testPOJONodeSerialization() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonGenerator gen = mapper.getFactory().createGenerator(outputStream);
		POJONode pojoNode = new POJONode("Test String");
		pojoNode.serialize(gen, new SerializerProvider());
		gen.close();
		String jsonString = outputStream.toString();
		assertEquals("\"Test String\"", jsonString);
	}

	private static class NonSerializableObject {
		// Non-serializable class
	}
}