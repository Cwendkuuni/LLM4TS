import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Before
public void setUp() {
dummyPayloadStorage = new DummyPayloadStorage();
}

@After
public void tearDown() {
dummyPayloadStorage = null;
}

@Test
public void testGetLocation() {
Operation operation = Operation.CREATE;
PayloadType payloadType = PayloadType.DATA;
String path = "dummy/path";

assertEquals(null, dummyPayloadStorage.getLocation(operation, payloadType, path));
}

@Test
public void testUpload() {
String path = "dummy/path";
InputStream payload = new ByteArrayInputStream("Dummy payload".getBytes());
long payloadSize = 12L;

dummyPayloadStorage.upload(path, payload, payloadSize);

// Since the DummyPayloadStorage implementation does not store the uploaded payload,
// we cannot assert anything here.
}

@Test
public void testDownload() {
String path = "dummy/path";

assertNull(dummyPayloadStorage.download(path));
}
}