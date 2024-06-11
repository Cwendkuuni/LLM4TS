package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    // Test for getLocation() method
    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.START, PayloadType.TASK, "path"));
    }

    // Test for upload() method
    @Test
    public void testUpload() {
        String path = "path";
        InputStream payload = null;
        long payloadSize = 0;
        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    // Test for download() method with null InputStream
    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("path"));
    }
}