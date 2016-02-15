package com.wordcount;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.wordcount.bo.WordCountBusiness;

@Path("/counter-api")
public class WordCountService {

	private static final String fileName="C:/Data/words.txt";
	private static final String outPutFileName = "C:/Data/words.csv";
	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject getSearchResult(JSONObject inputJsonObj) {
		String input = null;
		JSONObject outputJsonObj= null;
		try {
		input = (String) inputJsonObj.getString("searchText");
		   
		    Map<String,Integer> createdMapFromText = WordCountBusiness.readFileAndCreateWordMap(fileName);
			String sortByValueInDecreasingOrder =  WordCountBusiness.getCountOfWords(createdMapFromText,input);
		
		    outputJsonObj = new JSONObject();
		    outputJsonObj.put("output", sortByValueInDecreasingOrder);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    return outputJsonObj;
	}
	
	@GET
	@Path("/top/{param}")
	@Produces("application/vnd.ms-excel")
	public Response getCountResult(@PathParam("param") Integer count) {
		 Map<String,Integer> createdMapFromText = WordCountBusiness.readFileAndCreateWordMap(fileName);
		 List<Entry<String, Integer>> sortByValueInDecreasingOrder = WordCountBusiness.sortByValueInDecreasingOrder(createdMapFromText,count);
		WordCountBusiness.generateCsvFile(sortByValueInDecreasingOrder,outPutFileName);
		File file = new File(outPutFileName);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition","attachment; filename=wordsCount.csv");
		return response.build();
	}
}
