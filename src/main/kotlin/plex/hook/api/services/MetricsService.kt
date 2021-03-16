package plex.hook.api.services

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import javax.inject.Singleton

@Singleton
class MetricsService(
        meterRegistry: MeterRegistry
) {
    private var receivedCounter: Counter = Counter
            .builder("hook.received")
            .register(meterRegistry)

    private var unmonitoredEventCounter: Counter = Counter
            .builder("hook.ignored")
            .tag("type", "event")
            .register(meterRegistry)

    private var unmonitoredUserCounter: Counter = Counter
            .builder("hook.ignored")
            .tag("type", "user")
            .register(meterRegistry)

    private var unmonitoredServerCounter: Counter = Counter
            .builder("hook.ignored")
            .tag("type", "server")
            .register(meterRegistry)


    fun incReceived() = receivedCounter.increment()
    fun incUnwantedEvent() = unmonitoredEventCounter.increment()
    fun incUnwantedUser() = unmonitoredUserCounter.increment()
    fun incUnwantedServer() = unmonitoredServerCounter.increment()
}
