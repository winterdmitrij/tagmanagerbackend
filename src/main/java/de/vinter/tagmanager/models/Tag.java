package de.vinter.tagmanager.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "tags")
public class Tag {
	@Id
	private String tagId;
	private String tagTitle;
	
	@JsonBackReference(value = "taggroup-tag")
	@ManyToOne(cascade = {
			CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.PERSIST,
			CascadeType.REFRESH})
	@JoinColumn(name = "tag_group_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TagGroup tagGroup;
	
	//-------------------------------------------------------------------
	/*
	 *  Constructors
	 */
	public Tag() {
	}
	public Tag(String tagId, String tagTitle) {
		this.tagId = tagId;
		this.tagTitle = tagTitle;
	}
	
	//-------------------------------------------------------------------
	/*
	 *  Getters and Setters
	 */
	public String getTagId() { return tagId; }
	public void setTagId(String tagId) { this.tagId = tagId; }
	
	public String getTagTitle() { return tagTitle; }
	public void setTagTitle(String tagTitle) { this.tagTitle = tagTitle; }
	
	public TagGroup getTagGroup() { return tagGroup; }
	public void setTagGroup(TagGroup tagGroup) { this.tagGroup = tagGroup; }
	
	//-------------------------------------------------------------------
	@Override
	public String toString() {
		return "\n        {\"tagId\": \"" + tagId + "\", \"tagTitle\": \"" + tagTitle + "\"}";
	}
}
