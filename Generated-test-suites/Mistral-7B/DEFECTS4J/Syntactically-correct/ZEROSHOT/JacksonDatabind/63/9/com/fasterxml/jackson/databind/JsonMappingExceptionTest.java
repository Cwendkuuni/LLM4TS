package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        try {
            // Test constructor with message only
            JsonMappingException e1 = new JsonMappingException("Message 1");
            assertEquals("Message 1", e1.getMessage());
            assertNull(e1.getFrom());
            assertNull(e1.getPath());

            // Test constructor with message and root cause
            JsonProcessingException e2 = new JsonProcessingException("Message 2", new IOException("IO Error"));
            JsonMappingException e3 = new JsonMappingException(e2);
            assertEquals("Message 2", e3.getMessage());
            assertEquals("IO Error", e3.getCause().getMessage());
            assertNull(e3.getFrom());
            assertNull(e3.getPath());

            // Test constructor with message, location and root cause
            JsonLocation loc = new JsonLocation(1, 1, "test.json");
            JsonProcessingException e4 = new JsonProcessingException("Message 3", loc, new IOException("IO Error"));
            JsonMappingException e5 = new JsonMappingException(e4);
            assertEquals("Message 3", e5.getMessage());
            assertEquals("IO Error", e5.getCause().getMessage());
            assertEquals(loc, e5.getLocation());
            assertNull(e5.getFrom());
            assertNull(e5.getPath());

            // Test constructor with message, processor and root cause
            Closeable processor = new ByteArrayInputStream(new byte[0]);
            JsonProcessingException e6 = new JsonProcessingException("Message 4", processor, new IOException("IO Error"));
            JsonMappingException e7 = new JsonMappingException(e6);
            assertEquals("Message 4", e7.getMessage());
            assertEquals("IO Error", e7.getCause().getMessage());
            assertEquals(processor, e7.getProcessor());
            assertNull(e7.getFrom());
            assertNull(e7.getPath());

            // Test from method with JsonParser
            JsonParser p = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(new byte[0]));
            JsonMappingException e8 = JsonMappingException.from(p, "Message 5");
            assertEquals("Message 5", e8.getMessage());
            assertNull(e8.getCause());
            assertNull(e8.getProcessor());
            assertNull(e8.getFrom());
            assertNull(e8.getPath());

            // Test from method with JsonParser, message and root cause
            JsonMappingException e9 = JsonMappingException.from(p, "Message 6", new IOException("IO Error"));
            assertEquals("Message 6", e9.getMessage());
            assertEquals("IO Error", e9.getCause().getMessage());
            assertNull(e9.getProcessor());
            assertNull(e9.getFrom());
            assertNull(e9.getPath());

            // Test from method with JsonGenerator
            JsonGenerator g = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream());
            JsonMappingException e10 = JsonMappingException.from(g, "Message 7");
            assertEquals("Message 7", e10.getMessage());
            assertNull(e10.getCause());
            assertEquals(g, e10.getProcessor());
            assertNull(e10.getFrom());
            assertNull(e10.getPath());

            // Test from method with JsonGenerator, message and root cause
            JsonMappingException e11 = JsonMappingException.from(g, "Message 8", new IOException("IO Error"));
            assertEquals("Message 8", e11.getMessage());
            assertEquals("IO Error", e11.getCause().getMessage());
            assertEquals(g, e11.getProcessor());
            assertNull(e11.getFrom());
            assertNull(e11.getPath());

            // Test from method with DeserializationContext
            ObjectMapper mapper = new ObjectMapper();
            JsonMappingException e12 = JsonMappingException.from(mapper.getDeserializationContext(), "Message 9");
            assertEquals("Message 9", e12.getMessage());
            assertNull(e12.getCause());
            assertNull(e12.getProcessor());
            assertNull(e12.getFrom());
            assertNull(e12.getPath());

            // Test from method with DeserializationContext, message and root cause
            JsonParser p2 = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));
            JsonMappingException e13 = JsonMappingException.from(mapper.getDeserializationContext(), "Message 10", p2);
            assertEquals("Message 10", e13.getMessage());
            assertNull(e13.getCause());
            assertEquals(p2, e13.getProcessor());
            assertNull(e13.getFrom());
            assertNull(e13.getPath());

            // Test from method with SerializerProvider
            ObjectMapper mapper2 = new ObjectMapper();
            JsonMappingException e14 = JsonMappingException.from(mapper2.getSerializerProvider(), "Message 11");
            assertEquals("Message 11", e14.getMessage());
            assertNull(e14.getCause());
            assertNull(e14.getProcessor());
            assertNull(e14.getFrom());
            assertNull(e14.getPath());

            // Test from method with SerializerProvider, message and root cause
            JsonGenerator g2 = mapper2.getFactory().createGenerator(new ByteArrayOutputStream());
            JsonMappingException e15 = JsonMappingException.from(mapper2.getSerializerProvider(), "Message 12", g2);
            assertEquals("Message 12", e15.getMessage());
            assertNull(e15.getCause());
            assertEquals(g2, e15.getProcessor());
            assertNull(e15.getFrom());
            assertNull(e15.getPath());

            // Test fromUnexpectedIOE method
            IOException ioException = new IOException("IO Error");
            JsonMappingException e16 = JsonMappingException.fromUnexpectedIOE(ioException);
            assertEquals("Unexpected IOException (of type java.io.IOException): IO Error", e16.getMessage());
            assertNull(e16.getCause());
            assertNull(e16.getProcessor());
            assertNull(e16.getFrom());
            assertNull(e16.getPath());

            // Test wrapWithPath method with a String refFieldName
            IOException ioException2 = new IOException("IO Error");
            JsonParser p3 = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(new byte[0]));
            JsonMappingException e17 = new JsonMappingException("Message 13", ioException2);
            Reference ref = new Reference(p3, "fieldName");
            JsonMappingException e18 = JsonMappingException.wrapWithPath(e17, ref);
            assertEquals("Message 13", e18.getMessage());
            assertEquals("IO Error", e18.getCause().getMessage());
            assertEquals(p3, e18.getProcessor());
            assertEquals(ref, e18.getFrom());
            List<Reference> path = e18.getPath();
            assertEquals(1, path.size());
            assertEquals(ref, path.get(0));

            // Test wrapWithPath method with an int index
            JsonParser p4 = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(new byte[0]));
            JsonMappingException e19 = new JsonMappingException("Message 14", ioException2);
            Reference ref2 = new Reference(p4, 0);
            JsonMappingException e20 = JsonMappingException.wrapWithPath(e19, ref2);
            assertEquals("Message 14", e20.getMessage());
            assertEquals("IO Error", e20.getCause().getMessage());
            assertEquals(p4, e20.getProcessor());
            assertEquals(ref2, e20.getFrom());
            List<Reference> path2 = e20.getPath();
            assertEquals(1, path2.size());
            assertEquals(ref2, path2.get(0));

            // Test toString method
            JsonParser p5 = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(new byte[0]));
            JsonMappingException e21 = new JsonMappingException("Message 15", p5, new IOException("IO Error"));
            assertEquals("JsonMappingException: Message 15 (through reference chain: [com.fasterxml.jackson.core.JsonParser$Flags@42e6db42->])", e21.toString());

            // Test writeReplace method
            Reference ref3 = new Reference(p3, "fieldName");
            JsonMappingException e22 = new JsonMappingException("Message 16", ref3);
            Object e22Obj = e22.writeReplace();
            assertEquals("JsonMappingException{path=[com.fasterxml.jackson.databind.JsonMappingException$Reference@15db9782], processor=null, message='Message 16', cause=null, location=null}", e22Obj.toString());

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            try {
                if (processor != null) {
                    processor.close();
                }
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}