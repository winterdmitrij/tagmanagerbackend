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

import com.mashape.unirest.http.exceptions.UnirestException;

import de.vinter.tagmanager.models.Instance;
import de.vinter.tagmanager.models.TagGroup;
import de.vinter.tagmanager.services.InstanceService;

@RestController
@RequestMapping(path = "/instances")
public class InstanceController {

	@Autowired
	InstanceService instanceService;
	
	//-------------------------------------------------------------------
	/*
	 * GET
	 */	
	@GetMapping(path = "all")
	public ResponseEntity<List<Instance>> getAllInstances() {
		return new ResponseEntity<>( instanceService.getAllInstances(), HttpStatus.OK );
	}
	
	@GetMapping(path = "{site}")
	public ResponseEntity<Instance> getInstanceById( @PathVariable("site") String site ) {
		return new ResponseEntity<>( instanceService.getInstanceById(site), HttpStatus.OK );
	}
	
	//-------------------------------------------------------------------
	/*
	 * POST
	 */	
	@PostMapping(path = "add")
	public ResponseEntity<Instance> addNewInstance( @RequestBody Instance instance ) {
		return new ResponseEntity<>( instanceService.addNewInstance(instance), HttpStatus.CREATED );
	}
	
	//-------------------------------------------------------------------
	/*
	 * DELETE
	 */	
	@DeleteMapping(path = "delete/{site}")
	public ResponseEntity<?> delInstanceById( @PathVariable("site") String site ) {
		instanceService.delInstanceById(site);
		return new ResponseEntity<>( HttpStatus.OK );
	}
	
	//-------------------------------------------------------------------
	/*
	 * PUT
	 */	
	@PutMapping(path = "edit")
	public ResponseEntity<Instance> updInstance( @RequestBody Instance instance ) {
		return new ResponseEntity<>( instanceService.updInstance(instance), HttpStatus.OK );
	}

	@PutMapping(path = "{site}/taggroups/delete/{tagGroupId}")
	public ResponseEntity<Instance> delTagGroupFromInstance(
			@PathVariable("site") String site,
			@PathVariable("tagGroupId") String tagGroupId ) {
		
		return new ResponseEntity<>( instanceService.delTagGroupFromInstance(site, tagGroupId), HttpStatus.OK );
	}
	
	@PutMapping(path = "{site}/taggroups/add")
	public ResponseEntity<Instance> addTagGroupsToInstance(
			@PathVariable("site") String site,
			@RequestBody List<TagGroup> tagGroups ) {
		return new ResponseEntity<>( instanceService.addTagGroupsToInstance(site, tagGroups), HttpStatus.OK );
	}
	
//========================================================================================================
	/*
	 * ------------CheckMK-Methoden----------------------------
	 */
	/*
	 * Add all Tag Groups from a Instance (Checkmk to DB)
	 */
	@PutMapping(path = "{site}/taggroups/fromcmk")
	public ResponseEntity<Instance> addTagGroupsFromInstance(@PathVariable("site") String site) {
		return new ResponseEntity<>( instanceService.addAllTagGroupsFromCMK(site), HttpStatus.OK );
	}
	
	
	/*
	 * Add all new Tag Groups to Instance (DB -> Checkmk)
	 */
	@PutMapping(path = "{site}/taggroups/tocmk")
	public ResponseEntity<Instance> addTagGroupsToCheckmk( @PathVariable("site") String site ) throws UnirestException {
		return new ResponseEntity<>( instanceService.addTagGroupsToCheckmk(site), HttpStatus.OK );
	}
}
