import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @BeforeEach
 public void setup() {
 WhoIS whois = new WhoIS();
 this.whois = whois;
 }

 @Test
 public void testWhoISConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanelIsCreated() {
 assertTrue(whois.queryPanel() instanceof JPanel);
 }

 @Test
 public void testQueryTextFieldIsEmptyInitially() {
assertEquals("", whois.queryTextField.getText());
}

 @Test
 public void testQueryTextFieldAcceptsInput() {
 whois.queryTextField.setText("example.com");
 assertEquals("example.com", whois.queryTextField.getText());
}

 @Test
 public void testQueryButtonPerformsQuery() {
 whois.queryTextField.setText("example.com");
 mockStatic(Socket.class);
 when(Socket.class.getMethods()).thenReturn(new Class[] { Socket.class });
 given(Socket.class.getMethod("getOutputStream")).willReturn(mock(PrintStream.class));
 given(Socket.class.getMethod("getInputStream")).willReturn(mock(BufferedReader.class));
 given(Socket.class.getMethod("getOutputStream")).willReturn(mock(PrintStream.class));
 given(Socket.class.getMethod("getInputStream")).willReturn(mock(BufferedReader.class));
 whois.queryButton.doClick();
 verify(Socket.class, times(1)).newSocket(whois.WHOIS_ADDRESS, whois.WHOIS_PORT);
}

 @Test
 public void testResetButtonResetsTextField() {
 whois.queryTextField.setText("example.com");
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
}

 @Test
 public void testInvalidQueryThrowsError() {
 whois.queryTextField.setText(" ");
 whois.queryButton.doClick();
 String errorMessage = "No Query Detected";
 verify(JOptionPane.class, times(1)).showMessageDialog(whois, errorMessage, "Error", 0);
}

 @Test
 public void testInvalidHostThrowsError() {
 whois.queryTextField.setText("invalidHost.com");
 whois.queryButton.doClick();
 String errorMessage = "Unknown Whois host: invalidHost.com";
 verify(JOptionPane.class, times(1)).showMessageDialog(whois, errorMessage, "Error", 0);
}

 @Test
 public void testInvalidPortThrowsError() {
 whois.queryTextField.setText("example.com:12345");
 whois.queryButton.doClick();
 String errorMessage = "Whois server could not be contacted.\nPlease check your internet connection.";
 verify(JOptionPane.class, times(1)).showMessageDialog(whois, errorMessage, "Error", 0);
}
}