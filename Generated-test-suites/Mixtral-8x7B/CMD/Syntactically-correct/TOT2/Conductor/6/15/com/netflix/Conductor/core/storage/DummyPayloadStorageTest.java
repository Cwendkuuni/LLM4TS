import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.io.InputStream;

public class DummyPayloadStorageTest {

DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

@Test
public void testGetLocation() {
Operation operation = Operation.CREATE;
PayloadType payloadType = PayloadType.FILE;
String path = "dummyPath";
assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
}

@Test
public void testUpload() {
String path = "dummyPath";
InputStream payload = null;
long payloadSize = 0;
dummyPayloadStorage.upload(path, payload, payloadSize);
}

@Test
public void testDownload() {
String path = "dummyPath";
assertNull(dummyPayloadStorage.download(path));
}
}