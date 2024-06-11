package macaw.presentationLayer;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import javax.swing.*;
import org.junit.Test;

public class MacawWorkBenchTest {

 @Test
 public void testMacawWorkBenchCreation() {
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 assertNotNull(macawWorkBench);
 }

 @Test
 public void testCreateButtonPanel() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 JPanel buttonPanel = macawWorkBench.createButtonPanel(userInterfaceFactory);
 assertNotNull(buttonPanel);
 }

 @Test
 public void testEditConstantsButton() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 ActionEvent actionEvent = mock(ActionEvent.class);
 macawWorkBench.editConstants = macawWorkBench.createButton("test");
 macawWorkBench.editConstants.addActionListener(macawWorkBench);
 macawWorkBench.editConstants.doClick();
 verify(macawWorkBench).editConstants();
 }

 @Test
 public void testEditVariablesButton() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 ActionEvent actionEvent = mock(ActionEvent.class);
 macawWorkBench.editVariables = macawWorkBench.createButton("test");
 macawWorkBench.editVariables.addActionListener(macawWorkBench);
 macawWorkBench.editVariables.doClick();
 verify(macawWorkBench).editVariables();
 }

 @Test
 public void testExportVariableDataButton() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 ActionEvent actionEvent = mock(ActionEvent.class);
 macawWorkBench.exportVariableData = macawWorkBench.createButton("test");
 macawWorkBench.exportVariableData.addActionListener(macawWorkBench);
 macawWorkBench.exportVariableData.doClick();
 verify(macawWorkBench).exportVariableData();
 }

 @Test
 public void testExitButton() {
 UserInterfaceFactory userInterfaceFactory = mock(UserInterfaceFactory.class);
 MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
 ActionEvent actionEvent = mock(ActionEvent.class);
 macawWorkBench.exit = macawWorkBench.createButton("test");
 macawWorkBench.exit.addActionListener(macawWorkBench);
 macawWorkBench.exit.doClick();
 verify(macawWorkBench).exit();
 }
}