package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        POJONode node = new POJONode(new Integer(42));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        assertEquals("{\"value\":42}", json);

        POJONode node2 = mapper.readValue(mapper.writeValueAsBytes(node), POJONode.class);
        assertEquals(node.getPojo(), node2.getPojo());
    }

    @Test
    public void testPOJONodeSerializationWithJsonSerializableObject() throws JsonProcessingException {
        class TestClass implements JsonSerializable {
            private int value;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            @Override
            public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value);
            }
        }

        TestClass testObj = new TestClass();
        testObj.setValue(123);
        POJONode node = new POJONode(testObj);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        assertEquals("{\"value\":{\"class\":\"com.fasterxml.jackson.databind.TestClass\",\"value\":123}}", json);

        POJONode node2 = mapper.readValue(mapper.writeValueAsBytes(node), POJONode.class);
        TestClass deserializedObj = (TestClass) node2.getPojo();
        assertEquals(testObj.getValue(), deserializedObj.getValue());
    }

    @Test
    public void testPOJONodeBinaryValue() throws IOException {
        byte[] binaryData = new byte[] {1, 2, 3};
        POJONode node = new POJONode(binaryData);
        assertEquals(binaryData, node.binaryValue());
    }

    @Test
    public void testPOJONodeToString() {
        POJONode node = new POJONode(new String("test"));
        assertEquals("(raw value 'test')", node.toString());
    }

    @Test
    public void testPOJONodeEquals() {
        POJONode node1 = new POJONode(new Integer(42));
        POJONode node2 = new POJONode(new Integer(42));
        POJONode node3 = new POJONode("test");
        POJONode node4 = new POJONode(new Double(3.14));

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(node4));
    }
}