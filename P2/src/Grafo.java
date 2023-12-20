
import java.util.*;

public class Grafo<T> {
    private final Map<Vertice<T>, List<Vertice<T>>> grafo;

    public Grafo() {
        this.grafo = new HashMap<>();
    }

    public Grafo(Grafo<T> other) {
        this.grafo = new HashMap<>();
        for (var vertice : other.grafo.keySet()) {
            this.grafo.put(vertice, new LinkedList<>(other.grafo.get(vertice)));
        }
    }

    public void addVertice(Vertice<T> v) {
        grafo.putIfAbsent(v, new LinkedList<>());
    }

    public void addAresta(Vertice<T> src, Vertice<T> dest) {
        if(!grafo.containsKey(src) || !grafo.containsKey(dest)) throw new IllegalArgumentException("Vértice não existe");

        grafo.get(src).add(dest);
    }

    public List<Vertice<T>> getArestasSaida(Vertice<T> v) {
        if(!grafo.containsKey(v)) throw new IllegalArgumentException("Vértice não existe");
        return grafo.get(v);
    }

    public List<Vertice<T>> getArestasEntrada(Vertice<T> v) {
        if(!grafo.containsKey(v)) throw new IllegalArgumentException("Vértice não existe");
        var arestasEntrada = new ArrayList<Vertice<T>>();
        for(var vertice : grafo.keySet()) {
            if(grafo.get(vertice).contains(v)) arestasEntrada.add(vertice);
        }
        return arestasEntrada;
    }

    public void removerAresta(Vertice<T> src, Vertice<T> dest) {
        if(!grafo.containsKey(src) || !grafo.containsKey(dest)) throw new IllegalArgumentException("Vértice não existe");
        if(!grafo.get(src).contains(dest)) return;

        grafo.get(src).remove(dest);
    }

    public void removerVertice(Vertice<T> v) {
        if (!grafo.containsKey(v)) throw new IllegalArgumentException("Vértice não existe");
        // Remove toda aresta (v, x) e (x, v), onde x é qualquer vértice conectado a v.
        for(var lista : grafo.values()) {
            lista.removeIf(aresta -> aresta == v);
        }
        // Remove o vertice v
        grafo.remove(v);
    }

    public List<Vertice<T>> getVertices() {
        return new ArrayList<>(grafo.keySet());
    }

    public List<Vertice<T>> getAdjacentes(Vertice<T> v) {
        return new ArrayList<>(grafo.get(v));
    }

    public boolean hasVertice(Vertice<T> v) {
        return grafo.containsKey(v);
    }

    public boolean hasAresta(Vertice<T> src, Vertice<T> dest) {
        return grafo.get(src).contains(dest);
    }

    public int getGrauEntrada(Vertice<T> v) {
        int grau = 0;
        for (var vertice : grafo.keySet()) {
            if (grafo.get(vertice).contains(v)) grau++;
        }
        return grau;
    }

    public int getGrauSaida(Vertice<T> v) {
        return grafo.get(v).size();
    }

    public int nVertices() {
        return grafo.size();
    }

    public int nArestas() {
        int numArestas = 0;
        for (var vertice : grafo.keySet()) {
            numArestas += grafo.get(vertice).size();
        }
        return numArestas;
    }

    public boolean isEmpty() {
        return grafo.isEmpty();
    }

    @Override
    public String toString() {
        var sBuilder = new StringBuilder();
        sBuilder.append("GRAFO: \n");
        for (var node : this.grafo.entrySet()) {
            sBuilder.append("Nó: ");
            sBuilder.append(node.getKey()).append("  -->  ");
            // for each List entry, append the value
            for (var adj : node.getValue()) {
                sBuilder.append(adj).append(" | ");
            }
            sBuilder.append('\n');
        }
        return sBuilder.toString();
    }
}