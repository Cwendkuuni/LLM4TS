import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable_ManualSwapTableType_ManualSwapTableNotStarted_RunningTaskStatus_FullTransferProgressPercentage100_ConsistentFullVerificationResult() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted);

assertTrue(result);
}

@Test
public void testIsSwapTableEnable_ManualSwapTableType_ManualSwapTableNotStarted_RunningTaskStatus_FullTransferProgressPercentage100_UncheckFullVerificationResult() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted);

assertTrue(result);
}

@Test
public void testIsSwapTableEnable_ManualSwapTableType_ManualSwapTableNotStarted_RunningTaskStatus_FullTransferProgressPercentageNot100_ConsistentFullVerificationResult() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 50.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}

@Test
public void testIsSwapTableEnable_ManualSwapTableType_ManualSwapTableNotStarted_RunningTaskStatus_FullTransferProgressPercentage100_InconsistentFullVerificationResult() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}

@Test
public void testIsSwapTableEnable_ManualSwapTableType_ManualSwapTableStarted_RunningTaskStatus_FullTransferProgressPercentage100_ConsistentFullVerificationResult() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = true;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}

@Test
public void testIsSwapTableEnable_OtherSwapTableType_ManualSwapTableNotStarted_RunningTaskStatus_FullTransferProgressPercentage100_ConsistentFullVerificationResult() {
SwapTableType swapTableType = SwapTableType.AUTO;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}
}