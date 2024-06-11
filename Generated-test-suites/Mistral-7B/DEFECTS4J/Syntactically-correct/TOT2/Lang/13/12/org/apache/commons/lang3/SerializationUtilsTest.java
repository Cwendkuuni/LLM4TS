import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class SerializationUtilsTest {

    // Clone tests

    @Test
    public void testCloneSerializableObject() throws IOException, ClassNotFoundException {
        Serializable obj = new HashMap<>();
        Serializable clonedObj = SerializationUtils.clone(obj);
        Assert.assertNotSame(obj, clonedObj);
        Assert.assertEquals(obj, clonedObj);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Object readObj = SerializationUtils.deserialize(bais);
        Assert.assertEquals(obj, readObj);

        // Test cloning null object
        Serializable nullObj = null;
        try {
            SerializationUtils.clone(nullObj);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // expected
        }
    }

    // Serialize tests

    @Test
    public void testSerializeSerializableObject() throws IOException {
        Serializable obj = new HashMap<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedObj = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        Object readObj = SerializationUtils.deserialize(bais);
        Assert.assertEquals(obj, readObj);

        // Test serializing null object
        Serializable nullObj = null;
        try {
            ByteArrayOutputStream baosNull = new ByteArrayOutputStream();
            SerializationUtils.serialize(nullObj, baosNull);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    // Deserialize tests

    @Test
    public void testDeserializeSerializableObject() throws IOException, ClassNotFoundException {
        Serializable obj = new HashMap<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);

        byte[] serializedObj = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        Object readObj = SerializationUtils.deserialize(bais);
        Assert.assertEquals(obj, readObj);

        // Test deserializing null object
        try {
            InputStream nullInputStream = null;
            Object readObjNull = SerializationUtils.deserialize(nullInputStream);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    // Edge cases and exception handling tests

    @Test(expected = SerializationException.class)
    public void testCloneNonSerializableObject() {
        Object obj = new Object();
        SerializationUtils.clone(obj);
        // expected SerializationException
    }

    @Test(expected = IOException.class)
    public void testSerializeNonSerializableObject() {
        Object obj = new Object();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        // expected IOException
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeInvalidClass() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(new Object(), baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        SerializationUtils.deserialize(bais);
        // expected ClassNotFoundException
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidInputStream() {
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(invalidInputStream);
        // expected IOException
    }

}