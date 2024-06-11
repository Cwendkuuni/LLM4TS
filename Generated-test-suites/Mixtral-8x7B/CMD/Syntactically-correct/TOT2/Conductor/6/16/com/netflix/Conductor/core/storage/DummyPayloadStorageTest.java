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
ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.CREATE, PayloadType.BYTE_ARRAY, "/path/to/payload");
assertNull(result);
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload("/path/to/payload", null, 0);
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
InputStream result = dummyPayloadStorage.download("/path/to/payload");
assertNull(result);
}
}