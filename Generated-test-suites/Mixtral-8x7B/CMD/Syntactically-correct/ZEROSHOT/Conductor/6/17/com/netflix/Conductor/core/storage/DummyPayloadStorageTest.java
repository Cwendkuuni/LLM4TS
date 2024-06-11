import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(Mockito.any(Operation.class), Mockito.any(PayloadType.class), Mockito.anyString()));
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
InputStream payload = Mockito.mock(InputStream.class);
dummyPayloadStorage.upload("path", payload, 1234);
Mockito.verify(payload, Mockito.times(0)).close();
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.download("path"));
}
}