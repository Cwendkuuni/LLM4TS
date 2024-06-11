import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panel;

 @Before
 public void setUp() {
 model = new JTailModel();
 model.setContent("Line 1\nLine 2\nLine 3");
 panel = new JTailPanel(model);
 }

 @Test
 public void testInitialization() {
 assertEquals(0, panel.getTableModel().getRowCount());
 assertNotNull(panel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithNewContent() {
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, 0, 5));
 assertEquals(5, panel.getTableModel().getRowCount());
 }

 @Test
 public void testResize() {
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 JFrame frame = new JFrame();
 frame.setContentPane(panel);
 frame.pack();
 frame.setSize(300, 500);
 frame.setVisible(true);
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 100, 25));
 JTable table = (JTable) panel.getComponent(0);
 assertEquals(100, table.getColumnModel().getColumn(0).getPreferredWidth());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullModel() {
 panel.setModel(null);
 panel.update(null, new JTailModelEvent(null, JTailModelEvent.Type.RESIZED, 0, 0));
 }

 @Test
 public void testScrolling() {
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, 2, 0));
 JTable table = (JTable) panel.getComponent(0);
 assertEquals("Line 3", table.getValueAt(2, 1).toString());
 }

 @After
 public void tearDown() {
 model = null;
 panel = null;
 }

}