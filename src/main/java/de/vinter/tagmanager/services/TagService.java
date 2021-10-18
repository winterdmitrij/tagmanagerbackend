package de.vinter.tagmanager.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.vinter.tagmanager.models.Tag;
import de.vinter.tagmanager.models.TagGroup;
import de.vinter.tagmanager.repositories.TagGroupRepository;
import de.vinter.tagmanager.repositories.TagRepository;

@Service
public class TagService {

	@Autowired
	TagRepository tagRepository;
	
	@Autowired
	TagGroupRepository tagGroupRepository;
	
	@Autowired
	TagGroupService tagGroupService;
	
	//-------------------------------------------------------------------
	/*
	 * GET
	 */
	public List<Tag> getAllTags() {
		return tagRepository.findAll();
	}
	public Tag getTagById(String tagId) {
		return tagRepository.findById(tagId).orElseThrow(
				() -> new IllegalStateException("Tag mit tagId: " + tagId + " existiert nicht.") );
	}
	public List<Tag> getTagsByTagGroupId(String tagGroupId) {
		return tagRepository.findAllByTagGroupId(tagGroupId);
	}
	
	//-------------------------------------------------------------------
	/*
	 * POST
	 */
	public Tag addNewTag(String tagGroupId, Tag tag) {
		if ( tagRepository.existsById( tag.getTagId() ) ) {
			throw new IllegalStateException("Tag mit tagId: " + tag.getTagId() + " existiert schon.");
		}
		TagGroup tagGroup = tagGroupService.getTagGroupById(tagGroupId);
		tagGroup.setTag(tag);
		return tagRepository.save(tag);
	}

	//-------------------------------------------------------------------
	/*
	 * DELETE
	 */
	public void delTagById(String tagId) {
		if ( !tagRepository.existsById(tagId) ) {
			throw new IllegalStateException("Tag mit tagId: " + tagId + " existiert nicht.");
		}
		tagRepository.delTagById(tagId);
	}
	
	//-------------------------------------------------------------------
	/*
	 * PUT
	 */
	public Tag updTag(Tag tag) {
		Tag editTag = tagRepository.findById(tag.getTagId())
				.orElseThrow( () -> new IllegalStateException("Tag existiert nicht") );
		// Title
		if ( tag.getTagTitle() != null &&
				tag.getTagTitle().length() > 0 &&
				!Objects.equals(editTag.getTagTitle(), tag.getTagTitle()) ) {
			editTag.setTagTitle(tag.getTagTitle());
		}
		return tagRepository.save(editTag);
	}
}
