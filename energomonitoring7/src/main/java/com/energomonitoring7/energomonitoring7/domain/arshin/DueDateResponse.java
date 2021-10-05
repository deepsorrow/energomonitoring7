package com.energomonitoring7.energomonitoring7.domain.arshin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DueDateResponse {
    private boolean success;
    private String result;
    private HashMap<String, String> variants;
    public int variantsCount;

    public DueDateResponse(boolean success, String result) {
        this.success = success;
        this.result = result;
    }

    public DueDateResponse(HashMap<String, String> variants) {
        this.success = true;
        this.variants = variants;
        this.variantsCount = variants.size();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HashMap<String, String> getVariants() {
        return variants;
    }

    public void setVariants(HashMap<String, String> variants) {
        this.variants = variants;
    }
}
