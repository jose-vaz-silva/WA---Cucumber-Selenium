# language: pt
@Login
Funcionalidade: Automation Pratice

  @CT_AUT_001
  Cenário: realizar login no sistema
    Dado que o sistema de e-commerce é acessado
    E a página de sign in é acessada
    Quando o login é realizado com "${user}" usuário e senha "${password}"
    Então a página de ‘minha conta’ é exibida com sucesso

  @CT_AUT_002
  Cenário: Validar se não é possível acessar página de cadastro sem o preenchimento do campo email
    Dado que o sistema de e-commerce é acessado
    E a página de sign in é acessada
    Quando acessar página de cadastro de contas
    Então a seguinte mensagem de erro "Invalid email address." é exibida na página

  @CT_AUTO_003
  Cenário: Validar cadastro de contas com sucesso
    Dado que o sistema de e-commerce é acessado
    E a página de ‘sign in’ é acessada
    E em ‘authentication’ o campo "Email address" preenchido com valor "gabrielly.sophia@gmail.com"
    E a criação de contas é iniciada
    * o campo "First name" preenchido com com valor "gabrielly" em criação de contas
    * o campo "Last name" preenchido com com valor "sophia" em criação de contas
    * o campo "Password" preenchido com com valor "automation@15" em criação de contas
    * o campo "Adress" preenchido com com valor "3132  Doctors Drive" em criação de contas
    * o campo "City" preenchido com com valor "Los Angeles" em criação de contas
    * o campo "State" preenchido com com valor "California" em criação de contas
    * o campo "Zip/Postal Code" preenchido com com valor "90017" em criação de contas
    * o campo "Country" preenchido com com valor "United State" em criação de contas
    * o campo "Mobile phone" preenchido com com valor "213-200-6282 em criação de contas
    Quando registrar conta
    Então a página de ‘minha conta’ é exibida com sucesso

  @CT_AUTO_004
  Cenário: Pesquisar item no e-commerce
    Dado que o sistema de e-commerce é acessado
    Quando o item "Faded Short Sleeve T-shirts" é pesquisado no e-commerce
    Então o item "Faded Short Sleeve T-shirts" é exibido na página

  @CT_AUTO_005
  Cenário: Adicionar item no carrinho
    Dado que o sistema de e-commerce é acessado
    E o item "Faded Short Sleeve T-shirts" é pesquisado no e-commerce
    Quando o item "Faded Short Sleeve T-shirts" é adicionado no carrinho
    Então a mensagem de inserção de item no carrinho com sucesso "Product successfully added to your shopping cart" é exibida
    E é adicionado o item "Faded Short Sleeve T-shirts" no carrinho

  @CT_AUTO_006
  Cenário: Remover item do carrinho
    Dado que o sistema de e-commerce é acessado
    E o item "Faded Short Sleeve T-shirts" é pesquisado no e-commerce
    E o item "Faded Short Sleeve T-shirts" é adicionado no carrinho
    E a mensagem de inserção de item no carrinho com sucesso "Product successfully added to your shopping cart" é exibida
    Quando o item "Faded Short Sleeve T-shirts" é removido do carrinho
    Então na página do carrinho é exibida a seguinte mensagem "Your shopping cart is empty"

  @CT_AUTO_007
  Cenário: Verificar se ao realizar checkout no carrinho sem logar no sistema é exibido a página de ‘sign in
    Dado que o sistema de e-commerce é acessado
    E o item "Faded Short Sleeve T-shirts" é pesquisado no e-commerce
    E o item "Faded Short Sleeve T-shirts" é adicionado no carrinho
    E a mensagem de inserção de item no carrinho com sucesso "Product successfully added to your shopping cart" é exibida
    Quando realizar checkout no carrinho
    Então é exibido a página de ‘sign in’

  @CT_AUTO_008
  Cenário: Verificar se não é possível continuar com a compra do item sem aceitar os termos de serviço
    Dado que o sistema de e-commerce é acessado
    Quando o login é realizado com "${user}" usuário e senha "${password}"
    E o item "Faded Short Sleeve T-shirts" é pesquisado no e-commerce
    E o item "Faded Short Sleeve T-shirts" é adicionado no carrinho
    E em página do carrinho prosseguir para "shiping"
    Quando em página do carrinho prosseguir para "Payment"
    Então em página do carrinho é exibido a seguinte alerta "You must agree to the terms of service before continuing"

  @CT_AUTO_009
  Cenário: Finalizar ordem de compra pela forma de pagamento ‘Pay by bank wire’
    Dado que o sistema de e-commerce é acessado
    Quando o login é realizado com "${user}" usuário e senha "${password}"
    E o item "Faded Short Sleeve T-shirts" é pesquisado no e-commerce
    E o item "Faded Short Sleeve T-shirts" é adicionado no carrinho
    E em página do carrinho prosseguir para "shiping"
    E os termos de serviços são aceitos
    E em página do carrinho prosseguir para "Payment"
    Quando selecionar forma de pagamento "Pay by bank wire"
    E a ordem é confirmada
    Então a ordem é finalizada com a mensagem "Your order on My Store is complete."

  @CT_AUTO_010
  Cenário: Finalizar ordem de compra pela forma de pagamento ‘Pay by check’
    Dado que o sistema de e-commerce é acessado
    Quando o login é realizado com "${user}" usuário e senha "${password}"
    E o item "Faded Short Sleeve T-shirts" é pesquisado no e-commerce
    E o item "Faded Short Sleeve T-shirts" é adicionado no carrinho
    E em página do carrinho prosseguir para "shiping"
    E os termos de serviços são aceitos
    E em página do carrinho prosseguir para "Payment"
    Quando selecionar forma de pagamento "Pay by check"
    E a ordem é confirmada
    Então a ordem é finalizada com a mensagem "Your order on My Store is complete."
