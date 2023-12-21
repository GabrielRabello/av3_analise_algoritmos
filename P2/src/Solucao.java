import java.util.*;

public class Solucao<T> {
    private final Grafo<T> g;
    private List<Vertice<T>> L;

    Solucao(Grafo<T> g) {
        this.g = g;
    }

   public void printTemposFolga(int maxDist) {
        var tempoMaximoTermino = new HashMap<Vertice<T>, Integer>();

        for (Vertice<T> v : this.L) {
            // Tempo maximo onde a tarefa pode ser iniciada sem prejudicar o tempo global
            int inicioMaximo = maxDist - this.temposCadeiaTarefa.get(v);
            // Tempo maximo onde a tarefa pode ser terminada
            int terminoMaximo = inicioMaximo + v.getTempo();

            tempoMaximoTermino.put(v, terminoMaximo);
        }

        System.out.println("Tarefas que não podem sofrer qualquer atraso:");
        for (var tarefa : this.L) {
            // Tempo da tarefa == Tempo maximo de termino.
            if (tarefa.getTempo() == tempoMaximoTermino.get(tarefa)) {
                System.out.println("- " + tarefa.getValor());
            }
        }

        System.out.println("\nTarefas que podem sofrer atrasos:");
        for (var tarefa : this.L) {
            if ((tarefa.getTempo() != tempoMaximoTermino.get(tarefa))) {
                System.out.println("- " + tarefa.getValor());
            }
        }

        System.out.println("\nTempo máximo de atraso de cada tarefa:");
        for (var tarefa : this.L) {
            int atrasoMaximo = tempoMaximoTermino.get(tarefa) - tarefa.getTempo();
            System.out.println("- " + tarefa.getValor() + ": " + atrasoMaximo + "m");
        }
    }

    public int tempoMinimoParaTarefas() {
        var distancia = new HashMap<Vertice<T>, Integer>();

        for(var node : this.g.getVertices()) {
            distancia.put(node, Integer.MIN_VALUE);
        }

        int maxDist = 0;
        for (Vertice<T> v : this.L) {
            if (distancia.get(v).equals(Integer.MIN_VALUE)) {
                maxDist = Math.max(maxDist, maiorCaminho(distancia, v));
            }
        }

        this.temposCadeiaTarefa = distancia;
        return maxDist;
    }

    private int maiorCaminho(Map<Vertice<T>,Integer> distancia, Vertice<T> v) {
        int maxDist = 0;
        for (Vertice<T> w : this.g.getAdjacentes(v)) {
            if (distancia.get(w).equals(Integer.MIN_VALUE)) {
                maiorCaminho(distancia, w);
            }
            maxDist = Math.max(maxDist, distancia.get(w));
        }
        distancia.put(v, maxDist + v.getTempo());
//        System.out.println("Distancia de " + v + " = " + distancia.get(v) + "\n----------");
        return distancia.get(v);
    }

    /** Faz a ordenação topológica do grafo */
    public List<Vertice<T>> kahns() {
        var grafo = new Grafo<>(this.g);
        // Lista que conterá o resultado (os elementos ordenados)
        List<Vertice<T>> L = new ArrayList<>();
        // Todos os vértices sem arestas de entrada
        List<Vertice<T>> S = new ArrayList<>(buscarVerticesSemArestasEntrada());

        while (!S.isEmpty()) {
            var verticeEscolhido = S.remove(0);
            L.add(verticeEscolhido);

            // Pega as arestas de saída de n
            List<Vertice<T>> saidasVerticeEscolhido = List.copyOf(grafo.getArestasSaida(verticeEscolhido));

            for (var m : saidasVerticeEscolhido) {
                grafo.removerAresta(verticeEscolhido, m);
                boolean temArestaDeEntrada = grafo.getGrauEntrada(m) > 0;
                if (!temArestaDeEntrada) {
                    S.add(m);
                }
            }
        }

        if (grafo.nArestas() > 0) {
            throw new RuntimeException("O grafo possui ciclos");
        }
        return this.L = L;
    }

    private List<Vertice<T>> buscarVerticesSemArestasEntrada() {
        List<Vertice<T>> res = new ArrayList<>();
        for (var vertice : this.g.getVertices()) {
            if (this.g.getGrauEntrada(vertice) == 0) {
                res.add(vertice);
            }
        }
        return res;
    }

//     public int calcularTempoMinimo() {
//        var tarefas = encontrarTarefasConcorrentes(this.g);
//        var tempoMinimo = 0;
//        for (var tarefa : tarefas) {
//            // O tempo mínimo de cada "lote" de tarefas é o tempo da sub-tarefa mais demorada
//            var max = tarefa.stream().mapToInt(Vertice::getTempo).max().orElseThrow();
//            tempoMinimo += max;
//        }
//        return tempoMinimo;
//    }
//
//    // Beseado no algoritmo de Kahn
//    private ArrayList<ArrayList<Vertice<T>>> encontrarTarefasConcorrentes(Grafo<T> g) {
//        var trabalhosSimultaneos = new ArrayList<ArrayList<Vertice<T>>>();
//        var in = buscarArestasEntrada();
//        while(!g.isEmpty()) {
//            // A cada iteração os vértices sem aresta de entrada irão mudar.
//            var verticesSemArestasDeEntrada = in.entrySet().stream().filter(entry -> entry.getValue().equals(0)).map(Map.Entry::getKey).toList();
//            // Adiciona todos os vértices sem aresta de entrada na lista de trabalhos simultâneos
//            trabalhosSimultaneos.add(new ArrayList<>(verticesSemArestasDeEntrada));
////            for(var node : verticesSemArestasDeEntrada) {
////                g.removerVertice(node.getKey());
////            }
//            for(var v : verticesSemArestasDeEntrada) {
//                var adjacentes = this.g.getAdjacentes(v);
//                for(var adj : adjacentes) {
//                    in.put(adj, in.get(adj) - 1);
//                }
//                in.remove(v);
//            }
//        }
//        return trabalhosSimultaneos;

//    }

//    private Map<Vertice<T>, Integer> buscarArestasEntrada() {
//        var entradas = new HashMap<Vertice<T>, Integer>();
//        for(var vertice : this.g.getVertices()) {
//            int grau = this.g.getGrauEntrada(vertice);
//            entradas.put(vertice, grau);
//        }
//        return entradas;
//    }

    public void printOrdenacao() {
        System.out.println("Ordenação Topológica: ");
        for (var node : this.L) {
            System.out.println(node.valor + " -> " + node.tempo + "m");
        }
    }

    public static void main(String[] args) {
        var g = new Grafo<String>();
        var v1 = new Vertice<>("Preparar os tornos", 8);
        var v2 = new Vertice<>("Preparar as embalagens", 8);
        var v3 = new Vertice<>("Cortar e distribuir o PVC", 10);
        var v4 = new Vertice<>("Cortar e distribuir o aço", 12);
        var v5 = new Vertice<>("Tornear a peça A", 8);
        var v6 = new Vertice<>("Tornear a peça B", 11);
        var v7 = new Vertice<>("Tornear a peça C", 15);
        var v8 = new Vertice<>("Rosquear a peça A", 9);
        var v9 = new Vertice<>("Rosquear a peça B", 7);
        var v10 = new Vertice<>("Montar as peças A e C", 4);
        var v11 = new Vertice<>("Montar a peça B", 6);
        var v12 = new Vertice<>("Embalar e armazenar", 7);

        g.addVertice(v1);
        g.addVertice(v2);
        g.addVertice(v3);
        g.addVertice(v4);
        g.addVertice(v5);
        g.addAresta(v1, v5);
        g.addAresta(v3, v5);

        g.addVertice(v6);
        g.addAresta(v1, v6);
        g.addAresta(v3, v6);

        g.addVertice(v7);
        g.addAresta(v1, v7);
        g.addAresta(v4, v7);

        g.addVertice(v8);
        g.addAresta(v5, v8);

        g.addVertice(v9);
        g.addAresta(v6, v9);

        g.addVertice(v10);
        g.addAresta(v7, v10);
        g.addAresta(v8, v10);

        g.addVertice(v11);
        g.addAresta(v9, v11);
        g.addAresta(v10, v11);
        g.addVertice(v12);
        g.addAresta(v2, v12);
        g.addAresta(v11, v12);

//        System.out.println(g);

        var OTP = new Solucao<>(g);
        OTP.kahns();
        OTP.printOrdenacao();
        var tempo = OTP.tempoMinimoParaTarefas();
        System.out.println("\nTempo mínimo para realizar as tarefas: " + tempo + "m");
        OTP.printTemposFolga(tempo);
    }
}
