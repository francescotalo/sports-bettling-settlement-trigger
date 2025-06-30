package sporty.interview.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sporty.interview.jpa.model.Bet;

public interface BetsRepository extends JpaRepository<Bet, Long> {

	List<Bet> findByEventIdAndEventWinnerIdIsNull(Long eventId);
	
}
