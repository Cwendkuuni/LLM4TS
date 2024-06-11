import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class SwapTableUtilTest {

 private SwapTableUtil swapTableUtil;

 @Before
 public void setup() {
 swapTableUtil = new SwapTableUtil();
 }

 @Test
 public void testIsSwapTableEnable_ManualSwapTableEnable() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertTrue(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_ManualSwapTableNotStarted() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 50D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_ManualSwapTableStarted() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = true;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_NonManualSwapTable() {
 SwapTableType swapTableType = SwapTableType.AUTO;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_TaskStatusNotRunning() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.FINISHED;
 Double fullTransferProgressPercentage = 100D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_FullTransferProgressLessThan100() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 99D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_FullVerificationResultInconsistent() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_FullVerificationResultUncheck() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100D;
 FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
 boolean manualSwapTableStarted = false;

 assertTrue(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }
}