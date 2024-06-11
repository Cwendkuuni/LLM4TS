import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SwapTableUtilTest {

@Test
public void testIsSwapTableEnable_typicalUseCase() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = true;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_edgeCase_fullTransferProgressPercentage() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 99.9;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_edgeCase_fullVerificationResult() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_exception_swapTableType() {
SwapTableType swapTableType = SwapTableType.AUTO;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_exception_taskStatus() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.FINISHED;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}

@Test
public void testIsSwapTableEnable_exception_manualSwapTableStarted() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = true;

boolean expectedValue = false;
boolean actualValue = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(expectedValue, actualValue);
}
}