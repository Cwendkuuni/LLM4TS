package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeNull() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));
        ObjectNode actual = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(rootNode), null));
        ObjectNode expected = mapper.createObjectNode();
        expected.put("null", "&lt;null&gt;");
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializePrimitiveTypes() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));

        // Integer
        ObjectNode intTest = mapper.createObjectNode();
        intTest.put("number", 123);
        ObjectNode actualInt = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(intTest), 123));
        ObjectNode expectedInt = mapper.createObjectNode();
        expectedInt.put("number", "123");
        assertEquals(expectedInt, actualInt);

        // Boolean
        ObjectNode boolTest = mapper.createObjectNode();
        boolTest.put("boolean", true);
        ObjectNode actualBool = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(boolTest), true));
        ObjectNode expectedBool = mapper.createObjectNode();
        expectedBool.put("boolean", "true");
        assertEquals(expectedBool, actualBool);

        // Float
        ObjectNode floatTest = mapper.createObjectNode();
        floatTest.put("float", 123.45f);
        ObjectNode actualFloat = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(floatTest), 123.45f));
        ObjectNode expectedFloat = mapper.createObjectNode();
        expectedFloat.put("float", "123.45");
        assertEquals(expectedFloat, actualFloat);
    }

    @Test
    public void testSerializeValue() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));

        // Object
        ObjectNode objTest = mapper.createObjectNode();
        String testObjectName = "testObject";
        objTest.put("name", testObjectName);
        ObjectNode actualObj = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(objTest), new Object()));
        ObjectNode expectedObj = mapper.createObjectNode();
        expectedObj.put("name", testObjectName);
        assertEquals(expectedObj, actualObj);

        // Serializer
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value + "_serialized");
            }
        };
        ObjectNode strTest = mapper.createObjectNode();
        strTest.put("string", "test");
        ObjectWriter stringObjectWriter = mapper.writer(stringSerializer);
        ObjectNode actualStr = stringObjectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(strTest), "test"));
        ObjectNode expectedStr = mapper.createObjectNode();
        expectedStr.put("string", "test_serialized");
        assertEquals(expectedStr, actualStr);

        // Array
        ObjectNode arrayTest = mapper.createObjectNode();
        arrayTest.put("items", mapper.createValueNode(1));
        arrayTest.put("items", mapper.createValueNode(2));
        ObjectWriter arrayObjectWriter = mapper.writer(ObjectSerializer.instance);
        ObjectNode actualArray = arrayObjectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(arrayTest), new Object[] {1, 2}));
        ObjectNode expectedArray = mapper.createObjectNode();
        expectedArray.put("item", mapper.createArrayNode().add(mapper.createValueNode(1)).add(mapper.createValueNode(2)));
        assertEquals(expectedArray, actualArray);
    }

    @Test
    public void testSerializeValueWithRootType() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));

        // Object
        ObjectNode objTest = mapper.createObjectNode();
        String testObjectName = "testObject";
        objTest.put("name", testObjectName);
        JavaType rootType = mapper.getTypeFactory().constructType(ObjectNode.class);
        ObjectNode actualObj = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(objTest), objTest, rootType, null));
        ObjectNode expectedObj = objTest;
        assertEquals(expectedObj, actualObj);
    }

    @Test
    public void testSerializeValueWithConfiguredRootName() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));

        // Object
        ObjectNode objTest = mapper.createObjectNode();
        String testObjectName = "testObject";
        objTest.put("name", testObjectName);
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator("test_separator");
        ObjectNode actualObj = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(objTest), objTest, null, config));
        ObjectNode expectedObj = mapper.createObjectNode();
        expectedObj.put("test_separator", testObjectName);
        assertEquals(expectedObj, actualObj);
    }

    @Test
    public void testSerializeValueWithRootNameLookup() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));

        // Object
        ObjectNode objTest = mapper.createObjectNode();
        String testObjectName = "testObject";
        objTest.put("name", testObjectName);
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator("test_separator");
        config.setRootName("testRootName");
        ObjectNode actualObj = objectWriter.writeValueAsTree(provider.serializeValue(provider.getFactory().createGenerator(objTest), objTest, null, config));
        ObjectNode expectedObj = mapper.createObjectNode();
        expectedObj.put("testRootName", testObjectName);
        assertEquals(expectedObj, actualObj);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNullGenerator() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));

        // Object
        ObjectNode objTest = mapper.createObjectNode();
        String testObjectName = "testObject";
        objTest.put("name", testObjectName);
        ObjectNode actualObj = objectWriter.writeValueAsTree(provider.serializeValue(null, objTest, null, null));
        fail("Expected JsonMappingException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeValueWithInvalidObject() throws Exception {
        XmlMapper mapper = new XmlMapper();
        XmlSerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectWriter objectWriter = mapper.writer(StandardSerializer.Instance.getForObject(Object.class));

        // Object
        ObjectNode objTest = mapper.createObjectNode();
        Object actualObj = null;
        ObjectNode expectedObj = mapper.createObjectNode();
        try {
            actualObj = (Object) provider.serializeValue(provider.getFactory().createGenerator(objTest), actualObj, null, null);
        } catch (JsonMappingException e) {
            throw new RuntimeException("Unexpected JsonMappingException", e);
        }
        fail("Expected IllegalArgumentException");
    }
}