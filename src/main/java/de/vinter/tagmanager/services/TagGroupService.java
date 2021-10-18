package de.vinter.tagmanager.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.vinter.tagmanager.models.Tag;
import de.vinter.tagmanager.models.TagGroup;
import de.vinter.tagmanager.repositories.InstanceRepository;
import de.vinter.tagmanager.repositories.TagGroupRepository;
import de.vinter.tagmanager.repositories.TagRepository;

@Service
public class TagGroupService {
	
	@Autowired
	TagGroupRepository tagGroupRepository;
	
	@Autowired
	InstanceRepository instanceRepository;
	
	@Autowired
	TagRepository tagRepository;
	
	//-------------------------------------------------------------------
	/*
	 * GET
	 */
	public List<TagGroup> getAllTagGroups() {
		return tagGroupRepository.findAll();
	}
	
	public TagGroup getTagGroupById(String tagGroupId) {
		return tagGroupRepository.findById(tagGroupId)
				.orElseThrow( () -> new IllegalStateException("TagGroup existiert nicht") );
	}
	
	//-------------------------------------------------------------------
	/*
	 * POST
	 */
	public TagGroup addNewTagGroup(TagGroup tagGroup) {
		if ( tagGroupRepository.existsById( tagGroup.getTagGroupId() ) ) {
			throw new IllegalStateException("TagGroup existiert schon");
		}
		return tagGroupRepository.save(tagGroup);
	}
	
	//-------------------------------------------------------------------
	/*
	 * DELETE
	 */
	public void delTagGroupById(String tagGroupId) {
		if ( !tagGroupRepository.existsById(tagGroupId) ) {
			throw new IllegalStateException("TagGroup existiert nicht");
		}
		tagGroupRepository.deleteById(tagGroupId);
	}

	//-------------------------------------------------------------------
	/*
	 * PUT
	 */
	@Transactional
	public TagGroup updTagGroup(TagGroup tagGroup) {
		TagGroup editTagGroup = tagGroupRepository.findById(tagGroup.getTagGroupId())
				.orElseThrow( () -> new IllegalStateException("Tag Group existiert nicht") );
		// Title
		if ( tagGroup.getTagGroupTitle() != null && 
				tagGroup.getTagGroupTitle().length() > 0 &&
				!Objects.equals( editTagGroup.getTagGroupTitle(), tagGroup.getTagGroupTitle() ) ) {
			editTagGroup.setTagGroupTitle(tagGroup.getTagGroupTitle());
		}
		// Topic
		if ( tagGroup.getTopic() != null && 
				tagGroup.getTopic().length() > 0 &&
				!Objects.equals( editTagGroup.getTopic(), tagGroup.getTopic() ) ) {
			editTagGroup.setTopic(tagGroup.getTopic());
		}
		// Help
		if ( tagGroup.getHelp() != null && 
				tagGroup.getHelp().length() > 0 &&
				!Objects.equals( editTagGroup.getHelp(), tagGroup.getHelp() ) ) {
			editTagGroup.setHelp(tagGroup.getHelp());
		}
		return tagGroupRepository.save(editTagGroup);
	}
}
