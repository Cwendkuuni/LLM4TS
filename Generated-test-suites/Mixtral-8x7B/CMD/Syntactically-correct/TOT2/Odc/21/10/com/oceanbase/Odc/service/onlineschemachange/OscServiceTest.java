
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assert.assertNotNull(result);
        Assert.assertEquals(id, result.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfo_NonExistingId() {
        Long id = -1L;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOscDatabaseInfo_NullId() {
        oscService.getOscDatabaseInfo(null);
    }

    @Test
    @Transactional
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        Assert.assertNotNull(result);
        Assert.assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    @Transactional
    public void testSwapTable_NonExistingScheduleTaskId() {
        Long scheduleTaskId = -1L;
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testSwapTable_NullScheduleTaskId() {
        oscService.swapTable(null);
    }

}
