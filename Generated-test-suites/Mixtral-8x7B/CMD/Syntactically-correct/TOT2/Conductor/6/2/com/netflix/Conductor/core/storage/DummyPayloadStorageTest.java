import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Test
public void testGetLocationWithNullOperation() {
dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(null, PayloadType.FILE, "/path"));
}

@Test
public void testGetLocationWithNullPayloadType() {
dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(Operation.START, null, "/path"));
}

@Test
public void testGetLocationWithNullPath() {
dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(Operation.START, PayloadType.FILE, null));
}

@Test
public void testUploadWithNullPath() {
dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload(null, Mockito.mock(InputStream.class), 100);
}

@Test
public void testUploadWithNullPayload() {
dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload("/path", null, 100);
}

@Test
public void testDownloadWithNullPath() {
dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.download(null));
}
}