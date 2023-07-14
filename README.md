# fgeradores
Sistema de armazenamento de spawners para *Factions* com suporte a *todos* os spawners da versão *1.8.9*

### Como usar?
Após adicionar a jar do plugin em seu projeto, instancie a API do FGeradores:
````java
ServicesManager servicesManager = Bukkit.getServicesManager();
RegisteredServiceProvider<FactionGeneratorsApi> registration = servicesManager.getRegistration(FactionGeneratorsApi.class);
FactionGeneratorsApi factionGeneratorsApi = registration.getProvider();
````

Com isso, é possivel acessar o metodo ````factionGeneratorsApi.getFactionByTag()```` que retornará o objeto que salva os spawners armazenados.

