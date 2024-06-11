import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DummyPayloadStorageTest {

@Spy
@InjectMocks
DummyPayloadStorage dummyPayloadStorage;

@Test
public void testGetLocation_nullValues_returnsNull() {
// Arrange
Operation operation = null;
PayloadType payloadType = null;
String path = null;

// Act
ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

// Assert
assertNull(result);
}

@Test
public void testUpload_nullInputStream_doNothing() {
// Arrange
String path = "testPath";
InputStream payload = null;
long payloadSize = 100L;

doNothing().when(dummyPayloadStorage).upload(path, payload, payloadSize);

// Act
dummyPayloadStorage.upload(path, payload, payloadSize);

// Assert (nothing to assert since we are just checking if the method was called)
}

@Test
public void testDownload_returnsNull() {
// Act
InputStream result = dummyPayloadStorage.download("testPath");

// Assert
assertNull(result);
}
}