package de.vinter.tagmanager.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.vinter.tagmanager.models.Instance;

@Repository
public interface InstanceRepository extends JpaRepository<Instance, String>{
	
	@Query("SELECT i FROM Instance i WHERE i.site = ?1")
	Instance getInstanceById(String site);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Instance i WHERE i.site = ?1")
	void delInstanceById(String site);
}