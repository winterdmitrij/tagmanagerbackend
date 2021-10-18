package de.vinter.tagmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.vinter.tagmanager.models.TagGroup;

@Repository
public interface TagGroupRepository extends JpaRepository<TagGroup, String>{
}
