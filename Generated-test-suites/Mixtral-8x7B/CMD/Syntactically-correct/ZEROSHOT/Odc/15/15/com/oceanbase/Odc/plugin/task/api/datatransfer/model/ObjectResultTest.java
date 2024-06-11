import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

	private ObjectResult objectResult;

	@Before
	public void setup() {
		objectResult = new ObjectResult("schema", "name", "type");
		objectResult.setCount(new AtomicLong(10));
		objectResult.setTotal(new AtomicLong(20));
		objectResult.setStatus(Status.SUCCEEDED);
	}

	@Test
	public void testObjectResult() {
		assertEquals("schema", objectResult.getSchema());
		assertEquals("name", objectResult.getName());
		assertEquals("type", objectResult.getType());
		assertEquals(10, objectResult.getCount().get());
		assertEquals(20, objectResult.getTotal().get());
		assertEquals(Status.SUCCEEDED, objectResult.getStatus());
		assertEquals("schema.name[type]", objectResult.getSummary());
	}

	@Test
	public void testOf() {
		ObjectStatus objectStatus = new ObjectStatus("schema", "name", Status.SUCCEEDED);
		objectStatus.setCount(new AtomicLong(10));
		objectStatus.setTotal(new AtomicLong(20));
		ObjectResult result = ObjectResult.of(objectStatus);
		assertEquals("schema", result.getSchema());
		assertEquals("name", result.getName());
		assertEquals(Status.SUCCEEDED, result.getStatus());
		assertEquals(10, result.getCount().get());
		assertEquals(20, result.getTotal().get());
	}

	@Test
	public void testExportPaths() {
		objectResult.setExportPaths(null);
		assertEquals(null, objectResult.getExportPaths());
		List<URL> urls = new ArrayList<>();
		URL url = new URL("http://example.com");
		urls.add(url);
		objectResult.setExportPaths(urls);
		assertEquals(urls, objectResult.getExportPaths());
		assertEquals(1, objectResult.getExportPaths().size());
		assertEquals(url, objectResult.getExportPaths().get(0));
	}

	@Test
	public void testJsonIgnore() {
		assertNull(objectResult.getExportPaths());
	}

}