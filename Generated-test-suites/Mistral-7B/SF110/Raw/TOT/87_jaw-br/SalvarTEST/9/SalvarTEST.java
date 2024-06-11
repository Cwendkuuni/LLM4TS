package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private JFileChooser fileChooser;

 @Before
 public void setUp() {
 form = mock(ProcessarEntidades.class);
 fileChooser = mock(JFileChooser.class);
 salvar = new Salvar(form, 20);
 when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 }

 @After
 public void tearDown() {
 salvar = null;
 form = null;
 fileChooser = null;
 }

 @Test
 public void testSalvarJawFile() throws IOException {
 // Given
 List<Entidade> entidades = new ArrayList<Entidade>();
 Entidade entidade1 = new Entidade("Nome1", "Descricao1");
 Entidade entidade2 = new Entidade("Nome2", "Descricao2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 when(form.getEntidades()).thenReturn(entidades);

 // When
 Salvar salvar = new Salvar(form, 20);
 salvar.vela = true;
 JFileChooser escolhedor = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(escolhedor);
 when(escolhedor.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 salvar.arquivo = "path/to/save/myfile.jaw";
 salvar.save();

 // Then
 verify(fileChooser, times(1)).showSaveDialog(form);
 File expectedFile = new File("path/to/save/myfile.jaw");
 verify(form, times(1)).setNomeArquivoSalvo("myfile.jaw");
 Serializacao serializacao = new Serializacao();
 assertTrue(serializacao.salvar(entidades, expectedFile.getAbsolutePath()));
 }

 @Test
 public void testSalvarXmlFile() throws IOException {
 // Given
 List<Entidade> entidades = new ArrayList<Entidade>();
 Entidade entidade1 = new Entidade("Nome1", "Descricao1");
 Entidade entidade2 = new Entidade("Nome2", "Descricao2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 when(form.getEntidades()).thenReturn(entidades);

 // When
 Salvar salvar = new Salvar(form, 10);
 salvar.arquivo = "path/to/save/myfile.jaw.xml";
 salvar.saveAsXml();

 // Then
 File expectedFile = new File("path/to/save/myfile.jaw.xml");
 verify(form, times(1)).setNomeArquivoSalvo("myfile.jaw.xml");
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 String xml = serializacaoXML.entidades2XML(entidades);
 FileWriter escrevedor = mock(FileWriter.class);
 try {
 when(form.getFileWriter()).thenReturn(escrevedor);
 when(escrevedor.write(xml)).thenReturn(xml.length());
 salvar.xml = xml;
 salvar.saveAsXml();
 }
 catch (IOException e) {
 fail("Expected no exception");
 }
 File actualFile = new File("path/to/save/myfile.jaw.xml");
 assertTrue(actualFile.exists());
 assertEquals(xml, Files.readAllStrings(actualFile.toPath()));
 }

 @Test
 public void testSalvarWithEmptyName() {
 // Given
 when(form.getName()).thenReturn("");

 // When
 Salvar salvar = new Salvar(form, 20);
 salvar.vela = true;
 salvar.save();

 // Then
 verify(form, never()).setNomeArquivoSalvo("anyName.jaw");
 }

 @Test
 public void testSalvarWithNullFileChooser() {
 // Given
 when(form.getFileChooser()).thenReturn(null);
 salvar.vela = true;
 salvar.save();

 // Then
 verify(form, never()).setNomeArquivoSalvo("anyName.jaw");
 }

 @Test
 public void testSalvarWithoutFileSelection() {
 // Given
 when(fileChooser.getSelectedFile()).thenReturn(null);
 salvar.vela = true;
 salvar.save();

 // Then
 verify(form, never()).setNomeArquivoSalvo("anyName.jaw");
 }

 @Test
 public void testSalvarWithInvalidExtension() {
 // Given
 when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/myfile.txt"));
 salvar.vela = true;
 salvar.save();

 // Then
 verify(form, never()).setNomeArquivoSalvo("myfile.jaw");
 }
}