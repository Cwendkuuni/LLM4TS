import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanelCreation() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testQueryTextFieldFocus() {
 WhoIS whois = new WhoIS();
 JTextField queryTextField = whois.queryTextField;
 queryTextField.requestFocus();
 assertTrue(queryTextField.isFocusOwnerOfWindow());
 }

 @Test
 public void testQueryButtonAction() {
 WhoIS whois = new WhoIS();
 when(mockSocket.isConnected()).thenReturn(true);
 when(mockSocket.getOutputStream()).thenReturn(outputStream);
 when(mockSocket.getInputStream()).thenReturn(inputStream);
 doAnswer(invocation -> {
 inputStream.readLine();
 return null;
 }).when(mockBufferedReader).readLine();
 whois.queryButton.doClick();
 verify(mockSocket).connect(argThat(isA(InetSocketAddress.class)));
 verify(mockPrintStream).println("queryText");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHost() {
 WhoIS whois = new WhoIS();
 when(mockSocket.isConnected()).thenReturn(false);
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 WhoIS whois = new WhoIS();
 when(mockSocket.isConnected()).thenReturn(true);
 when(mockSocket.getOutputStream()).thenThrow(new IOException());
 whois.startQuery();
 }

 @Test
 public void testResetButtonAction() {
 WhoIS whois = new WhoIS();
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }
}