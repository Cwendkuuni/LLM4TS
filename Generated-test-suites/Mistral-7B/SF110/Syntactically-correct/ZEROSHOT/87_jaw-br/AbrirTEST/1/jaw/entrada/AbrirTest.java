package jaw.entrada.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;

import jaw.entrada.Abrir;
import jaw.entrada.Filtro;
import jaw.entrada.Filtro2;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.JanelaPrincipal;
import jaw.util.Main;
import jaw.util.Serializacao;
import jaw.util.SerializacaoXML;

public class AbrirTest {

 private Abrir abrir;
 private ProcessarEntidades form;
 private JFileChooser fileChooser;
 private JanelaPrincipal janelaPrincipal;
 private Main main;

 @Before
 public void setUp() {
 form = mock(ProcessarEntidades.class);
 fileChooser = mock(JFileChooser.class);
 janelaPrincipal = mock(JanelaPrincipal.class);
 main = mock(Main.class);
 when(main.configuracao.getCaminhoDosProjetos()).thenReturn(new File("path/to/projects"));

 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXmlValidArquivo() {
 final String xmlArquivo = "path/to/validXmlFile.jaw.xml";
 when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File(xmlArquivo));

 abrir = new Abrir(form, new Abrir.AbrirXML(fileChooser, Abrir.ABRIR_XML));

 assertEquals("", abrir.xml);
 assertEquals(form, abrir.form);

 FileReader leitor = mock(FileReader.class);
 when(leitor.readAllLines()).thenReturn(new String[]{"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <entidades>\n" +
 " <entidade id=\"1\" nome=\"teste\">\n" +
 " <campo1>teste</campo1>\n" +
 " </entidade>\n" +
 " </entidades>"});
 when(new File(xmlArquivo).exists()).thenReturn(true);
 when(new FileReader(xmlArquivo)).thenReturn(leitor);
 when(new SerializacaoXML().xml2Entidades(leitor)).thenReturn(form.getEntidades());

 abrir.abrirXML(xmlArquivo);

 assertEquals(form.getEntidades(), abrir.form.getEntidades());
 }

 @Test
 public void testAbrirXmlInvalidArquivo() {
 final String xmlArquivo = "path/to/invalidXmlFile.xml";
 when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File(xmlArquivo));
 when(new File(xmlArquivo).exists()).thenReturn(true);

 abrir = new Abrir(form, new Abrir.AbrirXML(fileChooser, Abrir.ABRIR_XML));
 abrir.abrirXML(xmlArquivo);

 assertEquals("", abrir.xml);
 assertEquals(form, abrir.form);

 JOptionPane mockJOptionPane = mock(JOptionPane.class);
 when(janelaPrincipal.getJanelaPrincipal()).thenReturn(janelaPrincipal);
 when(janelaPrincipal.showMessageDialog(null, anyString(), anyString(), 0)).thenReturn(mockJOptionPane);

 abrir.abrirXML(xmlArquivo);

 verify(mockJOptionPane).showMessageDialog(null, "Arquivo xml corrompido ou incompatível com aplicação!", "Importação XML", 0);
 }

 @Test
 public void testAbrirSerializadoValidArquivo() {
 final String serializadoArquivo = "path/to/validSerializadoFile.jaw";
 when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File(serializadoArquivo));

 abrir = new Abrir(form, new Abrir.Abrir(fileChooser, Abrir.ABRIR));

 assertEquals("", abrir.xml);
 assertEquals(form, abrir.form);

 when(new File(serializadoArquivo).exists()).thenReturn(true);
 when(form.setEntidades(any())).thenReturn(true);
 when(new Serializacao().abrir(anyString())).thenReturn(form.getEntidades());

 abrir.abrirSerializado(serializadoArquivo);

 assertEquals(form.getEntidades(), abrir.form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoInvalidArquivo() {
 final String serializadoArquivo = "path/to/invalidSerializadoFile.jaw";
 when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File(serializadoArquivo));

 abrir = new Abrir(form, new Abrir.Abrir(fileChooser, Abrir.ABRIR));
 abrir.abrirSerializado(serializadoArquivo);

 assertEquals("", abrir.xml);
 assertEquals(form, abrir.form);

 JOptionPane mockJOptionPane = mock(JOptionPane.class);
 when(janelaPrincipal.getJanelaPrincipal()).thenReturn(janelaPrincipal);
 when(janelaPrincipal.showMessageDialog(null, anyString(), anyString(), 0)).thenReturn(mockJOptionPane);

 abrir.abrirSerializado(serializadoArquivo);

 verify(mockJOptionPane).showMessageDialog(null, "Arquivo jaw corrompido ou incompatível com aplicação!", "Abrir Arquivo jaw", 0);
 }

 @Test
 public void testOpenFileDialogWithXmlFilter() {
 final String arquivoXml = "path/to/xmlFile.xml";
 when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File(arquivoXml));

 abrir = new Abrir(form, new Abrir.AbrirXML(fileChooser, Abrir.ABRIR_XML));

 assertEquals("", abrir.xml);
 assertEquals(form, abrir.form);

 abrir.abrirXML(arquivoXml);

 assertEquals(form.getEntidades(), abrir.form.getEntidades());
 }

 @Test
 public void testOpenFileDialogWithSerializadoFilter() {
 final String arquivoSerializado = "path/to/serializadoFile.jaw";
 when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File(arquivoSerializado));

 abrir = new Abrir(form, new Abrir.Abrir(fileChooser, Abrir.ABRIR));

 assertEquals("", abrir.xml);
 assertEquals(form, abrir.form);

 abrir.abrirSerializado(arquivoSerializado);

 assertEquals(form.getEntidades(), abrir.form.getEntidades());
 }
}