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
                "Factor de carga : "+ Rounder.round(tabela.getFactorCarga(),3)+"\n"+
                "Colisões : "+tabela.getTotalColisoes()+"\n"+
                "Sondagens realizadas : "+tabela.getTotalSondagens()+"\n"+
                "Media de sondagens/inserção : "+Rounder.round(tabela.getMediaSondagens(),3)+"\n"+
                "Maior sondagem : "+tabela.getMaiorSondagem()+"\n"+
                "Redimensionamentos : "+tabela.getNumRedimensionamentos()+"\n";
    }
}
