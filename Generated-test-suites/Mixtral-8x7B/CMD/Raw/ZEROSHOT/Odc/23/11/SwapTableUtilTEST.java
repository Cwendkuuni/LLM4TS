import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable_whenManualAndNotManualSwapTableStartedAndRunningAndFullTransferProgressIs100AndFullVerificationResultIsConsistentOrUncheck_returnsTrue() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

assertTrue(result);
}

@Test
public void testIsSwapTableEnable_whenManualAndManualSwapTableStartedAndRunningAndFullTransferProgressIs100AndFullVerificationResultIsConsistentOrUncheck_returnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = true;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}

@Test
public void testIsSwapTableEnable_whenNotManualAndNotManualSwapTableStartedAndRunningAndFullTransferProgressIs100AndFullVerificationResultIsConsistentOrUncheck_returnsFalse() {
SwapTableType swapTableType = SwapTableType.AUTO;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}

@Test
public void testIsSwapTableEnable_whenManualAndNotManualSwapTableStartedAndNotRunningAndFullTransferProgressIs100AndFullVerificationResultIsConsistentOrUncheck_returnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.NOT_RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}

@Test
public void testIsSwapTableEnable_whenManualAndNotManualSwapTableStartedAndRunningAndFullTransferProgressIsLessThan100AndFullVerificationResultIsConsistentOrUncheck_returnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 99.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}

@Test
public void testIsSwapTableEnable_whenManualAndNotManualSwapTableStartedAndRunningAndFullTransferProgressIs100AndFullVerificationResultIsNotConsistentOrUncheck_returnsFalse() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);

assertFalse(result);
}
}