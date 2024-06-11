import java.io.InputStream;

import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

 @Test
 public void testGetLocation() {
 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
 Operation operation = Mockito.mock(Operation.class);
 PayloadType payloadType = Mockito.mock(PayloadType.class);
 String path = "testPath";

 ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

 Mockito.verify(operation, Mockito.times(1)).getTaskName();
 Mockito.verify(payloadType, Mockito.times(1)).getName();
 Mockito.verifyNoMoreInteractions(operation, payloadType);

 assertEquals(null, result);
 }

 @Test
 public void testUpload() {
 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
 String path = "testPath";
 InputStream payload = Mockito.mock(InputStream.class);
 long payloadSize = 100;

 dummyPayloadStorage.upload(path, payload, payloadSize);

 Mockito.verifyNoInteractions(payload);
 Mockito.verify(payload, Mockito.times(0)).available();
 Mockito.verify(payload, Mockito.times(0)).read();
 Mockito.verifyNoMoreInteractions(payload);
 }

 @Test
 public void testDownload() {
 DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
 String path = "testPath";

 InputStream result = dummyPayloadStorage.download(path);

 Mockito.verifyNoInteractions(result);
 Mockito.verify(result, Mockito.times(0)).available();
 Mockito.verify(result, Mockito.times(0)).read();
 Mockito.verifyNoMoreInteractions(result);

 assertEquals(null, result);
 }
}