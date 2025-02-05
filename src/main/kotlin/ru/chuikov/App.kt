package ru.chuikov


import org.apache.tika.Tika
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
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