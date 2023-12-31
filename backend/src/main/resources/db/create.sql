DROP TABLE IF EXISTS ItemPedido;
DROP TABLE IF EXISTS Orcamentos;
DROP TABLE IF EXISTS Pedidos CASCADE;
DROP TABLE IF EXISTS Produtos CASCADE;
DROP TABLE IF EXISTS Estoques CASCADE;
DROP TABLE IF EXISTS Clientes;

CREATE TABLE IF NOT EXISTS Clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Estoques (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quantidade INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Produtos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS ItemEstoque (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    idProduto BIGINT NOT NULL,
    idEstoque BIGINT NOT NULL,
    quantidadeAtual INT NOT NULL,
    quantidadeMax INT NOT NULL,
    FOREIGN KEY (idProduto) REFERENCES Produtos(id),
    FOREIGN KEY (idEstoque) REFERENCES Estoques(id)
);

CREATE TABLE IF NOT EXISTS Pedidos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data DATE NOT NULL,
    idCliente BIGINT NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES Clientes(id)
);

CREATE TABLE IF NOT EXISTS ItemPedido(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    idPedido BIGINT NOT NULL,
    idProduto BIGINT NOT NULL,
    quantidade INT NOT NULL,
    FOREIGN KEY (idPedido) REFERENCES Pedidos(id),
    FOREIGN KEY (idProduto) REFERENCES Produtos(id)
);

CREATE TABLE IF NOT EXISTS Orcamentos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data DATE NOT NULL,
    valorTotal DECIMAL(10,2) NOT NULL,
    imposto DECIMAL(10,2) NOT NULL,
    desconto DECIMAL(10,2) NOT NULL,
    idPedido BIGINT NOT NULL,
    idCliente BIGINT NOT NULL,
    efetivado BOOLEAN NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES Clientes(id),
    FOREIGN KEY (idPedido) REFERENCES Pedidos(id)
);
