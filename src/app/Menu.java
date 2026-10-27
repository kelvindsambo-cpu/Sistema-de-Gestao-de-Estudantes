package app;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import dados.Dados;
import dados.LeitorCSV;
import desempenho.Desempenho;
import estatistica.EstatisticaDasMedias;
import estatistica.EstatisticaDeIdade;
import estatistica.EstatisticaGeral;
import estatistica.EstatisticaHashTable;
import estatistica.EstatisticaPorAno;
import estatistica.EstatisticaPorCurso;
import estatistica.EstatisticaPorSexo;
import hash.CodigoDuplicadoException;
import hash.TabelaHash;
import model.Estudante;
import utils.EstadoValidacao;
import utils.Resposta;
import utils.Rounder;
import utils.Validacao;

public class Menu {

private final BufferedReader entrada;
private Dados dados;     // resultado da última leitura do CSV
private TabelaHash tabela;

public Menu() {
    entrada = new BufferedReader(new InputStreamReader(System.in));
    dados = null;
    tabela = null;
}

public void executar() throws IOException {

    int opcao;

    do {
        apresentarMenu();
        try {
            opcao = lerInteiro("Opcao: ");
        } catch (EOFException e) {
            opcao = 0; // fim da entrada (ex.: Ctrl+Z / Ctrl+D)
        }

        try {
            switch (opcao) {

                case 1:
                    carregarCSV();
                    break;

                case 2:
                    pesquisarEstudante();
                    break;

                case 3:
                    inserirEstudante();
                    break;

                case 4:
                    removerEstudante();
                    break;

                case 5:
                    mostrarEstatisticas();
                    break;

                case 6:
                    mostrarEstatisticasHash();
                    break;

                case 7:
                    executarDesempenho();
                    break;

                case 8:
                    compararPesquisas();
                    break;

                case 9:
                    demonstrarSondagem();
                    break;

                case 0:
                    System.out.println("Sistema terminado!");
                    break;

                default:
                    System.out.println("Opcao invalida!");
            }
        } catch (EOFException e) {
            System.out.println("\nSistema terminado!");
            opcao = 0;
        }

    } while (opcao != 0);
}

private void apresentarMenu() {

    System.out.println("\n================================================");
    System.out.println("       SISTEMA DE GESTAO DE ESTUDANTES");
    System.out.println("================================================");
    System.out.println("1 - Carregar ficheiro CSV");
    System.out.println("2 - Pesquisar estudante");
    System.out.println("3 - Inserir estudante");
    System.out.println("4 - Remover estudante");
    System.out.println("5 - Estatisticas dos estudantes");
    System.out.println("6 - Estatisticas da Hash Table");
    System.out.println("7 - Executar teste de desempenho");
    System.out.println("8 - Comparar pesquisa sequencial e Hash");
    System.out.println("9 - Demonstrar colisao e REMOVIDO (sondagem)");
    System.out.println("0 - Sair");
    System.out.println("================================================");
}

// Lança EOFException quando a entrada termina, em vez de devolver null.
private String lerLinha() throws IOException {

    String linha = entrada.readLine();

    if (linha == null) {
        throw new EOFException();
    }

    // O PowerShell pode enviar um BOM (﻿) no início quando a entrada vem de um pipe.
    return linha.replace("﻿", "").trim();
}

private int lerInteiro(String mensagem) throws IOException {

    while (true) {
        System.out.print(mensagem);

        try {
            return Integer.parseInt(lerLinha());
        } catch (NumberFormatException e) {
            System.out.println("Introduza um numero inteiro valido.");
        }
    }
}

private double lerDouble(String mensagem) throws IOException {

    while (true) {
        System.out.print(mensagem);

        try {
            String valor = lerLinha().replace(',', '.');
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            System.out.println("Introduza um numero valido.");
        }
    }
}

private String lerTexto(String mensagem) throws IOException {

    System.out.print(mensagem);
    return lerLinha();
}

private boolean verificarTabela() {

    if (tabela == null) {
        System.out.println("Primeiro, carregue o ficheiro CSV na opcao 1.");
        return false;
    }

    return true;
}

// OPCao 1: carregar o ficheiro CSV

private void carregarCSV() {

    System.out.println("\n--- CARREGAR FICHEIRO CSV ---");

    LeitorCSV leitor = new LeitorCSV();
    dados = leitor.lerDados();
    tabela = dados.getTabelaHash();

    System.out.println("Leitura do ficheiro concluida!");
    System.out.println("Linhas lidas: " + dados.getLidas());
    System.out.println("Estudantes inseridos: " + dados.getInseridos());
    System.out.println("Registos validos: " + dados.getValidos());
    System.out.println("Registos invalidos: " + dados.getInvalidos());
    System.out.println("Registos duplicados: " + dados.getDuplicados());

    // O enunciado pede o relatório estatístico automaticamente após a leitura.
    System.out.println();
    imprimirRelatorioEstatistico();
}

// OPCao 2: pesquisar estudante

private void pesquisarEstudante() throws IOException {

    System.out.println("\n--- PESQUISAR ESTUDANTE ---");

    if (!verificarTabela()) {
        return;
    }

    String codigo = lerTexto("Codigo do estudante: ");

    Estudante estudante = tabela.pesquisar(codigo);

    if (estudante != null) {
        System.out.println("Estudante encontrado:");
        System.out.println(estudante);
    } else {
        System.out.println("Estudante nao encontrado.");
    }
    System.out.println("Posicoes sondadas: " + tabela.getUltimasSondagens());
}

// OPCao 3: inserir estudante

private void inserirEstudante() throws IOException {

    System.out.println("\n--- INSERIR ESTUDANTE ---");

    if (!verificarTabela()) {
        return;
    }

    String codigo = lerTexto("Codigo: ");

    // Verifica logo o código, para não pedir os restantes dados em vão.
    if (tabela.pesquisar(codigo) != null) {
        System.out.println("Ja existe um estudante com esse codigo.");
        return;
    }

    String nome = lerTexto("Nome: ");
    String curso = lerTexto("Curso: ");
    int ano = lerInteiro("Ano (" + Validacao.ANO_MIN + " a " + Validacao.ANO_MAX + "): ");
    int idade = lerInteiro("Idade (" + Validacao.IDADE_MIN + " a " + Validacao.IDADE_MAX + "): ");
    String sexo = lerTexto("Sexo (M/F): ").toUpperCase();
    double media = lerDouble("Media (0 a 20): ");

    Estudante estudante = new Estudante(
            codigo, nome, curso, ano, idade, sexo, media
    );

    Resposta resposta = Validacao.validarEstudante(estudante);

    if (resposta.getEstado() != EstadoValidacao.VALIDO) {
        System.out.println("Nao foi possivel inserir: " + resposta.getMensagem());
        return;
    }

    try {
        int capacidadeAntes = tabela.getCapacidade();
        tabela.inserir(estudante);
        System.out.println("Estudante inserido com sucesso!");
        System.out.println("Factor de carga actual: "
                + Rounder.formatar(tabela.getFactorCarga(), 4));
        if (tabela.getCapacidade() != capacidadeAntes) {
            System.out.println("A tabela foi redimensionada de " + capacidadeAntes
                    + " para " + tabela.getCapacidade() + " posicoes.");
        }
    } catch (CodigoDuplicadoException e) {
        System.out.println("Ja existe um estudante com esse codigo.");
    } catch (IllegalArgumentException e) {
        System.out.println("Nao foi possivel inserir: " + e.getMessage());
    }
}

// OPCao 4: remover estudante

private void removerEstudante() throws IOException {

    System.out.println("\n--- REMOVER ESTUDANTE ---");

    if (!verificarTabela()) {
        return;
    }

    String codigo = lerTexto("Codigo do estudante a remover: ");

    boolean removido = tabela.remover(codigo);

    if (removido) {
        System.out.println("Estudante removido com sucesso! A posicao fica marcada como REMOVIDO.");
    } else {
        System.out.println("Estudante nao encontrado.");
    }
}

// OPCao 5: estatisticas dos estudantes

private void mostrarEstatisticas() {

    System.out.println("\n--- ESTATISTICAS DOS ESTUDANTES ---");

    if (!verificarTabela()) {
        return;
    }

    imprimirRelatorioEstatistico();
}

private void imprimirRelatorioEstatistico() {

    List<Estudante> estudantes = tabela.listar();

    System.out.println(new EstatisticaGeral(dados));

    if (estudantes.isEmpty()) {
        System.out.println("Nao existem estudantes para apresentar.");
        return;
    }

    System.out.println(new EstatisticaPorSexo(estudantes));
    System.out.println(new EstatisticaPorCurso(estudantes));
    System.out.println(new EstatisticaPorAno(estudantes));
    System.out.println(new EstatisticaDasMedias(estudantes));
    System.out.println(new EstatisticaDeIdade(estudantes));
}

// OPCao 6: estatisticas da tabela hash

private void mostrarEstatisticasHash() {

    System.out.println("\n--- ESTATISTICAS DA HASH TABLE ---");

    if (!verificarTabela()) {
        return;
    }

    System.out.println(new EstatisticaHashTable(tabela));
}

// OPCao 7: teste de desempenho (1000, 5000 e 10000 registos)

private void executarDesempenho() {

    System.out.println("\n--- TESTE DE DESEMPENHO ---");
    System.out.println("A executar (pode demorar alguns segundos)...\n");

    System.out.println(new Desempenho().relatorioDesempenho());
}

// OPCao 8: comparar pesquisa sequencial e Hash

private void compararPesquisas() {

    System.out.println("\n--- COMPARAR PESQUISAS ---");
    System.out.println("A executar (pode demorar alguns segundos)...\n");

    System.out.println(new Desempenho().relatorioComparacao());
    System.out.println("Nota: os tempos podem variar entre execucoes e computadores.");
}

// OPCao 9: demonstrar colisao e o funcionamento de REMOVIDO (teste da defesa)

private void demonstrarSondagem() throws IOException {

    System.out.println("\n--- DEMONSTRAR COLISAO E REMOVIDO ---");

    if (!verificarTabela()) {
        return;
    }

    String[] par = tabela.encontrarColisao();

    if (par != null) {
        System.out.println("Exemplo de colisao: " + par[1] + " tem a mesma posicao inicial (h1)");
        System.out.println("que " + par[0] + ", que ja la estava.");
        System.out.println("Para o teste: remova " + par[0] + " (opcao 4) e volte aqui com " + par[1] + ".");
    }

    String codigo = lerTexto("\nCodigo a analisar (Enter para voltar): ");

    if (codigo.isEmpty()) {
        return;
    }

    System.out.print(tabela.descreverSondagem(codigo));
}


}
