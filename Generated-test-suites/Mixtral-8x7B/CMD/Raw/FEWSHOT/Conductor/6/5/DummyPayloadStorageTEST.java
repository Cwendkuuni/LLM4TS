import java.io.InputStream;

import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.storage.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

	@Test
	public void testGetLocation() {
		DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
		ExternalPayloadStorage spyDummyPayloadStorage = Mockito.spy(dummyPayloadStorage);
		Operation operation = Mockito.mock(Operation.class);
		PayloadType payloadType = Mockito.mock(PayloadType.class);
		String path = "testPath";
		ExternalStorageLocation result = spyDummyPayloadStorage.getLocation(operation, payloadType, path);
		Mockito.verify(spyDummyPayloadStorage, Mockito.times(1)).getLocation(operation, payloadType, path);
		assert (result == null);
	}

	@Test
	public void testUpload() {
		DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
		dummyPayloadStorage.upload("testPath", null, 0);
		Mockito.verify(dummyPayloadStorage, Mockito.times(1)).upload("testPath", null, 0);
	}

	@Test
	public void testDownload() {
		DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
		InputStream result = dummyPayloadStorage.download("testPath");
		Mockito.verify(dummyPayloadStorage, Mockito.times(1)).download("testPath");
		assert (result == null);
	}
}