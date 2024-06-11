import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testObjectResultCreation() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("schema", "name", "type", new AtomicLong(10), new AtomicLong(20));
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() throws JsonMappingException, JsonProcessingException, MalformedURLException {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        List<URL> exportPaths = Arrays.asList(new URL("http://example.com"), new URL("http://example.org"));
        objectResult.setExportPaths(exportPaths);
        String json = objectMapper.writeValueAsString(objectResult);
        ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
        assertEquals(exportPaths, deserializedObjectResult.getExportPaths());
    }

    @Test
    public void testNullExportPaths() throws JsonMappingException, JsonProcessingException {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String json = objectMapper.writeValueAsString(objectResult);
        ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
        assertNull(deserializedObjectResult.getExportPaths());
    }

}