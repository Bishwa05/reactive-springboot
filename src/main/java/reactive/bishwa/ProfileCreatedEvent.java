package reactive.bishwa;

import org.springframework.context.ApplicationEvent;
// Like spring application event
public class ProfileCreatedEvent extends ApplicationEvent {

    public ProfileCreatedEvent(Profile source) {
        super(source);
    }
}