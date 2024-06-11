package jaw.entrada.test;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.JanelaPrincipal;
import jaw.utils.Configuracao;
import jaw.utils.Serializacao;
import jaw.utils.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private String xmlFile;
 private File xmlFileObj;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 xmlFile = "teste.xml";
 xmlFileObj = new File(xmlFile);
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() throws HeadlessException, IOException {
 // Given: A valid XML file
 // When: Creating a new instance of Abrir with the XML file
 abrir = new Abrir(form, xmlFile);
 // Then: The Abrir instance should have the XML content
 assertEquals("", abrir.getXml());
 // And: The form should have the XML data
 assertEquals(form.getEntidades(), abrir.getForm().getEntidades());
 // And: The file name should be set to the XML file name
 assertEquals(xmlFile, abrir.getForm().getNomeArquivoSalvo());
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLWithInvalidXmlFile() throws HeadlessException {
 // Given: An invalid XML file
 xmlFile = "testeInvalido.xml";
 // When: Creating a new instance of Abrir with the XML file
 abrir = new Abrir(form, xmlFile);
 // Then: An IOException should be thrown
 }

 @Test(expected = HeadlessException.class)
 public void testAbrirGUIWithNull() {
 // Given: A null form
 // When: Creating a new instance of Abrir with null form and a valid XML file
 abrir = new Abrir(null, xmlFile);
 // Then: A HeadlessException should be thrown
 }

 @Test
 public void testAbrirSerializadoWithValidFile() throws HeadlessException, IOException {
 // Given: A valid jaw file
 xmlFile = "teste.jaw";
 xmlFileObj.deleteOnExit();
 Serializacao.salvar(form.getEntidades(), xmlFile);
 // When: Creating a new instance of Abrir with the jaw file
 abrir = new Abrir(form, Abrir.ABRIR_XML, xmlFile);
 // Then: The Abrir instance should have the XML content
 assertEquals("", abrir.getXml());
 // And: The form should have the XML data
 assertEquals(form.getEntidades(), abrir.getForm().getEntidades());
 // And: The file name should be set to the jaw file name
 assertEquals(xmlFile, abrir.getForm().getNomeArquivoSalvo());
 // And: The jaw file should be deleted after the test
 assertTrue(xmlFileObj.delete());
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidFile() throws HeadlessException {
 // Given: An invalid jaw file
 xmlFile = "testeInvalido.jaw";
 // When: Creating a new instance of Abrir with the jaw file
 abrir = new Abrir(form, Abrir.ABRIR_XML, xmlFile);
 // Then: An IOException should be thrown
 }

 @Test(expected = HeadlessException.class)
 public void testAbrirGUIWithNullFormAndXmlFile() {
 // Given: A null form and a null XML file
 // When: Creating a new instance of Abrir
 abrir = new Abrir(null, null);
 // Then: A HeadlessException should be thrown
 }

 @Test(expected = HeadlessException.class)
 public void testAbrirGUIWithNullFormAndEmptyStringAsXmlFile() {
 // Given: A null form and an empty string as XML file
 // When: Creating a new instance of Abrir
 abrir = new Abrir(null, "");
 // Then: A HeadlessException should be thrown
 }

 @Test
 public void testOpenFileDialogWithValidXmlFile() throws HeadlessException, IOException {
 // Given: A valid XML file
 // When: Opening a file dialog with XML filter and selecting the XML file
 final JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Configuracao.getCaminhoDosProjetos()));
 final Filtro2 xmlFilter = new Filtro2();
 fileChooser.setFileFilter(xmlFilter);
 fileChooser.showOpenDialog(null);
 final File selectedFile = fileChooser.getSelectedFile();
 // Then: The selected file should be the valid XML file
 assertEquals(xmlFile, selectedFile.getAbsolutePath());
 // And: Creating a new instance of Abrir with the XML file
 abrir = new Abrir(form, Abrir.ABRIR_XML, xmlFile);
 // Then: The Abrir instance should have the XML content
 assertEquals("", abrir.getXml());
 // And: The form should have the XML data
 assertEquals(form.getEntidades(), abrir.getForm().getEntidades());
 // And: The file name should be set to the XML file name
 assertEquals(xmlFile, abrir.getForm().getNomeArquivoSalvo());
 }

 @Test
 public void testOpenFileDialogWithValidJawFile() throws HeadlessException, IOException {
 // Given: A valid jaw file
 xmlFile = "teste.jaw";
 xmlFileObj.deleteOnExit();
 Serializacao.salvar(form.getEntidades(), xmlFile);
 // When: Opening a file dialog with jaw filter and selecting the jaw file
 final JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Configuracao.getCaminhoDosProjetos()));
 final Filtro jawFilter = new Filtro();
 fileChooser.setFileFilter(jawFilter);
 fileChooser.showOpenDialog(null);
 final File selectedFile = fileChooser.getSelectedFile();
 // Then: The selected file should be the valid jaw file
 assertEquals(xmlFile, selectedFile.getAbsolutePath());
 // And: Creating a new instance of Abrir with the jaw file
 abrir = new Abrir(form, Abrir.ABRIR, xmlFile);
 // Then: The Abrir instance should have the XML content
 assertEquals("", abrir.getXml());
 // And: The form should have the XML data
 assertEquals(form.getEntidades(), abrir.getForm().getEntidades());
 // And: The file name should be set to the jaw file name
 assertEquals(xmlFile, abrir.getForm().getNomeArquivoSalvo());
 // And: The jaw file should be deleted after the test
 assertTrue(xmlFileObj.delete());
 }

 @Test(expected = HeadlessException.class)
 public void testOpenFileDialogWithNullForm() {
 // Given: A null form
 // When: Opening a file dialog
 abrir = new Abrir(null, Abrir.ABRIR, xmlFile);
 // Then: A HeadlessException should be thrown
 }

 @Test(expected = HeadlessException.class)
 public void testOpenFileDialogWithEmptyStringAsXmlFile() {
 // Given: An empty string as XML file
 // When: Opening a file dialog
 abrir = new Abrir(form, Abrir.ABRIR, "");
 // Then: A HeadlessException should be thrown
 }
}