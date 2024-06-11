import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@InjectMocks
DummyPayloadStorage dummyPayloadStorage;


@Test
public void testGetLocationWithNullArguments() {
    assertNull(dummyPayloadStorage.getLocation(null, null, null));
}

@Test
public void testUploadWithNullPayload() {
    dummyPayloadStorage.upload("path", null, 0);
}

@Test
public void testDownloadWithNullPath() {
    assertNull(dummyPayloadStorage.download(null));
}

/* Add more test cases for edge cases and typical use cases */


}