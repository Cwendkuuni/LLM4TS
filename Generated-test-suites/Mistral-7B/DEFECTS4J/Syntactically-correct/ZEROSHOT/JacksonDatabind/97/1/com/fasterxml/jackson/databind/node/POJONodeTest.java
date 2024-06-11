package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test POJONode class
 */
public class POJONodeTest {

    @Test
    public void testPOJONode_Basic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node1 = mapper.valueToTree("{\"name\":\"John\",\"age\":30}");
        POJONode node2 = mapper.valueToTree(new Person("Mary", 25));

        assertEquals(JsonNodeType.POJO, node1.getNodeType());
        assertEquals(JsonNodeType.POJO, node2.getNodeType());

        Object pojo1 = node1.getPojo();
        Object pojo2 = node2.getPojo();

        assertEquals("John", pojo1.getClass().cast(pojo1).getName());
        assertEquals(30, pojo1.getClass().cast(pojo1).getAge());

        assertEquals("Mary", pojo2.getClass().cast(pojo2).getName());
        assertEquals(25, pojo2.getClass().cast(pojo2).getAge());

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node1));

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node1));

        assertFalse(node1.equals(node2));
        assertFalse(node2.equals(node1));

        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node1.hashCode());

        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node1.hashCode());

        assertNotEquals(node1.hashCode(), node2.hashCode());
        assertNotEquals(node2.hashCode(), node1.hashCode());

        assertEquals("{\"name\":\"John\",\"age\":30}", node1.asText());
        assertEquals("{\"name\":\"John\",\"age\":30}", node1.asText(null));

        assertTrue(node1.asBoolean(false));
        assertFalse(node1.asBoolean(false));

        assertEquals(30, node1.asInt(0));
        assertEquals(0, node1.asInt(0));

        assertEquals(30L, node1.asLong(0L));
        assertEquals(0L, node1.asLong(0L));

        assertEquals(30.0, node1.asDouble(0.0), 0.001);
        assertEquals(0.0, node1.asDouble(0.0), 0.001);

        // Test null
        POJONode nullNode = mapper.valueToTree(null);
        assertEquals(JsonNodeType.NULL, nullNode.getNodeType());
        assertNull(nullNode.getPojo());
        assertEquals("null", nullNode.asText());
        assertFalse(nullNode.asBoolean(false));
        assertEquals(0, nullNode.asInt(0));
        assertEquals(0L, nullNode.asLong(0L));
        assertEquals(0.0, node1.asDouble(0.0), 0.001);

        // Test binary data
        byte[] binaryData = new byte[] { 1, 2, 3 };
        POJONode binaryNode = mapper.valueToTree(binaryData);
        assertEquals(JsonNodeType.BINARY, binaryNode.getNodeType());
        assertArrayEquals(binaryData, binaryNode.binaryValue());
        assertNull(binaryNode.getPojo());
        assertEquals("(binary value of 3 bytes)", binaryNode.asText());
        assertFalse(binaryNode.asBoolean(false));
        assertEquals(0, binaryNode.asInt(0));
        assertEquals(0L, binaryNode.asLong(0L));
        assertEquals(0.0, node1.asDouble(0.0), 0.001);
    }

    static class Person {
        private final String name;
        private final int age;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (!name.equals(person.name)) return false;
            if (age != person.age) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }

        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d}", name, age);
        }
    }
}