import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.ExternalStorageLocation;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

@Test
public void testGetLocation() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
Operation operation = new Operation();
PayloadType payloadType = new PayloadType();
String path = "testPath";

ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

assertNull(result);
}

@Test
public void testUpload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";
InputStream payload = null;
long payloadSize = 0;

dummyPayloadStorage.upload(path, payload, payloadSize);
}

@Test
public void testDownload() {
DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
String path = "testPath";

InputStream result = dummyPayloadStorage.download(path);

assertNull(result);
}
}