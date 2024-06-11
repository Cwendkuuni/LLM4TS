import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

@Test
public void testConstructor() {
ObjectResult objRes = new ObjectResult("schemaName", "objName", "objType");
assertNotNull(objRes);
assertEquals("schemaName", objRes.getSchema());
assertEquals("objName", objRes.getName());
assertEquals("objType", objRes.getType());
assertEquals(0, objRes.getCount().get());
assertEquals(0, objRes.getTotal().get());
}

@Test
public void testOf() {
ObjectStatus objSts = new ObjectStatus("schemaName", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult objRes1 = ObjectResult.of(objSts);
ObjectResult objRes2 = ObjectResult.of(objSts);
assertNotSame(objRes1, objRes2);
assertEquals(objSts.getSchema(), objRes1.getSchema());
assertEquals(objSts.getType(), objRes1.getType());
assertEquals(objSts.getName(), objRes1.getName());
assertEquals(objSts.getCount().get(), objRes1.getCount().get());
assertEquals(objSts.getTotal().get(), objRes1.getTotal().get());
}

@Test
public void testGetSummary() {
ObjectResult objRes = new ObjectResult("schemaName", "objName", "objType");
assertEquals("schemaName.objName[objType]", objRes.getSummary());
}

@Test
public void testExportPaths() throws MalformedURLException {
ObjectResult objRes = new ObjectResult("schemaName", "objName", "objType");
List<URL> exportPaths = new ArrayList<URL>();
exportPaths.add(new URL("http://localhost:8080/export1"));
exportPaths.add(new URL("http://localhost:8080/export2"));
objRes.setExportPaths(exportPaths);
assertEquals(2, objRes.getExportPaths().size());
assertEquals("http://localhost:8080/export1", objRes.getExportPaths().get(0).toString());
assertEquals("http://localhost:8080/export2", objRes.getExportPaths().get(1).toString());
}

@Test
public void testGetSummaryWithExportPaths() throws JsonProcessingException, MalformedURLException {
ObjectResult objRes = new ObjectResult("schemaName", "objName", "objType");
List<URL> exportPaths = new ArrayList<URL>();
exportPaths.add(new URL("http://localhost:8080/export1"));
exportPaths.add(new URL("http://localhost:8080/export2"));
objRes.setExportPaths(exportPaths);
assertEquals("schemaName.objName[objType]{http://localhost:8080/export1, http://localhost:8080/export2}",
objRes.getSummaryWithExportPaths());
}

@Test
public void testJsonSerialization() throws JsonProcessingException {
ObjectResult objRes = new ObjectResult("schemaName", "objName", "objType");
String json = objRes.toJson();
assertNotNull(json);
}

@Test
public void testJsonDeserialization() throws JsonProcessingException {
String json = "{\"schema\":\"schemaName\", \"name\":\"objName\", \"type\":\"objType\", \"count\":0, \"total\":0}";
ObjectResult objRes = ObjectResult.fromJson(json);
assertNotNull(objRes);
assertEquals("schemaName", objRes.getSchema());
assertEquals("objName", objRes.getName());
assertEquals("objType", objRes.getType());
assertEquals(0, objRes.getCount().get());
assertEquals(0, objRes.getTotal().get());
}

@Test
public void testJsonDeserializationWithNull() throws JsonProcessingException {
String json = "{\"schema\":\"schemaName\", \"name\":\"objName\", \"type\":\"objType\", \"count\":null, \"total\":null}";
ObjectResult objRes = ObjectResult.fromJson(json);
assertNotNull(objRes);
assertEquals("schemaName", objRes.getSchema());
assertEquals("objName", objRes.getName());
assertEquals("objType", objRes.getType());
assertNull(objRes.getCount());
assertNull(objRes.getTotal());
}

@Test
public void testJsonDeserializationWithEmpty() throws JsonProcessingException {
String json = "{\"schema\":\"schemaName\", \"name\":\"objName\", \"type\":\"objType\", \"count\":0, \"total\":0}";
ObjectResult objRes1 = ObjectResult.fromJson(json);
String json2 = objRes1.toJson();
ObjectResult objRes2 = ObjectResult.fromJson(json2);
assertNotNull(objRes2);
assertEquals(objRes1.getSchema(), objRes2.getSchema());
assertEquals(objRes1.getType(), objRes2.getType());
assertEquals(objRes1.getName(), objRes2.getName());
assertEquals(objRes1.getCount().get(), objRes2.getCount().get());
assertEquals(objRes1.getTotal().get(), objRes2.getTotal().get());
}

}