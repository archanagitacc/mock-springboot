package au.com.nbnco.csa.smp.simulator.thread;

import au.com.nbnco.csa.smp.simulator.model.DiagnosticResponseMessage;
import au.com.nbnco.csa.smp.simulator.model.StartWorkflowRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krishnan on 14/6/17.
 */
@Component
@Scope("prototype")
public class SmpNotificationTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmpNotificationTask.class);

    private StartWorkflowRequest startWorkflowRequest;
    private RestTemplate restTemplate;

    private String smpProxyUrl;




    public void setStartWorkflowRequest(final StartWorkflowRequest startWorkflowRequest, final RestTemplate restTemplate, final String smpProxyUrl) {
        this.startWorkflowRequest = startWorkflowRequest;
        this.restTemplate = restTemplate;
        this.smpProxyUrl = smpProxyUrl;
    }

    public void run() {

        try {


            Thread.sleep(5000L);

            final ObjectMapper mapper = new ObjectMapper();


            DiagnosticResponseMessage diagnosticResponseMessage = mapper.readValue(jsonFromFile("json/rejectedNotification.json"), DiagnosticResponseMessage.class);

            diagnosticResponseMessage.setNbnCorrelationId(startWorkflowRequest.getNbnCorrelationId());
            //TODO add headers in future

            /*

            MultiValueMap<String, String> headerTemplate = new LinkedMultiValueMap<String, String>();
            headerTemplate.add("nbnCorrelationId", diagnosticResponseMessage.getNbnCorrelationId());
            headerTemplate.add("Content-Type", "application/json");
            headerTemplate.add("wfModule", diagnosticResponseMessage.getWfModule());
            */

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("wfModule", diagnosticResponseMessage.getWfModule());
            headers.set("nbnCorrelationId", diagnosticResponseMessage.getNbnCorrelationId());

            HttpEntity<DiagnosticResponseMessage> request = new HttpEntity<>(diagnosticResponseMessage, headers);

            /*

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            messageConverters.add(new FormHttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);

            */
            LOGGER.info("smpProxyUrl @@"+smpProxyUrl);
            LOGGER.info("HttpMethod.POST @@"+HttpMethod.POST);
            LOGGER.info("request @@"+request);
            LOGGER.info("Object.class @@"+Object.class);
            ResponseEntity<?> rejectedNotification = restTemplate.exchange(smpProxyUrl, HttpMethod.POST, request,  Object.class);

            if (rejectedNotification.getStatusCode().equals(org.springframework.http.HttpStatus.OK)) {
            	
                LOGGER.info("Successfully Triggered rejectedNotification ");
            } else {
                LOGGER.info("Status Code for Triggered SMP Workflow " + rejectedNotification.getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String jsonFromFile(String jsonFile) throws Exception {
        return IOUtils.toString(
                getClass().getClassLoader().getResourceAsStream(jsonFile),
                "UTF-8");
    }


}

