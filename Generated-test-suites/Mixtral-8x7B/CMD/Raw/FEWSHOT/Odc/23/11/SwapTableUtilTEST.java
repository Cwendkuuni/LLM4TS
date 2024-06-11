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

assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false));

swapTableType = SwapTableType.AUTO;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false));

taskStatus = TaskStatus.STOPPED;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false));

fullTransferProgressPercentage = 99.0;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false));

fullVerificationResult = FullVerificationResult.INCONSISTENT;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false));

fullVerificationResult = FullVerificationResult.PARTIAL;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, false));

boolean manualSwapTableStarted = true;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));
}
}