package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultPrettyPrinterEmptyJson() throws JsonProcessingException {
        String json = mapper.writer(DefaultPrettyPrinter.class).writeValueAsString(new Object());
        assertTrue(json.isEmpty());
    }

    @Test
    public void testDefaultPrettyPrinterSimpleObject() throws JsonProcessingException {
        String json = mapper.writer(DefaultPrettyPrinter.class)
                .withRootSeparator("--")
                .writeValueAsString(new SimpleObject("name", "John Doe"));
        String expected = "--{\n" +
                "  \"name\": \"John Doe\"\n" +
                "}";
        assertEquals(expected, json);
    }

    @Test
    public void testDefaultPrettyPrinterNestedObjects() throws JsonProcessingException {
        String json = mapper.writer(DefaultPrettyPrinter.class)
                .withRootSeparator("--")
                .writeValueAsString(new NestedObjects("outer", new NestedObject("name", "John"), new NestedObject("age", 30)));
        String expected = "--{\n" +
                "  \"outer\": {\n" +
                "    \"name\": {\n" +
                "      \"name\": \"John\"\n" +
                "    },\n" +
                "    \"age\": 30\n" +
                "  }\n" +
                "}";
        assertEquals(expected, json);
    }

    @Test
    public void testDefaultPrettyPrinterEmptyArray() throws JsonProcessingException {
        String json = mapper.writer(DefaultPrettyPrinter.class).writeValueAsString(new Object());
        assertTrue(json.isEmpty());
    }

    @Test
    public void testDefaultPrettyPrinterSimpleArray() throws JsonProcessingException {
        String json = mapper.writer(DefaultPrettyPrinter.class)
                .writeValueAsString(new Integer[3]);
        String expected = "[ ]";
        assertEquals(expected, json);
    }

    @Test
    public void testDefaultPrettyPrinterNestedArrays() throws JsonProcessingException {
        String json = mapper.writer(DefaultPrettyPrinter.class)
                .writeValueAsString(new int[][]{new int[]{1, 2}, new int[]{3, 4}});
        String expected = "[ [\n" +
                "  1,\n" +
                "  2\n" +
                "],\n" +
                "[ 3,\n" +
                "  4 ]\n" +
                "]";
        assertEquals(expected, json);
    }

    @Test(expected = IllegalStateException.class)
    public void testDefaultPrettyPrinterWithNullObject() throws JsonProcessingException {
        mapper.writer(DefaultPrettyPrinter.class).writeValueAsString(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testDefaultPrettyPrinterWithNullArray() throws JsonProcessingException {
        mapper.writer(DefaultPrettyPrinter.class).writeValueAsString(null);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomIndenters() throws JsonProcessingException {
        String json = mapper.writer(DefaultPrettyPrinter.class)
                .indentObjectsWith(new CustomObjectIndenter())
                .indentArraysWith(new CustomArrayIndenter())
                .writeValueAsString(new Object() {
                    Object obj = new Object();
                    Integer[] arr = {1, 2, 3};
                });
        String expected = "{\n" +
                "  obj:\n" +
                "  {\n" +
                "  },\n" +
                "  arr:\n" +
                "  [\n" +
                "    1,\n" +
                "    2,\n" +
                "    3\n" +
                "  ]\n" +
                "}";
        assertEquals(expected, json);
    }

    static class SimpleObject {
        private final String name;

        public SimpleObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class NestedObjects {
        private final String outerName;
        private final NestedObject outerObj;
        private final NestedObject[] nestedObjects;

        public NestedObjects(String outerName, NestedObject outerObj, NestedObject[] nestedObjects) {
            this.outerName = outerName;
            this.outerObj = outerObj;
            this.nestedObjects = nestedObjects;
        }

        public String getOuterName() {
            return outerName;
        }

        public NestedObject getOuterObj() {
            return outerObj;
        }

        public NestedObject[] getNestedObjects() {
            return nestedObjects;
        }
    }

    static class NestedObject {
        private final String name;
        private final int age;

        public NestedObject(String name, int age) {
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

    static class CustomObjectIndenter implements Indenter {
        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            g.writeRaw("  ");
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }

    static class CustomArrayIndenter implements Indenter {
        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            g.writeRaw("    ");
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }
}