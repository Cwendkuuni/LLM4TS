import java.io.InputStream;

import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.storage.ExternalPayloadStorage;
import com.netflix.conductor.common.storage.ExternalStorageLocation;

public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
Operation operation = Mockito.mock(Operation.class);
PayloadType payloadType = Mockito.mock(PayloadType.class);
String path = "testPath";

ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

Mockito.verify(operation, Mockito.times(1)).getName();
Mockito.verify(payloadType, Mockito.times(1)).getName();
Mockito.verifyNoMoreInteractions(operation, payloadType);

assertEquals(null, result);
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";
InputStream payload = Mockito.mock(InputStream.class);
long payloadSize = 123L;

dummyPayloadStorage.upload(path, payload, payloadSize);

Mockito.verify(payload, Mockito.times(0)).close();
Mockito.verifyNoMoreInteractions(payload);
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";

InputStream result = dummyPayloadStorage.download(path);

Mockito.verifyNoMoreInteractions();

assertEquals(null, result);
}
}