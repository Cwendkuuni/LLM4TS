import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import macaw.util.DisplayableListPanel;
import macaw.businessLayer.Displayable;
import macaw.system.Log;
import macaw.system.MacawException;
import macaw.util.UserInterfaceFactory;
import macaw.util.DisplayableListItemSelector;
import macaw.util.DisplayableListItemAdder;
import macaw.util.DisplayableListItemEditor;
import macaw.util.DisplayableListItemViewer;
import macaw.util.DisplayableListItemDeleter;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class DisplayableListPanelTest {

private DisplayableListPanel displayableListPanel;
private UserInterfaceFactory userInterfaceFactory;
private DisplayableListParentForm parentForm;
private SessionProperties sessionProperties;
private Log log;

@Before
public void setUp() {
displayableListPanel = new DisplayableListPanel(new SessionProperties(), null, "Test Panel", false, false);
userInterfaceFactory = new UserInterfaceFactory();
sessionProperties = new SessionProperties();
log = new Log();
parentForm = new DisplayableListParentForm();
}

@Test
public void testConstructor() {
DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false);
assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
}

@Test
public void testInit() {
displayableListPanel.init(sessionProperties, parentForm, "Test Panel", false, false);
assertNotNull(displayableListPanel.getSelect());
assertNotNull(displayableListPanel.getAdd());
assertNotNull(displayableListPanel.getEdit());
assertNotNull(displayableListPanel.getView());
assertNotNull(displayableListPanel.getDelete());
}

@Test
public void testBuildUI() {
displayableListPanel.buildUI();
assertNotNull(displayableListPanel.getPanel());
}

@Test
public void testSetNewRecord() {
displayableListPanel.setNewRecord(true);
assertTrue(displayableListPanel.isNewRecord());
}

@Test
public void testSetDisplayableListItemSelector() {
DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
displayableListPanel.setDisplayableListItemSelector(listItemSelector);
assertEquals(listItemSelector, displayableListPanel.getListItemSelector());
}

@Test
public void testAddListItem() {
DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
displayableListPanel.setDisplayableListItemAdder(listItemAdder);
displayableListPanel.addListItem();
assertTrue(displayableListPanel.isNewRecord());
}

@Test(expected = MacawException.class)
public void testAddListItemMissingAdder() {
displayableListPanel.addListItem();
fail("An instance of DisplayableListItemAdder is expected to be set.");
}

@Test
public void testEditSelectedListItem() {
DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
Displayable displayable = new Displayable();
displayable.setDisplayName("Test Displayable");
displayableListPanel.setDisplayableListItemEditor(listItemEditor);
displayableListPanel.editSelectedListItem();
assertNotSame("Test Displayable", displayable.getDisplayName());
}

@Test(expected = MacawException.class)
public void testEditSelectedListItemMissingEditor() {
Displayable displayable = new Displayable();
displayable.setDisplayName("Test Displayable");
displayableListPanel.editSelectedListItem();
fail("An instance of DisplayableListItemEditor is expected to be set.");
}

@Test
public void testViewSelectedListItem() {
DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
Displayable displayable = new Displayable();
displayable.setDisplayName("Test Displayable");
displayableListPanel.setDisplayableListItemViewer(listItemViewer);
displayableListPanel.viewSelectedListItem();
assertEquals("Test Displayable", displayable.getDisplayName());
}

@Test(expected = MacawException.class)
public void testViewSelectedListItemMissingViewer() {
Displayable displayable = new Displayable();
displayable.setDisplayName("Test Displayable");
displayableListPanel.viewSelectedListItem();
fail("An instance of DisplayableListItemViewer is expected to be set.");
}

@Test
public void testDeleteSelectedListItems() {
DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
assertFalse(displayableListPanel.displayableList.isEmpty());
displayableListPanel.deleteSelectedListItems();
assertTrue(displayableListPanel.displayableList.isEmpty());
}

@Test(expected = MacawException.class)
public void testDeleteSelectedListItemsMissingDeleter() {
assertFalse(displayableListPanel.displayableList.isEmpty());
displayableListPanel.deleteSelectedListItems();
fail("An instance of DisplayableListItemDeleter is expected to be set.");
}

@Test
public void testSelectListItems() {
DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
displayableListPanel.setDisplayableListItemSelector(listItemSelector);
assertFalse(displayableListPanel.displayableList.isEmpty());
displayableListPanel.selectListItems();
assertTrue(displayableListPanel.displayableList.itemsAreSelected());
}

@Test
public void testUpdateButtonStates() {
Displayable displayable = new Displayable();
Displayable otherDisplayable = new Displayable();
displayableListPanel.displayableList.add(displayable);
displayableListPanel.displayableList.add(otherDisplayable);
displayableListPanel.setNewRecord(true);
assertTrue(displayableListPanel.getAdd().isEnabled());
assertTrue(displayableListPanel.getDelete().isEnabled());
assertTrue(displayableListPanel.getEdit().isEnabled());
assertTrue(displayableListPanel.getView().isEnabled());
assertTrue(displayableListPanel.getSelect().isEnabled());
displayableListPanel.setNewRecord(false);
assertFalse(displayableListPanel.getAdd().isEnabled());
assertFalse(displayableListPanel.getDelete().isEnabled());
assertFalse(displayableListPanel.getEdit().isEnabled());
assertTrue(displayableListPanel.getView().isEnabled());
assertTrue(displayableListPanel.getSelect().isEnabled());
}

@Test
public void testSetDisplayableItems() {
ArrayList<Displayable> masterList = new ArrayList<Displayable>();
masterList.add(displayable);
masterList.add(otherDisplayable);
displayableListPanel.setDisplayableItems(masterList);
assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
}

@Test
public void testSave() {
ArrayList<Displayable> masterList = new ArrayList<Displayable>();
masterList.add(displayable);
masterList.add(otherDisplayable);
displayableListPanel.setDisplayableItems(masterList);
displayableListPanel.save();
assertEquals(masterList, displayableListPanel.masterList);
}

@Test
public void testGetAllListItems() {
ArrayList<Displayable> displayableList = new ArrayList<Displayable>();
Displayable displayable = new Displayable();
displayableList.add(displayable);
displayableListPanel.displayableList = displayableList;
assertEquals(displayableList, displayableListPanel.getAllListItems());
}

@Test
public void testSetListOwnerName() {
displayableListPanel.setListOwnerName("Test Owner");
assertEquals("Test Owner", displayableListPanel.listOwnerName);
}

@Test
public void testActionPerformed() {
DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
DisplayableList displayableList = new DisplayableList();
displayableListPanel.displayableList = displayableList;
displayableListPanel.setDisplayableListItemSelector(listItemSelector);
displayableListPanel.setDisplayableListItemAdder(listItemAdder);
displayableListPanel.setDisplayableListItemEditor(listItemEditor);
displayableListPanel.setDisplayableListItemViewer(listItemViewer);
displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
ActionEvent actionEvent = new ActionEvent(displayableListPanel, 0, "test");
displayableListPanel.actionPerformed(actionEvent);
if (actionEvent.getSource() == displayableListPanel.getSelect()) {
listItemSelector.selectListItems("Test Owner", displayableList);
assertTrue(displayableList.itemsAreSelected());
}
if (actionEvent.getSource() == displayableListPanel.getAdd()) {
listItemAdder.addItems("Test Owner", displayableList);
assertTrue(displayableList.size() > 0);
}
if (actionEvent.getSource() == displayableListPanel.getEdit()) {
Displayable selectedDisplayable = displayableList.get(0);
listItemEditor.editListItem("Test Owner", selectedDisplayable, displayableList);
assertNotSame(selectedDisplayable.getDisplayName(), displayableList.get(0).getDisplayName());
}
if (actionEvent.getSource() == displayableListPanel.getView()) {
Displayable selectedDisplayable = displayableList.get(0);
listItemViewer.viewListItem(selectedDisplayable);
assertEquals(selectedDisplayable.getDisplayName(), listItemViewer.getDisplayName());
}
if (actionEvent.getSource() == displayableListPanel.getDelete()) {
listItemDeleter.deleteListItems("Test Owner", displayableList);
assertTrue(displayableList.isEmpty());
}
}
}