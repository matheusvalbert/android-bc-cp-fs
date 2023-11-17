# Android Broadcast Receiver Content Provider Foreground Services

ATENÇÃO: iniciar o App1 primeiro (criação da permissão e do content provider com o banco).

## App1

App que possui o content provider, além da possibilidade de salvar e visualizar os registros. Possui a declaração das permissões de leitura e escrita do content provider.

## App2

App que possui o broadcast receiver, assim que o dispositivo liga, aguarda o evento de "android.intent.action.BOOT_COMPLETED" e inicia o serviço.
O serviço verifica a cada 3s se os dados do content provider são iguais ao da última leitura, caso sejam diferentes, emite um log no terminal.
O app possui as opções de atualizar e apagar os registros do content provider (não existe verificação para saber se o registro existe ou não).