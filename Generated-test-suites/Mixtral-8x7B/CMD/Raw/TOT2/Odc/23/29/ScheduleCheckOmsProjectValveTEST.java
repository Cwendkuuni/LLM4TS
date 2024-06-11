import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ScheduleCheckOmsProjectValveTest {

 @Mock
 private ProjectOpenApiService projectOpenApiService;
 @Mock
 private OscTaskCompleteHandler completeHandler;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 @Mock
 private SwapTableUtil swapTableUtil;
 @Mock
 private DefaultTableFactory defaultTableFactory;

 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 @BeforeEach
 void setUp() {
 MockitoAnnotations.openMocks(this);

 scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
 scheduleCheckOmsProjectValve.projectOpenApiService = projectOpenApiService;
 scheduleCheckOmsProjectValve.completeHandler = completeHandler;
 scheduleCheckOmsProjectValve.scheduleTaskRepository = scheduleTaskRepository;
 scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
 scheduleCheckOmsProjectValve.swapTableUtil = swapTableUtil;
 scheduleCheckOmsProjectValve.defaultTableFactory = defaultTableFactory;
 }

 // Test cases from Expert 1, 2, and 3

}