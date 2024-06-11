import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@Test
public void testGetLocation_nullOperation_nullPayloadType_nullPath() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.getLocation(null, null, null));
}

@Test
public void testUpload_nullPath_nullInputStream_zeroPayloadSize() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
dummyPayloadStorage.upload(null, null, 0);
}

@Test
public void testDownload_nullPath() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
assertNull(dummyPayloadStorage.download(null));
}
}