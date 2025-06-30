package sporty.interview.jpa.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name="bets", indexes = {
		  @Index(name = "event_id_index", columnList = "eventId", unique=false)
		  })
public class Bet {
	 @Id 
	 @GeneratedValue(strategy = GenerationType.AUTO) 
	 private Long id;

	 @Column(name = "user_id", nullable = false)
	 private Long userId;

	@Column(name = "event_id", nullable = false)
	 private Long eventId;
	 
	 @Column(name = "event_market_id", nullable = false)
	 private Long eventMarketId;
	 
	 @Column(name = "event_winner_id", nullable = true)
	 private Long eventWinnerId;
	 
	 @Column(name = "amount", nullable = false)
	 private Double amount;
	 
	 public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getEventMarketId() {
		return eventMarketId;
	}

	public void setEventMarketId(Long eventMarketId) {
		this.eventMarketId = eventMarketId;
	}

	public Long getEventWinnerId() {
		return eventWinnerId;
	}

	public void setEventWinnerId(Long eventWinnerId) {
		this.eventWinnerId = eventWinnerId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, eventId, eventMarketId, eventWinnerId, id, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bet other = (Bet) obj;
		return amount == other.amount && eventId == other.eventId && eventMarketId == other.eventMarketId
				&& eventWinnerId == other.eventWinnerId && id == other.id && userId == other.userId;
	}

	@Override
	public String toString() {
		return "Bet [id=" + id + ", userId=" + userId + ", eventId=" + eventId + ", eventMarketId=" + eventMarketId
				+ ", eventWinnerId=" + eventWinnerId + ", amount=" + amount + "]";
	}
	
	
}
