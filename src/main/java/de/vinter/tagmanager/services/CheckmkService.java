package de.vinter.tagmanager.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import de.vinter.tagmanager.models.Instance;
import de.vinter.tagmanager.models.Tag;
import de.vinter.tagmanager.models.TagGroup;

@Service
public class CheckmkService {

	@Autowired
	static
	InstanceService instanceService;
	
//-----FROM CHECKMK--------------------------------------------------------------
	public static List<TagGroup> GetDataFromCheckMK(Instance instance) {
		String host = "http://" + instance.getIpAddress() + "/";
		String site = "" + instance.getSite() + "/";
		String api = "check_mk/webapi.py?";
		String action = "action=get_hosttags";
		String outFormat = "&output_format=JSON"; 
		String username = "&_username=automation";
		String secret = "&_secret=" + instance.getSecret();
		
		String url = host + site + api + action + outFormat + username + secret;
		
		return GetDataFromCheckMK(url);
	}
	
	public static List<TagGroup> GetDataFromCheckMK(String url) {
		JSONObject json = null;
		
		HttpResponse<JsonNode> response;
		try {
			response = Unirest.get(url).asJson();
			
			json = new JSONObject(response.getBody().toString());
		} catch (UnirestException e) {
			e.printStackTrace();
		}

		
		List<TagGroup> tagGroups = new ArrayList<>();
		
		JSONObject jsonArray = json.getJSONObject("result");
		
		// Bekommen JsonArray mit TagGroups
		JSONArray jsonArrTagGroups = jsonArray.getJSONArray("tag_groups");
		
		for ( int i = 0; i < jsonArrTagGroups.length(); i++ ) {
			// JavaObjekt aus JSON
			JSONObject jsonObjectTagGroup = jsonArrTagGroups.getJSONObject(i);
			
			TagGroup tagGroup = jsonToTagGroup(jsonObjectTagGroup);

			tagGroups.add(tagGroup);
		}
		return tagGroups;
	}
	
	public static TagGroup jsonToTagGroup(JSONObject jsonObjectTagGroup) {
		String tagGroupId = jsonObjectTagGroup.getString("id");
		String tagGroupTitle = jsonObjectTagGroup.getString("title");
		String topic = "Tags";
		if ( jsonObjectTagGroup.has("topic") ) {
			topic = jsonObjectTagGroup.getString("topic");
		}
		String help = "";
		if ( jsonObjectTagGroup.has("help") ) {
			help = jsonObjectTagGroup.getString("help");
		}
		TagGroup tagGroup = new TagGroup( tagGroupId, tagGroupTitle, topic, help );
		
		// Bekommen Array mit Tags
		JSONArray jsonArrTags = jsonObjectTagGroup.getJSONArray("tags");
		
		for ( int i = 0; i < jsonArrTags.length(); i++ ) {
			JSONObject jsonObjectTag = jsonArrTags.getJSONObject(i);
			
			Tag tag = jsonToTag(jsonObjectTag);
			
			tagGroup.setTag(tag);
		}

		return tagGroup;
	}
	
	public static Tag jsonToTag(JSONObject jsonObjectTag) {
		String tagId = jsonObjectTag.getString("id");
		String tagTitle = jsonObjectTag.getString("title");				
		
		Tag tag = new Tag(tagId, tagTitle);
		
		return tag;
	}
	
//-----TO CHECKMK---------------------------------------------------------------
	public static void setDataToCheckMK(Instance instance, List<TagGroup> tagGroups) {
		
		String url = "http://";
		String ip = instance.getIpAddress();
		String site = instance.getSite();
		String authorization = "Bearer automation " + instance.getSecret();
		url += ip + "/" + site + "/check_mk/api/v1.0/domain-types/host_tag_group/collections/all";
		
		String bodyJson = "";
		for ( TagGroup tagGroup: tagGroups ) {
			bodyJson = tagGroupToJson(tagGroup);
			System.out.println(url);
			System.out.println(authorization);
			System.out.println(bodyJson);
			
			try {
				Unirest.post(url)
						.header("Authorization", authorization)
						.header("Content-Type", "application/json")
						.body(bodyJson)
						.asString();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			
			bodyJson = "";
		}
	}
	
	public static String tagGroupToJson(TagGroup tagGroup) {
		String jsonTagGroup = "{";
		// TO DO
		String ident = "\n  \"ident\": \"" + tagGroup.getTagGroupId() + "\",";
		String title = "\n  \"title\": \"" + tagGroup.getTagGroupTitle() + "\",";
		String topic = "\n  \"topic\": \"" + tagGroup.getTopic() + "\",";
		String help = "\n  \"help\": \"" + tagGroup.getHelp() + "\",";
		String tags = "\n  \"tags\": [";
		
		for ( Tag tag: tagGroup.getTags() ) {
			tags += tagToJson(tag);
			if ( tagGroup.getTags().indexOf(tag) < tagGroup.getTags().size() - 1 ) {
				tags += ",";
			}
		}
		tags += "\n  ]";
		jsonTagGroup += ident + title + topic + help + tags + "\n}";
		return jsonTagGroup;
	}
	
	public static String tagToJson(Tag tag) {
		String jsonTag = "\n    {";
		String ident = "\n      \"ident\": \"" + tag.getTagId() + "\",";
		String title = "\n      \"title\": \"" + tag.getTagTitle() + "\"";
		jsonTag += ident + title + "\n    }";
		return jsonTag;
	}
	
	public static void delDataByCheckMK(Instance instance, List<TagGroup> cmkTagGroups) {
		String url = "http://";
		String ip = instance.getIpAddress();
		String site = instance.getSite();
		String authorization = "Bearer automation " + instance.getSecret();
		String url2 = "";
		url += ip + "/" + site + "/check_mk/api/v1.0/objects/host_tag_group/";
		
		for ( TagGroup tagGroup: cmkTagGroups ) {
			url2 += tagGroup.getTagGroupId() + "?repair=true";

			try {
				Unirest.delete(url + url2)
				.header("Authorization", authorization)
				.asString();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			url2 = "";
		}
	}
}
