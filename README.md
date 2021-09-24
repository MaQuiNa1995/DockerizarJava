# Configurar pom.xml

Lo primero que tendremos que hacer es cofigurar nuestro pom para que cree un jar funcional, para esto nuestra aplicación deberá tener un main de entrada y tener configurado en el pom.xml los siguientes plugin en la parte de build

```
<build>
	<plugins>
		// Este se usa para cuando nuestra aplicación es spring boot
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
		// Este se usa para crear nuestro manifest automaticamente
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-jar-plugin</artifactId>
			<configuration>
				<archive>
					<manifest>
						<addClasspath>true</addClasspath>
						<classpathPrefix>lib/</classpathPrefix>
						// Aqui tendremos que indicar la ruta a nuestra clase main
						<mainClass>maquina1995.docker.Main</mainClass>
					</manifest>
				</archive>
			</configuration>
		</plugin>
	</plugins>
</build>
```
# Crear la aplicación

Despues de configurar el pom tendremos que crear el código de la aplicación en este caso he optado por un main y un controller embebido para simplificar las cosas y que la guía quede lo mas limpia posible

```
package maquina1995.docker;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@GetMapping
	public String holaMundo() {
		LOGGER.info("Se ha entrado al controller");
		return "Hola mundo desde docker";
	}
}
```


# Crear el jar

Tendremos que ir en la consola a nuestra ruta de la aplicación y ejecutar el `mvn clean package` para que nos cree nuestro jar en la carpeta llamada target

```
C:\Users\MaQuiNa1995\workspace\docker>mvn clean package
[INFO] Scanning for projects...
[INFO]
[INFO] -------------------------< maquina1995:docker >-------------------------
[INFO] Building docker 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ docker ---
[INFO] Deleting C:\Users\MaQuiNa1995\workspace\docker\target
[INFO]
[INFO] --- maven-resources-plugin:3.2.0:resources (default-resources) @ docker ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] Copying 0 resource
[INFO] Copying 0 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ docker ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 2 source files to C:\Users\MaQuiNa1995\workspace\docker\target\classes
[INFO]
[INFO] --- maven-resources-plugin:3.2.0:testResources (default-testResources) @ docker ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] Copying 0 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ docker ---
[INFO] Changes detected - recompiling the module!
[INFO]
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) @ docker ---
[INFO]
[INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ docker ---
[INFO] Building jar: C:\Users\MaQuiNa1995\workspace\docker\target\docker-0.0.1-SNAPSHOT.jar
[INFO]
[INFO] --- spring-boot-maven-plugin:2.5.4:repackage (repackage) @ docker ---
[INFO] Replacing main artifact with repackaged archive
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.799 s
[INFO] Finished at: 2021-09-24T23:52:26+02:00
[INFO] ------------------------------------------------------------------------
```

# Crear dockerfile

Será necesario crear nuestro dockerfile que llevará la configuración de nuestra imagen de docker este archivo tendrá que ir en la ruta raiz de nuestro proyecto

```
FROM openjdk:8-jdk-alpine
MAINTAINER MaQuiNa1995
COPY target/docker-0.0.1-SNAPSHOT.jar docker-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/docker-0.0.1-SNAPSHOT.jar"]
```
`docker-0.0.1-SNAPSHOT.jar` -> hace referencia al nombre del jar de nuestro proyecto que es el resultado de la unión de `artifactId-version.jar`

# Crear Imagen

Será necesario ejecutar el siguiente comando en la ruta raiz de nuestro proyecto donde debería de estar nuestro dockerfile tambien

Chuleta: `docker build --tag=nombreContenedor:version .`

```
C:\Users\MaQuiNa1995\workspace\docker>docker build --tag=hola-mundo:latest .

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.4)

2021-09-24 21:37:11.131  INFO 1 --- [           main] maquina1995.docker.Main                  : Starting Main v0.0.1-SNAPSHOT using Java 1.8.0_212 on 25e2fb055b6a with PID 1 (/docker-0.0.1-SNAPSHOT.jar started by root in /)
[+] Building 2.0s (8/8) FINISHED
 => [internal] load build definition from Dockerfile                                                                                                                                     0.0s
 => => transferring dockerfile: 32B                                                                                                                                                      0.0s
 => [internal] load .dockerignore                                                                                                                                                        0.0s
 => => transferring context: 2B                                                                                                                                                          0.0s
 => [internal] load metadata for docker.io/library/openjdk:8-jdk-alpine                                                                                                                  1.5s
 => [auth] library/openjdk:pull token for registry-1.docker.io                                                                                                                           0.0s
 => [internal] load build context                                                                                                                                                        0.2s
 => => transferring context: 17.35MB                                                                                                                                                     0.2s
 => CACHED [1/2] FROM docker.io/library/openjdk:8-jdk-alpine@sha256:94792824df2df33402f201713f932b58cb9de94a0cd524164a0f2283343547b3                                                     0.0s
 => [2/2] COPY target/docker-0.0.1-SNAPSHOT.jar docker-0.0.1-SNAPSHOT.jar                                                                                                                0.1s
 => exporting to image                                                                                                                                                                   0.1s
 => => exporting layers                                                                                                                                                                  0.1s
 => => writing image sha256:0447799d352aad73febbec595a77011c29800b440b5c6135123adf0c320a0c37                                                                                             0.0s
 => => naming to docker.io/library/hola-mundo:latest                                                                                                                                     0.0s
  '  |____| .__|_| |_|_| |_\__, | / / / /
Use 'docker scan' to run Snyk tests against images to find vulnerabilities and learn how to fix them
 :: Spring Boot ::                (v2.5.4)
 ```
 
 # Ejecutar Imagen 
 
 Chuleta: `docker run -pPuertoOrdenadorLocal:PuertoAplicaciónDocker nombreContenedor:version`
 
 ```
 C:\Users\MaQuiNa1995\workspace\docker>docker run -p9999:8080 hola-mundo:latest

,--.   ,--.         ,-----.           ,--.,--.  ,--.         ,--. ,---.  ,---. ,-----.        ,----.   ,--.  ,--.  ,--.             ,--.
|   `.'   | ,--,--.'  .-.  '  ,--.,--.`--'|  ,'.|  | ,--,--./   || o   \| o   \|  .--',-----.'  .-./   `--',-'  '-.|  ,---. ,--.,--.|  |-.
|  |'.'|  |' ,-.  ||  | |  |  |  ||  |,--.|  |' '  |' ,-.  |`|  |`..'  |`..'  |'--. `\'-----'|  | .---.,--.'-.  .-'|  .-.  ||  ||  || .-. '
|  |   |  |\ '-'  |'  '-'  '-.'  ''  '|  ||  | `   |\ '-'  | |  | .'  /  .'  / .--'  /       '  '--'  ||  |  |  |  |  | |  |'  ''  '| `-' |
`--'   `--' `--`--' `-----'--' `----' `--'`--'  `--' `--`--' `--' `--'   `--'  `----'         `------' `--'  `--'  `--' `--' `----'  `---'

Ejemplo de dockerización de aplicación Java Spring Boot
2021-09-24 22:19:23.214  INFO 1 --- [           main] maquina1995.docker.Main                  : Starting Main v0.0.1-SNAPSHOT using Java 1.8.0_212 on 19e4267c23f1 with PID 1 (/docker-0.0.1-SNAPSHOT.jar started by root in /)
2021-09-24 22:19:23.220  INFO 1 --- [           main] maquina1995.docker.Main                  : No active profile set, falling back to default profiles: default
2021-09-24 22:19:24.565  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-09-24 22:19:24.586  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-09-24 22:19:24.587  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.52]
2021-09-24 22:19:24.682  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-09-24 22:19:24.682  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1365 ms
2021-09-24 22:19:25.247  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-09-24 22:19:25.262  INFO 1 --- [           main] maquina1995.docker.Main                  : Started Main in 2.624 seconds (JVM running for 3.158)
 ```
 
# Comprobar la ejecución

para comprobar que nuestra imagen está levantada iremos a http://localhost:9999/

y veremos en pantalla `Hola mundo desde docker`

Adicionalmente podremos ver en la consola de nuestro docker la siguiente traza

`2021-09-24 22:21:33.024  INFO 1 --- [nio-8080-exec-1] maquina1995.docker.Main                  : Se ha entrado al controller`
