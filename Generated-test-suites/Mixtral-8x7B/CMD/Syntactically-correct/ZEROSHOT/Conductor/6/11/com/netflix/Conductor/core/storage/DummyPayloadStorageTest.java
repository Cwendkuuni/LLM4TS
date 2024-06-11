import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.TestPayLoad;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DummyPayloadStorageTest {

 private DummyPayloadStorage dummyPayloadStorage;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dummyPayloadStorage = new DummyPayloadStorage();
 }

 @Test
 public void testGetLocation() {
 Operation operation = mock(Operation.class);
 PayloadType payloadType = mock(PayloadType.class);
 String path = "/test/path";

 ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

 assertNull(result);
 verify(operation, never()).getName();
 verify(payloadType, never()).getName();
 verify(operation, never()).getWorkflowInstanceId();
 verify(operation, never()).getWorkflowType();
 verify(operation, never()).getTaskType();
 verify(operation, never()).getTaskId();
 verify(payloadType, never()).getMediaType();
 }

 @Test
 public void testUpload() {
 String path = "/test/path";
 InputStream payload = new ByteArrayInputStream("test payload".getBytes());
 long payloadSize = 12L;

 dummyPayloadStorage.upload(path, payload, payloadSize);

 verify(dummyPayloadStorage, times(1)).upload(path, payload, payloadSize);
 }

 @Test
 public void testDownload() {
 String path = "/test/path";

 InputStream result = dummyPayloadStorage.download(path);

 assertNull(result);
 verify(dummyPayloadStorage, times(1)).download(path);
 }
}