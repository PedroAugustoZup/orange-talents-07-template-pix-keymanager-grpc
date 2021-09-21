package br.com.alura.utils

import jakarta.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class TransactionalEvent {
    @Transactional
    open fun execute(runnable: Runnable){
        runnable.run()
    }
}
