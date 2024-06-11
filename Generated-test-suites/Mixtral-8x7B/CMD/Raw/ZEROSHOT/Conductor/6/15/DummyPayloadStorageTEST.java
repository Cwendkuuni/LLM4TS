import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;
private Operation operation;
private PayloadType payloadType;
private String path;
private InputStream inputStream;
private long payloadSize;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

dummyPayloadStorage = new DummyPayloadStorage();
operation = mock(Operation.class);
payloadType = mock(PayloadType.class);
path = "testPath";
inputStream = mock(InputStream.class);
payloadSize = 123L;
}

@Test
public void testGetLocation() {
assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
verify(operation, never()).getName();
verify(payloadType, never()).getName();
verify(operation, never()).getWorkflowId();
verify(operation, never()).getWorkflowType();
verify(operation, never()).getScheduledTime();
verify(operation, never()).getStartTime();
verify(operation, never()).getEndTime();
verify(operation, never()).getStatus();
}

@Test
public void testUpload() {
dummyPayloadStorage.upload(path, inputStream, payloadSize);
verify(inputStream, times(1)).close();
verify(dummyPayloadStorage, never()).getLocation(operation, payloadType, path);
}

@Test
public void testDownload() {
assertNull(dummyPayloadStorage.download(path));
verify(dummyPayloadStorage, never()).getLocation(operation, payloadType, path);
}
}