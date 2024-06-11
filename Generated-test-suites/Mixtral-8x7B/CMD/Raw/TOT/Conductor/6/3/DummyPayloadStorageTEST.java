
package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.CREATE, PayloadType.WORKFLOW, "/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload("/path", null, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadWithNullPayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload("/path", null, 0);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = dummyPayloadStorage.download("/path");
        assertNotNull(inputStream);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDownloadWithInvalidPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.download("/invalid/path");
    }
}
