import static org.junit.Assert.assertNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.junit.Test;

public class DummyPayloadStorageTest {
    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.CREATE, PayloadType.WORKFLOW, "");
        assertNull(result);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload("test", new ByteArrayInputStream("test".getBytes()), 4L);
        // Since there are no checks in the method, we can't really test it.
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream result = dummyPayloadStorage.download("test");
        assertNull(result);
    }
}