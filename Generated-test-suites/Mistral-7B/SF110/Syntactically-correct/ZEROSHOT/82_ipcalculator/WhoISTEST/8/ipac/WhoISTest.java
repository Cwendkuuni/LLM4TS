package ipac.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ipac.WhoIS;
import ipac.WhoIS.WhoISEvent;

@RunWith(MockitoJUnitRunner.class)
public class WhoISTest {

 private WhoIS whois;
 @Mock
 private JTextField queryTextField;
 @Mock
 private JButton queryButton;
 @Mock
 private JButton resetButton;
 @Mock
 private JCheckBox lowercaseD;
 @Mock
 private JRadioButton lowercaseL;
 @Mock
 private JRadioButton uppercaseL;
 @Mock
 private JRadioButton lowercaseM;
 @Mock
 private JRadioButton uppercaseM;
 @Mock
 private JRadioButton lowercaseX;
 @Mock
 private JRadioButton resetRadioButton;
 @Mock
 private ButtonGroup radioButtonGroup;
 @Mock
 private JPopupMenu popupMenu;
 @Mock
 private JComboBox inverseQueries;
 @Mock
 private JList miscList;

 @Before
 public void setUp() {
 whois = new WhoIS();
 WhoISPanel mockPanel = Mockito.spy(new WhoISPanel());
 whois.setLayout(new BoxLayout(whois, 1));
 whois.add(mockPanel);

 Mockito.when(mockPanel.getQueryTextField()).thenReturn(queryTextField);
 Mockito.when(mockPanel.getQueryButton()).thenReturn(queryButton);
 Mockito.when(mockPanel.getResetButton()).thenReturn(resetButton);
 Mockito.when(mockPanel.getLowercaseL()).thenReturn(lowercaseL);
 Mockito.when(mockPanel.getUppercaseL()).thenReturn(uppercaseL);
 Mockito.when(mockPanel.getLowercaseM()).thenReturn(lowercaseM);
 Mockito.when(mockPanel.getUppercaseM()).thenReturn(uppercaseM);
 Mockito.when(mockPanel.getLowercaseX()).thenReturn(lowercaseX);
 Mockito.when(mockPanel.getResetRadioButton()).thenReturn(resetRadioButton);
 Mockito.when(mockPanel.getRadioButtonGroup()).thenReturn(radioButtonGroup);
 Mockito.when(mockPanel.getPopupMenu()).thenReturn(popupMenu);
 Mockito.when(mockPanel.getInverseQueries()).thenReturn(inverseQueries);
 Mockito.when(mockPanel.getMiscList()).thenReturn(miscList);

 Mockito.doAnswer(invocation -> {
 WhoISEvent event = new WhoISEvent();
 event.setSource(invocation.getArgument(0));
 SwingUtilities.invokeLater(event::handleEvent);
 return null;
 }).when(mockPanel).handleEvent(ArgumentMatchers.any(ActionEvent.class));

 Mockito.doAnswer(invocation -> {
 WhoISEvent event = new WhoISEvent();
 event.setSource(invocation.getArgument(0));
 SwingUtilities.invokeLater(event::handleEvent);
 return null;
 }).when(mockPanel).handleEvent(ArgumentMatchers.any(KeyEvent.class));
 }

 @After
 public void tearDown() {
 whois = null;
 }

 @Test
 public void testConstructor() {
 assertNotNull(whois);
 assertEquals("APNIC", whois.getWHOIS_NAME());
 assertEquals("whois.apnic.net", whois.getWHOIS_ADDRESS());
 assertEquals(43, whois.getWHOIS_PORT());
 }

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 assertTrue(queryPanel.getLayout() instanceof FlowLayout);

 JLabel queryLabel = (JLabel) queryPanel.getComponent(0);
 assertEquals("Search for : ", queryLabel.getText());
 queryTextField = (JTextField) queryPanel.getComponent(2);
 assertTrue(queryTextField.isFocusable());
 assertEquals(40, queryTextField.getColumns());

 queryButton = (JButton) queryPanel.getComponent(3);
 assertEquals("Query", queryButton.getText());
 queryButton.addActionListener(Mockito.any());

 resetButton = (JButton) queryPanel.getComponent(4);
 assertEquals("Reset", resetButton.getText());
 resetButton.addActionListener(Mockito.any());
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 assertTrue(additionalQueriesPanel.getLayout() instanceof BoxLayout);

 JPanel LookupPanel = (JPanel) additionalQueriesPanel.getComponent(0);
 assertTrue(LookupPanel.getLayout() instanceof BoxLayout);

 JRadioButton lowercaseRL = (JRadioButton) LookupPanel.getComponent(0);
 assertEquals("1st level less specific", lowercaseRL.getText());
 lowercaseRL.setBackground(Color.WHITE);

 JRadioButton uppercaseRL = (JRadioButton) LookupPanel.getComponent(1);
 assertEquals("All less specific", uppercaseRL.getText());
 uppercaseRL.setBackground(Color.WHITE);

 JRadioButton lowercaseRM = (JRadioButton) LookupPanel.getComponent(2);
 assertEquals("1st level more specific", lowercaseRM.getText());
 lowercaseRM.setBackground(Color.WHITE);

 JRadioButton uppercaseRM = (JRadioButton) LookupPanel.getComponent(3);
 assertEquals("All more specific", uppercaseRM.getText());
 uppercaseRM.setBackground(Color.WHITE);

 JRadioButton lowercaseRX = (JRadioButton) LookupPanel.getComponent(4);
 assertEquals("Exact match only", lowercaseRX.getText());
 lowercaseRX.setBackground(Color.WHITE);

 JCheckBox lowercaseDChk = (JCheckBox) LookupPanel.getComponent(5);
 assertEquals("Associated reverse domain", lowercaseDChk.getText());

 JRadioButton resetRadioButton = (JRadioButton) LookupPanel.getComponent(6);
 assertEquals("Reset Radio Button", resetRadioButton.getText());
 }

 @Test
 public void testInverseQueries() {
 JComboBox inverseQueries = whois.getInverseQueries();
 assertNotNull(inverseQueries);
 assertEquals(15, inverseQueries.getItemCount());

 String[] inverseQueriesItems = new String[inverseQueries.getItemCount()];
 for (int i = 0; i < inverseQueries.getItemCount(); i++) {
 inverseQueriesItems[i] = inverseQueries.getItemAt(i).toString();
 }

 assertArrayEquals(new String[]{"None", "admin-c", "admin-c,tech-c,zone-c", "author", "cross-mnt", "cross-nfy", "local-as", "mbrs-by-ref", "member-of", "mnt-by", "mnt-lower", "mnt-nfy", "mnt-routes", "notify", "nserver", "origin", "person", "referral-by", "rev-srv", "sub-dom", "tech-c", "upd-to", "zone-c"}, inverseQueriesItems);
 }

 @Test
 public void testMiscQueries() {
 JList miscList = whois.getMiscList();
 assertNotNull(miscList);
 assertEquals(16, miscList.getModel().getSize());

 Object[] miscQueriesItems = miscList.getSelectedValues();
 assertEquals(1, miscQueriesItems.length);
 assertEquals("All", miscQueriesItems[0].toString());

 miscQueriesItems = new Object[16];
 miscQueriesItems[0] = "All";
 Arrays.sort(miscQueriesItems);
 miscList.setSelectedValues(miscQueriesItems);

 miscList.setSelectedValue("as-block", false);
 miscList.setSelectedValue("as-set", false);
 miscList.setSelectedValue("aut-num", false);
 miscList.setSelectedValue("domain", false);
 miscList.setSelectedValue("filter-set", false);
 miscList.setSelectedValue("inet-rtr", false);
 miscList.setSelectedValue("inet6num", false);
 miscList.setSelectedValue("inetnum", false);
 miscList.setSelectedValue("irt", false);
 miscList.setSelectedValue("key-cert", false);
 miscList.setSelectedValue("mntner", false);
 miscList.setSelectedValue("peering-set", false);
 miscList.setSelectedValue("person", false);
 miscList.setSelectedValue("role", false);
 miscList.setSelectedValue("route", false);
 miscList.setSelectedValue("route-set", false);
 miscList.setSelectedValue("rtr-set", false);

 miscQueriesItems = miscList.getSelectedValues();
 assertEquals(0, miscQueriesItems.length);
 }

 @Test
 public void testHandleEventQueryButton() throws IOException {
 String query = "example.com";
 String inverseQueriesSelected = "author";
 String lowercaseLSelected = "0.0.0.0/0";
 String lowercaseDSelected = "true";
 String[] miscQueriesSelected = {"as-block", "as-set"};

 byte[] queryData = ("T" + query.length() + "\n" +
                     "Q" + query + "\n" +
                     "I" + inverseQueriesSelected + "\n" +
                     "L" + lowercaseLSelected + "\n" +
                     "D" + lowercaseDSelected + "\n" +
                     "T" + miscQueriesSelected.length + "\n").getBytes();

 InputStream queryInputStream = new ByteArrayInputStream(queryData);
 Socket socket = Mockito.mock(Socket.class);
 Mockito.when(socket.getOutputStream()).thenReturn(Mockito.mock(PrintStream.class));
 Mockito.when(socket.getInputStream()).thenReturn(queryInputStream);

 whois.handleEvent(new ActionEvent(queryButton, ActionEvent.ACTION_PERFORMED, ""));

 Mockito.verify(queryTextField, Mockito.times(1)).setText("");
 Mockito.verify(queryTextField, Mockito.times(1)).requestFocus();
 Mockito.verify(resetRadioButton, Mockito.times(1)).setSelected(true);
 Mockito.verify(lowercaseD, Mockito.times(1)).setSelected(Boolean.parseBoolean(lowercaseDSelected));
 Mockito.verify(inverseQueries, Mockito.times(1)).setSelectedIndex(Arrays.asList(inverseQueriesSelected.split(",")).indexOf(Arrays.asList(inverseQueries.getItemAt(1)).toString()));
 Mockito.verify(inverseQueries, Mockito.times(1)).setSelectedItem(inverseQueries.getItemAt(Arrays.asList(inverseQueriesSelected.split(",")).indexOf(Arrays.asList(inverseQueries.getItemAt(1)).toString())));
 Mockito.verify(lowercaseL, Mockito.times(1)).setSelected(lowercaseLSelected.equals("0.0.0.0/0"));
 Mockito.verify(lowercaseM, Mockito.times(1)).setSelected(!lowercaseLSelected.equals("0.0.0.0/0"));
 Mockito.verify(uppercaseL, Mockito.times(1)).setSelected(false);
 Mockito.verify(uppercaseM, Mockito.times(1)).setSelected(false);
 Mockito.verify(lowercaseX, Mockito.times(1)).setSelected(false);
 Mockito.verify(resetRadioButton, Mockito.times(1)).setSelected(false);
 Mockito.verify(socket, Mockito.times(1)).connect(ArgumentMatchers.eq(new InetSocketAddress(whois.getWHOIS_ADDRESS(), whois.getWHOIS_PORT())));
 Mockito.verify(socket, Mockito.times(1)).getOutputStream().write(queryData);
 Mockito.verify(socket, Mockito.times(1)).close();
 }

 @Test
 public void testHandleEventResetButton() throws IOException {
 whois.handleEvent(new ActionEvent(resetButton, ActionEvent.ACTION_PERFORMED, ""));

 Mockito.verify(queryTextField, Mockito.times(1)).setText("");
 Mockito.verify(resetRadioButton, Mockito.times(1)).setSelected(true);
 Mockito.verify(lowercaseD, Mockito.times(1)).setSelected(false);
 Mockito.verify(inverseQueries, Mockito.times(1)).setSelectedIndex(0);
 Mockito.verify(miscList, Mockito.times(1)).setSelectedIndex(0);
 }

 @Test
 public void testHandleEventPopupMenu() throws IOException {
 ActionEvent actionEvent = new ActionEvent(queryTextField, ActionEvent.ACTION_PERFORMED, "Cut");
 whois.handleEvent(actionEvent);

 Mockito.verify(queryTextField, Mockito.times(1)).cut();
 }
}