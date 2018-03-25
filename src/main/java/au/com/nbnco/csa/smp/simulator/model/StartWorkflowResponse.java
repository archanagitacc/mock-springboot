package au.com.nbnco.csa.smp.simulator.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by krishnan on 9/6/17.
 */
@Data
@Builder
public class StartWorkflowResponse {
    private String executionId;
}
