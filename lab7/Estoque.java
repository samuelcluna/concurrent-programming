import java.util.concurrent.*;

public class Estoque {
    private final ConcurrentHashMap<NomesProdutos, Integer> estoque = new ConcurrentHashMap<>();

    public Estoque() {
        for (NomesProdutos produto : NomesProdutos.values()) {
            estoque.put(produto, 100);
        }
    }

    public int consultarEstoque(NomesProdutos item) {
        return estoque.getOrDefault(item, 0);
    }

    public void atualizarEstoque(NomesProdutos item, int quantidade) {
        estoque.put(item, quantidade);
    }

    public void removerProduto(NomesProdutos item) {
        estoque.remove(item);
    }

    public synchronized void recomporEstoque() {
        System.out.println("Recompondo estoque...");
        estoque.forEach((item, quantidade) -> {
            int quantidadeNova = quantidade + 50;
            estoque.put(item, quantidadeNova);
        });
    }

    public void adicionarProduto(NomesProdutos item, int quantidade) {
        estoque.put(item, quantidade);
    }
}