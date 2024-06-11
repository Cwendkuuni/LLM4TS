import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable_ManualSwapTableDisable() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.FAILED;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, 0d, fullVerificationResult,
manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualSwapTableEnabled() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
double fullTransferProgressPercentage = 100d;
boolean manualSwapTableStarted = true;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_AutomaticSwapTable() {
SwapTableType swapTableType = SwapTableType.AUTOMATIC;
TaskStatus taskStatus = TaskStatus.RUNNING;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
double fullTransferProgressPercentage = 100d;
boolean manualSwapTableStarted = false;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_FullTransferProgressLessThan100() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
double fullTransferProgressPercentage = 99d;
boolean manualSwapTableStarted = false;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_FullVerificationResultInconsistent() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
double fullTransferProgressPercentage = 100d;
boolean manualSwapTableStarted = false;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_FullVerificationResultUnchecked() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
double fullTransferProgressPercentage = 100d;
boolean manualSwapTableStarted = false;

assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualSwapTableEnabledWithUncheckedVerification() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
double fullTransferProgressPercentage = 100d;
boolean manualSwapTableStarted = true;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}

@Test
public void testIsSwapTableEnable_ManualSwapTableEnabledWithConsistentVerification() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
double fullTransferProgressPercentage = 100d;
boolean manualSwapTableStarted = true;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}
}