import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    // Test Case 1: Basic functionality of EnumSerializer with serialization as name
    @Test
    public void testSerializationAsName() throws IOException {
        final EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(Color[].class), false);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);

        gen.writeValue(Color.RED);
        gen.flush();
        gen.close();

        String output = "\"RED\"";
        Assert.assertEquals(output, objectMapper.writeValueAsString(Color.RED));
    }

    // Test Case 2: Basic functionality of EnumSerializer with serialization as index
    @Test
    public void testSerializationAsIndex() throws IOException {
        final EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(Color[].class), true);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);

        gen.writeValue(Color.RED);
        gen.flush();
        gen.close();

        String output = "0";
        Assert.assertEquals(output, objectMapper.writeValueAsString(Color.RED));
    }

    // Test Case 3: Edge case: Validation of EnumSerializer when using custom JsonFormat annotation
    @Test
    public void testSerializationWithCustomJsonFormat() throws IOException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, order = JsonFormat.Feature.ORDER_ALPHABETIC)
        enum CustomColor {
            BLUE, RED, GREEN
        }

        final EnumSerializer serializer = new EnumSerializer(CustomColor[].class, null, null);
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);

        gen.writeValue(CustomColor.RED);
        gen.flush();
        gen.close();

        String output = "\"RED\"";
        Assert.assertEquals(output, objectMapper.writeValueAsString(CustomColor.RED));
    }

    // Test Case 4: Error scenario: Invalid EnumSerializer constructor
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEnumSerializerConstruction() {
        new EnumSerializer(null, null);
    }
}