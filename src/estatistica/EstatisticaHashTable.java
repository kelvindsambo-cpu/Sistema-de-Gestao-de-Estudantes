package estatistica;

import hash.TabelaHash;
import utils.Rounder;

public class EstatisticaHashTable extends Estatistica{
    private TabelaHash tabela;

    public EstatisticaHashTable(TabelaHash tabela){
        this.tabela = tabela;
    }

    @Override
    public String toString(){
        return "Capacidade inicial : "+tabela.getCapacidadeInicial()+"\n"+
                "Capacidade final : "+tabela.getCapacidade()+"\n"+
                "Elementos armazenados : "+tabela.getTamanho()+"\n"+
                "Factor de carga : "+ tabela.getFactorCarga()+"\n"+
                "Colisões : "+tabela.getTotalColisoes()+"\n"+
                "Sondagens realizadas : "+tabela.getTotalSondagens()+"\n"+
                "Media de sondagens/inserção : "+tabela.getMediaSondagens()+"\n"+
                "Maior sondagem : "+tabela.getMaiorSondagem()+"\n"+
                "Redimensionamentos : "+tabela.getNumRedimensionamentos()+"\n";
    }
}
