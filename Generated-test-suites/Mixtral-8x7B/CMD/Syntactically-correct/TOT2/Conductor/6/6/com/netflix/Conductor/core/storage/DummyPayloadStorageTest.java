import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Before
public void setup() {
dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
Operation operation = Operation.READ;
PayloadType payloadType = PayloadType.BYTE;
String path = "/dummy/path";

assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
}

@Test
public void testUpload() {
String path = "/dummy/path";
InputStream payload = new ByteArrayInputStream("Dummy payload".getBytes());
long payloadSize = 13;

dummyPayloadStorage.upload(path, payload, payloadSize);

// Since the DummyPayloadStorage implementation does not store the payload, we can't assert anything here
}

@Test
public void testDownload() {
String path = "/dummy/path";

assertNull(dummyPayloadStorage.download(path));
}
}