
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.CREATE, PayloadType.TASK, "/test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";
        InputStream payload = new ByteArrayInputStream("Test Content".getBytes());
        dummyPayloadStorage.upload(path, payload, 11);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test/path";
        InputStream downloadedPayload = dummyPayloadStorage.download(path);
        // Since the uploaded content is known, verify the downloaded content
        byte[] expectedContent = "Test Content".getBytes();

        byte[] downloadedBytes = new byte[expectedContent.length];
        int readBytes = downloadedPayload.read(downloadedBytes, 0, expectedContent.length);
        if (readBytes == -1) {
            throw new RuntimeException("Failed to read data from downloaded InputStream");
        }

        for (int i = 0; i < expectedContent.length; i++) {
            if (downloadedBytes[i] != expectedContent[i]) {
                throw new RuntimeException("Downloaded content does not match the expected content");
            }
        }
    }
}
