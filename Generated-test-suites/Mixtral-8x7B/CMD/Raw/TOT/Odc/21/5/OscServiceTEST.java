
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.schedule.model.JobType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OscServiceTest {

  @Autowired
  private OscService oscService;

  @Test
  public void testGetOscDatabaseInfoWithValidId() {
    Long id = 1L;
    OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
    assertEquals(id, oscDatabase.getDatabaseId());
    assertTrue(oscDatabase.isLockDatabaseUserRequired());
  }

  @Test
  public void testGetOscDatabaseInfoWithInvalidId() {
    Long id = -1L;
    OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
    assertNotNull(oscDatabase);
    assertEquals(0L, oscDatabase.getDatabaseId());
    assertFalse(oscDatabase.isLockDatabaseUserRequired());
  }

  @Test
  @Transactional
  public void testSwapTableWithValidScheduleTaskId() {
    Long scheduleTaskId = 1L;
    OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
    oscParameters.setSwapTableType(SwapTableType.MANUAL);
    ScheduleEntity scheduleEntity = new ScheduleEntity();
    scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
    scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
    ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
    scheduleTask.setJobName(String.valueOf(scheduleEntity.getScheduleId()));
    scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
    ScheduleTaskEntity savedScheduleTask = scheduleTaskRepository.save(scheduleTask);
    OscSwapTableVO oscSwapTable = oscService.swapTable(savedScheduleTask.getScheduleTaskId());
    assertEquals(savedScheduleTask.getScheduleTaskId(), oscSwapTable.getScheduleTaskId());
  }

  @Test(expected = PreConditions.InvalidArgumentException.class)
  @Transactional
  public void testSwapTableWithInvalidScheduleTaskId() {
    Long scheduleTaskId = -1L;
    oscService.swapTable(scheduleTaskId);
  }

}
