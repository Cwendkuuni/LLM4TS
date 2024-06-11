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

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(result, true);
}

@Test(expected = IllegalArgumentException.class)
public void testIsSwapTableEnable_NullSwapTableType() {
SwapTableType swapTableType = null;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
}

@Test(expected = IllegalArgumentException.class)
public void testIsSwapTableEnable_NullTaskStatus() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = null;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
}

@Test(expected = IllegalArgumentException.class)
public void testIsSwapTableEnable_NullFullTransferProgressPercentage() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = null;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
}

@Test(expected = IllegalArgumentException.class)
public void testIsSwapTableEnable_NullFullVerificationResult() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = null;
boolean manualSwapTableStarted = false;

SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
}

@Test
public void testIsSwapTableEnable_FullVerificationResultUNCHECK() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.UNCHECK;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(result, true);
}

@Test
public void testIsSwapTableEnable_ManualSwapTableStarted() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = true;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(result, false);
}

@Test
public void testIsSwapTableEnable_NonManualSwapTableType() {
SwapTableType swapTableType = SwapTableType.AUTO;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(result, false);
}

@Test
public void testIsSwapTableEnable_LessThan100FullTransferProgressPercentage() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.RUNNING;
Double fullTransferProgressPercentage = 99.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(result, false);
}

@Test
public void testIsSwapTableEnable_TaskStatusNOTRUNNING() {
SwapTableType swapTableType = SwapTableType.MANUAL;
TaskStatus taskStatus = TaskStatus.FINISHED;
Double fullTransferProgressPercentage = 100.0;
FullVerificationResult fullVerificationResult = FullVerificationResult.CONSISTENT;
boolean manualSwapTableStarted = false;

boolean result = SwapTableUtil.isSwapTableEnable(swapTableType, taskStatus, fullTransferProgressPercentage, fullVerificationResult, manualSwapTableStarted);
assertEquals(result, false);
}
}