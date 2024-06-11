import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    private ObjectResult anotherObjectResult;
    private List<URL> exportPaths;
    private String schema;
    private String name;
    private String type;
    private static final ObjectMapper om = new ObjectMapper();

    @Before
    public void setup() {
        schema = "schema";
        name = "name";
        type = "type";
        exportPaths = new ArrayList<URL>();
        try {
            exportPaths.add(new URL("http://example.com"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objectResult = new ObjectResult(schema, name, type);
        anotherObjectResult = ObjectResult.of(objectResult);
    }

    @Test
    public void testGetSchema() {
        assertEquals(schema, objectResult.getSchema());
    }

    @Test
    public void testGetName() {
        assertEquals(name, objectResult.getName());
    }

    @Test
    public void testGetType() {
        assertEquals(type, objectResult.getType());
    }

    @Test
    public void testGetExportPaths() {
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        String expected = schema + "." + name + "[" + type + "]";
        assertEquals(expected, objectResult.getSummary());
    }

    @Test
    public void testObjectResultConstructorWithSchemaNameAndType() {
        ObjectResult result = new ObjectResult(schema, name, type);
        assertNotNull(result);
    }

    @Test
    public void testOfMethod() {
        ObjectResult result = ObjectResult.of(objectResult);
        assertNotSame(objectResult, result);
        assertEquals(objectResult.getSchema(), result.getSchema());
        assertEquals(objectResult.getName(), result.getName());
        assertEquals(objectResult.getType(), result.getType());
        assertEquals(objectResult.getCount(), result.getCount());
        assertEquals(objectResult.getTotal(), result.getTotal());
    }

    @Test
    public void testSetSchema() {
        String newSchema = "newSchema";
        objectResult.setSchema(newSchema);
        assertEquals(newSchema, objectResult.getSchema());
    }

    @Test
    public void testSetName() {
        String newName = "newName";
        objectResult.setName(newName);
        assertEquals(newName, objectResult.getName());
    }

    @Test
    public void testSetType() {
        String newType = "newType";
        objectResult.setType(newType);
        assertEquals(newType, objectResult.getType());
    }

    @Test
    public void testSetExportPaths() {
        URL newURL = null;
        try {
            newURL = new URL("http://newExample.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        exportPaths.add(newURL);
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testSerialization() throws JsonMappingException, JsonProcessingException {
        String json = om.writeValueAsString(objectResult);
        ObjectResult deserialized = om.readValue(json, ObjectResult.class);
        assertEquals(objectResult.getSchema(), deserialized.getSchema());
        assertEquals(objectResult.getName(), deserialized.getName());
        assertEquals(objectResult.getType(), deserialized.getType());
        assertEquals(objectResult.getCount(), deserialized.getCount());
        assertEquals(objectResult.getTotal(), deserialized.getTotal());
    }

    @Test
    public void testSerializationWithNullExportPaths() throws JsonMappingException, JsonProcessingException {
        objectResult.setExportPaths(null);
        String json = om.writeValueAsString(objectResult);
        ObjectResult deserialized = om.readValue(json, ObjectResult.class);
        assertNull(deserialized.getExportPaths());
    }

    @Test
    public void testSerializationWithEmptyExportPaths() throws JsonMappingException, JsonProcessingException {
        objectResult.setExportPaths(new ArrayList<URL>());
        String json = om.writeValueAsString(objectResult);
        ObjectResult deserialized = om.readValue(json, ObjectResult.class);
        assertTrue(deserialized.getExportPaths().isEmpty());
    }

    @Test
    public void testSerializationWithExportPaths() throws JsonMappingException, JsonProcessingException {
        objectResult.setExportPaths(exportPaths);
        String json = om.writeValueAsString(objectResult);
        ObjectResult deserialized = om.readValue(json, ObjectResult.class);
        assertEquals(objectResult.getExportPaths(), deserialized.getExportPaths());
    }

}