package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        prettyPrinter = new DefaultPrettyPrinter();
        mapper = new ObjectMapper();
        mapper.setPrettyPrinter(prettyPrinter);
    }

    @Test
    public void testDefaultConstructor() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new int[] {1, 2, 3});
        assertTrue(json.contains("[\n 0:\n  \n  0: 1\n  \n  1: 2\n  \n  2: 3\n]"));
    }

    @Test
    public void testRootSeparatorConstructor() throws JsonProcessingException {
        String rootSeparator = "//";
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(rootSeparator);
        mapper.setPrettyPrinter(prettyPrinterWithRootSeparator);
        String json = mapper.writeValueAsString(new int[] {1, 2, 3});
        assertTrue(json.contains("//[\n// 0:\n//  \n//  0: 1\n//  \n//  1: 2\n//  \n//  2: 3\n]"));
    }

    @Test
    public void testSerialization() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter2 = mapper.readValue(mapper.writeValueAsBytes(prettyPrinter1), DefaultPrettyPrinter.class);
        assertSame(prettyPrinter1, prettyPrinter2);
    }

    @Test
    public void testWriteRootValueSeparator() throws JsonProcessingException {
        String rootSeparator = "---";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        String json = mapper.writeValueAsString("rootValue");
        assertTrue(json.contains("---rootValue"));
    }

    @Test
    public void testWriteStartObject() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            String address = new Object() {{
                String street = "123 Main St";
                String city = "Springfield";
            }};
        }});
        assertTrue(json.contains("{\n  \"name\": \"John Doe\"\n ,\n  \"address\": {\n    \"street\": \"123 Main St\"\n   ,\n    \"city\": \"Springfield\"\n  }\n}"));
    }

    @Test
    public void testBeforeObjectEntries() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            String address = new Object() {{
                String street = "123 Main St";
                String city = "Springfield";
            }};
        }});
        assertTrue(json.contains("\n  {\n    \"name\": \"John Doe\""));
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            String address = new Object() {{
                String street = "123 Main St";
                String city = "Springfield";
            }};
        }});
        assertTrue(json.contains("\n    \"city\": \"Springfield\"\n"));
        assertTrue(json.contains(": "));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            String address = new Object() {{
                String street = "123 Main St";
                String city = "Springfield";
            }};
        }});
        assertTrue(json.contains("\n  }"));
        assertTrue(json.contains(",\n  {\n"));
        assertTrue(json.contains("\n    \"name\": \"John Doe\""));
    }

    @Test
    public void testWriteEndObject() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            String address = new Object() {{
                String street = "123 Main St";
                String city = "Springfield";
            }};
        }});
        assertTrue(json.contains("}"));
    }

    @Test
    public void testWriteStartArray() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new int[] {1, 2, 3});
        assertTrue(json.contains("["));
    }

    @Test
    public void testBeforeArrayValues() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new int[] {1, 2, 3});
        assertTrue(json.contains("\n  ["));
    }

    @Test
    public void testWriteArrayValueSeparator() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new int[] {1, 2, 3});
        assertTrue(json.contains(",\n  2: "));
        assertTrue(json.contains(",\n  1:\n   "));
    }

    @Test
    public void testWriteEndArray() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new int[] {1, 2, 3});
        assertTrue(json.contains("]"));
    }
}