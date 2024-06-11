import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;
private Operation operation;
private PayloadType payloadType;
private String path;
private InputStream inputStream;
private long payloadSize;

@Before
public void setup() {
dummyPayloadStorage = new DummyPayloadStorage();
operation = mock(Operation.class);
payloadType = mock(PayloadType.class);
path = "testPath";
inputStream = mock(InputStream.class);
payloadSize = 100L;
}

@Test
public void testGetLocation_nullReturned() {
assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
verify(operation, never()).getName();
verify(payloadType, never()).getName();
}

@Test
public void testUpload_noExceptionThrown() {
dummyPayloadStorage.upload(path, inputStream, payloadSize);
verify(inputStream, times(1)).close();
}

@Test
public void testDownload_nullReturned() {
assertNull(dummyPayloadStorage.download(path));
}
}