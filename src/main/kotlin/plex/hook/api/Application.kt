package plex.hook.api

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("plex.hook.api")
		.start()
}

