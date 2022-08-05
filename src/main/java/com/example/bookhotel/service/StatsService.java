package com.example.bookhotel.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface StatsService {

    void onRequest(HttpServletRequest request);
    List<String> getRequestPathsAsStrings();
}
