package reactive.bishwa;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController // this is yet another stereotype annotation that tells Spring WebFlux that this class provides HTTP handler methods
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)  // There are some attributes that are common to all the HTTP endpoints, like the root URI, and the default content-type of all responses produced. You can use @RequestMapping to spell this out at the class level and the configuration is inherited for each subordinate handler method
@org.springframework.context.annotation.Profile("classic")
class ProfileRestController {

    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
    private final ProfileService profileRepository;

    ProfileRestController(ProfileService profileRepository) {
        this.profileRepository = profileRepository;
    }

    // There are specializations of @RequestMapping, one for each HTTP verb, that you can use. This annotation says, "this endpoint is identical to that specified in the root. @RequestMapping except that it is limited to HTTP GET endpoints"
    @GetMapping
    Publisher<Profile> getAll() {
        return this.profileRepository.all();
    }

    // This endpoint uses a path variable — a part of the URI that is matched against the incoming request and used to extract a parameter. In this case, it extracts the id parameter and makes it available as a method parameter in the handler method.
    @GetMapping("/{id}")
    Publisher<Profile> getById(@PathVariable("id") String id) {
        return this.profileRepository.get(id);
    }

    // This method supports creating a new Profile with an HTTP POST action. In this handler method we expect incoming requests to have a JSON body that the framework then marshals into a Java object, Profile. This happens automatically based on the content-type of the incoming request and the configured, acceptable, convertible payloads supported by Spring WebFlux.
    @PostMapping
    Publisher<ResponseEntity<Profile>> create(@RequestBody Profile profile) {
        return this.profileRepository
            .create(profile.getEmail())
            .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
                .contentType(mediaType)
                .build());
    }

    @DeleteMapping("/{id}")
    Publisher<Profile> deleteById(@PathVariable String id) {
        return this.profileRepository.delete(id);
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<Profile>> updateById(@PathVariable String id, @RequestBody Profile profile) {
        return Mono
            .just(profile)
            .flatMap(p -> this.profileRepository.update(id, p.getEmail()))
            .map(p -> ResponseEntity
                .ok()
                .contentType(this.mediaType)
                .build());
    }
}
