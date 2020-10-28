package reactive.bishwa;

import org.springframework.context.ApplicationEvent;

public class ProfileCreatedEvent extends ApplicationEvent {

    public ProfileCreatedEvent(Profile source) {
        super(source);
    }
}