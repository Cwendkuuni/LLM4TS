import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable() {
// Test for SwapTableType.MANUAL, manualSwapTableStarted = false, taskStatus = RUNNING,
// fullTransferProgressPercentage = 100.0, fullVerificationResult = CONSISTENT
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

// Test for SwapTableType.MANUAL, manualSwapTableStarted = true, taskStatus = RUNNING,
// fullTransferProgressPercentage = 100.0, fullVerificationResult = CONSISTENT
swapTableType = SwapTableType.MANUAL;
taskStatus = TaskStatus.RUNNING;
fullTransferProgressPercentage = 100.0;
fullVerificationResult = FullVerificationResult.CONSISTENT;
manualSwapTableStarted = true;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

// Test for SwapTableType.MANUAL, manualSwapTableStarted = false, taskStatus = NOT_RUNNING,
// fullTransferProgressPercentage = 100.0, fullVerificationResult = CONSISTENT
swapTableType = SwapTableType.MANUAL;
taskStatus = TaskStatus.NOT_RUNNING;
fullTransferProgressPercentage = 100.0;
fullVerificationResult = FullVerificationResult.CONSISTENT;
manualSwapTableStarted = false;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

// Test for SwapTableType.MANUAL, manualSwapTableStarted = false, taskStatus = RUNNING,
// fullTransferProgressPercentage = 99.0, fullVerificationResult = CONSISTENT
swapTableType = SwapTableType.MANUAL;
taskStatus = TaskStatus.RUNNING;
fullTransferProgressPercentage = 99.0;
fullVerificationResult = FullVerificationResult.CONSISTENT;
manualSwapTableStarted = false;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

// Test for SwapTableType.MANUAL, manualSwapTableStarted = false, taskStatus = RUNNING,
// fullTransferProgressPercentage = 100.0, fullVerificationResult = INCONSISTENT
swapTableType = SwapTableType.MANUAL;
taskStatus = TaskStatus.RUNNING;
fullTransferProgressPercentage = 100.0;
fullVerificationResult = FullVerificationResult.INCONSISTENT;
manualSwapTableStarted = false;

assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

// Test for SwapTableType.MANUAL, manualSwapTableStarted = false, taskStatus = RUNNING,
// fullTransferProgressPercentage = 100.0, fullVerificationResult = UNCHECKED
swapTableType = SwapTableType.MANUAL;
taskStatus = TaskStatus.RUNNING;
fullTransferProgressPercentage = 100.0;
fullVerificationResult = FullVerificationResult.UNCHECK;
manualSwapTableStarted = false;

assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}
}