package de.vinter.tagmanager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.vinter.tagmanager.models.TagGroup;
import de.vinter.tagmanager.services.TagGroupService;

@RestController
@RequestMapping(path = "/taggroups")
public class TagGroupController {
	
	@Autowired
	TagGroupService tagGroupService;
	
	//-------------------------------------------------------------------
	/*
	 * GET
	 */	
	@GetMapping(path = "all")
	public ResponseEntity<List<TagGroup>> getAllTagGroups() {
		return new ResponseEntity<>( tagGroupService.getAllTagGroups(), HttpStatus.OK );
	}

	@GetMapping(path = "{tagGroupId}")
	public ResponseEntity<TagGroup> getTagGroupById( @PathVariable("tagGroupId") String tagGroupId ) { 
		return new ResponseEntity<>( tagGroupService.getTagGroupById(tagGroupId), HttpStatus.OK );
	}
	
	//-------------------------------------------------------------------
	/*
	 * POST
	 */	
	@PostMapping(path = "add")
	public ResponseEntity<TagGroup> addNewTagGroup( @RequestBody TagGroup tagGroup ){
		return new ResponseEntity<>( tagGroupService.addNewTagGroup(tagGroup), HttpStatus.CREATED );
	}
	
	//-------------------------------------------------------------------
	/*
	 * DELETE
	 */	
	@DeleteMapping(path = "delete/{tagGroupId}")
	public ResponseEntity<?> delTagGroupById( @PathVariable("tagGroupId") String tagGroupId ) {
		tagGroupService.delTagGroupById(tagGroupId);
		return new ResponseEntity<>( HttpStatus.OK );
	}
	
	//-------------------------------------------------------------------
	/*
	 * PUT
	 */	
	@PutMapping(path = "edit")
	public ResponseEntity<TagGroup> updTagGroup( @RequestBody TagGroup tagGroup ) {
		return new ResponseEntity<>( tagGroupService.updTagGroup(tagGroup), HttpStatus.OK );
	}
	
//	@PutMapping(path = "{tagGroupId}/tags/delete/{tagId}")
//	public ResponseEntity<?> delTagById(
//			@PathVariable("tagGroupId") String tagGroupId,
//			@PathVariable("tagId") String tagId ) {
//		tagGroupService.delTagById(tagGroupId, tagId);
//		return new ResponseEntity<>( HttpStatus.OK );
//	}
	
	//-------------------------------------------------------------------
//	/*
//	 * Add a new TagGroup to a Instence (DB)
//	 */
//	@PostMapping(path = "/taggroups/{tag_group_id}")
//	public void addNewTagGroupToInstance(@RequestBody Tag_group tagGroup, String site) {
//		tagGroupService.addNewTagGroupToInstance(tagGroup, site);
//	}
}
