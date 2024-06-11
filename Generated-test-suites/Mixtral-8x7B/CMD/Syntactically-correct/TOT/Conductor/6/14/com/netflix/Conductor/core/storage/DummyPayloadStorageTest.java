
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Test;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        assertNull(payloadStorage.getLocation(Operation.START, PayloadType.TASK, "path"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        payloadStorage.upload("path", inputStream, 4L);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        InputStream inputStream = payloadStorage.download("path");
        // check if inputStream is not null
        assertNull(inputStream);
    }
}
