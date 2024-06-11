import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

@RunWith(JUnit4.class)
public class ObjectResultTest {

	private ObjectResult or;

	@Before
	public void setup() {
		or = new ObjectResult("schema1", "table1", "table");
		or.setCount(new AtomicLong(100));
		or.setTotal(new AtomicLong(500));
	}

	@Test
	public void testGetSummary() {
		assertEquals("schema1.table1[table]", or.getSummary());
	}

	@Test
	public void testOf() {
		ObjectStatus os = new ObjectStatus("schema1", "table1", new AtomicLong(100), new AtomicLong(500), "table");
		ObjectResult or2 = ObjectResult.of(os);
		assertEquals("schema1.table1[table]", or2.getSummary());
		assertEquals(new Long(100), or2.getCount().get());
		assertEquals(new Long(500), or2.getTotal().get());
	}

	@Test
	public void testConstructor() {
		ObjectResult or3 = new ObjectResult("schema2", "table2", "view");
		assertEquals("schema2.table2[view]", or3.getSummary());
	}

	@Test
	public void testExportPaths() throws MalformedURLException {
		URL url1 = new URL("http://example.com/1");
		URL url2 = new URL("http://example.com/2");
		List<URL> urls = new ArrayList<URL>(Arrays.asList(url1, url2));
		or.setExportPaths(urls);

		assertEquals(urls, or.getExportPaths());
		assertEquals(2, or.getExportPaths().size());
		assertEquals(url1, or.getExportPaths().get(0));
		assertEquals(url2, or.getExportPaths().get(1));
	}

}