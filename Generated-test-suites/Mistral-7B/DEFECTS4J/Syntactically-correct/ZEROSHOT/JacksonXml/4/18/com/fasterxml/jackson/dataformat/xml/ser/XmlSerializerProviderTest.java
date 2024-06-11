package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Unit tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest extends BaseTest {

    @Test
    public void testXmlSerializerProviderBasic() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.putPOJOIn("person", new Person());
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mapper().getXmlFactory().createGenerator(testOutput, JsonEncoding.UTF8, new DefaultSerializerProvider());
        xmlProvider.writeValue(gen, rootNode);
        gen.close();

        String output = testOutput.toString();
        String expected = "<person xmlns=\"\">" +
                "<name>John Doe</name>" +
                "<age>35</age>" +
                "</person>";
        assertEquals(expected, output);
    }

    @Test
    public void testXmlSerializerProviderWithRootName() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.putPOJOIn("person", new Person("John Doe", 35));
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        xmlProvider.setSerializationConfig(mapper().getSerializationConfig());
        xmlProvider.setConfig(mapper().getSerializationConfig());
        xmlProvider.setActiveAttributeFilter(mapper().getSerializationConfig().getAttributeFilters());
        xmlProvider.setNilValueSerializer(mapper().getSerializationConfig().getNullValueSerializer());
        xmlProvider.setRootValueSerializer(new SimpleBeanPropertySerializer(mapper().constructType(Person.class)));
        xmlProvider.setDefaultSerializerProviders(Arrays.asList(xmlProvider, new DefaultSerializerProvider()));
        String rootName = "employee";
        xmlProvider.setRootName(rootName);

        JsonGenerator gen = mapper().getXmlFactory().createGenerator(testOutput, JsonEncoding.UTF8, new DefaultSerializerProvider());
        xmlProvider.writeValue(gen, rootNode);
        gen.close();

        String output = testOutput.toString();
        String expected = "<employee xmlns=\"\">" +
                "<name>John Doe</name>" +
                "<age>35</age>" +
                "</employee>";
        assertEquals(expected, output);
    }

    @Test(expected = JsonMappingException.class)
    public void testXmlSerializerProviderWithInvalidRootName() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.putPOJOIn("person", new Person());
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        xmlProvider.setSerializationConfig(mapper().getSerializationConfig());
        xmlProvider.setConfig(mapper().getSerializationConfig());
        xmlProvider.setActiveAttributeFilter(mapper().getSerializationConfig().getAttributeFilters());
        xmlProvider.setNilValueSerializer(mapper().getSerializationConfig().getNullValueSerializer());
        xmlProvider.setRootValueSerializer(new SimpleBeanPropertySerializer(mapper().constructType(Person.class)));
        xmlProvider.setDefaultSerializerProviders(Arrays.asList(xmlProvider, new DefaultSerializerProvider()));
        String rootName = "person{invalidXMLName}";
        xmlProvider.setRootName(rootName);

        JsonGenerator gen = mapper().getXmlFactory().createGenerator(testOutput, JsonEncoding.UTF8, new DefaultSerializerProvider());
        xmlProvider.writeValue(gen, rootNode);
        gen.close();

        fail("Expected JsonMappingException, but got no exception");
    }

    @Test
    public void testXmlSerializerProviderWithNullValue() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.putNull("age");
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mapper().getXmlFactory().createGenerator(testOutput, JsonEncoding.UTF8, new DefaultSerializerProvider());
        xmlProvider.writeValue(gen, rootNode);
        gen.close();

        String output = testOutput.toString();
        String expected = "<person xmlns=\"\">" +
                "<name>null</name>" +
                "</person>";
        assertEquals(expected, output);
    }

    @Test
    public void testXmlSerializerProviderWithEmptyList() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        rootNode.set("list", mapper().createValue(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, String.class)));
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mapper().getXmlFactory().createGenerator(testOutput, JsonEncoding.UTF8, new DefaultSerializerProvider());
        xmlProvider.writeValue(gen, rootNode);
        gen.close();

        String output = testOutput.toString();
        String expected = "<person xmlns=\"\">" +
                "<list xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"arrays:array\">" +
                "</list>" +
                "</person>";
        assertEquals(expected, output);
    }

    @Test
    public void testXmlSerializerProviderWithList() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        ObjectNode listNode = mapper().createObjectNode();
        listNode.add("item1");
        listNode.add("item2");
        rootNode.set("list", listNode);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        xmlProvider.setSerializationConfig(mapper().getSerializationConfig());
        xmlProvider.setConfig(mapper().getSerializationConfig());
        xmlProvider.setActiveAttributeFilter(mapper().getSerializationConfig().getAttributeFilters());
        xmlProvider.setNilValueSerializer(mapper().getSerializationConfig().getNullValueSerializer());
        xmlProvider.setRootValueSerializer(new SimpleBeanPropertySerializer(mapper().constructType(Person.class)));
        xmlProvider.setDefaultSerializerProviders(Arrays.asList(xmlProvider, new DefaultSerializerProvider()));
        String rootName = "person";
        xmlProvider.setRootName(rootName);

        JsonGenerator gen = mapper().getXmlFactory().createGenerator(testOutput, JsonEncoding.UTF8, new DefaultSerializerProvider());
        xmlProvider.writeValue(gen, rootNode);
        gen.close();

        String output = testOutput.toString();
        String expected = "<person xmlns=\"\">" +
                "<name>John Doe</name>" +
                "<list xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"arrays:array\">" +
                "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xsd:string\">item1</item>" +
                "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xsd:string\">item2</item>" +
                "</list>" +
                "</person>";
        assertEquals(expected, output);
    }

    @Test
    public void testXmlSerializerProviderWithArray() throws IOException {
        ObjectNode rootNode = mapper().createObjectNode();
        ObjectNode arrayNode = mapper().createObjectNode();
        arrayNode.add("item1");
        arrayNode.add("item2");
        rootNode.set("items", arrayNode);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        xmlProvider.setSerializationConfig(mapper().getSerializationConfig());
        xmlProvider.setConfig(mapper().getSerializationConfig());
        xmlProvider.setActiveAttributeFilter(mapper().getSerializationConfig().getAttributeFilters());
        xmlProvider.setNilValueSerializer(mapper().getSerializationConfig().getNullValueSerializer());
        xmlProvider.setRootValueSerializer(new SimpleBeanPropertySerializer(mapper().constructType(Person[].class)));
        xmlProvider.setDefaultSerializerProviders(Arrays.asList(xmlProvider, new DefaultSerializerProvider()));
        String rootName = "person";
        xmlProvider.setRootName(rootName);

        JsonGenerator gen = mapper().getXmlFactory().createGenerator(testOutput, JsonEncoding.UTF8, new DefaultSerializerProvider());
        xmlProvider.writeValue(gen, rootNode);
        gen.close();

        String output = testOutput.toString();
        String expected = "<person xmlns=\"\">" +
                "<name>John Doe</name>" +
                "<items xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"arrays:array\">" +
                "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xsd:string\">item1</item>" +
                "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xsd:string\">item2</item>" +
                "</items>" +
                "</person>";
        assertEquals(expected, output);
    }

    static class Person {
        private final String name;
        private final int age;

        public Person() {
            this("Unknown", 0);
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}