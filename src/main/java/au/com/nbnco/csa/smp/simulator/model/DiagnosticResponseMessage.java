package au.com.nbnco.csa.smp.simulator.model;



import lombok.Data;

import java.util.LinkedHashMap;

public class DiagnosticResponseMessage {



    private String nbnCorrelationId;


    private String wfModule;

    private LinkedHashMap<String, Object> diagnosticsData;


    public String getWfModule() {
        return wfModule;
    }


    public void setWfModule(String wfModule) {
        this.wfModule = wfModule;
    }


    public String getNbnCorrelationId() {
        return nbnCorrelationId;
    }


    public void setNbnCorrelationId(String nbnCorrelationId) {
        this.nbnCorrelationId = nbnCorrelationId;
    }


    public LinkedHashMap<String, Object> getDiagnosticsData() {
        return diagnosticsData;
    }


    public void setDiagnosticsData(LinkedHashMap<String, Object> diagnosticsData) {
        this.diagnosticsData = diagnosticsData;
    }

}
