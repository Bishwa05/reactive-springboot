package reactive.bishwa;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2 // 	a Lombok annotation that results in the creation of a log field that is a Log4J logger being added to the class
@Component
@org.springframework.context.annotation.Profile("demo")
/**
 * 	this bean initializes sample data that is only useful for a demo.
 * 	We don’t want this sample data being initialized every time.
 * 	Spring’s Profile annotation tags an object for initialization only when the profile
 * 	that matches the profile specified in the annotation is specifically activated.
 */
class SampleDataInitializer
    implements ApplicationListener<ApplicationReadyEvent> {

    private final ProfileRepository repository;
    // we’ll use the ProfileRepository to handle persistence

    public SampleDataInitializer(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        repository
            .deleteAll() // here we start a reactive pipeline by first deleting everything in the database. This operation returns a Mono<T>. Both Mono<T> and Flux<T> support chaining processing with the thenMany(Publisher<T>) method. So, after the deleteAll() method completes, we then want to process the writes of new data to the database.
            .thenMany(
                Flux
                    .just("A", "B", "C", "D") // we use Reactor’s Flux<T>.just(T…​) factory method to create a new Publisher with a static list of String records, in-memory…​
                    .map(name -> new Profile(UUID.randomUUID().toString(), name + "@email.com")) // transform each record in turn into a Profile object
                    .flatMap(repository::save) // then persist to the database using our repository
            )
            .thenMany(repository.findAll()) // 	after all the data has been written to the database, we want to fetch all the records from the database to confirm what we have there
            .subscribe(profile -> log.info("saving " + profile.toString())); // if we’d stopped at the previous line, the save operation, and run this program then we would see…​ nothing! Publisher<T> instances are lazy — you need to subscribe() to them to trigger their execution. This last line is where the rubber meets the road. In this case, we’re using the subscribe(Consumer<T>) variant that lets us visit every record returned from the repository.findAll() operation and print out the record.
    }
}

