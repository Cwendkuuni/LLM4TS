import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(JUnit4.class)
public class ObjectResultTest {

    private ObjectResult or;

    @Before
    public void setup() {
        or = new ObjectResult("schema", "name", "type");
        or.setCount(new AtomicLong(10));
        or.setTotal(new AtomicLong(20));
    }

    @Test
    public void testGetSummary() {
        String expected = "schema.name[type]";
        String actual = or.getSummary();
        assertEquals(expected, actual);
    }

    @Test
    public void testOf() {
        ObjectStatus os = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult or2 = ObjectResult.of(os);
        assertEquals("name", or2.getName());
        assertEquals(10, or2.getCount().get());
        assertEquals(20, or2.getTotal().get());
    }

    @Test
    public void testConstructor() {
        ObjectResult or2 = new ObjectResult("schema2", "name2", "type2");
        assertEquals("schema2", or2.getSchema());
        assertEquals("name2", or2.getName());
        assertEquals("type2", or2.getType());
    }

    @Test
    public void testSetExportPaths() throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        URL url1 = new URL("http://example.com");
        URL url2 = new URL("http://example2.com");
        urls.add(url1);
        urls.add(url2);
        or.setExportPaths(urls);
        List<URL> actual = or.getExportPaths();
        assertEquals(2, actual.size());
        assertEquals(url1, actual.get(0));
        assertEquals(url2, actual.get(1));
    }

    @Test
    public void testSerialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(or);
        ObjectResult or2 = mapper.readValue(json, ObjectResult.class);
        assertEquals(or.getSchema(), or2.getSchema());
        assertEquals(or.getName(), or2.getName());
        assertEquals(or.getType(), or2.getType());
        assertEquals(or.getCount().get(), or2.getCount().get());
        assertEquals(or.getTotal().get(), or2.getTotal().get());
    }

}