package de.vinter.tagmanager.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tag_groups")
public class TagGroup {
	@Id
	private String tagGroupId;
	private String tagGroupTitle;
	private String topic;
	private String help;
	
	@JsonBackReference(value = "instance-taggroup")
	@ManyToMany(
			cascade = {
					CascadeType.DETACH,
					CascadeType.MERGE,
					CascadeType.PERSIST,
					CascadeType.REFRESH})
	@JoinTable(
			name = "instances_taggroups",
			joinColumns = @JoinColumn(name = "tag_group_id"),
			inverseJoinColumns = @JoinColumn(name = "instance_id"))
	private List<Instance> instances = new ArrayList<>();

	
	@JsonManagedReference(value = "taggroup-tag")
	@OneToMany(
			fetch = FetchType.EAGER,
			mappedBy = "tagGroup",
			orphanRemoval = true,
			cascade = CascadeType.ALL
	)
	@JsonIgnore
	private List<Tag> tags = new ArrayList<>();

	//-------------------------------------------------------------------
	/*
	 *  Constructors
	 */
	public TagGroup() {
	}
	public TagGroup(String tagGroupId, String tagGroupTitle, String topic, String help) {
		this.tagGroupId = tagGroupId;
		this.tagGroupTitle = tagGroupTitle;
		this.topic = topic;
		this.help = help;
	}
	public TagGroup(String tagGroupId, String tagGroupTitle, String topic, String help, List<Tag> tags) {
		this.tagGroupId = tagGroupId;
		this.tagGroupTitle = tagGroupTitle;
		this.topic = topic;
		this.help = help;
		this.tags = tags;
	}
	
	//-------------------------------------------------------------------
	/*
	 *  Getters and Setters
	 */
	public String getTagGroupId() { return tagGroupId; }
	public void setTagGroupId(String tagGroupId) { this.tagGroupId = tagGroupId; }

	public String getTagGroupTitle() { return tagGroupTitle; }
	public void setTagGroupTitle(String tagGroupTitle) { this.tagGroupTitle = tagGroupTitle; }

	public String getTopic() { return topic; }
	public void setTopic(String topic) { this.topic = topic; }
	
	public String getHelp() { return help; }
	public void setHelp(String help) { this.help = help; }
	
	public List<Tag> getTags() { return tags; }
	public void setTags(List<Tag> tags) {
		if ( tags != null ) {
			tags.forEach(t -> this.setTag(t));
		}
	}
	public void setTag(Tag tag) {
		if ( tag != null && !this.tags.contains(tag) ) {
			this.tags.add(tag);
			tag.setTagGroup(this);
		}
	}
	public void delTag(Tag tag) {
		if ( this.tags.contains(tag) ) {
			tag.setTagGroup(null);
			this.tags.remove(tag);
		}
	}
	public void delTags() {
		if ( this.tags != null && this.tags.size() > 0 ) {
			this.tags.forEach( t -> this.delTag(t) );
		}
	}
	
	public List<Instance> getInstances() { return instances; }
	public void setInstances(List<Instance> instances) { this.instances = instances; }
	public void setInstance(Instance instance) { 
		if ( !this.instances.contains(instance) ) {
			this.instances.add(instance);
		}
	}
	public void delInstance(Instance instance) {
		if ( instance != null && this.instances.contains(instance) ) {
			this.instances.remove(instance);
		}
	}
	
	//-------------------------------------------------------------------
	@Override
	public String toString() {
		return "\n    {"
				+ "\n      \"tagGroupId\": \"" + tagGroupId + "\","
				+ "\n      \"tagGroupTitle\": \"" + tagGroupTitle + "\","
				+ "\n      \"topic\": \"" + topic + "\","
				+ "\n      \"help\": \"" + help + "\","
				+ "\n      \"tags\": " + tags + "\n"
				+ "\n    }";
	}
}
