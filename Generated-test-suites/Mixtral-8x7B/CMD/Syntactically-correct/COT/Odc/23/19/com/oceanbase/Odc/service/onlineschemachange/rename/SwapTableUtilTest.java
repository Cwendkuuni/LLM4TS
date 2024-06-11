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

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author yaobin
 * @date 2023-11-08
 * @since 4.2.3
 */
public class SwapTableUtilTest {

    @Test
    public void testIsSwapTableEnable_normalCase() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean expected = true;
        boolean actual = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSwapTableEnable_notManualSwapTable() {
        SwapTableType swapTableType = SwapTableType.AUTO;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean expected = false;
        boolean actual = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSwapTableEnable_manualSwapTableStarted() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = true;

        boolean expected = false;
        boolean actual = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSwapTableEnable_notRunningTask() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.FINISHED;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean expected = false;
        boolean actual = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSwapTableEnable_transferProgressNot100() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 99.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean expected = false;
        boolean actual = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSwapTableEnable_fullVerificationResultUncheck() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
        boolean manualSwapTableStarted = false;

        boolean expected = true;
        boolean actual = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsSwapTableEnable_fullVerificationResultInconsistent() {
        SwapTableType swapTableType = SwapTableType.MANUAL;
        TaskStatus taskStatus = TaskStatus.RUNNING;
        Double fullTransferProgressPercentage = 100.0;
        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        boolean manualSwapTableStarted = false;

        boolean expected = false;
        boolean actual = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
                fullVerificationResult, manualSwapTableStarted);

        assertEquals(expected, actual);
    }

}