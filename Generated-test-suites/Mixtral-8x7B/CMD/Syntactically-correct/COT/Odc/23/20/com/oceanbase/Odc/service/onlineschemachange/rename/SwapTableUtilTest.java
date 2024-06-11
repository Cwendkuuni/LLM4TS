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
 public void setUp() {
 swapTableUtil = new SwapTableUtil();
 }

 // Test for isSwapTableEnable()
 @Test
 public void testIsSwapTableEnable_whenManualAndNotManualSwapStartedAndRunningAndProgress100AndVerificationConsistentOrUncheck_thenReturnTrue() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertTrue(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_whenManualAndManualSwapStartedAndRunningAndProgress100AndVerificationConsistentOrUncheck_thenReturnFalse() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = true;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_whenManualAndRunningAndProgressLessThan100AndVerificationConsistentOrUncheck_thenReturnFalse() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 99.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_whenManualAndRunningAndProgress100AndVerificationInconsistent_thenReturnFalse() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }

 @Test
 public void testIsSwapTableEnable_whenNotManualAndNotRunningAndProgress100AndVerificationConsistentOrUncheck_thenReturnFalse() {
 SwapTableType swapTableType = SwapTableType.NOT_MANUAL;
 TaskStatus taskStatus = TaskStatus.NOT_RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 assertFalse(swapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
 fullVerificationResult, manualSwapTableStarted));
 }
}