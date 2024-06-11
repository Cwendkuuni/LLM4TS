import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
Operation operation = mock(Operation.class);
PayloadType payloadType = mock(PayloadType.class);
String path = "testPath";

assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));

verify(operation, times(1)).getName();
verify(payloadType, times(1)).getName();
verify(operation, times(1)).getTaskType();
verify(payloadType, times(1)).getType();
}

@Test
public void testUpload() {
String path = "testPath";
InputStream payload = mock(InputStream.class);
long payloadSize = 100L;

dummyPayloadStorage.upload(path, payload, payloadSize);

verify(payload, times(0)).close();
}

@Test
public void testDownload() {
String path = "testPath";

assertNull(dummyPayloadStorage.download(path));
}
}