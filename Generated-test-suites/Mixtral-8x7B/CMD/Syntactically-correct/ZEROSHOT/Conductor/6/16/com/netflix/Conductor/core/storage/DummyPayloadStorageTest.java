import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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
 String path = "path";

 ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

 assertNull(result);
 verify(operation, never()).getName();
 verify(payloadType, never()).getName();
 verify(operation, never()).getHeaders();
 verify(payloadType, never()).getHeaders();
 verify(operation, never()).getMetadata();
 verify(payloadType, never()).getMetadata();
 }

 @Test
 public void testUpload() {
 String path = "path";
 InputStream payload = mock(InputStream.class);
 long payloadSize = 10L;

 dummyPayloadStorage.upload(path, payload, payloadSize);

 verify(payload, never()).close();
 verify(payload, never()).mark(0);
 verify(payload, never()).reset();
 verify(payload, never()).skip(0L);
 verify(payload, never()).available();
 verify(payload, never()).markSupported();
 verify(payload, never()).transferTo(mock(OutputStream.class));
 }

 @Test
 public void testDownload() {
 String path = "path";

 InputStream result = dummyPayloadStorage.download(path);

 assertNull(result);
 }
}