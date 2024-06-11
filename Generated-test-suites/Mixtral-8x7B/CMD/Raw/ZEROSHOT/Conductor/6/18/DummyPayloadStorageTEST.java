import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
Operation operation = Operation.START;
PayloadType payloadType = PayloadType.WORKFLOW_DEFINITION;
String path = "/test/path";

ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

assertNull(result);
}

@Test
public void testUpload() {
String path = "/test/path";
InputStream payload = null;
long payloadSize = 0;

dummyPayloadStorage.upload(path, payload, payloadSize);
}

@Test
public void testDownload() {
String path = "/test/path";

InputStream result = dummyPayloadStorage.download(path);

assertNull(result);
}
}