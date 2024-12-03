# Lab5 - bodocongo-torrent - 24.1

## Objetivo
Neste laboratório, exploraremos uma construção essencial de golang para concorrência: **select statements**

Como motivação, iremos construir uma aplicação distribuída. Essa aplicação funciona como um buscador de arquivos para filesharing (pense em `Bittorrent`). Então, um cliente deve procurar que máquinas na rede armazenam o arquivo buscado. Como chave de busca, ele deve passar o hash do arquivo (calcular tal como o sum dos labs passados). Considere que haverá, pelo menos, um grupo de quatro máquinas.

1. O time precisará pensar em um modelo de comunicação. Sockets funcionam. 
2. Você precisará pensar em um esquema de organização. Completamente P2P ou cliente-servidor? 
3. Você precisa pensar em um esquema de descoberta. Quais as máquinas que fazem parte do sistema.
4. Desempenho continua sendo importante. Pense em minimizar o tempo total, do ponto de vista de um cliente, para obter a lista de máquinas (seus IPs). Embora a lista de otimizações possíveis seja enorme, primeiro **FAZ FUNCIONAR**!
5. Considere que os arquivos buscados estão em um diretório no /tmp. Por exemplo, /tmp/dataset


## Grupo

- Samuel Cabral de Luna - 121210376
- Victor Vinicius Freire de Araújo - 1211110361
- Paola Kathrein Moura Marques - 121111034
- Marcos Antônio Cardoso Pereira - 121210927

## COMO EXECUTAR

## Setup inicial

Edite o arquivo `peers.txt` e adicione os IPs e portas das máquinas que participarão da execução do programa, exceto o IP da própria máquina. Cada linha deve conter um IP e uma porta no formato `IP:PORTA`.

- Exemplo de `peers.txt`:

```
192.168.1.2:8080
192.168.1.3:8080
192.168.1.4:8080
```
## Executando o programa

Execute o programa em máquinas diferentes utilizando o comando abaixo:

```
go run *.go
```

## Buscando um Hash de Arquivo

Após a execução do programa, você pode buscar um hash de arquivo digitando o `filehash` no terminal.

O programa irá procurar o hash informado entre os peers configurados.

