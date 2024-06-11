import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalStorageLocation;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.ANY;
        String path = "/test/path";

        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";
        InputStream payload = new ByteArrayInputStream("test data".getBytes());
        long payloadSize = 9;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }
}