package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializerTests;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootElement;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Test
public class XmlSerializerProviderTest extends StdSerializerTests {

    private XmlMapper mapper = new XmlMapper();

    /**
     * Test serialization of simple Java object using default root name.
     */
    @Test
    public void testSerializationOfSimpleObject() throws IOException {
        SimpleObject obj = new SimpleObject();
        obj.setName("Test");
        obj.setNumber(123);

        String xml = mapper.writeValueAsString(obj);

        JsonParser parser = mapper.getFactory().createParser(xml);
        Object readValue = parser.readValueAs(SimpleObject.class);

        assertEquals(readValue, obj);
    }

    /**
     * Test serialization of simple Java object with explicitly defined root name.
     */
    @Test
    public void testSerializationOfSimpleObjectWithRootName() throws IOException {
        SimpleObject obj = new SimpleObject();
        obj.setName("Test");
        obj.setNumber(123);

        XmlRootElement rootElement = XmlRootElement.field("myRootElement");
        SimpleObjectWithRootName objWithRootName = new SimpleObjectWithRootName();
        objWithRootName.setMyRootElement(obj);

        String xml = mapper.writeValueAsString(objWithRootName);

        JsonParser parser = mapper.getFactory().createParser(xml);
        Object readValue = parser.readValueAs(SimpleObjectWithRootName.class);

        assertEquals(readValue, objWithRootName);
        assertEquals(readValue.getMyRootElement(), obj);
    }

    /**
     * Test serialization of null value.
     */
    @Test
    public void testSerializationOfNull() throws IOException {
        SimpleObject obj = null;

        String xml = mapper.writeValueAsString(obj);

        JsonParser parser = mapper.getFactory().createParser(xml);
        Object readValue = parser.readValueAs(SimpleObject.class);

        assertNull(readValue);
    }

    /**
     * Test serialization of Java array.
     */
    @Test
    public void testSerializationOfJavaArray() throws IOException {
        SimpleObject[] array = new SimpleObject[3];
        array[0] = new SimpleObject();
        array[1] = new SimpleObject();
        array[2] = new SimpleObject();

        String xml = mapper.writeValueAsString(array);

        JsonParser parser = mapper.getFactory().createParser(xml);
        SimpleObject[] readValue = parser.readValueAs(SimpleObject[].class);

        assertEquals(Arrays.toString(readValue), Arrays.toString(array));
    }

    /**
     * Test serialization of Java array with explicitly defined root name.
     */
    @Test
    public void testSerializationOfJavaArrayWithRootName() throws IOException {
        SimpleObject[] array = new SimpleObject[3];
        array[0] = new SimpleObject();
        array[1] = new SimpleObject();
        array[2] = new SimpleObject();

        SimpleObjectArrayWithRootName objWithRootName = new SimpleObjectArrayWithRootName();
        objWithRootName.setMyArray(array);

        String xml = mapper.writeValueAsString(objWithRootName);

        JsonParser parser = mapper.getFactory().createParser(xml);
        SimpleObjectArrayWithRootName readValue = parser.readValueAs(SimpleObjectArrayWithRootName.class);

        assertEquals(readValue, objWithRootName);
        assertEquals(readValue.getMyArray(), array);
    }

    /**
     * Test serialization of empty Java array.
     */
    @Test
    public void testSerializationOfEmptyJavaArray() throws IOException {
        SimpleObject[] array = new SimpleObject[0];

        String xml = mapper.writeValueAsString(array);

        JsonParser parser = mapper.getFactory().createParser(xml);
        SimpleObject[] readValue = parser.readValueAs(SimpleObject[].class);

        assertEquals(Arrays.toString(readValue), Arrays.toString(array));
    }

    /**
     * Test serialization of empty Java array with explicitly defined root name.
     */
    @Test
    public void testSerializationOfEmptyJavaArrayWithRootName() throws IOException {
        SimpleObject[] array = new SimpleObject[0];

        SimpleObjectArrayWithRootName objWithRootName = new SimpleObjectArrayWithRootName();

        String xml = mapper.writeValueAsString(objWithRootName);

        JsonParser parser = mapper.getFactory().createParser(xml);
        SimpleObjectArrayWithRootName readValue = parser.readValueAs(SimpleObjectArrayWithRootName.class);

        assertEquals(readValue, objWithRootName);
        assertNull(readValue.getMyArray());
    }

    /**
     * Test serialization of JavaBean with custom serializer.
     */
    @Test
    public void testSerializationOfCustomSerializer() throws IOException {
        CustomSerializerObject obj = new CustomSerializerObject();
        obj.setName("Test");

        String xml = mapper.writeValueAsString(obj);

        JsonParser parser = mapper.getFactory().createParser(xml);
        Object readValue = parser.readValueAs(SimpleObject.class);

        assertEquals(readValue, obj);
    }

    /**
     * Test serialization of JavaBean with empty string root name.
     */
    @Test
    public void testSerializationOfEmptyStringRootName() throws IOException {
        SimpleObject obj = new SimpleObject();
        obj.setName("Test");
        obj.setNumber(123);

        SimpleObjectWithEmptyStringRootName objWithEmptyStringRootName = new SimpleObjectWithEmptyStringRootName();
        objWithEmptyStringRootName.setMyObject(obj);

        String xml = mapper.writeValueAsString(objWithEmptyStringRootName);

        JsonParser parser = mapper.getFactory().createParser(xml);
        Object readValue = parser.readValueAs(SimpleObjectWithEmptyStringRootName.class);

        assertEquals(readValue, objWithEmptyStringRootName);
        assertEquals(readValue.getMyObject(), obj);
    }

    /**
     * Test serialization of JavaBean with null root name.
     */
    @Test(expectedExceptions = JsonMappingException.class)
    public void testSerializationOfNullRootName() throws IOException {
        SimpleObject obj = new SimpleObject();
        obj.setName("Test");
        obj.setNumber(123);

        SimpleObjectWithNullRootName objWithNullRootName = new SimpleObjectWithNullRootName();
        objWithNullRootName.setMyObject(obj);

        String xml = mapper.writeValueAsString(objWithNullRootName);

        JsonParser parser = mapper.getFactory().createParser(xml);
        Object readValue = parser.readValueAs(SimpleObjectWithNullRootName.class);

        assertEquals(readValue, objWithNullRootName);
        assertEquals(readValue.getMyObject(), obj);
    }
}