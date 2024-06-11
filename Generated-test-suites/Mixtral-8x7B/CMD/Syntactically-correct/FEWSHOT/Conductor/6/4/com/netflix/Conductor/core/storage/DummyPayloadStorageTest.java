import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import java.io.InputStream;
import org.junit.Test;

public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
Operation operation = mock(Operation.class);
PayloadType payloadType = mock(PayloadType.class);
String path = "testPath";

assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
verify(operation, never()).getWorkflowId();
verify(payloadType, never()).getName();
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";
InputStream payload = mock(InputStream.class);
long payloadSize = 100L;

dummyPayloadStorage.upload(path, payload, payloadSize);
verify(payload, never()).close();
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";

assertNull(dummyPayloadStorage.download(path));
}
}