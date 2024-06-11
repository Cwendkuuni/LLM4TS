import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.OscService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, oscDatabase.getDatabaseId());
        assertNotNull(oscDatabase.getLockDatabaseUserRequired());
    }

    @Test
    @Transactional
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertNotNull(oscSwapTable.getTableName());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);
        assertEquals(true, isLockUserRequired);
    }

    // Add more test cases for edge cases and exception handling scenarios

}