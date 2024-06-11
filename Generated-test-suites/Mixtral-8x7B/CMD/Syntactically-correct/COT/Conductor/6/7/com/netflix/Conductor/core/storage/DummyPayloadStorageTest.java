import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.io.InputStream;

public class DummyPayloadStorageTest {

DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

// Test for getLocation() method
@Test
public void testGetLocation() {
assertNull(dummyPayloadStorage.getLocation(Operation.START, PayloadType.DATA, "path"));
}

// Test for upload() method
@Test
public void testUpload() {
dummyPayloadStorage.upload("path", null, 0);
}

// Test for download() method
@Test
public void testDownload() {
assertNull(dummyPayloadStorage.download("path"));
}
}