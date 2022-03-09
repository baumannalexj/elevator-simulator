package application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

//import org.springframework.boot.runApplication

@SpringBootApplication
open class ElevatorApplication

//    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<ElevatorApplication>(*args)
    }
