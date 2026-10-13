package desempenho;
import hash.TabelaHash;

public class Desempenho{
    private TabelaHash tabela;
    private long tempoExec;
    private int registos;
    public Desempenho(int registos){
        this.registos = registos;
    }
    public void PP(){
        tabela.getTotalColisoes();
        tabela.getMediaSondagens();
        tabela.getMaiorSondagem();
        tabela.getFactorCarga();
    }
}