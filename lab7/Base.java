import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Base {
    public static final int NUM_CLIENTES = 5;
    public static final int NUM_CONSUMIDORES = 10;
    public static final int TAMANHO_FILA = 10;

    BlockingQueue<Pedido> queue = new ArrayBlockingQueue<>(TAMANHO_FILA);
    BlockingQueue<Pedido> pedidosPendentes = new LinkedBlockingQueue<>();
    ScheduledExecutorService clientes = Executors.newScheduledThreadPool(NUM_CLIENTES);
    ScheduledExecutorService consumidores = Executors.newScheduledThreadPool(NUM_CONSUMIDORES);
    ScheduledExecutorService reabastecedor = Executors.newSingleThreadScheduledExecutor();
    ScheduledExecutorService relatorios = Executors.newSingleThreadScheduledExecutor();

    Estoque estoque = new Estoque();
    private final AtomicInteger pedidosProcessados = new AtomicInteger(0);
    private final AtomicInteger pedidosRejeitados = new AtomicInteger(0);
    private double valorTotalVendas = 0.0;

    public void run() {
        for (int i = 0; i < NUM_CLIENTES; i++) {
            clientes.scheduleAtFixedRate(new Cliente(queue), 0, new Random().nextInt(5) + 1, TimeUnit.SECONDS);
        }

        for (int i = 0; i < NUM_CONSUMIDORES; i++) {
            consumidores.scheduleAtFixedRate(new ProcessadorDePedidos(queue, estoque, this, pedidosPendentes), 0, new Random().nextInt(5) + 1, TimeUnit.SECONDS);
        }

        reabastecedor.scheduleAtFixedRate(() -> {
            estoque.recomporEstoque();
            reprocessarPedidosPendentes();
        }, 0, 10, TimeUnit.SECONDS);

        relatorios.scheduleAtFixedRate(() -> {
            System.out.println("Relatório de Vendas:");
            System.out.println("Pedidos Processados: " + pedidosProcessados.get());
            System.out.println("Valor Total das Vendas: R$" + String.format("%.2f", getValorTotalVendas()));
            System.out.println("Pedidos Rejeitados: " + pedidosRejeitados.get());
        }, 0, 30, TimeUnit.SECONDS);
    }

    private synchronized void reprocessarPedidosPendentes() {
        Pedido pedido;
        while ((pedido = pedidosPendentes.poll()) != null) {
            try {
                queue.put(pedido);
                System.out.println("Reprocessando pedido pendente: " + pedido);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void incrementarPedidosProcessados(double valorPedido) {
        pedidosProcessados.incrementAndGet();
        synchronized (this) {
            valorTotalVendas += valorPedido;
        }
    }

    public void incrementarPedidosRejeitados() {
        pedidosRejeitados.incrementAndGet();
    }

    public synchronized double getValorTotalVendas() {
        return valorTotalVendas;
    }

    public static void main(String[] args) {
        Base scenario = new Base();
        scenario.run();
    }
}