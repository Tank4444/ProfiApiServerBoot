package ru.chuikov


import org.apache.tika.Tika
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.modelmapper.spi.MatchingStrategy
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@SpringBootApplication
class Application{

    @Bean
    fun modelMapper() = ModelMapper().also {
        it.configuration.matchingStrategy = MatchingStrategies.LOOSE
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
fun String.checkPrefix() = if (this!="")  "${this}_" else this

//
//@Profile("dev")
//@Bean(initMethod = "start", destroyMethod = "stop")
//@Throws(
//    SQLException::class
//)
//fun h2Server(): Server {
//    return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")
//}