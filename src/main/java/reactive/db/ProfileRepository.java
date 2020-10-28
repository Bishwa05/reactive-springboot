package reactive.db;

import reactive.bishwa.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
