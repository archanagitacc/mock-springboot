package au.com.nbnco.csa.smp.simulator.service;

import au.com.nbnco.csa.smp.simulator.model.StartWorkflowRequest;

/**
 * Created by krishnan on 14/6/17.
 */
public interface SmpProxyService {

    String triggerNotifications(StartWorkflowRequest request);
}
