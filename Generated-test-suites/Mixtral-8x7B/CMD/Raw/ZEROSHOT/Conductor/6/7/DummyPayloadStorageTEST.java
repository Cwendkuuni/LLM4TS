import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.TestPaylad;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
ExternalStorageLocation location = dummyPayloadStorage.getLocation(mock(Operation.class), mock(PayloadType.class), "/path");
assertNull(location);
}

@Test
public void testUpload() {
InputStream payload = new ByteArrayInputStream("test-data".getBytes());
dummyPayloadStorage.upload("/path", payload, payload.available());
verify(payload, never()).close();
}

@Test
public void testDownload() {
InputStream payload = dummyPayloadStorage.download("/path");
assertNull(payload);
}
}