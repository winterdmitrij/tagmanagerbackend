package de.vinter.tagmanager.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.exceptions.UnirestException;

import de.vinter.tagmanager.models.Instance;
import de.vinter.tagmanager.models.TagGroup;
import de.vinter.tagmanager.repositories.InstanceRepository;
import de.vinter.tagmanager.repositories.TagGroupRepository;

@Service
public class InstanceService {
	
	@Autowired
	InstanceRepository instanceRepository;
	
	@Autowired
	TagGroupRepository tagGroupRepository;
	
	@Autowired
	TagGroupService tagGroupService;
	
	//-----------------------------------------------------------------------------------------
	/*
	 * GET
	 */
	public List<Instance> getAllInstances() {
		return instanceRepository.findAll();
	}
	
	public Instance getInstanceById(String site) {
		return instanceRepository.getInstanceById(site);
	}
	
	//-----------------------------------------------------------------------------------------
	/*
	 * POST
	 */
	public Instance addNewInstance(Instance instance) {
		if ( instanceRepository.findById(instance.getSite()).isPresent() ) {
			throw new IllegalStateException("Instance existiert schon");
		}
		return instanceRepository.save(instance);
	}
	
	//-----------------------------------------------------------------------------------------
	/*
	 * DELETE
	 */
	public void delInstanceById(String site) {
		if ( !instanceRepository.existsById(site) ) {
			throw new IllegalStateException("Instance existiert nicht");
		}
		instanceRepository.delInstanceById(site);
	}
	
	//-----------------------------------------------------------------------------------------	
	/*
	 * PUT
	 */
	public Instance updInstance(Instance instance) {
		return instanceRepository.save(instance);
	}
	
	// Delete a Tag Group from Instance
	@Transactional
	public Instance delTagGroupFromInstance(String site, String tagGroupId) {
		Instance instance = instanceRepository.findById(site)
				.orElseThrow( () -> new IllegalStateException("Instance existiert nicht") );
		TagGroup tagGroup = tagGroupService.getTagGroupById(tagGroupId);
		instance.delTagGroup(tagGroup);
		return instanceRepository.save(instance);
	}

	// Add Tag Groups to Instance
	@Transactional
	public Instance addTagGroupsToInstance(String site, List<TagGroup> tagGroups) {
		Instance instance = instanceRepository.findById(site)
				.orElseThrow( () -> new IllegalStateException("Instance existiert nicht") );
		instance.setTagGroups(tagGroups);
		return instanceRepository.save(instance);
	}
	
//===========================================================================================================	
	/*
	 * ------------CheckMK -Methoden----------------------------
	 */
	
	// Add Tag Group FROM Checkmk to Database
	@Transactional
	public Instance addAllTagGroupsFromCMK(String site) {
//		List<TagGroup> dbTagGroups = tagGroupService.getAllTagGroups();
		
		Instance instance = instanceRepository.findById(site)
				.orElseThrow( () -> new IllegalStateException("Instance existiert nicht") );
		
		List<TagGroup> cmkTagGroups = CheckmkService.GetDataFromCheckMK(instance);
		
		instance.setTagGroups(cmkTagGroups);
		
		return instanceRepository.save(instance);
	}
	
	
	
	// Add all new Tag Groups to Instance (DB -> Checkmk)
	public Instance addTagGroupsToCheckmk(String site) throws UnirestException {
		System.out.println("\n==========================================\n Daten wurden to CMK abgeschikt \n========================================");
		
		Instance instance = instanceRepository.findById(site)
				.orElseThrow( () -> new IllegalStateException("Instance existiert nicht") );
		
		List<TagGroup> cmkTagGroups = CheckmkService.GetDataFromCheckMK(instance);
		CheckmkService.delDataByCheckMK(instance, cmkTagGroups);
		
		List<TagGroup> dbTagGroups = new ArrayList<>();
		for ( TagGroup tagGroup: instance.getTagGroups() ) {
			if ( !dbTagGroups.contains(tagGroup) ) {
				dbTagGroups.add(tagGroup);
			}
		}
		
		CheckmkService.setDataToCheckMK(instance, dbTagGroups);

		return instance;
	}
}
