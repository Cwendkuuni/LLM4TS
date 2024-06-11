import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_ValidInputs_ReturnsTrue() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_UncheckVerificationResult_ReturnsTrue() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_InProgressTransfer_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 99d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_SwapTableManuallyStarted_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_NotManualSwapTableType_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_NullSwapTableType_ReturnsFalse() {
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(null, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_NullTaskStatus_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_NullFullTransferProgressPercentage_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_NullFullVerificationResult_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                null, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_NullManualSwapTableStarted_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, null));
    }

    @Test
    public void testIsSwapTableEnable_NegativeFullTransferProgressPercentage_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = -1d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }

    @Test
    public void testIsSwapTableEnable_FullTransferProgressPercentageGreaterThan100_ReturnsFalse() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 101d;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }
}

