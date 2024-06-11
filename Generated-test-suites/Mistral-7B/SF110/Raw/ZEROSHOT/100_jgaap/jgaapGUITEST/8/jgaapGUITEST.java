import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;

public class jgaapGUITest {

 private jgaapGUI frame;

 @Before
 public void setUp() {
 try {
 frame = new jgaapGUI();
 JFrame f = new JFrame();
 f.setContentPane(frame);
 f.pack();
 f.setVisible(true);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @After
 public void tearDown() {
 if (frame != null) {
 frame.dispose();
 }
 }

 @Test
 public void testInitComponents() {
 assertNotNull(frame.menuBar);
 assertNotNull(frame.menuFile);
 assertNotNull(frame.menuFileExit);
 assertNotNull(frame.menuFileDemo);
 assertNotNull(frame.menuHelp);
 assertNotNull(frame.menuHelpContents);
 assertNotNull(frame.menuHelpAbout);
 assertNotNull(frame.paneOpts);
 assertNotNull(frame.panelAcquire);
 assertNotNull(frame.panelDocs);
 assertNotNull(frame.labelKnown);
 assertNotNull(frame.scrollPane1);
 assertNotNull(frame.listKnown);
 assertNotNull(frame.labelUnknown);
 assertNotNull(frame.scrollPane2);
 assertNotNull(frame.listUnknown);
 assertNotNull(frame.panelOpts);
 assertNotNull(frame.radioKnown);
 assertNotNull(frame.radioUnknown);
 assertNotNull(frame.buttonLocal);
 assertNotNull(frame.panelCanonicize);
 assertNotNull(frame.panelCOpts);
 assertNotNull(frame.canonicizers);
 assertNotNull(frame.eventifiers);
 assertNotNull(frame.esButtonGroup);
 assertNotNull(frame.panelProgress);
 assertNotNull(frame.buttonCanonicize);
 assertNotNull(frame.label1);
 assertNotNull(frame.progressCanonicize);
 assertNotNull(frame.labelCanon);
 assertNotNull(frame.panelAnalyze);
 assertNotNull(frame.labelStatInf);
 assertNotNull(frame.panelMethods);
 assertNotNull(frame.radioCrossEnt);
 assertNotNull(frame.radioLZW);
 assertNotNull(frame.panelAnalProg);
 assertNotNull(frame.buttonAnalyze);
 assertNotNull(frame.labelAnalProg);
 assertNotNull(frame.progressAnalysis);
 assertNotNull(frame.scrollPane3);
 assertNotNull(frame.listResults);
 assertNotNull(frame.buttonEventSet);
 assertNotNull(frame.paneEvProgress);
 assertNotNull(frame.labelevSet);
 assertNotNull(frame.progressEventSet);
 assertNotNull(frame.panelEventSet);
 assertNotNull(frame.panelEOpts);
 assertNotNull(frame.panelEvProgress);
 }

 @Test
 public void testActionPerformed() {
 // Add test cases for actionPerformed method here
 }

 @Test
 public void testLoadDemo() {
 // Add test cases for loadDemo method here
 }
}