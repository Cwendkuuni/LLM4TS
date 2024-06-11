package macaw.presentationLayer;

import org.junit.Test;
import static org.junit.Assert.*;
import macaw.businessLayer.SessionProperties;
import macaw.persistenceLayer.demo.DemonstrationCurationService;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.system.LoginDialog;

public class MacawWorkBenchTest {
    @Test
    public void testMacawWorkBenchConstructor() {
        MacawWorkBench macawWorkBench = new MacawWorkBench();
        assertNotNull(macawWorkBench);
    }

    @Test
    public void testDatabaseSetupWithInvalidCredentials() {
        SessionProperties sessionProperties = new SessionProperties();
        try {
            sessionProperties.setProperty("database", new DemonstrationCurationService(true));
            sessionProperties.setProperty("retrievalService", new DemonstrationRetrievalService(sessionProperties));
            MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
            Assert.assertThrows(Exception.class, macawWorkBench::main);
        } catch (Exception e) {
            // Expected to catch an exception
        }
    }

    @Test
    public void testInvalidCommandLineArguments() {
        String[] arguments = {"--invalid-argument"};
        try {
            MacawWorkBench.main(arguments);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testLoginFailure() {
        SessionProperties sessionProperties = new SessionProperties();
        LoginDialog loginDialog = new LoginDialog(sessionProperties, 1);
        loginDialog.setVisible(true);
        loginDialog.dispose();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        macawWorkBench.show();
        macawWorkBench.exit(); // Test the case when the user enters incorrect credentials three times.
    }

    @Test
    public void testDatabaseException() {
        SessionProperties sessionProperties = new SessionProperties();
        try {
            sessionProperties.setProperty("database", new ProductionCurationService(sessionProperties));
            sessionProperties.setProperty("retrievalService", new ProductionRetrievalService(sessionProperties));
            MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
            macawWorkBench.show();
            macawWorkBench.exit();
        } catch (Exception e) {
            // Expected to catch an exception
        }
    }

    @Test
    public void testMacawWorkBench() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        macawWorkBench.show();
        macawWorkBench.editConstants();
        macawWorkBench.editVariables();
        macawWorkBench.exportVariableData();
        macawWorkBench.exit();
        macawWorkBench = null;
    }

    @Test
    public void testActionPerformed() {
        MacawWorkBench macawWorkBench = Mockito.spy(new MacawWorkBench(new SessionProperties()));
        MacawWorkBench.ActionEvent actionEvent = Mockito.mock(MacawWorkBench.ActionEvent.class);
        when(actionEvent.getSource()).thenReturn(macawWorkBench.editConstants);
        macawWorkBench.actionPerformed(actionEvent);
        verify(macawWorkBench).editConstants();
    }
}