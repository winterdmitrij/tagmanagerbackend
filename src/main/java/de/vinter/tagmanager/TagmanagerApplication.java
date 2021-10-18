package de.vinter.tagmanager;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.vinter.tagmanager.models.Instance;
import de.vinter.tagmanager.models.Tag;
import de.vinter.tagmanager.models.TagGroup;
import de.vinter.tagmanager.repositories.InstanceRepository;
import de.vinter.tagmanager.repositories.TagGroupRepository;
import de.vinter.tagmanager.repositories.TagRepository;
import de.vinter.tagmanager.services.CheckmkService;
import de.vinter.tagmanager.services.InstanceService;

@SpringBootApplication
public class TagmanagerApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(TagmanagerApplication.class, args);
	}

	@Autowired
	TagGroupRepository tagGroupRepository;
	
	@Autowired
	TagRepository tagRepository;
	
	@Autowired
	InstanceRepository instanceRepository;
	
	@Autowired
	InstanceService instanceService;
	
	@Override
	public void run(String... args) throws Exception {
		// 1 Test-Instances
		Instance mysite = new Instance("mysite1", "192.168.178.180", "ed188039-1f55-4c98-a407-f3cd77d13f20");
		
		Instance mytest = new Instance("mytest", "192.168.178.180", "33274c6d-dfe5-4cd1-8330-6523eea651a6");
		
		Instance instance = new Instance("cl_test_vinter", "192.168.241.91", "92a720ee-7e7e-4dc9-8e7a-9a16320f880b");
//		Instance instance = new Instance("mysite", "192.168.178.180", "ed188039-1f55-4c98-a407-f3cd77d13f20");
		
		// 2 Test-TagGroups
		TagGroup test1 = new TagGroup("betriebssystem", "Betriebssystem", "software", "");
		TagGroup test2 = new TagGroup("cpu_core", "Number of Core", "hardware", "");
		
		// 5 Test-Tags
		Tag tag1 = new Tag("linux", "Linux");
		Tag tag2 = new Tag("windows", "Windows");
		Tag tag3 = new Tag("mcos", "McOs");
		Tag tag4 = new Tag("1_core", "1 Core");
		Tag tag5 = new Tag("2_cors", "2 Cors");
		
		// Add Tags to TagGroups
		test1.setTags( Arrays.asList(tag1, tag2, tag3) );
		test2.setTags( Arrays.asList(tag4, tag5) );
		
		// Add TagGroups to Instances
		mysite.setTagGroups( Arrays.asList(test1, test2) );
		mytest.setTagGroups( Arrays.asList(test1) );
		
		/*
		 * Save Test-Data
		 */
		instanceRepository.saveAll( List.of(mysite, mytest, instance) );
	}
}
