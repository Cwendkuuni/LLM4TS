import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.TestPaylad;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
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
operation = mock(Operation.class);
payloadType = mock(PayloadType.class);
path = "testPath";
externalStorageLocation = mock(ExternalStorageLocation.class);
inputStream = new ByteArrayInputStream("testPayload".getBytes());

dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
Mockito.verify(externalStorageLocation, Mockito.never()).getPath();
}

@Test
public void testUpload() {
dummyPayloadStorage.upload(path, inputStream, 12L);
Mockito.verify(externalStorageLocation, Mockito.never()).getPath();
}

@Test
public void testDownload() {
assertNull(dummyPayloadStorage.download(path));
Mockito.verify(externalStorageLocation, Mockito.never()).getPath();
}

@Test
public void testDownloadWithPayloadType() {
when(payloadType.getName()).thenReturn("testPayloadType");
when(externalStorageLocation.getPath()).thenReturn("testPath");

dummyPayloadStorage = new DummyPayloadStorage() {
@Override
public ExternalStorageLocation getLocation(Operation operation, PayloadType payloadType, String path) {
return externalStorageLocation;
}
};

assertNull(dummyPayloadStorage.download(path));
Mockito.verify(externalStorageLocation).getPath();
}
}