import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

    @InjectMocks
    private DummyPayloadStorage dummyPayloadStorage;

    @Test
    public void testGetLocation() {
        Operation operation = new Operation();
        PayloadType payloadType = new PayloadType();
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        String path = "testPath";

        assertNull(dummyPayloadStorage.download(path));
    }
}