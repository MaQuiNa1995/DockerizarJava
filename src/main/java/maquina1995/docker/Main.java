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
