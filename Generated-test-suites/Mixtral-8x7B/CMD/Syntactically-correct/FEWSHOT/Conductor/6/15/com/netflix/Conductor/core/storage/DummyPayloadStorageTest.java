import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import java.io.InputStream;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
Operation operation = mock(Operation.class);
PayloadType payloadType = mock(PayloadType.class);
String path = "test-path";

assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = spy(new DummyPayloadStorage());
String path = "test-path";
InputStream payload = mock(InputStream.class);
long payloadSize = 100L;

dummyPayloadStorage.upload(path, payload, payloadSize);

verify(dummyPayloadStorage).upload(ArgumentMatchers.eq(path), ArgumentMatchers.eq(payload), ArgumentMatchers.eq(payloadSize));
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = spy(new DummyPayloadStorage());
String path = "test-path";
InputStream downloadedPayload = mock(InputStream.class);

when(dummyPayloadStorage.download(path)).thenReturn(downloadedPayload);

assertEquals(downloadedPayload, dummyPayloadStorage.download(path));

verify(dummyPayloadStorage).download(ArgumentMatchers.eq(path));
}
}