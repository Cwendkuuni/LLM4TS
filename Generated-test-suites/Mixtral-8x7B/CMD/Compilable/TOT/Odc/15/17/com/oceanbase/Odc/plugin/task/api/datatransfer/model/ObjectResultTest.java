package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "table", "table");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("table", objectResult.getName());
        assertEquals("table", objectResult.getType());
    }
}