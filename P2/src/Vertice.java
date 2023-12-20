import java.util.Objects;

public class Vertice<T> {
    public final T valor;
    public final int tempo;

    public Vertice(T valor, int tempo) {
        this.valor = valor;
        this.tempo = tempo;
    }

    public Vertice(T valor) {
        this(valor, 0);
    }

    public T getValor() {
        return valor;
    }

    public int getTempo() {
        return tempo;
    }

    // Apenas considera o valor nas verificações por igualdade
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice<?> vertice = (Vertice<?>) o;
        return Objects.equals(valor, vertice.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString() ;
    }
}
