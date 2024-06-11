import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

swapTableType = SwapTableType.AUTO;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

fullTransferProgressPercentage = 99.0;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

fullVerificationResult = FullVerificationResult.INCONSISTENT;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

manualSwapTableStarted = true;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

taskStatus = TaskStatus.STOPPED;
assertFalse(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

fullVerificationResult = FullVerificationResult.UNCHECK;
assertTrue(SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage,
fullVerificationResult, manualSwapTableStarted));

}

}