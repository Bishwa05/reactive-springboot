package reactive.bishwa;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// A reactive service

@Log4j2
@Service
class ProfileService {

    private final ApplicationEventPublisher publisher; // we’ll want to publish events to other beans managed in the Spring ApplicationContext. Earlier, we defined an ApplicationListener<ApplicationReadyEvent> that listened for an event that was published in the ApplicationContext. Now, we’re going to publish an event for consumption of other beans of our devices in the ApplicationContext.
    private final ProfileRepository profileRepository; // we defer to our repository to

    ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
        this.publisher = publisher;
        this.profileRepository = profileRepository;
    }

    public Flux<Profile> all() { // ​find all documents
        return this.profileRepository.findAll();
    }

    public Mono<Profile> get(String id) { // ​find a document by its ID
        return this.profileRepository.findById(id);
    }

    public Mono<Profile> update(String id, String email) { // update a Profile and give it a new email
        return this.profileRepository
            .findById(id)
            .map(p -> new Profile(p.getId(), email))
            .flatMap(this.profileRepository::save);
    }

    public Mono<Profile> delete(String id) { // delete a record by its id
        return this.profileRepository
            .findById(id)
            .flatMap(p -> this.profileRepository.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<Profile> create(String email) { // create a new Profile in the database and publish an ApplicationContextEvent, one of our own creation called ProfileCreatedEvent, on successful write to the database. The doOnSuccess callback takes a Consumer<T> that gets invoked after the data in the reactive pipeline has been written to the database. We’ll see later why this event is so useful.
        return this.profileRepository
            .save(new Profile(null, email))
            .doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
    }
}

