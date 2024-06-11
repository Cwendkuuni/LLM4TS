package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import javax.swing.filechooser.*;
import javax.swing.*;
import org.junit.*;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private Filtro2 filtroXML;
 private Filtro filtroJAW;
 private JFileChooser fileChooser;

 @Before
 public void setUp() {
 form = mock(ProcessarEntidades.class);
 salvar = new Salvar(form, 10);
 filtroXML = new Filtro2();
 filtroJAW = new Filtro();
 fileChooser = mock(JFileChooser.class);
 }

 @Test
 public void testSalvarXML() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 10);
 salvar.entidade = entidades;
 when(form.getNomeArquivoSalvo()).thenReturn("");
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 File arquivo = new File("teste.jaw.xml");
 when(fileChooser.getSelectedFile()).thenReturn(arquivo);
 salvar.arquivo = arquivo.getAbsolutePath();

 SerializacaoXML serializaXML = mock(SerializacaoXML.class);
 when(serializaXML.entidades2XML(entidades)).thenReturn("XML Teste");
 Serializacao serializacao = mock(Serializacao.class);
 when(serializaXML.getSerializacao()).thenReturn(serializacao);
 when(salvar.new Serializacao()).thenReturn(serializacao);

 try {
 FileWriter escrevedor = mock(FileWriter.class);
 salvar.salvarXML(entidades, "teste.jaw.xml");
 verify(escrevedor).write("XML Teste");
 verify(escrevedor).flush();
 verify(escrevedor).close();
 } catch (Exception e) {
 fail();
 }
 }

 @Test
 public void testSalvarArquivoComoNaoJaw() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 20);
 salvar.entidade = entidades;
 when(form.getNomeArquivoSalvo()).thenReturn("");
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 File arquivo = new File("teste.txt");
 when(fileChooser.getSelectedFile()).thenReturn(arquivo);

 try {
 salvar.salvar(entidades, "teste.txt");
 fail();
 } catch (Exception e) {
 verify(form).setNomeArquivoSalvo("teste.jaw");
 }
 }

 @Test
 public void testSalvarArquivoJaw() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 30);
 salvar.entidade = entidades;
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");
 File arquivo = new File("teste.jaw");

 Serializacao serializacao = mock(Serializacao.class);
 when(salvar.new Serializacao()).thenReturn(serializacao);

 try {
 salvar.salvar(entidades, "teste.jaw");
 verify(serializacao).salvar(entidades, "teste.jaw");
 } catch (Exception e) {
 fail();
 }
 }

 @Test
 public void testSalvarSemNomeArquivo() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 10);
 salvar.entidade = entidades;
 when(form.getNomeArquivoSalvo()).thenReturn("");
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

 try {
 salvar.salvarXML(entidades, "");
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSalvarSemArquivo() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());salvar = new Salvar(form, 20);
 salvar.entidade = entidades;
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

 try {
 salvar.salvar(entidades, "teste.jaw");
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSalvarSemEntidades() {
 Salvar salvar = new Salvar(form, 20);
 salvar.arquivo = "teste.jaw";
 try {
 salvar.salvar(null, "teste.jaw");
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSalvarSemFileChooser() {
 Salvar salvar = new Salvar(form, 20);
 salvar.entidade = Arrays.asList(new Entidade(), new Entidade());
 try {
 salvar.salvar(entidades, "teste.jaw");
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSalvarComNomeArquivoExistente() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 30);
 salvar.entidade = entidades;
 File arquivo = new File("teste.jaw");
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(arquivo);

 try {
 salvar.salvar(entidades, arquivo.getAbsolutePath());
 fail();
 } catch (Exception e) {
 verify(form).setNomeArquivoSalvo("teste_copy.jaw");
 }
 }

 @Test
 public void testSalvarComNomeArquivoInvalido() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 30);
 salvar.entidade = entidades;
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 File arquivo = new File("teste~.jaw");
 when(fileChooser.getSelectedFile()).thenReturn(arquivo);

 try {
 salvar.salvar(entidades, arquivo.getAbsolutePath());
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSalvarComFormatoInvalido() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 30);
 salvar.entidade = entidades;
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 File arquivo = new File("teste.txt");
 when(fileChooser.getSelectedFile()).thenReturn(arquivo);

 try {
 salvar.salvar(entidades, arquivo.getAbsolutePath());
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSalvarComNomeArquivoNulo() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 30);
 salvar.entidade = entidades;
 when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 File arquivo = null;

 try {
 salvar.salvar(entidades, arquivo);
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSalvarComArquivoNulo() {
 List<Entidade> entidades = Arrays.asList(new Entidade(), new Entidade());
 salvar = new Salvar(form, 30);
 salvar.entidade = entidades;
 salvar.arquivo = null;

 try {
 salvar.salvar(entidades, "");
 fail();
 } catch (Exception e) {
 // expected
 }
 }
}