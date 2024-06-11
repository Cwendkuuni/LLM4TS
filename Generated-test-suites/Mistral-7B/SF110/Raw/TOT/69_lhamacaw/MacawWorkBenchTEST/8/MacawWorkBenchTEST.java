package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.UserInterfaceFactory;
import macaw.businessLayer.SessionProperties;

import org.junit.Test;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;

 @Before
 public void setup() {
 // Initialize MacawWorkBench and UserInterfaceFactory
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testCreateButtonPanel() {
 // Test creating button panel
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertEquals(4, buttonPanel.getComponentCount());
 }

 // Add more test cases for other methods and edge cases here

 @After
 public void teardown() {
 // Clean up resources
 macawWorkBench = null;
 userInterfaceFactory = null;
 sessionProperties = null;
 }

}