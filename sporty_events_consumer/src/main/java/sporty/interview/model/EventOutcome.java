package sporty.interview.model;

import java.util.Objects;

public class EventOutcome {
	
	private Long eventId;
	
	private Long eventWinnerId;
	
	private String eventName;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getEventWinnerId() {
		return eventWinnerId;
	}

	public void setEventWinnerId(Long eventWinnerId) {
		this.eventWinnerId = eventWinnerId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(eventId, eventName, eventWinnerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventOutcome other = (EventOutcome) obj;
		return Objects.equals(eventId, other.eventId) && Objects.equals(eventName, other.eventName)
				&& Objects.equals(eventWinnerId, other.eventWinnerId);
	}

	@Override
	public String toString() {
		return "EventOutcome [eventId=" + eventId + ", eventWinnerId=" + eventWinnerId + ", eventName=" + eventName
				+ "]";
	}
	
	

}
