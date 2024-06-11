package macaw.presentationLayer;

import org.junit.*;
import static org.junit.Assert.*;
import macaw.businessLayer.SessionProperties;
import macaw.io.IOException;
import macaw.presentationLayer.MacawWorkBench;
import macaw.presentationLayer.userInterfaceFactory.UserInterfaceFactory;
import org.mockito.Mockito;

public class MacawWorkBenchTest {
@Before
public void setUp() {
MacawWorkBench.main(new String[0]);
}

@Test
public void testMain() {
// Given
String[] arguments = new String[0];

// When
MacawWorkBench.main(arguments);

// Then
// In this case, it's hard to verify anything, as the main method just opens the dialog.
// To test this method thoroughly, we'd need to mock the database, session properties, and user interface factory.
}

@Test
public void testConstructor() {
// Given
UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
SessionProperties sessionProperties = new SessionProperties();

// When
MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);

// Then
assertNotNull(macawWorkBench.dialog);
assertNotNull(macawWorkBench.editConstants);
assertNotNull(macawWorkBench.editVariables);
assertNotNull(macawWorkBench.exportVariableData);
assertNotNull(macawWorkBench.exit);
}

@Test
public void testEditConstants() {
// Given
MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());

// When
macawWorkBench.editConstants();

// Then
// We cannot verify the behavior of this method directly, as it opens a dialog.
// To test this method thoroughly, we'd need to mock the dialog, user interface factory, and session properties.
}

@Test
public void testEditVariables() {
// Given
MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());

// When
macawWorkBench.editVariables();

// Then
// We cannot verify the behavior of this method directly, as it opens a dialog.
// To test this method thoroughly, we'd need to mock the dialog, user interface factory, and session properties.
}

@Test
public void testExportVariableData_Success() {
// Given
UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());

// When
macawWorkBench.exportVariableData();

// Then
// We cannot verify the behavior of this method directly, as it exports data and closes the application.
// To test this method thoroughly, we'd need to mock the file output stream, session properties, and user interface factory.
}

@Test
public void testExportVariableData_IOException() {
// Given
UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
doThrow(new IOException("Error exporting data")).when(macawWorkBench).export();

// When
macawWorkBench.exportVariableData();

// Then
verify(macawWorkBench).exit();
}

@Test
public void testExit() {
// Given
UserInterfaceFactory userInterfaceFactory = new UserInterfaceFactory();
MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());

// When
macawWorkBench.exit();

// Then
verify(macawWorkBench.dialog).dispose();
verify(macawWorkBench).systemExit(0);
}

@After
public void tearDown() {
// Clear mocks
Mockito.reset(macawWorkBench);
}
}