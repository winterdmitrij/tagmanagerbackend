package de.vinter.tagmanager.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.vinter.tagmanager.models.Tag;
import de.vinter.tagmanager.models.TagGroup;

@Repository
public interface TagRepository extends JpaRepository<Tag, String>{
	@Transactional
	@Modifying
	@Query("DELETE FROM Tag t WHERE t.tagId = ?1")
	void delTagById(String tagId);
	
	@Query("SELECT t FROM Tag t LEFT JOIN TagGroup tg ON t.tagGroup = tg.tagGroupId WHERE tg.tagGroupId = ?1")
	List<Tag> findAllByTagGroupId(String tagGroupId);
	
	@Query("SELECT t FROM Tag t WHERE t.tagGroup = ?1")
	List<Tag> findByTagGroup(Optional<TagGroup> tagGroup);
}