package au.com.nbnco.csa.smp.simulator.service.impl;

import au.com.nbnco.csa.smp.simulator.model.DiagnosticResponseMessage;
import au.com.nbnco.csa.smp.simulator.model.StartWorkflowRequest;
import au.com.nbnco.csa.smp.simulator.service.SmpProxyService;
import au.com.nbnco.csa.smp.simulator.thread.SmpNotificationTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.io.IOUtils;

/**
 * Created by krishnan on 14/6/17.
 */
@Service
public class SmpProxyServiceImpl implements SmpProxyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmpProxyServiceImpl.class);

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
	private RestTemplate restTemplate;

    @Value("${nbnco.smpproxyurl}")
    private String smpProxyUrl;



    @Override
    public String triggerNotifications(StartWorkflowRequest request) {

        final SmpNotificationTask notificationTask = new SmpNotificationTask();
        System.out.println(">>>>>");
        LOGGER.info(smpProxyUrl);
        LOGGER.info(""+restTemplate);
        LOGGER.info(""+request);
        notificationTask.setStartWorkflowRequest(request, restTemplate, smpProxyUrl);
        
        taskExecutor.execute(notificationTask);

        return request.getNbnCorrelationId();
    }

    //private class SmpNotificationTask


}
