package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ObjectCodec objectCodec;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        objectCodec = new ObjectMapper().getFactory().getEncoder().getCodec();
        generator = new GeneratorBase(Feature.AUTOFILL_CREATOR_ARRAYS.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }

            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // Do nothing, as we are only testing if the generator behaves correctly
            }
        };
        outputStream = new ByteArrayOutputStream();
        generator.setOutputPersonalizedStream(outputStream);
    }

    @Test
    public void testWriteBoolean() throws IOException, JsonProcessingException {
        generator.writeBoolean(true);
        generator.flush();
        Assert.assertEquals("true", outputStream.toString());
    }

    @Test
    public void testWriteNull() throws IOException, JsonProcessingException {
        generator.writeNull();
        generator.flush();
        Assert.assertEquals("null", outputStream.toString());
    }

    @Test
    public void testWriteNumber() throws IOException, JsonProcessingException {
        generator.writeNumber(123);
        generator.flush();
        Assert.assertEquals("123", outputStream.toString());
    }

    @Test
    public void testWriteNumberWithFeatureDisabled() throws IOException, JsonProcessingException {
        generator = new GeneratorBase(Feature.AUTOFILL_CREATOR_ARRAYS.getMask(), objectCodec) {
            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // Do nothing, as we are only testing if the generator behaves correctly
            }
        };
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        generator.flush();
        Assert.assertEquals("123", outputStream.toString());
    }

    @Test
    public void testWriteString() throws IOException, JsonProcessingException {
        generator.writeString("Test String");
        generator.flush();
        Assert.assertEquals("\"Test String\"", outputStream.toString());
    }

    @Test
    public void testWriteRawValue() throws IOException, JsonProcessingException {
        generator.writeRawValue("Test Raw Value");
        generator.flush();
        Assert.assertEquals("\"Test Raw Value\"", outputStream.toString());
    }

    @Test
    public void testWriteRawValueWithFeatureDisabled() throws IOException, JsonProcessingException {
        generator = new GeneratorBase(Feature.AUTOFILL_CREATOR_ARRAYS.getMask(), objectCodec) {
            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // Do nothing, as we are only testing if the generator behaves correctly
            }
        };
        generator.disable(Feature.WRITE_STRINGS_AS_RAW);
        generator.writeRawValue("Test Raw Value");
        generator.flush();
        Assert.assertEquals("\"Test Raw Value\"", outputStream.toString());
    }

    @Test
    public void testWriteRawValueWithCustomEncoder() throws IOException, JsonProcessingException {
        ObjectCodec customObjectCodec = new ObjectMapper().getFactory().createObjectCodec();
        customObjectCodec.setCodecFeature(JsonGenerator.Feature.WRITE_STRINGS_AS_RAW, false);
        generator = new GeneratorBase(Feature.AUTOFILL_CREATOR_ARRAYS.getMask(), customObjectCodec) {
            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // Do nothing, as we are only testing if the generator behaves correctly
            }
        };
        generator.writeRawValue("Test Raw Value");
        generator.flush();
        Assert.assertEquals("\"Test Raw Value\"", outputStream.toString());
    }

    @Test
    public void testWriteArray() throws IOException, JsonProcessingException {
        generator.writeStartArray();
        generator.writeNumber(1);
        generator.writeNumber(2);
        generator.writeNumber(3);
        generator.writeEndArray();
        generator.flush();
        Assert.assertEquals("[1,2,3]", outputStream.toString());
    }

    @Test
    public void testWriteObject() throws IOException, JsonProcessingException {
        MyObject obj = new MyObject();
        generator.writeObject(obj);
        generator.flush();
        Assert.assertEquals(new ObjectMapper().writeValueAsString(obj), outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testWriteObjectWithNullValue() throws IOException, JsonProcessingException {
        generator.writeObject(null);
        generator.flush();
    }

    @Test
    public void testWriteObjectWithCustomEncoder() throws IOException, JsonProcessingException {
        ObjectCodec customObjectCodec = new ObjectMapper().getFactory().createObjectCodec();
        customObjectCodec.setCodecFeature(JsonGenerator.Feature.WRITE_NULL_AS_NULL_OBJECT, false);
        generator = new GeneratorBase(Feature.AUTOFILL_CREATOR_ARRAYS.getMask(), customObjectCodec) {
            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // Do nothing, as we are only testing if the generator behaves correctly
            }
        };
        generator.writeObject(null);
        generator.flush();
        Assert.assertEquals("null", outputStream.toString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteObjectWithInvalidValue() throws IOException, JsonProcessingException {
        generator.writeObject(new Integer(1));
        generator.flush();
    }

    @Test
    public void testWriteBigDecimal() throws IOException, JsonProcessingException {
        generator.writeNumber(new BigDecimal("1234567890.1234567890"));
        generator.flush();
        Assert.assertEquals("\"1234567890.1234567890\"", outputStream.toString());
    }

    @Test
    public void testWriteBigDecimalWithFeatureDisabled() throws IOException, JsonProcessingException {
        generator = new GeneratorBase(Feature.AUTOFILL_CREATOR_ARRAYS.getMask(), objectCodec) {
            @Override
            protected void _verifyValueWrite(String typeMsg) throws IOException {
                // Do nothing, as we are only testing if the generator behaves correctly
            }
        };
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(new BigDecimal("1234567890.1234567890"));
        generator.flush();
        Assert.assertEquals("1234567890.123456789", outputStream.toString());
    }

    @Test
    public void testWriteBigDecimalWithCustomEncoder() throws IOException, JsonProcessingException {
        ObjectCodec customObjectCodec = new ObjectMapper().getFactory().createObjectCodec();
        customObjectCodec.setCodecFeature(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, false);
        generator = new GeneratorBase(Feature.AUTOFILL_CREATOR_ARRAYS.getMask(), customObjectCodec) {
            @Override
            protected String _asString(BigDecimal value) throws IOException {
                return value.toString();
            }
        };
        generator.writeNumber(new BigDecimal("1234567890.1234567890"));
        generator.flush();
        Assert.assertEquals("1234567890.1234567890", outputStream.toString());
    }

    @Test
    public void testWriteStringWithSurrogateCharacters() throws IOException, JsonProcessingException {
        generator.writeString("\uD800\uDC00");
        generator.flush();
        Assert.assertEquals("\"\uD800\uDC00\"", outputStream.toString());
    }

    private static class MyObject {
        private String name;

        public String getName() {
            return name;
        }
    }

    private static class MyEncoder extends StdSerializer<MyObject> {
        @Override
        public void serialize(MyObject t, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeFieldName("name");
            jg.writeString(t.getName());
        }
    }
}