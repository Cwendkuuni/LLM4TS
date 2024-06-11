import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@Test
public void testGetLocationWithNullArguments() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(null, null, null));
assertNull(dummyPayloadStorage.getLocation(Operation.START, null, null));
assertNull(dummyPayloadStorage.getLocation(null, PayloadType.TASK, null));
assertNull(dummyPayloadStorage.getLocation(Operation.START, PayloadType.TASK, null));
}

@Test
public void testUploadWithNullArguments() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload(null, null, 0);
dummyPayloadStorage.upload("path", null, 0);
}

@Test
public void testDownloadWithNullArguments() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.download(null));
}
}