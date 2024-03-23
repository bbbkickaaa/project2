package com.API.Alarm;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.API.Alarm.Entity.Alarm;

public interface AlarmRepository extends Repository<Alarm, Long> {
 
	Optional<Alarm> findById(Long id);
	List<Alarm> findByrecipientId(Long id, Pageable pageable);
	@Query("SELECT COUNT(a) FROM Alarm a WHERE a.recipientId = :recipientId AND a.view = false")
	Long countByRecipientId(@Param("recipientId") Long recipientId);
	Alarm save(Alarm alarm);
	void deleteById(Long id);
}
