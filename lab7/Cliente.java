import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Cliente implements Runnable {
    static final Logger logger = Logger.getLogger(Cliente.class.getName());

    BlockingQueue<Pedido> queue;
    NomesProdutos[] nomeProdutos = NomesProdutos.values();

    public Cliente(BlockingQueue<Pedido> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Pedido pedido = new Pedido();
            int qtdPedidos = ThreadLocalRandom.current().nextInt(10) + 1;
            for (int i = 0; i < qtdPedidos; i++) {
                String nome = nomeProdutos[ThreadLocalRandom.current().nextInt(nomeProdutos.length)].name();
                Produto produto = new Produto(nome, ThreadLocalRandom.current().nextInt(5) + ThreadLocalRandom.current().nextDouble());
                pedido.adicionar(produto);
            }
            queue.put(pedido);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}