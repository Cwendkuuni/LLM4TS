import java.io.InputStream;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
Operation operation = mock(Operation.class);
PayloadType payloadType = mock(PayloadType.class);
String path = "testPath";
InputStream payload = mock(InputStream.class);
long payloadSize = 100L;

@Test
public void testGetLocation_nullReturned() {
ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);
assertNull(result);
}

@Test
public void testUpload_noOperation() {
dummyPayloadStorage.upload(path, payload, payloadSize);
verify(payload, never()).close();
}

@Test
public void testDownload_nullReturned() {
InputStream result = dummyPayloadStorage.download(path);
assertNull(result);
}
}