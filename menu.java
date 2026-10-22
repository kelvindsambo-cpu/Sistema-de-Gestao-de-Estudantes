package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import dados.Dados;
import dados.LeitorCSV;
import desempenho.Desempenho;
import estatistica.EstatisticaHashTable;
import hash.CodigoDuplicadoException;
import hash.TabelaHash;
import model.Estudante;
import utils.EstadoValidacao;
import utils.Resposta;
import utils.Validacao;

public class menu {

private final BufferedReader entrada;
private TabelaHash tabela;
private int totalInvalidos;
private int totalDuplicados;

public menu() {
    entrada = new BufferedReader(new InputStreamReader(System.in));
    tabela = null;
    totalInvalidos = 0;
    totalDuplicados = 0;
}

public void executar() throws IOException {

    int opcao;

    do {
        apresentarMenu();
        opcao = lerInteiro("Opcao: ");

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

            case 0:
                System.out.println("Sistema terminado!");
                break;

            default:
                System.out.println("Opcao invalida!");
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
    System.out.println("0 - Sair");
    System.out.println("================================================");
}

private int lerInteiro(String mensagem) throws IOException {

    while (true) {
        System.out.print(mensagem);

        try {
            return Integer.parseInt(entrada.readLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Introduza um numero inteiro valido.");
        }
    }
}

private double lerDouble(String mensagem) throws IOException {

    while (true) {
        System.out.print(mensagem);

        try {
            String valor = entrada.readLine().trim().replace(',', '.');
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            System.out.println("Introduza um numero valido.");
        }
    }
}

private String lerTexto(String mensagem) throws IOException {

    System.out.print(mensagem);
    return entrada.readLine().trim();
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
    Dados dados = leitor.lerDados();

    tabela = dados.getTabelaHash();
    totalInvalidos = dados.getInvalidos();
    totalDuplicados = dados.getDuplicados();

    System.out.println("Leitura do ficheiro concluida!");
    System.out.println("Linhas lidas: " + dados.getLidas());
    System.out.println("Estudantes inseridos: " + dados.getInseridos());
    System.out.println("Registos validos: " + dados.getValidos());
    System.out.println("Registos invalidos: " + totalInvalidos);
    System.out.println("Registos duplicados: " + totalDuplicados);
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
}

// OPCao 3: inserir estudante

private void inserirEstudante() throws IOException {

    System.out.println("\n--- INSERIR ESTUDANTE ---");

    if (!verificarTabela()) {
        return;
    }

    String codigo = lerTexto("Codigo: ");
    String nome = lerTexto("Nome: ");
    String curso = lerTexto("Curso: ");
    int ano = lerInteiro("Ano (1 a 4): ");
    int idade = lerInteiro("Idade (17 a 70): ");
    String sexo = lerTexto("Sexo (M/F): ");
    double media = lerDouble("Media (0 a 20): ");

    Estudante estudante = new Estudante(
            codigo, nome, curso, ano, idade, sexo, media
    );

    Resposta resposta = Validacao.validarEstudante(estudante);

    if (resposta.getEstado() != EstadoValidacao.VALIDO) {
        System.out.println(resposta.getMensagem());
        return;
    }

    try {
        tabela.inserir(estudante);
        System.out.println("Estudante inserido com sucesso!");
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
        System.out.println("Estudante removido com sucesso!");
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

    System.out.println("Estudantes actualmente armazenados: "
            + tabela.getTamanho());

    System.out.println("Registos invalidos na ultima leitura: "
            + totalInvalidos);

    System.out.println("Registos duplicados na ultima leitura: "
            + totalDuplicados);

    if (tabela.getTamanho() == 0) {
        System.out.println("Nao existem estudantes para apresentar.");
        return;
    }

    int total = tabela.getTamanho();
    double somaMedias = 0;
    double somaIdades = 0;
    int masculinos = 0;
    int femininos = 0;

    for (Estudante e : tabela.listar()) {

        somaMedias += e.getMedia();
        somaIdades += e.getIdade();

        if (e.getSexo().equalsIgnoreCase("M")) {
            masculinos++;
        } else if (e.getSexo().equalsIgnoreCase("F")) {
            femininos++;
        }
    }

    System.out.printf("Media geral das notas: %.2f%n",
            somaMedias / total);

    System.out.printf("Media das idades: %.2f%n",
            somaIdades / total);

    System.out.println("Estudantes do sexo masculino: " + masculinos);
    System.out.println("Estudantes do sexo feminino: " + femininos);
}

// OPCao 6: estatisticas da tabela hash

private void mostrarEstatisticasHash() {

    System.out.println("\n--- ESTATISTICAS DA HASH TABLE ---");

    if (!verificarTabela()) {
        return;
    }

    EstatisticaHashTable estatistica =
            new EstatisticaHashTable(tabela);

    System.out.println(estatistica);
}

// OPCao 7: teste de desempenho

private void executarDesempenho() throws IOException {

    System.out.println("\n--- TESTE DE DESEMPENHO ---");

    int registos = lerInteiro(
            "Quantidade de registos a testar: ");

    if (registos <= 0) {
        System.out.println("A quantidade deve ser superior a zero.");
        return;
    }

    String codigo = lerTexto(
            "Codigo do estudante a pesquisar: ");

    Desempenho desempenho = new Desempenho();

    desempenho.imprimirRelatorio(registos, codigo);
}

// OPCao 8: comparar pesquisa sequencial e Hash

private void compararPesquisas() throws IOException {

    System.out.println("\n--- COMPARAR PESQUISAS ---");

    if (!verificarTabela()) {
        return;
    }

    String codigo = lerTexto(
            "Codigo do estudante a pesquisar: ");

    // Pesquisa na tabela Hash actual.
    long inicioHash = System.nanoTime();
    Estudante estudanteHash = tabela.pesquisar(codigo);
    long fimHash = System.nanoTime();

    long tempoHash = (fimHash - inicioHash) / 1000;

    // Pesquisa sequencial nos estudantes actualmente armazenados.
    long inicioSequencial = System.nanoTime();
    Estudante estudanteSequencial = null;

    for (Estudante estudante : tabela.listar()) {
        if (estudante.getCodigo().equals(codigo)) {
            estudanteSequencial = estudante;
            break;
        }
    }

    long fimSequencial = System.nanoTime();

    long tempoSequencial =
            (fimSequencial - inicioSequencial) / 1000;

    System.out.println("\nResultado da pesquisa Hash:");

    if (estudanteHash != null) {
        System.out.println(estudanteHash);
    } else {
        System.out.println("Estudante nao encontrado.");
    }

    System.out.println("Tempo Hash: " + tempoHash + " microssegundos");

    System.out.println("\nResultado da pesquisa sequencial:");

    if (estudanteSequencial != null) {
        System.out.println(estudanteSequencial);
    } else {
        System.out.println("Estudante nao encontrado.");
    }

    System.out.println("Tempo sequencial: "
            + tempoSequencial + " microssegundos");

    System.out.println("\nNota: os tempos podem variar entre execucoes.");
}


}
