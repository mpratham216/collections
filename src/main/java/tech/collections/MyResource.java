package tech.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    @POST
	@Path("commonlist")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCommonFromList(@FormParam("list1") String list1, @FormParam("list2") String list2) {
    	List<Integer> l1 = Arrays.stream(list1.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    	List<Integer> l2 = Arrays.stream(list2.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
		List<Integer> commonList = l1.stream().filter(l2::contains).collect(Collectors.toList());
		return Response.ok(commonList.toString()).build();
	}
    
    @POST
	@Path("sortchars")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getSortedChar(@FormParam("input") String input) {
    	char[] charArr = input.toCharArray();
    	Arrays.sort(charArr);
		return Response.ok(Arrays.toString(charArr)).build();
	}
    
    
    @POST
   	@Path("removeduplicates")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   	@Produces(MediaType.TEXT_PLAIN)
   	public Response removeDuplicates(@FormParam("list") String list) {
       	List<Integer> l1 = Arrays.stream(list.split(","))
                   .map(Integer::parseInt)
                   .collect(Collectors.toList());
       	
   		List<Integer> removeDuplicates = l1.stream().distinct().collect(Collectors.toList());
   		return Response.ok(removeDuplicates.toString()).build();
   	}
    
    
    @POST
    @Path("employeebonus")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBonus(@FormParam("list") String list) {
    	List<Integer> salary = Arrays.stream(list.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    	
    	List<Double> bonuses = salary.stream()
    			.map(salaries ->{
    				if (salaries > 25000) {
                        return 10.0;
                    } else if (salaries >= 10000 && salaries <= 25000) {
                        return 15.0;
                    } else {
                        return salaries * 0.20;
                    }
    			}).collect(Collectors.toList());
    	
    	return Response.ok(bonuses.toString()).build();
    }
    
//    @POST
//    @Path("shuffle")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response getBonus(@FormParam("value") String values) {
//    	List<Character> chars = new ArrayList<>;
//    	for(char c : values.toCharArray()) {
//    		chars.add(c);
//    	}
//    	
//    	//Collections.shuffle();
//    	
//    	return Response.ok(bonuses.toString()).build();
//    }
    
    @POST
    @Path("descendingorder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sortDescending(@FormParam("numbers") String numbers) {
    	Set<Integer> numberSet = Arrays.stream(numbers.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    	
    	Set<Integer> descendingSet = numberSet.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(LinkedHashSet::new));
    	return Response.ok(descendingSet.toString()).build();
    	
    }
    
    @POST
    @Path("descendingorder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response calculatePercentage(@FormParam("numbers") String marksMapStr) {
    	Map<String, List<Integer>> marksMap = convertToMap(marksMapStr);
    	Map<String, Double> percentagesMap = new HashMap<>();
         for (Map.Entry<String, List<Integer>> entry : marksMap.entrySet()) {
             String key = entry.getKey();
             List<Integer> marksList = entry.getValue();
             double percentage = calculatePercentage(marksList);
             percentagesMap.put(key, percentage);
         }
         return Response.ok(percentagesMap.toString()).build();
    	
    }
    
    private Map<String, List<Integer>> convertToMap(String marksMapStr) {
        Map<String, List<Integer>> marksMap = new HashMap<>();
        String[] entries = marksMapStr.split(";");
        for (String entry : entries) {
            String[] parts = entry.split("=");
            String key = parts[0].trim();
            List<Integer> marks = Arrays.stream(parts[1].trim().split(","))
                                        .map(String::trim)
                                        .map(Integer::parseInt)
                                        .collect(Collectors.toList());
            marksMap.put(key, marks);
        }
        return marksMap;
    }
    
    private double calculatePercentage(List<Integer> marksList) {
        int sum = marksList.stream().mapToInt(Integer::intValue).sum();
        return (sum / (double) (marksList.size() * 100)) * 100;
    }
}