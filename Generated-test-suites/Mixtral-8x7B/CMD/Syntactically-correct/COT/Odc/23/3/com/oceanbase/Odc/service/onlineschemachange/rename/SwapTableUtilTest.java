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
    public void testIsSwapTableEnable() {
        // Test with valid inputs
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100D;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;
        assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test with null swapTableType
        assertFalse(SwapTableUtil.isSwapTableEnable(null, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test with null taskStatus
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, null, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test with null fullTransferProgressPercentage
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, null,
                fullVerificationResult, manualSwapTableStarted));

        // Test with null fullVerificationResult
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                null, manualSwapTableStarted));

        // Test with null manualSwapTableStarted
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, null));

        // Test with invalid swapTableType
        swapTableType = SwapTableType.AUTO;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test with invalid taskStatus
        taskStatus = TaskStatus.STOPPED;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test with invalid fullTransferProgressPercentage
        fullTransferProgressPercentage = 99D;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test with invalid fullVerificationResult
        fullVerificationResult = FullVerificationResult.INCONSISTENT;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));

        // Test with manualSwapTableStarted = true
        manualSwapTableStarted = true;
        assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted));
    }
}