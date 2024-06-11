package jaw.entrada;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.metamodelo.Tipo;
import jaw.metamodelo.Variavel;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<>();
 }

 @Test
 public void testSalvarComTipoSalvar20EEntidades() {
 salvar = new Salvar(form, 20);
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 assertNotNull(salvar.xml);
 assertNotNull(salvar.entidade);
 assertFalse(salvar.entidade.isEmpty());
 }

 @Test
 public void testSalvarComTipoSalvar20EArquivoValido() {
 entidades.add(MetamodeloFactory.createEntidade("Teste", "Teste", new Tipo("int", "a"), new Tipo("String", "b")));
 salvar = new Salvar(form, 20);
 salvar.entidade = entidades;
 String arquivoValido = "teste.jaw";
 File file = new File(arquivoValido);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(file);
 salvar.fileChooser(escolhedor, form);
 assertTrue(salvar.vela);
 assertTrue(salvar.arquivo.equals(arquivoValido));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarComTipoSalvar20EArquivoInvalido() {
 entidades.add(MetamodeloFactory.createEntidade("Teste", "Teste", new Tipo("int", "a"), new Tipo("String", "b")));
 salvar = new Salvar(form, 20);
 salvar.entidade = entidades;
 String arquivoInvalido = "teste.txt";
 File file = new File(arquivoInvalido);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(file);
 salvar.fileChooser(escolhedor, form);
 fail("Deve lançar uma exceção de IllegalArgumnetException");
 }

 @Test(expected = IOException.class)
 public void testSalvarComTipoSalvar20EFalhaAoEscreverArquivo() {
 entidades.add(MetamodeloFactory.createEntidade("Teste", "Teste", new Tipo("int", "a"), new Tipo("String", "b")));
 salvar = new Salvar(form, 20);
 salvar.entidade = entidades;
 String arquivo = "teste.jaw";
 File file = new File(arquivo);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(file);
 salvar.fileChooser(escolhedor, form);
 salvar.salvar();
 fail("Deve lançar uma exceção de IOException");
 }

 @Test
 public void testSalvarComTipoSalvar10EEntidades() {
 salvar = new Salvar(form, 10);
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 assertNull(salvar.xml);
 assertNotNull(salvar.entidade);
 assertFalse(salvar.entidade.isEmpty());
 }

 @Test
 public void testSalvarComTipoSalvar10EFalhaAoEscreverArquivoXML() {
 entidades.add(MetamodeloFactory.createEntidade("Teste", "Teste", new Tipo("int", "a"), new Tipo("String", "b")));
 Tipo tipoInt = new Tipo("int", "a");
 Variavel variavelInt = new Variavel("x", tipoInt);
 Entidade entidadeInt = new Entidade("testeInt", "TesteInt", new Tipo("int", "a"), new Tipo("String", "b"), null, null, null, null, variavelInt);
 entidades.add(entidadeInt);
 salvar = new Salvar(form, 10);
 salvar.entidade = entidades;
 String arquivo = "teste.jaw.xml";
 File file = new File(arquivo);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(file);
 salvar.fileChooser(escolhedor, form);
 salvar.salvarXML();
 fail("Deve lançar uma exceção");
 }

 @After
 public void tearDown() {
 form = null;
 salvar = null;
 entidades = null;
 }

}