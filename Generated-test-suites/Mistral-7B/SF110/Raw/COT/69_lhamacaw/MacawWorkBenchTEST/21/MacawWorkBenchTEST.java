package macaw.presentationLayer.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;

public class MacawWorkBenchTest {

 private MacawWorkBench macawWorkBench;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new SwingUserInterfaceFactory();
 macawWorkBench = new MacawWorkBench(sessionProperties);
 }

 @Test
 public void testMain() {
 MacawWorkBench.main(new String[0]);
 // add assertions for the valid scenario here
 }

 @Test(expected = Exception.class)
 public void testMainWithException() {
 MacawWorkBench.main(new String[]{"invalidArgument"});
 // add assertions for the exception scenario here
 }

 @Test
 public void testMacawWorkBenchConstructor() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 // add assertions for the valid scenario here
 }

 @Test
 public void testCreateButtonPanel() {
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 // add assertions for the valid scenario here
 }

 @Test
 public void testShowDialog() {
 macawWorkBench.show();
 JDialog dialog = macawWorkBench.dialog;
 assertTrue(dialog.isVisible());
 // add assertions for the valid scenario here
 }

 @Test
 public void testEditConstants() {
 macawWorkBench.editConstants();
 // add assertions for the valid scenario here
 }

 @Test
 public void testEditVariables() {
 macawWorkBench.editVariables();
 // add assertions for the valid scenario here
 }

 @Test
 public void testExportVariableData() {
 macawWorkBench.exportVariableData();
 // add assertions for the valid scenario here
 }

 @Test
 public void testExit() {
 macawWorkBench.exit();
 // add assertions for the valid scenario here
 }

 @Test(expected = IllegalStateException.class)
 public void testEditConstantsWhenDialogIsNotVisible() {
 macawWorkBench.editConstants();
 macawWorkBench.dialog.setVisible(false);
 macawWorkBench.editConstants();
 // add assertions for the exception scenario here
 }

 @Test(expected = NullPointerException.class)
 public void testEditConstantsWithNullUserInterfaceFactory() {
 macawWorkBench = new MacawWorkBench(sessionProperties);
 macawWorkBench.sessionProperties.setProperty("userInterfaceFactory", null);
 macawWorkBench.editConstants();
 // add assertions for the exception scenario here
 }

}