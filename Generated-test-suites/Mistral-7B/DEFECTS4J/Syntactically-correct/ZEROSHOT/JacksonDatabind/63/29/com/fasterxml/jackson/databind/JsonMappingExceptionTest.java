package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException$Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testJsonMappingException() throws IOException {
        String json = "{\"name\":\"John\",\"age\":[18,21,37]," +
                "\"friends\":[{\"name\":\"Alice\",\"friends\":[" +
                "{\"name\":\"Bob\",\"age\":10}]},{\"name\":\"Charlie\"}]}}";

        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser(json);

        try {
            mapper.readValue(jsonParser, LinkedHashMap.class);
        } catch (JsonMappingException e) {
            assertEquals("Expecting property value of type: java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>, but found: [B", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertEquals(jsonParser, e.getProcessor());
            assertNotNull(e.getPath());
            assertEquals(1, e.getPath().size());
            JsonMappingException referenceException = (JsonMappingException) e.getPath().get(0);
            assertEquals("age", referenceException.getFieldName());
            assertEquals(-1, referenceException.getIndex());
            assertEquals("[B", referenceException.getDescription());
            assertEquals("java.lang.Class", referenceException.getFrom().getClass().getName());
            assertNotNull(e.getLocalizedMessage());
            assertTrue(e.getLocalizedMessage().startsWith("Unexpected type found: [B"));
            assertTrue(e.getLocalizedMessage().endsWith("at [Source: (StringReader) at line 1, column 31]"));
            return;
        }

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testJsonMappingExceptionPathSize() throws IOException {
        String json = "{\"name\":\"John\",\"age\":[18,21,37]," +
                "\"friends\":[{\"name\":\"Alice\",\"friends\":[" +
                "{\"name\":\"Bob\",\"age\":10}]},{\"name\":\"Charlie\",\"friends\":[{\"name\":\"David\"}]}" +
                "}}";

        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser(json);

        try {
            mapper.readValue(jsonParser, LinkedHashMap.class);
        } catch (JsonMappingException e) {
            assertEquals("Cannot construct instance of `java.util.LinkedHashMap` (although at least one Creator exists): can not instantiate value class [simple type, class java.util.LinkedHashMap$Entry] from JSON String", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertEquals(jsonParser, e.getProcessor());
            assertNotNull(e.getPath());
            assertEquals(2, e.getPath().size());
            JsonMappingException referenceException1 = (JsonMappingException) e.getPath().get(0);
            assertEquals("friends", referenceException1.getFieldName());
            assertEquals(-1, referenceException1.getIndex());
            assertEquals("java.lang.Class", referenceException1.getFrom().getClass().getName());
            JsonMappingException referenceException2 = (JsonMappingException) e.getPath().get(1);
            assertEquals("friends", referenceException2.getFieldName());
            assertEquals(-1, referenceException2.getIndex());
            assertEquals("java.util.LinkedHashMap$Entry", referenceException2.getFrom().getClass().getName());
            return;
        }

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testJsonMappingExceptionWithReferenceFromArray() throws IOException {
        String json = "[1,2,3]";

        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser(json);

        try {
            mapper.readValue(jsonParser, int[].class);
        } catch (JsonMappingException e) {
            assertEquals("Unable to test array serialization as called with JsonParser", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertEquals(jsonParser, e.getProcessor());
            assertNotNull(e.getPath());
            assertEquals(1, e.getPath().size());
            JsonMappingException referenceException = (JsonMappingException) e.getPath().get(0);
            assertEquals("-1", referenceException.getFieldName());
            assertEquals(-1, referenceException.getIndex());
            assertEquals("[I", referenceException.getDescription());
            assertEquals("java.lang.Class", referenceException.getFrom().getClass().getName());
            return;
        }

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testJsonMappingExceptionWithReferenceFromMap() throws IOException {
        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("a", new HashMap<String, String>() {{
            put("b", "1");
            put("c", "2");
        }});

        String json = mapper.writeValueAsString(map);

        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser(json);

        try {
            mapper.readValue(jsonParser, String[].class);
        } catch (JsonMappingException e) {
            assertEquals("Expected type 'java.lang.String[]' but was: 'java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>'", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertEquals(jsonParser, e.getProcessor());
            assertNotNull(e.getPath());
            assertEquals(1, e.getPath().size());
            JsonMappingException referenceException = (JsonMappingException) e.getPath().get(0);
            assertEquals("asMap()", referenceException.getFieldName());
            assertEquals(-1, referenceException.getIndex());
            assertEquals("java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.String>>", referenceException.getDescription());
            assertEquals("java.util.HashMap", referenceException.getFrom().getClass().getName());
            return;
        }

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testJsonMappingExceptionWithReferenceFromBean() throws IOException {
        class TestBean {
            private int value;
            private TestBean nestedBean;

            public TestBean() {}

            public TestBean(int value, TestBean nestedBean) {
                this.value = value;
                this.nestedBean = nestedBean;
            }

            public int getValue() {
                return value;
            }

            public TestBean getNestedBean() {
                return nestedBean;
            }
        }

        TestBean testBean1 = new TestBean(1, null);
        TestBean testBean2 = new TestBean(2, testBean1);

        String json = mapper.writeValueAsString(testBean2);

        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser(json);

        try {
            mapper.readValue(jsonParser, String[].class);
        } catch (JsonMappingException e) {
            assertEquals("Expected type 'java.lang.String[]' but was: com.fasterxml.jackson.databind.JsonMappingException$Reference@7952e982", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertEquals(jsonParser, e.getProcessor());
            assertNotNull(e.getPath());
            assertEquals(1, e.getPath().size());
            JsonMappingException referenceException = (JsonMappingException) e.getPath().get(0);
            assertEquals("nestedBean", referenceException.getFieldName());
            assertEquals(-1, referenceException.getIndex());
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException$Reference@7952e982", referenceException.getDescription());
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException", referenceException.getFrom().getClass().getName());
            return;
        }

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testJsonMappingExceptionWithReferenceFromList() throws IOException {
        List<TestBean> testBeanList = Arrays.asList(new TestBean(1, null), new TestBean(2, null));

        String json = mapper.writeValueAsString(testBeanList);

        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser(json);

        try {
            mapper.readValue(jsonParser, String[].class);
        } catch (JsonMappingException e) {
            assertEquals("Expected type 'java.lang.String[]' but was: java.util.List<com.fasterxml.jackson.databind.JsonMappingException$Reference>", e.getMessage());
            assertEquals(JsonMappingException.Class.class, e.getClass());
            assertEquals(jsonParser, e.getProcessor());
            assertNotNull(e.getPath());
            assertEquals(1, e.getPath().size());
            JsonMappingException referenceException = (JsonMappingException) e.getPath().get(0);
            assertEquals("[Lcom.fasterxml.jackson.databind.JsonMappingException$Reference;", referenceException.getDescription());
            assertEquals("java.util.ArrayList", referenceException.getFrom().getClass().getName());
            return;
        }

        fail("Expected JsonMappingException to be thrown");
    }
}