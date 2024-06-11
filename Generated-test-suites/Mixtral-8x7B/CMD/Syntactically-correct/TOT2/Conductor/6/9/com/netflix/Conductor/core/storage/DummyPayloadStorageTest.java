import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

 private DummyPayloadStorage dummyPayloadStorage = Mockito.spy(new DummyPayloadStorage());

 @Test
 public void testGetLocationWithNullOperation() {
 ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, PayloadType.BYTEARRAY, "/path");
 assertNull(location);
 }

 @Test
 public void testGetLocationWithNullPayloadType() {
 ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.START, null, "/path");
 assertNull(location);
 }

 @Test
 public void testGetLocationWithNullPath() {
 ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.START, PayloadType.BYTEARRAY, null);
 assertNull(location);
 }

 @Test
 public void testUploadWithNullPath() {
 dummyPayloadStorage.upload(null, Mockito.mock(InputStream.class), 10L);
 // No exception expected
 }

 @Test
 public void testUploadWithNullInputStream() {
 dummyPayloadStorage.upload("/path", null, 10L);
 // No exception expected
 }

 @Test
 public void testDownloadWithNullPath() {
 InputStream inputStream = dummyPayloadStorage.download(null);
 assertNull(inputStream);
 }
}