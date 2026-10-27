package desempenho;

import model.Estudante;

import java.util.List;

public class PesquisaSequencial {
    private final Estudante[] estudantes;
    private int ultimasComparacoes;

    public PesquisaSequencial(List<Estudante> estudantes){
        this.estudantes = estudantes.toArray(new Estudante[0]);
    }

    public Estudante pesquisar(String codigo){
        ultimasComparacoes = 0;
        for (Estudante e : estudantes) {
            ultimasComparacoes++;
            if(e.getCodigo().equals(codigo)){
                return e;
            }
        }
        return null;
    }

    public int getUltimasComparacoes() {
        return ultimasComparacoes;
    }

}
