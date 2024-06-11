import com.netflix.conductor.common.run.ExternalStorageLocation;
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
import static org.mockito.Mockito.when;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;
private Operation operation;
private PayloadType payloadType;
private String path;
private ExternalStorageLocation externalStorageLocation;
private InputStream inputStream;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

dummyPayloadStorage = new DummyPayloadStorage();
operation = mock(Operation.class);
payloadType = mock(PayloadType.class);
path = "testPath";
externalStorageLocation = mock(ExternalStorageLocation.class);
inputStream = mock(InputStream.class);
}

@Test
public void testGetLocation() {
assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
verify(dummyPayloadStorage, times(1)).getLocation(operation, payloadType, path);
}

@Test
public void testUpload() {
dummyPayloadStorage.upload(path, inputStream, 1L);
verify(dummyPayloadStorage, times(1)).upload(path, inputStream, 1L);
verify(inputStream, never()).close();
}

@Test
public void testDownload() {
assertNull(dummyPayloadStorage.download(path));
verify(dummyPayloadStorage, times(1)).download(path);
}
}