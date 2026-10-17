package desempenho;
import dados.LeitorCSV;
import hash.TabelaHash;
import utils.Rounder;

public class Desempenho{
    private TabelaHash tabela;
    private LeitorCSV leitor;
    private PesquisaSequencial sequencia;

    public Desempenho(){
        this.leitor = new LeitorCSV();
        this.sequencia = new PesquisaSequencial();
    }

    public int Colisoes(int registos){
        TabelaHash tabela = leitor.lerDados(registos).getTabelaHash();
        return tabela.getTotalColisoes();
    }

    public double MediaSondagem(int registos){
        TabelaHash tabela = leitor.lerDados(registos).getTabelaHash();
        return Rounder.round(tabela.getMediaSondagens(),2);
    }

    public int MaximaSondagem(int registos){
        TabelaHash tabela = leitor.lerDados(registos).getTabelaHash();
        return tabela.getMaiorSondagem();
    }

    public double Carga(int registos){
        TabelaHash tabela = leitor.lerDados(registos).getTabelaHash();
        return tabela.getFactorCarga();
    }

    public long TempoInsercao(int registos){
        long inicio = System.nanoTime();
        TabelaHash tabela = leitor.lerDados(registos).getTabelaHash();
        long fim = System.nanoTime();
        return (fim - inicio)/1000;
    }

    public long TempoPesquisaHashTable(int registos, String codigo){
        TabelaHash tabela = leitor.lerDados(registos).getTabelaHash();
        long inicio = System.nanoTime();
        tabela.pesquisar(codigo);
        long fim = System.nanoTime();
        return (fim - inicio)/1000;
    }

    public long TempoPesquisaSequencial(String codigo){
        long inicio = System.nanoTime();
        sequencia.pesquisar(codigo);
        long fim = System.nanoTime();
        return (fim - inicio)/1000;
    }

    public void imprimirRelatorio(int registos,String codigo){
        System.out.println("------------ Desempenho --------------"+"\n"+
                "Registos: "+registos+"\n"+
                "Colisões: "+Colisoes(registos)+"\n"+
                "Média Sondagem: "+MediaSondagem(registos)+"\n"+
                "Máxima Sondagem: "+MaximaSondagem(registos)+"\n"+
                "Carga: "+Carga(registos)+"\n"+
                "Tempo de inserção: "+TempoInsercao(registos)+"µs\n"+
                "Tempo de pesquisa: "+ TempoPesquisaHashTable(registos,codigo)+"µs\n"+
        "--------------------------------------\n");
    }

   /* public static void main(){ Código de teste
        Desempenho des = new Desempenho();
        des.imprimirRelatorio(1000,"20260931");
        des.imprimirRelatorio(5000,"20260931");
        des.imprimirRelatorio(10000,"20260931");
        System.out.println("Pesquisa Tabela: "+des.TempoPesquisaHashTable(10000,"20269012")+"µs\n"+
                "Pesquisa Sequencial: "+des.TempoPesquisaSequencial("20269012")+"µs\n"
                );
    }
    */
}