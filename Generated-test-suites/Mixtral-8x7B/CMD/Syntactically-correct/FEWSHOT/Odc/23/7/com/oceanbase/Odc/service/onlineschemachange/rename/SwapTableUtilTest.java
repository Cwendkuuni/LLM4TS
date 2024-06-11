import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable_ManualAndNotManualSwapTableStarted_TaskRunningAndFullTransferProgress100_ReturnsTrue() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
boolean manualSwapTableStarted = false;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualAndManualSwapTableStarted_TaskRunningAndFullTransferProgress100_ReturnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
boolean manualSwapTableStarted = true;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_OtherThanManualSwapTableType_ReturnsFalse() {
SwapTableType swapTableType = SwapTableType.AUTOMATIC;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
boolean manualSwapTableStarted = false;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualAndNotManualSwapTableStarted_TaskNotRunningAndFullTransferProgress100_ReturnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.COMPLETED;
Double fullTransferProgressPercentage = 100.0;
boolean manualSwapTableStarted = false;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualAndNotManualSwapTableStarted_TaskRunningAndFullTransferProgressNot100_ReturnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 90.0;
boolean manualSwapTableStarted = false;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualAndNotManualSwapTableStarted_TaskRunningAndFullVerificationResultIsInconsistent_ReturnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
boolean manualSwapTableStarted = false;
FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualAndNotManualSwapTableStarted_TaskRunningAndFullVerificationResultUnchecked_ReturnsTrue() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
boolean manualSwapTableStarted = false;
FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;

assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}
}