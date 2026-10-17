package estatistica;
import dados.*;
import utils.Rounder;

import java.io.*;

public abstract class Estatistica {
    protected LeitorCSV ficheiroEstudante;
    protected final Dados dados;
    protected final int totalLinhas;
    protected final int totalEstudantes;

    public Estatistica(){
        this.ficheiroEstudante = new LeitorCSV();
        this.dados = ficheiroEstudante.lerDados();
        this.totalLinhas = dados.getLidas();
        this.totalEstudantes = dados.getInseridos();
    }

    public  abstract String toString();
}

