import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Pedido {
    static final AtomicInteger incrementador = new AtomicInteger(-1);
    final int id;
    LinkedList<Produto> produtos = new LinkedList<>();

    public Pedido() {
        this.id = incrementador.incrementAndGet();
    }

    public void adicionar(Produto produto) {
        this.produtos.add(produto);
    }

    public double total() {
        double total = 0;
        for (Produto produto : this.produtos) {
            total += produto.valor;
        }
        return total;
    }

    @Override
    public String toString() {
        return "Pedido Nº" + id;
    }
}

enum NomesProdutos {
    ARROZ, FEIJAO, MACARRAO, LEITE, CAFE, AÇUCAR, SAL, OLEO, FARINHA, MANTEIGA;
}

class Produto {
    String nome;
    double valor;

    public Produto(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return nome + " - R$ " + valor;
    }
}