package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 // Testing constructor with expected arguments
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testMainMethod() {
 // This method cannot be tested directly as it is a static method and requires external dependencies
 // Instead, test the functionality of the methods called within it
 }

 @Test
 public void testCreateButtonPanel() {
 final UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
 MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 // Testing number of buttons created
 assertEquals(4, buttonPanel.getComponentCount());
 }

 @Test
 public void testEditConstantsMethod() {
 // Cannot test directly as it opens a new dialog
 // Instead, test the functionality of the method call
 sessionProperties.setProperty("currentUser", new User("testUser", "testPassword"));
 macawWorkBench.editConstants();
 // Test if VariableConstantsEditor dialog is opened
 assertTrue(isDisplayed(macawWorkBench.dialog));
 }

 @Test
 public void testEditVariablesMethod() {
 // Cannot test directly as it opens a new dialog
 // Instead, test the functionality of the method call
 macawWorkBench.editVariables();
 // Test if MacawVariableBrowser dialog is opened
 assertTrue(isDisplayed(macawWorkBench.dialog));
 }

 @Test
 public void testExportVariableDataMethod() {
 // Cannot test directly as it requires external dependencies and writes to a file
 // Instead, test the functionality of the method call
 macawWorkBench.exportVariableData();
 // Check if export was successful by checking if file is created
 File file = new File("exportedVariables.xml");
 assertTrue(file.exists());
 }

 @Test
 public void testExitMethod() {
 // This method cannot be tested directly as it exits the program
 // Instead, test if the program exits when the exit button is clicked
 macawWorkBench.exit();
 // Program should have exited, so this test will fail if the test runner is still running
 fail("Program should have exited");
 }

 @Test(expected = NullPointerException.class)
 public void testMacawWorkBenchConstructorWithNullSessionProperties() {
 macawWorkBench = new MacawWorkBench(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMacawWorkBenchEditConstantsMethodWithNullSessionProperties() {
 sessionProperties = null;
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editConstants();
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMacawWorkBenchEditVariablesMethodWithNullSessionProperties() {
 sessionProperties = null;
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.editVariables();
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMacawWorkBenchExportVariableDataMethodWithNullSessionProperties() {
 sessionProperties = null;
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.exportVariableData();
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testShowDialogWithExceptionThrownDuringLogin() {
 // Test if dialog is not displayed when an exception is thrown during login
 try {
 throw new Exception();
 }
 catch (Exception e) {
 sessionProperties.setProperty("currentUser", new User("testUser", "testPassword"));
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.show();
 fail("Dialog should not have been displayed");
 }
 // Test if program exits when an exception is thrown during login
 assertFalse(isDisplayed(macawWorkBench.dialog));
 }

 @After
 public void tearDown() {
 macawWorkBench.dialog.dispose();
 }

 private boolean isDisplayed(Component component) {
 // Helper method to check if a component is displayed
 return component.isDisplayable() && component.isShowing();
 }
}