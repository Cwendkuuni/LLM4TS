import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws SerializationException, IOException {
        Map<String, Integer> mapToSerialize = new HashMap<>();
        mapToSerialize.put("one", 1);
        mapToSerialize.put("two", 2);

        byte[] serializedData = SerializationUtils.serialize(mapToSerialize);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(serializedData);

        Assert.assertEquals(mapToSerialize, deserializedMap);
    }

    @Test
    public void testSerializeAndDeserializeNullObject() throws SerializationException, IOException {
        Object nullObject = null;
        byte[] serializedNullObject = SerializationUtils.serialize(nullObject);

        try {
            Object deserializedNullObject = SerializationUtils.deserialize(serializedNullObject);
            Assert.fail("Expected SerializationException but got " + deserializedNullObject.getClass().getName());
        } catch (SerializationException e) {
            // expected
        }
    }

    @Test
    public void testSerializeAndDeserializeEmptyObject() throws SerializationException, IOException {
        Map<String, Integer> emptyMap = new HashMap<>();
        byte[] serializedEmptyMap = SerializationUtils.serialize(emptyMap);
        Map<String, Integer> deserializedEmptyMap = (Map<String, Integer>) SerializationUtils.deserialize(serializedEmptyMap);

        Assert.assertEquals(emptyMap, deserializedEmptyMap);
    }

    @Test
    public void testSerializeAndDeserializeComplexObjectGraph() throws SerializationException, IOException {
        Map<String, Map<String, Integer>> complexMap = new HashMap<>();
        Map<String, Integer> nestedMap = new HashMap<>();
        nestedMap.put("one", 1);
        nestedMap.put("two", 2);
        complexMap.put("one", nestedMap);
        complexMap.put("two", nestedMap);

        byte[] serializedComplexMap = SerializationUtils.serialize(complexMap);
        Map<String, Map<String, Integer>> deserializedComplexMap = (Map<String, Map<String, Integer>>) SerializationUtils.deserialize(serializedComplexMap);

        Assert.assertEquals(complexMap, deserializedComplexMap);
    }
}