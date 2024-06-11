
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.START, PayloadType.DATA, "testPath"));
    }

    @Test
    public void testUpload() {
        InputStream payload = new ByteArrayInputStream("Test".getBytes());
        dummyPayloadStorage.upload("testPath", payload, 4L);
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("testPath"));
    }
}
