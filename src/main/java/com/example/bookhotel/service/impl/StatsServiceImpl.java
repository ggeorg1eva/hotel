package com.example.bookhotel.service.impl;

import com.example.bookhotel.service.StatsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {
    private Map<String, Integer> requstsCountByUsersMap = new HashMap<>();

    @Override
    public void onRequest(HttpServletRequest request) {
        countRequestsPerUser(request);
    }

    @Override
    public List<String> getRequestPathsAsStrings() {
        List<String> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : requstsCountByUsersMap.entrySet()) {
            String combination = entry.getKey() + " -> " + entry.getValue();
            list.add(combination);
        }
        //todo fix this interceptor to be nicer
        return list;
    }


    private void countRequestsPerUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        String name = principal != null ? principal.getName() : "anonymous";

        if (requstsCountByUsersMap.containsKey(name)) {
            requstsCountByUsersMap.put(name, requstsCountByUsersMap.get(name) + 1);
        } else {
            requstsCountByUsersMap.put(name, 1);
        }
    }
}
