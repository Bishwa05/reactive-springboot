package reactive.bishwa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document // identifies the entity as a document to be persisted in MongoDB
@Data

/**
 * @AllArgsConstructor, and @NoArgsConstructor are all from Lombok.
 * They’re compile-time annotations that tell Lombok to generate getters/setters,
 * constructors, a toString() method and an equals method.
 */
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id // @Id is a Spring Data annotation that identifies the document ID for this document
    private String id;

    // this field email is the thing that we want to store and retrieve later
    private String email;
}
