import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@Test
public void testGetLocation_returnsNull() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.START, PayloadType.WORKFLOW, "path");
assertNull(result);
}

@Test
public void testUpload_doesNothing() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload("path", null, 0);
// No exception thrown, so we assume it does nothing
}

@Test
public void testDownload_returnsNull() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
InputStream result = dummyPayloadStorage.download("path");
assertNull(result);
}
}