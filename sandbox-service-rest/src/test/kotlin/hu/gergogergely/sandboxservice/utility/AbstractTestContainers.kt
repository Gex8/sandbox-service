package hu.gergogergely.sandboxservice.utility

import org.junit.ClassRule
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import java.io.File

private const val DOCKER_FILE = "./environment/docker-compose.yml"

abstract class AbstractTestContainers {

    class Containers(file: File) : DockerComposeContainer<Containers>(file)

    companion object {

        private val environment = Containers(File(DOCKER_FILE)).withLocalCompose(false)
            .waitingFor("mongodb", WaitAllStrategy())

        @ClassRule
        val runningContainer = environment.start()
    }
}
