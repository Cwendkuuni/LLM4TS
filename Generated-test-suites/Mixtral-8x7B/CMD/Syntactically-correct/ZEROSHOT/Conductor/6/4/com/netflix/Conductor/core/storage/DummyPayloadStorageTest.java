import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.TestPayloads;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DummyPayloadStorageTest {

@Mock
private Operation operation;
@Mock
private PayloadType payloadType;

private DummyPayloadStorage dummyPayloadStorage;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, "/path");
assertNull(location);
}

@Test
public void testUpload() {
InputStream payload = TestPayloads.getPayloadStream(TestPayloads.PAYLOAD1);
dummyPayloadStorage.upload("/path", payload, 10L);
verify(payload).close();
}

@Test
public void testDownload() {
assertNull(dummyPayloadStorage.download("/path"));
}
}