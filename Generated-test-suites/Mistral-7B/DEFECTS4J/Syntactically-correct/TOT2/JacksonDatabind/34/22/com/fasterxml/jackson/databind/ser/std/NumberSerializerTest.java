package com.example.junit; // Replace with your test package

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerializeInt() throws JsonGenerationException, IOException {
        // ... test case for int goes here
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, IOException {
        // ... test case for null goes here
    }

    @Test
    public void testSerializeOutOfRangeInt() throws JsonGenerationException, IOException {
        // ... test case for out-of-range int goes here
    }

    // ... test cases for BigDecimal, BigInteger, and other edge cases and exceptions go here

    @Test
    public void testSerializeThrowIOException() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream());

        try {
            serializer.serialize(new IllegalStateException(), generator, null);
        } catch (IOException e) {
            // expected
        }
    }

    // ... other test cases for edge cases and exceptions go here

}