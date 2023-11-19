package com.cavaleirosDaNoite.demo.Dominio;

import com.cavaleirosDaNoite.demo.Aplicacao.PedidoRequest;
import com.cavaleirosDaNoite.demo.Dominio.Entidades.Cliente;
import com.cavaleirosDaNoite.demo.Dominio.Entidades.ItemPedido;
import com.cavaleirosDaNoite.demo.Dominio.Entidades.Pedido;
import com.cavaleirosDaNoite.demo.Dominio.Entidades.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoPedido {
    private final RepPedidos repPedidos;
    private final ServicoCliente servicoCliente;
    private final ServicoProduto servicoProduto;

    private final ServicoItemPedido servicoItemPedido;

    @Autowired
    public ServicoPedido(RepPedidos repPedidos, ServicoCliente servicoCliente, ServicoProduto servicoProduto, ServicoItemPedido servicoItemPedido) {
        this.repPedidos = repPedidos;
        this.servicoCliente = servicoCliente;
        this.servicoProduto = servicoProduto;
        this.servicoItemPedido = servicoItemPedido;
    }

    @Transactional
    public Pedido cadastrarPedido(PedidoRequest pedidoRequest) {
        Date data = new Date();
        Cliente cliente = servicoCliente.buscarCliente(pedidoRequest.getIdCliente());
        Pedido pedido = new Pedido(data, cliente, new ArrayList<>());
        List<ItemPedido> itensPedido = pedidoRequest.getItens().stream()
                .map(itemRequest -> {
                    Produto produto = servicoProduto.buscarProduto(itemRequest.getIdProduto());
                    ItemPedido itemPedido = new ItemPedido(produto, itemRequest.getQuantidade());
                    // Adicione a linha abaixo para configurar a associação bidirecional
                    itemPedido.setPedido(pedido);
                    return itemPedido;
                }).toList();
        pedido.setItens(itensPedido);
        return repPedidos.save(pedido);
    }

    public void removerPedido(long id) {
        repPedidos.deleteById(id);
    }

    public Pedido buscarPedido(long id) {
        return repPedidos.findById(id).orElse(null);
    }

    public List<Pedido> listarPedidos() {
        return repPedidos.findAll();
    }

    public List<Pedido> buscarPedidoCliente(long idCliente) {
        return repPedidos.findByClienteId(idCliente);
    }

    @Transactional
    public Pedido atualizarPedido(PedidoRequest pedidoRequest, long idPedido) {
        Cliente cliente = servicoCliente.buscarCliente(pedidoRequest.getIdCliente());
        Pedido pedidoAtualizado = repPedidos.findById(idPedido).orElse(null);
        Date data = new Date();
        if (pedidoAtualizado == null) {
            return null;
        }
        pedidoAtualizado.getItens().forEach(item -> item.setPedido(null));
        pedidoAtualizado.clearItens();
        //        pedidoAtualizado.getItens().stream().map(item -> { item.setPedido(null); return servicoItemPedido.atualizarItemPedido(item); });
//        pedidoAtualizado.clearItens();
        List<ItemPedido> itensPedido = pedidoRequest.getItens().stream()
                .map(itemRequest -> {
                    Produto produto = servicoProduto.buscarProduto(itemRequest.getIdProduto());
                    ItemPedido itemPedido = new ItemPedido(produto, itemRequest.getQuantidade());
                    // Adicione a linha abaixo para configurar a associação bidirecional
                    itemPedido.setPedido(pedidoAtualizado);
                    return itemPedido;
                })
                .collect(Collectors.toList());
        pedidoAtualizado.setData(data);
        pedidoAtualizado.setCliente(cliente);
        pedidoAtualizado.updateItens(itensPedido);
        System.out.println("Pedido atualizado: " + pedidoAtualizado);
        return repPedidos.save(pedidoAtualizado);
    }
}
