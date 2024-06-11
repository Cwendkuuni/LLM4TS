import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

DummyPayloadStorage dummyPayloadStorage;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
assertNull(dummyPayloadStorage.getLocation(Operation.START, PayloadType.TASK, "path"));
}

@Test
public void testUpload() {
InputStream payload = new ByteArrayInputStream("Dummy Payload".getBytes());
dummyPayloadStorage.upload("path", payload, 12L);
}

@Test
public void testDownload() {
assertNull(dummyPayloadStorage.download("path"));
}
}