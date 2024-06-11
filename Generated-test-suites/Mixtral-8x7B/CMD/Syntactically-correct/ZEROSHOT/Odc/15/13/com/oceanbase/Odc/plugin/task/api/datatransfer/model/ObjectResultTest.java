import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {

	private ObjectResult or;
	private ObjectResult or2;
	private ObjectMapper mapper;

	@Before
	public void setUp() throws Exception {
		or = new ObjectResult("schema1", "table1", "table");
		or.setCount(new AtomicLong(100));
		or.setTotal(new AtomicLong(200));

		List<URL> list = new ArrayList<URL>();
		try {
			list.add(new URL("http://localhost:8080/test1"));
			list.add(new URL("http://localhost:8080/test2"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		or.setExportPaths(list);

		or2 = ObjectResult.of(or);

		mapper = new ObjectMapper();
	}

	@Test
	public void testObjectResult() {
		assertNotNull(or);
		assertEquals("schema1", or.getSchema());
		assertEquals("table1", or.getName());
		assertEquals("table", or.getType());
		assertEquals(100, or.getCount().get());
		assertEquals(200, or.getTotal().get());
		assertEquals("schema1.table1[table]", or.getSummary());

		assertEquals(or.getSchema(), or2.getSchema());
		assertEquals(or.getName(), or2.getName());
		assertEquals(or.getType(), or2.getType());
		assertEquals(or.getCount().get(), or2.getCount().get());
		assertEquals(or.getTotal().get(), or2.getTotal().get());
		assertEquals(or.getSummary(), or2.getSummary());
	}

	@Test
	public void testGetSummary() {
		assertEquals("schema1.table1[table]", or.getSummary());
	}

	@Test
	public void testJson() throws JsonMappingException, JsonProcessingException {
		String json = mapper.writeValueAsString(or);
		ObjectResult or3 = mapper.readValue(json, ObjectResult.class);
		assertEquals(or.getSchema(), or3.getSchema());
		assertEquals(or.getName(), or3.getName());
		assertEquals(or.getType(), or3.getType());
		assertEquals(or.getCount().get(), or3.getCount().get());
		assertEquals(or.getTotal().get(), or3.getTotal().get());
		assertEquals(or.getSummary(), or3.getSummary());
	}

}