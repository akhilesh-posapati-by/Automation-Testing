package com.by.dp.azure.functions;

import com.by.dp.azure.models.EventFilterRequest;
import com.by.dp.azure.models.EventFilterResponse;
import com.google.gson.Gson;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerFunction {
    /**
     * This function listens at endpoint "/api/filterEventByServiceStatus". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/filterEventByServiceStatus
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    public static String BAD_REQUEST = "invalid payload";

    @FunctionName("filterEventByServiceStatus")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a  event filter validation request.");
        if (request.getBody() == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body(BAD_REQUEST).build();
        } else {
            final String event = request.getBody().get();
            boolean filterValidationStatus = processFilter(event);
            EventFilterResponse response = new EventFilterResponse();
            response.setFilterRequired(filterValidationStatus);
            return request.createResponseBuilder(HttpStatus.OK).body(response).build();
        }
    }

    /**
     * accepts request and provides internal logic to return true value based on different conditions.
     *
     * @param request
     * @return
     */
    public boolean processFilter(String request) {
        Gson gson = new Gson();
        EventFilterRequest eventFilterRequest = gson.fromJson(request, EventFilterRequest.class);
        return EventFilter.processRuleSetFilter(eventFilterRequest);
    }
}
