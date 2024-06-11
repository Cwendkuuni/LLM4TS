package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilterFinder;
import com.fasterxml.jackson.databind.ser.std.StdSerializerTests;
import com.fasterxml.jackson.databind.test.BaseMapTest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link StdSerializer} abstract class.
 *
 * @author Kai Waehner
 */
public class StdSerializerTest extends BaseMapTest {

    // create an instance of the StdSerializer under test
    private StdSerializer<Object> serializer;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer = new StdSerializer<Object>(Object.class, provider);
    }

    /**
     * Test method for {@link StdSerializer#serialize(Object, JsonGenerator, SerializerProvider)}
     * with a simple Java object and default configuration.
     */
    @Test
    public void testSerializerWithSimpleObject() throws Exception {
        ObjectNode node;

        // prepare test data
        String testData = "{\"test\": \"test\"}";
        Object testObject = mapper.readValue(testData, Object.class);

        // run test
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jgen = jsonFactory.createGenerator(new StringBuilder(), null);
        serializer.serialize(testObject, jgen, null);
        jgen.flush();
        String jsonOutput = jsonFactory.createParser(new StringBuilder().toString()).getValueAsString();

        // check results
        node = mapper.readTree(jsonOutput);
        assertNotNull(node);
        assertEquals(testData, node.asText());
    }

    /**
     * Test method for {@link StdSerializer#serialize(Object, JsonGenerator, SerializerProvider)}
     * with a Java object and custom filter.
     */
    @Test
    public void testSerializerWithCustomFilter() throws Exception {
        ObjectNode node;

        // prepare test data
        Object testObject = new Object() {{
            put("test1", "test1");
            put("test2", "test2");
        }};
        FilterProvider filters = new SimpleBeanPropertyFilterFinder();
        PropertyFilter filter = filters.findPropertyFilters(Object.class, null, null)[0];

        // run test
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jgen = jsonFactory.createGenerator(new StringBuilder(), null);
        serializer = new StdSerializer<Object>(Object.class, filter, null);
        serializer.serialize(testObject, jgen, null);
        jgen.flush();
        String jsonOutput = jsonFactory.createParser(new StringBuilder().toString()).getValueAsString();

        // check results
        node = mapper.readTree(jsonOutput);
        assertNotNull(node);
        assertEquals("{\"test1\": \"test1\"}", node.asText());
    }
}