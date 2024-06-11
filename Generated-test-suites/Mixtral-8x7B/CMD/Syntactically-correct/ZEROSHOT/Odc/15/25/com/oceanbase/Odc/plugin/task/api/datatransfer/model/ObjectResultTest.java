import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {

	private ObjectResult or;
	private List<URL> exportPaths;
	private ObjectMapper mapper;

	@Before
	public void setUp() throws Exception {
		or = new ObjectResult("schema", "name", "type");
		exportPaths = new ArrayList<URL>();
		mapper = new ObjectMapper();

		URL url1 = new URL("http://example.com");
		URL url2 = new URL("http://example.org");
		exportPaths.add(url1);
		exportPaths.add(url2);
		or.setExportPaths(exportPaths);
	}

	@Test
	public void testObjectResult() {
		assertNotNull(or);
		assertEquals("schema", or.getSchema());
		assertEquals("name", or.getName());
		assertEquals("type", or.getType());
		assertEquals(0L, or.getCount().get());
		assertEquals(0L, or.getTotal().get());
	}

	@Test
	public void testOf() {
		ObjectStatus os = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), "type");
		ObjectResult or2 = ObjectResult.of(os);
		assertEquals("schema", or2.getSchema());
		assertEquals("name", or2.getName());
		assertEquals("type", or2.getType());
		assertEquals(10L, or2.getCount().get());
		assertEquals(20L, or2.getTotal().get());
	}

	@Test
	public void testGetSummary() {
		assertEquals("schema.name[type]", or.getSummary());
	}

	@Test
	public void testSetExportPaths() throws MalformedURLException {
		List<URL> exportPaths2 = new ArrayList<URL>();
		URL url3 = new URL("http://example.net");
		exportPaths2.add(url3);
		or.setExportPaths(exportPaths2);
		assertEquals(1, or.getExportPaths().size());
		assertEquals(url3, or.getExportPaths().get(0));
	}

	@Test
	public void testSerialization() throws JsonMappingException, JsonProcessingException {
		String json = mapper.writeValueAsString(or);
		ObjectResult or2 = mapper.readValue(json, ObjectResult.class);
		assertEquals(or.getSchema(), or2.getSchema());
		assertEquals(or.getName(), or2.getName());
		assertEquals(or.getType(), or2.getType());
		assertEquals(or.getCount().get(), or2.getCount().get());
		assertEquals(or.getTotal().get(), or2.getTotal().get());
		assertEquals(or.getExportPaths(), or2.getExportPaths());
	}

}