package de.vinter.tagmanager.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "instances")
public class Instance {
	@Id
	private String site;
	private String ipAddress;
	private String secret;
	
	@JsonManagedReference(value = "instance-taggroup")
	@ManyToMany(
			fetch = FetchType.EAGER,
			mappedBy = "instances",
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JsonIgnore
	private List<TagGroup> tagGroups = new ArrayList<>();
	
	
	//-------------------------------------------------------------------
	/*
	 * Constructors
	 */
	public Instance() { }
	public Instance(String site, String ipAddress, String secret) {
		this.site = site;
		this.ipAddress = ipAddress;
		this.secret = secret;
	}

	//-------------------------------------------------------------------
	/*
	 * Getters and Setters
	 */
	public String getSite() { return site; }
	public void setSite(String site) { this.site = site; }
	
	public String getIpAddress() { return ipAddress; }
	public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
	
	public String getSecret() { return secret; }
	public void setSecret(String secret) { this.secret = secret; }

	public List<TagGroup> getTagGroups() { return tagGroups; }
	public void setTagGroups(List<TagGroup> tagGroups) {
//		this.delTagGroups();
		if ( tagGroups != null ) {
			tagGroups.forEach( t -> this.setTagGroup(t) );
		}
	}
	public void setTagGroup(TagGroup tagGroup) {
		if ( tagGroup != null && !this.tagGroups.contains(tagGroup) ) {
			tagGroup.setInstance(this);
			this.tagGroups.add(tagGroup);
		}
	}
	
	public void delTagGroup(TagGroup tagGroup) {
		if ( this.tagGroups.contains(tagGroup) ) {
			tagGroup.delInstance(this);
			this.tagGroups.remove(tagGroup);
		}
	}	
	public void delTagGroups() {
		this.tagGroups.forEach( t -> this.delTagGroup(t) );
	}

	//-------------------------------------------------------------------
	@Override
	public String toString() {
		return "\n{"
				+ "\n  \"site\": \"" + site + "\","
				+ "\n  \"ipAddress\": \"" + ipAddress + "\","
				+ "\n  \"tagGroups\": " + tagGroups + "\n"
				+ "\n}";
	}
}