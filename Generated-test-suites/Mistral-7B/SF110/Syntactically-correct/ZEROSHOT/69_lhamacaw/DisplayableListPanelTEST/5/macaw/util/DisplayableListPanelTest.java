package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.*;
import static macaw.util.MacawMessages.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private User user;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private ArrayList<Displayable> masterList;
 private String panelTitleText;
 private String currentUserID;
 private boolean allowWriteAccess;
 private boolean allowRecordEditing;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 displayableList = mock(DisplayableList.class);
 user = new User("testUser", "password", "Test User", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
 sessionProperties.setProperty("currentUser", user);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", allowWriteAccess);
 masterList = new ArrayList<>();
 displayableListPanel.setDisplayableItems(masterList);
 }

 @Test
 public void testDisplayableListPanelConstructionWithWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructionWithWriteAndRecordEditingAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDisplayableListPanelConstructionWithNullParentForm() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, null, "Test Panel", true);
 }

 @Test(expected = MacawException.class)
 public void testDisplayableListPanelWithMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.addListItem();
 }

 @Test
 public void testBuildUI() {
 final JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testCreateControlButtonPanel() {
 final JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.updateButtonStates();
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 }

 @Test
 public void testGetAllListItems() {
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test List Owner");
 assertEquals("Test List Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedSelect() {
 displayableListPanel.select = mock(JButton.class);
 final ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).selectListItems();
 }

 @Test
 public void testActionPerformedAdd() {
 displayableListPanel.add = mock(JButton.class);
 final ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).addListItem();
 }

 @Test
 public void testActionPerformedEdit() {
 displayableListPanel.edit = mock(JButton.class);
 final ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).editSelectedListItem();
 }

 @Test
 public void testActionPerformedView() {
 displayableListPanel.view = mock(JButton.class);
 final ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).viewSelectedListItem();
 }

 @Test
 public void testActionPerformedDelete() {
 displayableListPanel.delete = mock(JButton.class);
 final ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).deleteSelectedListItems();
 }
}