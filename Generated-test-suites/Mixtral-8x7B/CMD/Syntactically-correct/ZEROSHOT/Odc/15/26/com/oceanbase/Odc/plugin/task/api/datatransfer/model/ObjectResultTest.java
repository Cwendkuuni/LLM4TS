import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "table", "table");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));
    }

    @Test
    public void testGetSummary() {
        String expected = "schema.table[table]";
        assertEquals(expected, objectResult.getSummary());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), "table");
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testExportPaths() throws JsonMappingException, JsonProcessingException {
        URL url1 = new URL("http://example.com/1");
        URL url2 = new URL("http://example.com/2");
        List<URL> urls = Arrays.asList(url1, url2);
        objectResult.setExportPaths(urls);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(objectResult);

        ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
        assertEquals(urls, deserializedObjectResult.getExportPaths());
    }

    @Test(expected = MalformedURLException.class)
    public void testSetExportPathsWithMalformedURL() {
        List<String> urls = Arrays.asList("http://example.com/1", "not a url");
        objectResult.setExportPaths(urls);
    }

    @Test
    public void testExportPathsNull() throws JsonMappingException, JsonProcessingException {
        ObjectResult objectResultWithNullExportPaths = new ObjectResult("schema", "table", "table");
        objectResultWithNullExportPaths.setCount(new AtomicLong(10));
        objectResultWithNullExportPaths.setTotal(new AtomicLong(20));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(objectResultWithNullExportPaths);

        ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
        assertNull(deserializedObjectResult.getExportPaths());
    }

    @Test
    public void testExportPathsEmptyList() throws JsonMappingException, JsonProcessingException {
        ObjectResult objectResultWithEmptyExportPaths = new ObjectResult("schema", "table", "table");
        objectResultWithEmptyExportPaths.setCount(new AtomicLong(10));
        objectResultWithEmptyExportPaths.setTotal(new AtomicLong(20));
        objectResultWithEmptyExportPaths.setExportPaths(new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(objectResultWithEmptyExportPaths);

        ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
        assertTrue(deserializedObjectResult.getExportPaths().isEmpty());
    }

}