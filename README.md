# fgeradores
Sistema de armazenamento de spawners para *Factions* com suporte a *todos* os spawners da versão *1.8.9*

### Como usar?
Após adicionar a jar do plugin em seu projeto, instancie a API do FGeradores:
````java
final ServicesManager servicesManager = Bukkit.getServicesManager();
final RegisteredServiceProvider<FactionGeneratorsApi> registration = servicesManager.getRegistration(FactionGeneratorsApi.class);
final FactionGeneratorsApi factionGeneratorsApi = registration.getProvider();
````

Com isso, é possivel acessar o metodo ````getFactionByTag()```` que retornará o objeto que salva os spawners armazenados.

