<template>
  <v-container>
    <!-- Loading-->
    <div class="text-center">
      <v-dialog v-model="loading" hide-overlay persistent width="300">
        <v-card color="primary" dark>
          <v-card-text>
            Aguarde
            <v-progress-linear indeterminate color="white" class="mb-0"></v-progress-linear>
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>
    <!-- Remove-->
    <div class="text-center">
      <v-dialog v-model="remove" hide-overlay persistent width="300">
        <v-alert prominent type="error">
          Você tem certeza que deseja remover este cliente?
          <v-col class="shrink">
            <v-btn color="error" @click="deleteClientes">Remover</v-btn>
            <v-btn text @click="remove = false">Cancelar</v-btn>
          </v-col>
        </v-alert>
      </v-dialog>
    </div>


    <!-- Adicionar Cliente -->
    <div class="tex-center">
      <v-dialog v-model="addDialog" persistent max-width="600px">
        <v-card>
          <v-card-title>
            <span class="text-h5">Adicionar Cliente</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field v-model="formData.nome" label="Nome*" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="formData.cpf" label="CPF*" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="formData.email" label="Email*" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="formData.senha" label="Senha*" type="password" required></v-text-field>
                </v-col>
              </v-row>
            </v-container>
            <small>*Indica um campo obrigatório</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="addDialog = false">
              Close
            </v-btn>
            <v-btn color="blue darken-1" text @click="addClientes">
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>

    <!-- Editar Cliente -->
    <div class="tex-center">
      <v-dialog v-model="editDialog" persistent max-width="600px">
        <v-card>
          <v-card-title>
            <span class="text-h5">Editar Cliente</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field v-model="formData.nome" label="Nome*" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="formData.cpf" label="CPF*" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="formData.email" label="Email*" required></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="formData.senha" label="Senha*" type="password" required></v-text-field>
                </v-col>
              </v-row>
            </v-container>
            <small>*Indica um campo obrigatório</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="editDialog = false">
              Close
            </v-btn>
            <v-btn color="blue darken-1" text @click="editCliente">
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>

    <!-- Adicionar Pedido -->
    <div class="tex-center">
      <v-dialog v-model="addDialogPedido" persistent max-width="600px">
        <v-card>
          <v-card-title>
            <span class="text-h5">Adicionar Pedido</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field v-model="clienteAddPedido.nome" label="Cliente" disabled
                    :value="clienteAddPedido.nome"></v-text-field>
                </v-col>
                <v-row v-for="(item, index) in formPedido.itens" :key="index">
                  <v-col cols="12" sm="6">
                    <v-select :items="produtos" v-model="item.idProduto" label="Produto" item-text="idProduto"
                      prefix="Produto " required></v-select>

                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-text-field v-model="item.quantidade" label="Quantidade" required></v-text-field>
                  </v-col>
                </v-row>
              </v-row>
            </v-container>
            <v-btn @click="adicionarLinha">Adicionar Produto</v-btn>
            <small>*Indica um campo obrigatório</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="addDialogPedido = false">
              Close
            </v-btn>
            <v-btn color="blue darken-1" text @click="addProdutos">
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>


    <!-- Clientes-->
    <v-expansion-panels accordion focusable inset>
      <v-toolbar dark>
        <v-app-bar-nav-icon></v-app-bar-nav-icon>

        <v-toolbar-title>Clientes</v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn text @click="openAdicionar">
          Adicionar Cliente
          <v-icon right>mdi-account-plus</v-icon>
        </v-btn>
      </v-toolbar>
      <v-expansion-panel v-for="cliente in clientes" :key="cliente.id">
        <v-expansion-panel-header dark>
          <v-row>
            {{ cliente.nome }}
          </v-row>

        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <v-card class="mx-auto">

            <v-row class="fill-height">
              <v-card-title>
                <div class="text-h4 pl-12">
                  {{ cliente.nome }}
                </div>
              </v-card-title>
              <v-spacer></v-spacer>
              <v-card-title>
                <v-btn light icon class="mr-4" @click="openEdit(cliente.id)">
                  <v-icon>mdi-pencil</v-icon>
                </v-btn>

                <v-btn light icon @click="openRemove(cliente.id)">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </v-card-title>
            </v-row>
            <v-card-subtitle>
              <div class="pl-10">
                CPF: {{ cliente.cpf }}
              </div>
            </v-card-subtitle>
            <v-list two-line>
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="indigo">
                    mdi-email
                  </v-icon>
                </v-list-item-icon>

                <v-list-item-content>
                  <v-list-item-title> {{ cliente.email }}</v-list-item-title>
                  <v-list-item-subtitle>Personal</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-divider inset></v-divider>
            </v-list>
            <v-card-action>
              <v-btn text @click="openAdicionarPedido(cliente.id)">Adicionar Pedido</v-btn>
            </v-card-action>
          </v-card>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-container>
</template>

<script>
export default {
  name: "HelloWorld",

  data: () => ({
    clientes: [],
    remove: false,
    loading: false,
    idRemove: null,
    editDialog: false,
    addDialog: false,
    idEdit: null,
    addDialogPedido: false,
    produtos: [],
    formData: {
      nome: "",
      cpf: "",
      email: "",
      senha: "",
    },
    formPedido: {
      idCliente: "",
      itens: [{
        idProduto: "",
        quantidade: "",
      }]
    },
    clienteAddPedido: {
      id: "",
      nome: "",
      cpf: "",
      email: "",
      senha: "",
    },

  }),
  methods: {
    getClientes() {
      this.$http
        .get("/clientes")
        .then((response) => {
          this.clientes = response.data;
        })
        .catch((error) => {
          alert("Erro ao listar clientes");
          console.log(error);
        })
        .finally(() => {
        })
    },

    getCliente(id) {
      this.loading = true;
      this.$http
        .get(`/clientes/${id}`)
        .then((response) => {
          const cliente = response.data;
          this.formData.nome = cliente.nome;
          this.formData.cpf = cliente.cpf;
          this.formData.email = cliente.email;
          this.formData.senha = cliente.senha;
        })
        .catch((error) => {
          alert("Erro ao buscar cliente");
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        })
    },

    adicionarLinha() {
      this.formPedido.itens.push({ idProduto: "", quantidade: "" });
    },

    limparItemPedido() {
      this.formData.itemPedido = [{ produto: "", quantidade: "" }];
    },

    addClientes() {
      this.addDialog = false;
      this.loading = true;
      this.$http
        .post("/clientes", this.formData)
        .then((response) => {
          alert("Cliente adicionado com sucesso");
          this.getClientes();
        })
        .catch((error) => {
          alert("Erro ao adicionar cliente");
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        })
    },

    editCliente() {
      this.editDialog = false;
      this.loading = true;
      const id = this.idEdit;
      this.$http
        .put(`/clientes/${id}`, this.formData)
        .then((response) => {
          alert("Cliente editado com sucesso");
          this.getClientes();
        })
        .catch((error) => {
          alert("Erro ao editar cliente");
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        })
    },

    getProdutos() {
      this.$http
        .get("/produtos")
        .then((response) => {
          const apiResponse = response.data;
          this.produtos = apiResponse.map(produto => produto.id);
          // this.produtos = response.data;
        })
        .catch((error) => {
          alert("Erro ao listar produtos");
          console.log(error);
        })
        .finally(() => {
        })
    },

    addProdutos() {
      this.addDialogPedido = false;
      this.loading = true;
      this.$http
        .post("/pedidos", this.formPedido)
        .then((response) => {
          alert("Pedido adicionado com sucesso");
          this.getClientes();
        })
        .catch((error) => {
          alert("Erro ao adicionar pedido");
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        })
    },

    openRemove(id) {
      this.remove = true;
      this.idRemove = id;
    },

    openEdit(id) {
      this.idEdit = id;
      this.getCliente(id);
      this.editDialog = true;
    },

    openAdicionar() {
      this.addDialog = true;
    },

    openAdicionarPedido(id) {
      this.formPedido.idCliente = id;
      this.getClientePedido(id);
      this.getProdutos();
      this.addDialogPedido = true;
    },

    getClientePedido(id) {
      this.loading = true;
      this.$http
        .get(`/clientes/${id}`)
        .then((response) => {
          const cliente = response.data;
          this.clienteAddPedido.id = cliente.id;
          this.clienteAddPedido.nome = cliente.nome;
          this.clienteAddPedido.cpf = cliente.cpf;
          this.clienteAddPedido.email = cliente.email;
          this.clienteAddPedido.senha = cliente.senha;
        })
        .catch((error) => {
          alert("Erro ao buscar cliente");
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        })
    },

    deleteClientes() {
      this.remove = false;
      this.loading = true;
      const id = this.idRemove;
      this.$http
        .delete(`/clientes/${id}`)
        .then((response) => {
          alert("Cliente removido com sucesso");
          this.getClientes();
        })
        .catch((error) => {
          alert("Erro ao remover cliente");
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        })
    }
  },

  mounted() {
    this.getClientes();
  },
};
</script>
