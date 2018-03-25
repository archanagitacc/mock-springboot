package au.com.nbnco.csa.smp.simulator.web;

import au.com.nbnco.csa.smp.simulator.model.CreateWorkflowRequest;
import au.com.nbnco.csa.smp.simulator.model.StartWorkflowRequest;
import au.com.nbnco.csa.smp.simulator.model.StartWorkflowResponse;
import au.com.nbnco.csa.smp.simulator.service.SmpProxyService;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.UUID;

@RestController
@RequestMapping("rest/products/v2/productinstances")
public class SmpRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmpRestController.class);

    @Autowired
    private SmpProxyService smpProxyService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<String> createWorkflow(
            @RequestBody final CreateWorkflowRequest request
    ) {

        LOGGER.info("Received request {}", request);
        final String executionId = UUID.randomUUID().toString();

        LOGGER.info("Returning response {}", executionId);
        return new ResponseEntity<>(executionId, HttpStatus.OK);
    }

    @PostMapping(path = "/{executionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public final ResponseEntity<StartWorkflowResponse> startWorkflow(
            @RequestBody final StartWorkflowRequest request,
            @PathVariable("executionId") final String executionId
    ) {

        LOGGER.info("Requested diagnostic for {}", executionId);
        LOGGER.info("Request : @@", request);

        final String nbnCorId = smpProxyService.triggerNotifications(request);

        LOGGER.info("Trigger notifications started for {}", nbnCorId);
        
        return new ResponseEntity<>(StartWorkflowResponse.builder().executionId(executionId).build(), HttpStatus.OK);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping(path="/{priid}",produces = {MediaType.APPLICATION_JSON_VALUE})
	public String getPRIresponse(@PathVariable("priid") String priid) throws JSONException {

    	String inputString = validateJsonResponseFile(priid);
    	JSONObject jsonObj = new JSONObject(inputString);
    	System.out.println("json : "+jsonObj);
		return jsonObj.toString();
		
	}
    
    
    public static String validateJsonResponseFile(String priid){
		String fileContent = null;
		String filePath = "C:\\Automation\\smp_simulator\\src\\main\\resources\\json\\PRI_response_"+priid+".json";
		System.out.println("File path : "+filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			String responseString;
			while ((responseString = br.readLine()) != null) {
				sb.append(responseString);
			}
			fileContent = sb.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println("fileContent" +fileContent);
		return fileContent;
	}

}
