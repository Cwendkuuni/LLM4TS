import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwapTableUtilTest {

 @Test
 public void testIsSwapTableEnable() {
 SwapTableType swapTableType = SwapTableType.MANUAL;
 TaskStatus taskStatus = TaskStatus.RUNNING;
 Double fullTransferProgressPercentage = 100.0;
 FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
 boolean manualSwapTableStarted = false;

 // Testing typical use case
 assertEquals(true, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Testing edge case: fullVerificationResult is UNCHECK
 fullVerificationResult = FullVerificationResult.UNCHECK;
 assertEquals(true, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Testing exception handling scenario: manualSwapTableStarted is true
 manualSwapTableStarted = true;
 assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Testing exception handling scenario: fullTransferProgressPercentage is not 100.0
 fullTransferProgressPercentage = 99.0;
 assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Testing exception handling scenario: taskStatus is not RUNNING
 taskStatus = TaskStatus.FINISHED;
 assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));

 // Testing exception handling scenario: swapTableType is not MANUAL
 swapTableType = SwapTableType.AUTO;
 assertEquals(false, SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted));
 }
}