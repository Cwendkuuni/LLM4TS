import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import org.junit.Test;

public class DummyPayloadStorageTest {

 @Test
 public void testGetLocation() {
 Operation operation = mock(Operation.class);
 PayloadType payloadType = mock(PayloadType.class);
 String path = "testPath";

 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

 assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
 }

 @Test
 public void testUpload() {
 String path = "testPath";
 InputStream payload = mock(InputStream.class);
 long payloadSize = 100L;

 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

 dummyPayloadStorage.upload(path, payload, payloadSize);
 }

 @Test
 public void testDownload() {
 String path = "testPath";

 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

 assertNull(dummyPayloadStorage.download(path));
 }
}