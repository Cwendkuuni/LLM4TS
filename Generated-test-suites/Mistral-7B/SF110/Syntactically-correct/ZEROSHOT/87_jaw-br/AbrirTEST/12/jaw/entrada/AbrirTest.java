package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXmlWithValidFile() {
 // Given: a valid XML file
 String xmlFilePath = "src/test/resources/validxmlfile.jaw.xml";
 File xmlFile = new File(xmlFilePath);
 // When: the Abrir class is instantiated with the XML file
 abrir = new Abrir(form, xmlFile.getAbsolutePath());
 // Then: the XML file is read and the Entidades property is set
 assertEquals("validxmlfile.jaw.xml", form.getNomeArquivoSalvo());
 assertNotNull(form.getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXmlWithInvalidFile() {
 // Given: an invalid XML file
 String xmlFilePath = "src/test/resources/invalidxmlfile.txt";
 File xmlFile = new File(xmlFilePath);
 // When: the Abrir class is instantiated with the invalid XML file
 abrir = new Abrir(form, xmlFile.getAbsolutePath());
 // Then: an IllegalArgumentException is thrown
 }

 @Test(expected = FileNotFoundException.class)
 public void testOpenFileChooserWithInvalidFilterAndNoFileSelected() {
 // Given: an instance of Abrir with a FileChooser and no file selected
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = ((JFileChooser) abrir.form.getJFileChooser());
 // When: the Open button is clicked without selecting a file
 chooser.setSelectedFile(null);
 chooser.showOpenDialog(null);
 // Then: no file is opened and no exception is thrown
 assertNull(form.getNomeArquivoSalvo());
 assertNull(form.getEntidades());
 }

 @Test
 public void testOpenFileChooserWithValidFileAndXmlFilter() {
 // Given: an instance of Abrir with a FileChooser and a valid XML file
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = ((JFileChooser) abrir.form.getJFileChooser());
 // When: the Open button is clicked with a valid XML file selected
 File xmlFile = new File("src/test/resources/validxmlfile.jaw.xml");
 chooser.setSelectedFile(xmlFile);
 chooser.showOpenDialog(null);
 // Then: the XML file is opened and the Entidades property is set
 assertEquals("validxmlfile.jaw.xml", form.getNomeArquivoSalvo());
 assertNotNull(form.getEntidades());
 }

 @Test(expected = FileNotFoundException.class)
 public void testOpenFileChooserWithInvalidFileAndXmlFilter() {
 // Given: an instance of Abrir with a FileChooser and an invalid XML file
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = ((JFileChooser) abrir.form.getJFileChooser());
 // When: the Open button is clicked with an invalid XML file selected
 File invalidXmlFile = new File("src/test/resources/invalidxmlfile.txt");
 chooser.setSelectedFile(invalidXmlFile);
 chooser.showOpenDialog(null);
 // Then: no file is opened and a FileNotFoundException is thrown
 assertEquals("src/test/resources/invalidxmlfile.txt", chooser.getSelectedFile().getAbsolutePath());
 assertNull(form.getEntidades());
 }

 @Test(expected = FileNotFoundException.class)
 public void testOpenFileChooserWithInvalidFilterAndInvalidFile() {
 // Given: an instance of Abrir with a FileChooser and an invalid file
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = ((JFileChooser) abrir.form.getJFileChooser());
 // When: the Open button is clicked with an invalid file selected
 File invalidFile = new File("src/test/resources/invalidfile.txt");
 chooser.setSelectedFile(invalidFile);
 chooser.showOpenDialog(null);
 // Then: no file is opened and a FileNotFoundException is thrown
 assertNull(form.getNomeArquivoSalvo());
 assertNull(form.getEntidades());
 }

 @Test
 public void testOpenFileChooserWithValidFileAndSerializacaoFilter() {
 // Given: an instance of Abrir with a FileChooser and a valid JAW file
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = ((JFileChooser) abrir.form.getJFileChooser());
 // When: the Open button is clicked with a valid JAW file selected
 File jawFile = new File("src/test/resources/validfile.jaw");
 chooser.setSelectedFile(jawFile);
 chooser.showOpenDialog(null);
 // Then: the JAW file is opened and the Entidades property is set
 assertEquals("validfile.jaw", form.getNomeArquivoSalvo());
 assertNotNull(form.getEntidades());
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 // Given: an instance of Abrir with an invalid JAW file
 abrir = new Abrir(form, "src/test/resources/invalidfile.txt");
 // When: the abrirSerializado method is called
 abrir.abrirSerializado("src/test/resources/invalidfile.txt");
 // Then: an IOException is thrown
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 // Given: an instance of Abrir with a valid JAW file
 abrir = new Abrir(form, "src/test/resources/validfile.jaw");
 // When: the abrirSerializado method is called
 abrir.abrirSerializado("src/test/resources/validfile.jaw");
 // Then: the Entidades property is set
 assertNotNull(form.getEntidades());
 }

 @Test
 public void testAbrirXmlWithValidFileAndFiltro2Filter() {
 // Given: an instance of Abrir with a FileChooser and a valid XML file
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = ((JFileChooser) abrir.form.getJFileChooser());
 // When: the Open button is clicked with a valid XML file selected
 File xmlFile = new File("src/test/resources/validxmlfile.jaw.xml");
 chooser.setSelectedFile(xmlFile);
 chooser.showOpenDialog(null);
 // Then: the XML file is opened and the Entidades property is set
 assertEquals("validxmlfile.jaw.xml", form.getNomeArquivoSalvo());
 assertNotNull(form.getEntidades());
 }

 @Test(expected = FileFilter.java.lang.NullPointerException.class)
 public void testFiltroConstructorWithNull() {
 // Given: a null FileFilter
 // When: a new instance of Filtro is created
 new Abrir().new Filtro(null);
 // Then: a NullPointerException is thrown
 }

 @Test
 public void testFiltroAcceptWithDirectory() {
 // Given: an instance of Filtro
 FileFilter filtro = new Abrir().new Filtro();
 // When: the accept method is called with a directory
 File directory = new File("src/test/resources");
 // Then: the method returns true
 assertTrue(filtro.accept(directory));
 }

 @Test
 public void testFiltroAcceptWithValidXmlFile() {
 // Given: an instance of Filtro
 FileFilter filtro = new Abrir().new Filtro();
 // Given: a valid XML file
 File xmlFile = new File("src/test/resources/validxmlfile.jaw.xml");
 // When: the accept method is called with the valid XML file
 // Then: the method returns true
 assertTrue(filtro.accept(xmlFile));
 }

 @Test
 public void testFiltroAcceptWithInvalidFile() {
 // Given: an instance of Filtro
 FileFilter filtro = new Abrir().new Filtro();
 // Given: an invalid file
 File invalidFile = new File("src/test/resources/invalidfile.txt");
 // When: the accept method is called with the invalid file
 // Then: the method returns false
 assertFalse(filtro.accept(invalidFile));
 }

 @Test
 public void testFiltroGetDescriptionWithXmlFilter() {
 // Given: an instance of Filtro
 FileFilter filtro = new Abrir().new Filtro();
 // When: the getDescription method is called
 // Then: the method returns "XML JAW files"
 assertEquals("XML JAW files", filtro.getDescription());
 }

 @Test
 public void testFiltroGetDescriptionWithSerializacaoFilter() {
 // Given: an instance of Filtro
 FileFilter filtro = new Abrir().new Filtro();
 // Given: a Serializacao filter
 FileFilter serializacaoFilter = new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 };
 // When: the getDescription method is called
 // Then: the method returns "JAW files"
 assertEquals("JAW files", filtro.getDescription());
 }
}