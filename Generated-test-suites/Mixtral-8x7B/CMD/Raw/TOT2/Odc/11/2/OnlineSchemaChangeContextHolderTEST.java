
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.core.shared.constant.OdcConstants;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLongLongLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringStringStringNullEmpty() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        OnlineSchemaChangeContextHolder.trace("", "", "");
        OnlineSchemaChangeContextHolder.trace("1", "", "3");
        OnlineSchemaChangeContextHolder.trace("1", "2", "");
        OnlineSchemaChangeContextHolder.trace("", "2", "3");
        for (String key : OnlineSchemaChangeContextHolder.getMDCProperties()) {
            assertEquals(key.isEmpty() ? null : "0", OnlineSchemaChangeContextHolder.get(key));
        }
    }

    @Test
    public void testRetraceMap() {
        OnlineSchemaChangeContextHolder.retrace(null);
        OnlineSchemaChangeContextHolder.retrace(Collections.emptyMap());
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");
        OnlineSchemaChangeContextHolder.retrace(context);
        for (String key : context.keySet()) {
            assertEquals(context.get(key), OnlineSchemaChangeContextHolder.get(key));
        }
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        for (String key : OnlineSchemaChangeContextHolder.getMDCProperties()) {
            assertEquals(null, OnlineSchemaChangeContextHolder.get(key));
        }
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get("invalidKey"));
    }

}
