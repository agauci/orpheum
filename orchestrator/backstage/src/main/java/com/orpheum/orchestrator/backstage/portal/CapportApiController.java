package com.orpheum.orchestrator.backstage.portal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/.well-known")
public class CapportApiController {

    @GetMapping("/captive-portal")
    public Map<String, Object> getCapportInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("captive", true); // true if restricted access
        response.put("user-portal-url", "https://orpheum.backstage.cloud");
        response.put("venue-info-url", "https://orpheum.com.mt");
        response.put("can-extend-session", false);

        return response;
    }

}
