import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

 DummyPayloadStorage dummyPayloadStorage;
 Operation operation;
 PayloadType payloadType;
 String path;
 InputStream inputStream;

 @Before
 public void setup() {
 dummyPayloadStorage = new DummyPayloadStorage();
 operation = mock(Operation.class);
 payloadType = mock(PayloadType.class);
 path = "testPath";
 inputStream = spy(InputStream.class);
 }

 @Test
 public void testGetLocation() {
 ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);
 assertNull(result);
 }

 @Test
 public void testUpload() {
 dummyPayloadStorage.upload(path, inputStream, 10L);
 }

 @Test
 public void testDownload() {
 InputStream result = dummyPayloadStorage.download(path);
 assertNull(result);
 }
}