import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.io.InputStream;

public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.START, PayloadType.BYTEARRAY, "/path/to/payload");
assertNull(result);
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "/path/to/payload";
InputStream payload = null;
long payloadSize = 0;
dummyPayloadStorage.upload(path, payload, payloadSize);
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "/path/to/payload";
InputStream result = dummyPayloadStorage.download(path);
assertNull(result);
}
}