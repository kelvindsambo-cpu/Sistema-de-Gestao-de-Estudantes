package desempenho;
import dados.LeitorCSV;
import hash.CodigoDuplicadoException;
import hash.TabelaHash;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Estudante;
import utils.Rounder;

public class Desempenho{
    public static final int[] TAMANHOS = {1000, 5000, 10000};
    private static final int REPETICOES = 10;
    private static final int AQUECIMENTOS = 3;
    private static final int NUM_PESQUISAS = 1000;

    private final List<Estudante> todos;
    private long encontrados;

    public Desempenho(){
        this.todos = new LeitorCSV().lerEstudantes();
    }

    private void aquecer(){
        for (int n : TAMANHOS) {
            List<Estudante> amostra = amostra(n);
            medirInsercao(amostra);
            TabelaHash tabela = construirTabela(amostra);
            PesquisaSequencial sequencial = new PesquisaSequencial(amostra);
            for (String[] codigos : new String[][] { codigosExistentes(amostra), codigosInexistentes(tabela) }) {
                medirPesquisaHash(tabela, codigos);
                medirPesquisaSequencial(sequencial, codigos);
            }
        }
    }

    public String relatorioDesempenho(){
        aquecer();
        StringBuilder sb = new StringBuilder();
        sb.append("================ EXPERIENCIA DE DESEMPENHO ================\n");
        sb.append(String.format("%-9s %-9s %-11s %-10s %-7s %-16s %-16s%n",
                "Registos", "Colisoes", "Media sond.", "Max. sond.", "Carga",
                "Tempo insercao", "Tempo pesquisa"));

        for (int n : TAMANHOS) {
            List<Estudante> amostra = amostra(n);
            double tempoInsercaoNs = medirInsercao(amostra);
            TabelaHash tabela = construirTabela(amostra);
            double tempoPesquisaNs = medirPesquisaHash(tabela, codigosExistentes(amostra));

            sb.append(String.format("%-9d %-9d %-11s %-10d %-7s %-16s %-16s%n",
                    amostra.size(),
                    tabela.getTotalColisoes(),
                    Rounder.formatar(tabela.getMediaSondagens(), 2),
                    tabela.getMaiorSondagem(),
                    Rounder.formatar(tabela.getFactorCarga(), 4),
                    Rounder.formatar(tempoInsercaoNs / 1_000_000, 3) + " ms",
                    Rounder.formatar(tempoPesquisaNs, 1) + " ns"));
        }

        sb.append("\nTempo insercao: tempo total para inserir todos os registos (mediana de "
                + REPETICOES + " execucoes).\n");
        sb.append("Tempo pesquisa: tempo medio por pesquisa, com " + NUM_PESQUISAS
                + " codigos existentes (mediana de " + REPETICOES + " execucoes).\n");
        return sb.toString();
    }

    public String relatorioComparacao(){
        aquecer();
        StringBuilder sb = new StringBuilder();
        sb.append("============== PESQUISA SEQUENCIAL vs HASH ==============\n");
        sb.append("Tempo medio por pesquisa (ns) e media de posicoes examinadas\n\n");
        sb.append(String.format("%-9s %-12s %-22s %-22s %-10s%n",
                "Registos", "Codigos", "Hash", "Sequencial", "Sequencial/Hash"));

        for (int n : TAMANHOS) {
            List<Estudante> amostra = amostra(n);
            TabelaHash tabela = construirTabela(amostra);
            PesquisaSequencial sequencial = new PesquisaSequencial(amostra);

            String[][] grupos = { codigosExistentes(amostra), codigosInexistentes(tabela) };
            String[] nomes = { "existentes", "inexistentes" };

            for (int g = 0; g < grupos.length; g++) {
                String[] codigos = grupos[g];
                double hashNs = medirPesquisaHash(tabela, codigos);
                double seqNs = medirPesquisaSequencial(sequencial, codigos);

                sb.append(String.format("%-9d %-12s %-22s %-22s %-10s%n",
                        amostra.size(),
                        nomes[g],
                        Rounder.formatar(hashNs, 1) + " ns ("
                                + Rounder.formatar(sondagensMediasHash(tabela, codigos), 2) + " sond.)",
                        Rounder.formatar(seqNs, 1) + " ns ("
                                + Rounder.formatar(comparacoesMediasSequencial(sequencial, codigos), 0) + " comp.)",
                        Rounder.formatar(seqNs / hashNs, 0) + "x"));
            }
        }

        sb.append("\n" + NUM_PESQUISAS + " codigos de cada tipo; tempo medio por pesquisa, mediana de "
                + REPETICOES + " execucoes.\n");
        sb.append("sond. = posicoes da tabela sondadas; comp. = estudantes comparados na lista.\n");
        return sb.toString();
    }

    private List<Estudante> amostra(int n){
        return todos.subList(0, Math.min(n, todos.size()));
    }

    private TabelaHash construirTabela(List<Estudante> estudantes){
        TabelaHash tabela = new TabelaHash();
        for (Estudante e : estudantes) {
            try {
                tabela.inserir(e);
            } catch (CodigoDuplicadoException ex) {
            }
        }
        return tabela;
    }

    private double medirInsercao(List<Estudante> estudantes){
        for (int a = 0; a < AQUECIMENTOS; a++) {
            construirTabela(estudantes);
        }
        long[] tempos = new long[REPETICOES];
        for (int r = 0; r < REPETICOES; r++) {
            long inicio = System.nanoTime();
            TabelaHash tabela = construirTabela(estudantes);
            long fim = System.nanoTime();
            tempos[r] = fim - inicio;
            encontrados += tabela.getTamanho();
        }
        return mediana(tempos);
    }

    private double medirPesquisaHash(TabelaHash tabela, String[] codigos){
        for (int a = 0; a < AQUECIMENTOS; a++) {
            pesquisarHash(tabela, codigos);
        }
        long[] tempos = new long[REPETICOES];
        for (int r = 0; r < REPETICOES; r++) {
            long inicio = System.nanoTime();
            pesquisarHash(tabela, codigos);
            long fim = System.nanoTime();
            tempos[r] = fim - inicio;
        }
        return mediana(tempos) / codigos.length;
    }

    private double medirPesquisaSequencial(PesquisaSequencial sequencial, String[] codigos){
        for (int a = 0; a < AQUECIMENTOS; a++) {
            pesquisarSequencial(sequencial, codigos);
        }
        long[] tempos = new long[REPETICOES];
        for (int r = 0; r < REPETICOES; r++) {
            long inicio = System.nanoTime();
            pesquisarSequencial(sequencial, codigos);
            long fim = System.nanoTime();
            tempos[r] = fim - inicio;
        }
        return mediana(tempos) / codigos.length;
    }

    private double mediana(long[] tempos){
        long[] ordenados = tempos.clone();
        Arrays.sort(ordenados);
        int meio = ordenados.length / 2;
        if (ordenados.length % 2 == 0) {
            return (ordenados[meio - 1] + ordenados[meio]) / 2.0;
        }
        return ordenados[meio];
    }

    private void pesquisarHash(TabelaHash tabela, String[] codigos){
        for (String codigo : codigos) {
            if (tabela.pesquisar(codigo) != null) {
                encontrados++;
            }
        }
    }

    private void pesquisarSequencial(PesquisaSequencial sequencial, String[] codigos){
        for (String codigo : codigos) {
            if (sequencial.pesquisar(codigo) != null) {
                encontrados++;
            }
        }
    }

    private double sondagensMediasHash(TabelaHash tabela, String[] codigos){
        long total = 0;
        for (String codigo : codigos) {
            tabela.pesquisar(codigo);
            total += tabela.getUltimasSondagens();
        }
        return (double) total / codigos.length;
    }

    private double comparacoesMediasSequencial(PesquisaSequencial sequencial, String[] codigos){
        long total = 0;
        for (String codigo : codigos) {
            sequencial.pesquisar(codigo);
            total += sequencial.getUltimasComparacoes();
        }
        return (double) total / codigos.length;
    }

    private String[] codigosExistentes(List<Estudante> amostra){
        int quantidade = Math.min(NUM_PESQUISAS, amostra.size());
        String[] codigos = new String[quantidade];
        for (int i = 0; i < quantidade; i++) {
            codigos[i] = amostra.get((int) ((long) i * amostra.size() / quantidade)).getCodigo();
        }
        return codigos;
    }

    private String[] codigosInexistentes(TabelaHash tabela){
        List<String> codigos = new ArrayList<>();
        long candidato = 99990000L;
        while (codigos.size() < NUM_PESQUISAS) {
            String codigo = String.valueOf(candidato++);
            if (tabela.pesquisar(codigo) == null) {
                codigos.add(codigo);
            }
        }
        return codigos.toArray(new String[0]);
    }

}
