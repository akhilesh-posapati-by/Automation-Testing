package com.by.dp.azure.functions;

import java.util.List;

import com.by.dp.azure.models.EventFilterRequest;
import com.by.dp.azure.models.Events;

public class EventFilter {

    public static final String ALL = "ALL";

    /**
     * pparse the request logic app rule filter along with actual event data for comparison.
     *
     * @param eventFilterRequest
     * @return
     */
    public static boolean processRuleSetFilter(EventFilterRequest eventFilterRequest) {
        List<Events> preconfigured = eventFilterRequest.getStatusFilter();
        String component = eventFilterRequest.getEvents().getComponent().toUpperCase();
        String service = eventFilterRequest.getEvents().getService().toUpperCase();
        String status = eventFilterRequest.getEvents().getStatus().toUpperCase();
        if (preconfigured.isEmpty()) {
            return true;
        } else {
            return processRuleSet(preconfigured, component, service, status);
        }
    }

    /**
     * each Payload Set Boolean Match = false; for each rule
     * <p>
     * if status (1st element) == "All" Skip Status checking otherwise check status
     * ==payload.status ; if result true set Match = true; if result false set Match
     * = false and continue;
     * if component (2nd element) == "All" Skip component
     * checking otherwise check component ==payload.component ; if result true set
     * Match = true;
     * if result false set Match = false and continue; if Service (3rd
     * element) == "All" Skip service checking otherwise check service
     * ==payload.service ; if result true set Match = true; if result false set
     * Match = false
     * <p>
     * if ( Match == false) Continue; otherwise break and return Match;
     *
     * @param preconfigured
     * @param component
     * @param service
     * @param status
     * @return
     */

    public static boolean processRuleSet(List<Events> preconfigured, String component, String service, String status) {
        boolean match = false;
        for (int i = 0; i < preconfigured.size(); i++) {
            if (preconfigured.get(i).getComponent().equals(ALL)) {
                //do nothing
            } else {
                if (preconfigured.get(i).getComponent().equalsIgnoreCase(component)) {
                    match = true;
                } else {
                    match = false;
                }
            }
            if (preconfigured.get(i).getService().equals(ALL)) {
                //do nothing
            } else {
                if (preconfigured.get(i).getService().equalsIgnoreCase(service)) {
                    match = true;
                } else {
                    match = false;
                }
            }
            if (preconfigured.get(i).getStatus().equals(ALL)) {
                //do nothing
            } else {
                if (preconfigured.get(i).getStatus().equalsIgnoreCase(status)) {
                    match = true;
                } else {
                    match = false;
                }
            }
            if (match) {
                break;
            } else {
                continue;
            }
        }
        return match;
    }


}
