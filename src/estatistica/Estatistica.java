package estatistica;

import java.util.List;
import model.Estudante;

public abstract class Estatistica {
    protected final List<Estudante> estudantes;
    protected final int totalEstudantes;

    public Estatistica(List<Estudante> estudantes){
        this.estudantes = estudantes;
        this.totalEstudantes = estudantes.size();
    }

    protected double percentagem(int parte) {
        if (totalEstudantes == 0) {
            return 0;
        }
        return 100.0 * parte / totalEstudantes;
    }

    public  abstract String toString();
}
