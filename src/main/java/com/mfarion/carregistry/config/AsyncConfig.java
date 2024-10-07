package com.mfarion.carregistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    // Configura y define un bean para el ejecutor de tareas asíncronas.


    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        //crea una instancia de ThreadPoolTaskExecutor
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //establece el tamaño mínimo del pool de hilos
        executor.setCorePoolSize(5);
        //establece el tamaño máximo del pool de hilos
        executor.setMaxPoolSize(10);
        //establece la capacidad de la cola de tareas
        executor.setQueueCapacity(100);
        //establece un prefijo para los nombres de los hilos
        executor.setThreadNamePrefix("carThread-");
        //inicializa el ejecutor
        executor.initialize();
        //retorna el ejecutor configurado
        return executor;

    }
}
