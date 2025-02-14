package ru.chuikov



import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.env.Environment


@SpringBootApplication
class Application(
    val environment: Environment
){

    @Bean
    fun modelMapper() = ModelMapper().also {
        it.configuration.matchingStrategy = MatchingStrategies.LOOSE
    }

    @Bean
    fun defineOpenAPI(): OpenAPI {
        val server: Server = Server()
        val serverUrl: String = environment.getProperty("api.server.url")?:""
        server.setUrl(serverUrl)
        server.setDescription("Development")


        val info: Info = Info()
            .title("Профессионалы 2025")
            .description("В разработке.")
        return OpenAPI().info(info).servers(listOf(server))
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
fun String.checkPrefix() = if (this!="")  "${this}_" else this

fun String.getToken():String?{
    var spl = this.split(" ")
    if (spl.size==2) return spl[1]
    else return null
}

//
//@Profile("dev")
//@Bean(initMethod = "start", destroyMethod = "stop")
//@Throws(
//    SQLException::class
//)
//fun h2Server(): Server {
//    return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")
//}