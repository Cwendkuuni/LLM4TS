
package com.oceanbase.odc.service.onlineschemachange.rename;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnableWithValidInputs() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertTrue(result);
    }

    @Test
    public void testIsSwapTableEnableWithSwapTableTypeAuto() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithTaskStatusNotRunning() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.NOT_RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithFullTransferProgressPercentage99() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 99d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithFullVerificationResultInconsistent() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }

    @Test
    public void testIsSwapTableEnableWithManualSwapTableStartedTrue() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertFalse(result);
    }
}
