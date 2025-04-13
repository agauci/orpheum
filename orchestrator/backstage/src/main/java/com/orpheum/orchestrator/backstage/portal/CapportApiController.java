package com.orpheum.orchestrator.backstage.portal;

import ch.qos.logback.core.testUtil.RandomUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/.well-known")
public class CapportApiController {

    Set<Integer> tokens = new HashSet<>();

    @GetMapping("/captive-portal")
    public ResponseEntity<Map<String, Object>> getCapportInfo(HttpServletRequest request) {
        Integer currentToken = 0;
        String cookie = request.getHeader("Cookie");
        System.out.println("COOKIE " + cookie);
        if (cookie != null ) {
            currentToken = Integer.parseInt(cookie.substring(cookie.indexOf("=") + 1));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("captive", !tokens.contains(currentToken)); // true if restricted access
        response.put("user-portal-url", "https://backstage.orpheum.cloud");
        response.put("venue-info-url", "https://orpheum.com.mt");
        response.put("can-extend-session", false);

        currentToken = (currentToken == 0) ? RandomUtil.getPositiveInt() : currentToken;
        tokens.add(currentToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/captive+json");
        headers.add("Cache-Control", "private");
        headers.add("Set-Cookie", "token=" + currentToken);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

}
