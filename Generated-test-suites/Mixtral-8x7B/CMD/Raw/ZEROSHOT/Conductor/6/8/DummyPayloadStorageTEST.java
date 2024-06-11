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
public void testGetLocation_whenOperationPayloadTypeAndPathAreNull_returnsNull() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(null, null, null));
}

@Test
public void testUpload_whenPayloadAndPathAreNull_doesNothing() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload(null, null, 0);
}

@Test
public void testDownload_whenPathIsNull_returnsNull() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.download(null));
}
}