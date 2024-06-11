import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import java.util.Map;
import java.util.HashMap;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
        JsonSerializer<?> intTypeSerializer = allDeserializers.get(Integer.TYPE.getName());
        JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());
        JsonSerializer<?> longTypeSerializer = allDeserializers.get(Long.TYPE.getName());
        JsonSerializer<?> byteSerializer = allDeserializers.get(Byte.class.getName());
        JsonSerializer<?> byteTypeSerializer = allDeserializers.get(Byte.TYPE.getName());
        JsonSerializer<?> shortSerializer = allDeserializers.get(Short.class.getName());
        JsonSerializer<?> shortTypeSerializer = allDeserializers.get(Short.TYPE.getName());
        JsonSerializer<?> floatSerializer = allDeserializers.get(Float.class.getName());
        JsonSerializer<?> floatTypeSerializer = allDeserializers.get(Float.TYPE.getName());
        JsonSerializer<?> doubleSerializer = allDeserializers.get(Double.class.getName());
        JsonSerializer<?> doubleTypeSerializer = allDeserializers.get(Double.TYPE.getName());

        Assert.assertEquals(intSerializer, allDeserializers.get(Integer.class.getName()));
        Assert.assertEquals(intSerializer, allDeserializers.get(Integer.TYPE.getName()));
        Assert.assertEquals(longSerializer, allDeserializers.get(Long.class.getName()));
        Assert.assertEquals(longSerializer, allDeserializers.get(Long.TYPE.getName()));
        Assert.assertEquals(byteSerializer, allDeserializers.get(Byte.class.getName()));
        Assert.assertEquals(byteSerializer, allDeserializers.get(Byte.TYPE.getName()));
        Assert.assertEquals(shortSerializer, allDeserializers.get(Short.class.getName()));
        Assert.assertEquals(shortSerializer, allDeserializers.get(Short.TYPE.getName()));
        Assert.assertEquals(floatSerializer, allDeserializers.get(Float.class.getName()));
        Assert.assertEquals(floatSerializer, allDeserializers.get(Float.TYPE.getName()));
        Assert.assertEquals(doubleSerializer, allDeserializers.get(Double.class.getName()));
        Assert.assertEquals(doubleSerializer, allDeserializers.get(Double.TYPE.getName()));
    }
}