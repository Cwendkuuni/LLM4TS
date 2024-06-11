import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.START, PayloadType.TASK, "/path");
        assertNull(result);
    }

    @Test
    public void testGetLocationWithNullPayloadType() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.START, null, "/path");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadNullInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload("/path", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload(null, new ByteArrayInputStream(new byte[0]), 0);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream result = dummyPayloadStorage.download("/path");
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDownloadWithEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.download("");
    }
}