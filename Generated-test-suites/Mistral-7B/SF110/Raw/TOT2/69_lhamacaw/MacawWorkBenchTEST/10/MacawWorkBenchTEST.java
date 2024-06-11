package macaw.presentationLayer.test;

import macaw.businessLayer.*;
import macaw.persistenceLayer.demo.*;
import macaw.persistenceLayer.production.*;
import macaw.presentationLayer.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MacawWorkBenchTest {

    private MacawWorkBench macawWorkBench;

    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        macawWorkBench = new MacawWorkBench(sessionProperties);
    }

    @Test
    public void testMacawWorkBenchConstructor() {
        assertNotNull(macawWorkBench.dialog);
        assertNotNull(macawWorkBench.editConstants);
        assertNotNull(macawWorkBench.editVariables);
        assertNotNull(macawWorkBench.exportVariableData);
        assertNotNull(macawWorkBench.exit);
    }

    @Test
    public void testShowMethod() {
        macawWorkBench.show();
        assertTrue(macawWorkBench.dialog.isVisible());
    }

    @Test
    public void testEditConstantsMethod() {
        SessionProperties sessionProperties = mock(SessionProperties.class);
        VariableConstantsEditor variableConstantsEditor = mock(VariableConstantsEditor.class);

        when(sessionProperties.getUserInterfaceFactory()).thenReturn(mock(UserInterfaceFactory.class));
        when(sessionProperties.getUserInterfaceFactory().createDialog()).thenReturn(macawWorkBench.dialog);
        when(sessionProperties.getUserInterfaceFactory().createButton(anyString())).thenReturn(macawWorkBench.editConstants);
        when(sessionProperties.getUserInterfaceFactory().createVariableConstantsEditor(any(SessionProperties.class), anyBoolean())).thenReturn(variableConstantsEditor);

        macawWorkBench.editConstants();

        verify(variableConstantsEditor).show();
    }

    @After
    public void tearDown() {
        macawWorkBench = null;
    }
}