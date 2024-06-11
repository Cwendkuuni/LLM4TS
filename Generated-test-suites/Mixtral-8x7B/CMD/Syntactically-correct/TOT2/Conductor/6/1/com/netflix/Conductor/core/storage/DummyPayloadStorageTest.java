import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

private DummyPayloadStorage dummyPayloadStorage;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dummyPayloadStorage = new DummyPayloadStorage();
}

@Test
public void testGetLocation() {
assertNull(dummyPayloadStorage.getLocation(Operation.CREATE, PayloadType.TASK, "path"));
}

@Test
public void testUpload() {
dummyPayloadStorage.upload("path", null, 0);
}

@Test
public void testDownload() {
assertNull(dummyPayloadStorage.download("path"));
}
}