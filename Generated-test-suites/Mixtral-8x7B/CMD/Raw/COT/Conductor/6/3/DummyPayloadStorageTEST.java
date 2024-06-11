import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DummyPayloadStorageTest {

DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

@Test
public void testGetLocation_NullArguments_ReturnsNull() {
Operation operation = null;
PayloadType payloadType = null;
String path = null;

assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
verify(dummyPayloadStorage, never()).getLocation(operation, payloadType, path);
}

@Test
public void testGetLocation_NonNullableArguments_DoesNothing() {
Operation operation = mock(Operation.class);
PayloadType payloadType = mock(PayloadType.class);
String path = "testPath";

dummyPayloadStorage.getLocation(operation, payloadType, path);
verify(dummyPayloadStorage, times(1)).getLocation(operation, payloadType, path);
}

@Test
public void testUpload_NonNullableArguments_DoesNothing() {
String path = "testPath";
InputStream payload = mock(InputStream.class);
long payloadSize = 100L;

dummyPayloadStorage.upload(path, payload, payloadSize);
verify(dummyPayloadStorage, times(1)).upload(path, payload, payloadSize);
}

@Test
public void testDownload_NonNullableArguments_ReturnsNull() {
String path = "testPath";

assertNull(dummyPayloadStorage.download(path));
verify(dummyPayloadStorage, times(1)).download(path);
}
}