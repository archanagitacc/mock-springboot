package au.com.nbnco.csa.smp.simulator.model;

import lombok.Data;
import lombok.Singular;

import java.util.HashMap;
import java.util.List;

@Data
public class StartWorkflowRequest {

    private String subscriberId;

    private String nbnCorrelationId;

    private @Singular List<String> roles ;

    private HashMap<String, Object> data;
}
