package reactive.bishwa;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
