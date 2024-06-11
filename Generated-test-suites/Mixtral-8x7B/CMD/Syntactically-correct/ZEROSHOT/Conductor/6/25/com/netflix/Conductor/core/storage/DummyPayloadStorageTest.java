import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
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
 ExternalStorageLocation result = dummyPayloadStorage.getLocation(mock(Operation.class), mock(PayloadType.class), "path");
 assertNull(result);
 }

 @Test
 public void testUpload() {
 dummyPayloadStorage.upload("path", null, 0);
 verify(dummyPayloadStorage, times(1)).upload("path", null, 0);
 }

 @Test
 public void testDownload() {
 InputStream result = dummyPayloadStorage.download("path");
 assertNull(result);
 }
}