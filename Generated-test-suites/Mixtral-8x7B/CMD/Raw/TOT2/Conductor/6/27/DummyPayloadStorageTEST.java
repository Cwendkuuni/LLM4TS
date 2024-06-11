import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@InjectMocks
private DummyPayloadStorage dummyPayloadStorage;

@Test
public void testGetLocation_whenCalledWithOperationPayloadTypeAndPath_returnsNull() {
Operation operation = Operation.START;
PayloadType payloadType = PayloadType.BYTE_ARRAY;
String path = "/dummy/path";

assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
verify(dummyPayloadStorage, times(1)).getLocation(any(Operation.class), any(PayloadType.class), anyString());
}

@Test
public void testUpload_whenCalledWithPathPayloadAndPayloadSize_doesNothing() {
String path = "/dummy/path";
InputStream payload = null;
long payloadSize = 123;

dummyPayloadStorage.upload(path, payload, payloadSize);
verify(dummyPayloadStorage, times(1)).upload(anyString(), any(InputStream.class), anyLong());
}

@Test
public void testDownload_whenCalledWithPath_returnsNull() {
String path = "/dummy/path";

assertNull(dummyPayloadStorage.download(path));
verify(dummyPayloadStorage, times(1)).download(anyString());
}
}