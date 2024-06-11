import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

 @Test
 public void testGetLocation() {
 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
 assertNull(dummyPayloadStorage.getLocation(mock(Operation.class), mock(PayloadType.class), "path"));
 }

 @Test
 public void testUpload() {
 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
 InputStream payload = mock(InputStream.class);
 dummyPayloadStorage.upload("path", payload, 10);
 verify(dummyPayloadStorage, times(0)).upload("path", payload, 10);
 }

 @Test
 public void testDownload() {
 DummyPayloadStorage dummyPayloadStorage = spy(new DummyPayloadStorage());
 when(dummyPayloadStorage.download("path")).thenReturn(mock(InputStream.class));
 InputStream result = dummyPayloadStorage.download("path");
 verify(dummyPayloadStorage, times(1)).download("path");
 assertNull(result);
 }

 @Test
 public void testDownloadNeverCalled() {
 DummyPayloadStorage dummyPayloadStorage = spy(new DummyPayloadStorage());
 dummyPayloadStorage.download("path");
 verify(dummyPayloadStorage, never()).download("path");
 }
}