import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ObjectResultTest {

    private ObjectResult objectResult;
    private List<URL> exportPaths;

    @Before
    public void setUp() throws Exception {
        objectResult = new ObjectResult("schema", "name", "type");
        exportPaths = new ArrayList<URL>();
        exportPaths.add(new URL("http://localhost:8080/test1"));
        exportPaths.add(new URL("http://localhost:8080/test2"));
    }

    @Test
    public void testObjectResult() {
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), "status");
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals("schema", result.getSchema());
        assertEquals(10, (long) result.getCount().get());
        assertEquals(20, (long) result.getTotal().get());
        assertEquals("status", result.getStatus());
    }

    @Test
    public void testGetSummary() {
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() throws MalformedURLException {
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testSerialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(objectResult);
        System.out.println(json);
        ObjectResult result = mapper.readValue(json, ObjectResult.class);
        assertEquals(objectResult.getSchema(), result.getSchema());
        assertEquals(objectResult.getName(), result.getName());
        assertEquals(objectResult.getType(), result.getType());
        assertEquals(objectResult.getCount().get(), result.getCount().get());
        assertEquals(objectResult.getTotal().get(), result.getTotal().get());
    }

}