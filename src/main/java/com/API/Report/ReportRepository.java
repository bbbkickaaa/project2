package com.API.Report;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.API.Report.DTO.ReportDTO;
import com.API.Report.Entity.Report;

public interface ReportRepository extends Repository<Report,Long> {
	
	void save (Report report);
	Optional<Report> findById(Long id);
	
	@Query("SELECT new com.API.Report.DTO.ReportDTO(b.id, b.category, b.content, u.id , u.nickname) " +
		       "FROM Report b JOIN b.userId u")
	Page<ReportDTO> findAllReports(Pageable pageable);

}
