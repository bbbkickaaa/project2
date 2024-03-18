package com.API.Message;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import com.API.Message.Entity.Message;

public interface MessageRepository extends Repository<Message, Long> {

	Optional<Message> findById(Long id);
	Message save(Message message);
	void deleteById(Long id);
}
