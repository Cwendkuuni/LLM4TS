import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.START, PayloadType.TASK, "/path");
assertNull(result);
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload("/path", null, 0);
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
InputStream result = dummyPayloadStorage.download("/path");
assertNull(result);
}
}