import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@Test
public void testGetLocation_nullParams_returnsNull() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(null, null, null));
}

@Test
public void testUpload_nullPayload_doesNothing() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload("path", null, 0);
}

@Test
public void testDownload_returnsNull() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.download("path"));
}
}