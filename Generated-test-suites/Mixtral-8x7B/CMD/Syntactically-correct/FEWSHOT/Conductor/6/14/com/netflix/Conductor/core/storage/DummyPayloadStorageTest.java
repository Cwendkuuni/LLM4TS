import java.io.InputStream;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
Operation operation = mock(Operation.class);
PayloadType payloadType = mock(PayloadType.class);
String path = "testPath";

ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

verify(dummyPayloadStorage, times(1)).getLocation(operation, payloadType, path);
verify(operation, times(1)).getId();
verify(payloadType, times(1)).getName();
assertEquals(null, result);
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";
InputStream payload = mock(InputStream.class);
long payloadSize = 100L;

dummyPayloadStorage.upload(path, payload, payloadSize);

verify(dummyPayloadStorage, times(1)).upload(path, payload, payloadSize);
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";

InputStream result = dummyPayloadStorage.download(path);

verify(dummyPayloadStorage, times(1)).download(path);
assertEquals(null, result);
}

@Test
public void testGetLocationWithMockOperation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
Operation operation = mock(Operation.class);
PayloadType payloadType = mock(PayloadType.class);
String path = "testPath";
ExternalStorageLocation mockLocation = mock(ExternalStorageLocation.class);

when(operation.getId()).thenReturn("testOperationId");
when(payloadType.getName()).thenReturn("testPayloadType");
when(mockLocation.getPath()).thenReturn(path);

when(dummyPayloadStorage.getLocation(any(Operation.class), any(PayloadType.class), any(String.class)))
.thenReturn(mockLocation);

ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

verify(dummyPayloadStorage, times(1)).getLocation(operation, payloadType, path);
verify(operation, times(1)).getId();
verify(payloadType, times(1)).getName();
assertEquals(path, result.getPath());
}

@Test
public void testUploadWithMockInputStream() throws Exception {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";
InputStream mockInputStream = Mockito.mock(InputStream.class);
byte[] payloadBytes = "testPayload".getBytes();
long payloadSize = payloadBytes.length;

when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invocation -> {
byte[] buffer = invocation.getArgument(0);
int offset = invocation.getArgument(1);
int length = invocation.getArgument(2);
System.arraycopy(payloadBytes, offset, buffer, 0, length);
return length;
});

dummyPayloadStorage.upload(path, mockInputStream, payloadSize);

verify(dummyPayloadStorage, times(1)).upload(path, mockInputStream, payloadSize);
}
}