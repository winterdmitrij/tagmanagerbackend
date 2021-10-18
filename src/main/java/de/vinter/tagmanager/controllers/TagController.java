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

import de.vinter.tagmanager.models.Tag;
import de.vinter.tagmanager.services.TagService;

@RestController
@RequestMapping
public class TagController {

	@Autowired
	TagService tagService;
	
	//-------------------------------------------------------------------
	/*
	 * GET
	 */	
	@GetMapping(path = "/tags")
	public ResponseEntity<List<Tag>> getAllTags(){
		return new ResponseEntity<>( tagService.getAllTags(), HttpStatus.OK );
	}
	@GetMapping(path = "/tags/{tagId}")
	public ResponseEntity<Tag> getTagById( @PathVariable("tagId") String tagId ){
		return new ResponseEntity<>( tagService.getTagById(tagId), HttpStatus.OK );
	}
	@GetMapping(path = "/taggroups/{tagGroupId}/tags")
	public ResponseEntity<List<Tag>> getTagsByTagGroupId( @PathVariable("tagGroupId") String tagGroupId ){
		return new ResponseEntity<>( tagService.getTagsByTagGroupId(tagGroupId), HttpStatus.OK );
	}
	
	//-------------------------------------------------------------------
	/*
	 * POST
	 */
	@PostMapping(path = "/taggroups/{tagGroupId}/tags/add")
	public ResponseEntity<Tag> addNewTag(
			@PathVariable("tagGroupId") String tagGroupId,
			@RequestBody Tag tag) {
		return new ResponseEntity<>( tagService.addNewTag(tagGroupId, tag), HttpStatus.CREATED );
	}
	
	//-------------------------------------------------------------------
	/*
	 * DELETE
	 */
	@DeleteMapping(path = "/taggroups/{tagGroupId}/tags/delete/{tagId}")
	public ResponseEntity<?> delTagById( @PathVariable("tagId") String tagId ) {
		tagService.delTagById(tagId);
		return new ResponseEntity<>( HttpStatus.OK );
	}
	//-------------------------------------------------------------------
	/*
	 * PUT
	 */
	@PutMapping(path = "/taggroups/{tagGroupId}/tags/edit")
	public ResponseEntity<Tag> updTag( @RequestBody Tag tag ) {
		return new ResponseEntity<>( tagService.updTag(tag), HttpStatus.OK );
	}
}
