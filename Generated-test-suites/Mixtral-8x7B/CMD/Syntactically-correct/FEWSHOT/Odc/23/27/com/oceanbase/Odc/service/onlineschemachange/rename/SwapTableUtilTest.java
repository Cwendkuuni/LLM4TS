package com.oceanbase.odc.service.onlineschemachange.rename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false);
assertTrue(result);

swapTableType = SwapTableType.AUTOMATIC;
fullTransferProgressPercentage = 99.0;
fullVerificationResult = FullVerificationResult.INCONSISTENT;

result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false);
assertFalse(result);

swapTableType = SwapTableType.MANUAL;
taskStatus = TaskStatus.STOPPED;
fullTransferProgressPercentage = 100.0;
fullVerificationResult = FullVerificationResult.CONSISTENT;

result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, true);
assertFalse(result);

swapTableType = SwapTableType.MANUAL;
taskStatus = TaskStatus.RUNNING;
fullTransferProgressPercentage = 50.0;
fullVerificationResult = FullVerificationResult.UNCHECK;

result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false);
assertFalse(result);
}
}